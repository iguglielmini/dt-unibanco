package br.com.itau.challenge.balance.consumer;

import br.com.itau.challenge.balance.service.BalanceUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsBalanceConsumer {

  private final SqsAsyncClient sqs;
  private final BalanceUpdateService balanceUpdateService;

  @Value("${app.sqs.queue-url}")
  private String queueUrl;

  @Value("${app.sqs.max-messages:10}")
  private int maxMessages;

  @Value("${app.sqs.wait-seconds:10}")
  private int waitSeconds;

  @Scheduled(fixedDelay = 500)
  public void poll() {
    var req = ReceiveMessageRequest.builder()
        .queueUrl(queueUrl)
        .maxNumberOfMessages(maxMessages)
        .waitTimeSeconds(waitSeconds)
        .build();

    sqs.receiveMessage(req).thenAccept(resp -> {
      log.info("Mensagens recebidas: {}", resp.messages().size());
      for (Message m : resp.messages()) {
        try {
          balanceUpdateService.applyMessage(m.body());
          // deletar somente após processar OK
          sqs.deleteMessage(DeleteMessageRequest.builder()
              .queueUrl(queueUrl)
              .receiptHandle(m.receiptHandle())
              .build());
          log.info("Deletada messageId={}", m.messageId());
        } catch (Exception e) {
          log.error("Falha ao processar messageId={}", m.messageId(), e);
          // sem delete -> mensagem volta após visibility timeout (simula DLQ real)
        }
      }
    }).exceptionally(ex -> {
      log.error("Erro ao receber mensagens SQS: {}", ex.toString(), ex);
      return null;
    });
  }
}
