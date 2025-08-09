package br.com.itau.challenge.balance.consumer;

import br.com.itau.challenge.balance.dto.sqs.SqsTransactionMessage;
import br.com.itau.challenge.balance.model.Account;
import br.com.itau.challenge.balance.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.time.*;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsBalanceConsumer {

    private final SqsAsyncClient sqs;
    private final ObjectMapper mapper;          // usa o ObjectMapper do Spring
    private final AccountRepository repository;

    @Value("${app.sqs.queue-url}") private String queueUrl;
    @Value("${app.sqs.max-messages:10}") private int maxMessages;
    @Value("${app.sqs.wait-seconds:10}") private int waitSeconds;

    // polling periódico; o receive usa long polling (até waitSeconds)
    @Scheduled(fixedDelay = 200)
    public void poll() {
        var req = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxMessages)
                .waitTimeSeconds(waitSeconds)
                .build();

        sqs.receiveMessage(req).thenAccept(resp -> {
            List<Message> msgs = resp.messages();
            if (msgs == null || msgs.isEmpty()) return;

            for (Message m : msgs) {
                try {
                    process(m.body());
                    // só apaga se processou OK
                    sqs.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(m.receiptHandle())
                            .build());
                } catch (Exception e) {
                    log.error("Erro processando mensagem SQS: {}", e.getMessage(), e);
                    // não deleta => reentrega automática pela fila
                }
            }
        }).exceptionally(ex -> {
            log.error("Erro ao receber mensagens SQS", ex);
            return null;
        });
    }

    private void process(String body) throws Exception {
        var msg = mapper.readValue(body, SqsTransactionMessage.class);

        // somente transações aprovadas
        if (!"APPROVED".equalsIgnoreCase(msg.transaction().status())) return;

        // timestamp em microssegundos -> OffsetDateTime preciso
        long micros = msg.transaction().timestamp();
        var instant = Instant.ofEpochSecond(micros / 1_000_000L, (micros % 1_000_000L) * 1_000L);
        var updatedAt = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());

        var accountId = UUID.fromString(msg.account().id());
        var ownerId   = UUID.fromString(msg.account().owner());

        // last-write-wins por timestamp (ignora mensagens atrasadas)
        var current = repository.findById(accountId).orElse(null);
        if (current != null && current.getUpdatedAt() != null) {
            if (updatedAt.isBefore(current.getUpdatedAt())) return;
        }

        var entity = (current == null ? new Account() : current);
        entity.setId(accountId);
        entity.setOwner(ownerId);
        entity.setAmount(msg.account().balance().amount());
        entity.setCurrency(msg.account().balance().currency());
        entity.setUpdatedAt(updatedAt);

        repository.save(entity);
    }
}
