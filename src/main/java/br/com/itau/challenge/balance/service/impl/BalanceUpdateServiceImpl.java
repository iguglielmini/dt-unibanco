package br.com.itau.challenge.balance.service.impl;

import br.com.itau.challenge.balance.dto.TransactionMessage;
import br.com.itau.challenge.balance.model.Account;
import br.com.itau.challenge.balance.repository.AccountRepository;
import br.com.itau.challenge.balance.service.BalanceUpdateService;
import br.com.itau.challenge.balance.utils.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceUpdateServiceImpl implements BalanceUpdateService {

  private final ObjectMapper objectMapper;
  private final AccountRepository accountRepository;

  @Override
  @Transactional
  public void applyMessage(String sqsBodyJson) {
    try {
      final TransactionMessage msg = objectMapper.readValue(sqsBodyJson, TransactionMessage.class);

      // extrai dados do payload
      UUID accountId = msg.account.id;
      UUID owner = msg.account.owner;
      var amount = msg.account.balance.amount;      // usa o saldo já calculado!
      var currency = msg.account.balance.currency;  // usa a moeda do payload
      OffsetDateTime updatedAt = TimeUtil.microsToOffsetDateTime(msg.transaction.timestamp);

      // carrega/instancia conta
      Account account = accountRepository.findById(accountId)
          .orElseGet(() -> new Account(accountId));

      // LWW: só aplica se for mais novo
      if (account.getUpdatedAt() == null || updatedAt.isAfter(account.getUpdatedAt())) {
        account.setOwner(owner);
        account.setAmount(amount);
        account.setCurrency(currency);
        account.setUpdatedAt(updatedAt);
        accountRepository.save(account);
        log.info("Saldo atualizado (LWW) accountId={} amount={} currency={} updatedAt={}",
            accountId, amount, currency, updatedAt);
      } else {
        log.debug("Mensagem ignorada por ser mais antiga. accountId={} msgTs={} currentTs={}",
            accountId, updatedAt, account.getUpdatedAt());
      }

    } catch (Exception e) {
      log.error("Falha ao aplicar mensagem SQS. body={}", sqsBodyJson, e);
      // deixe propagar se quiser DLQ; aqui optei de por não propagar para não travar o delete no consumer
    }
  }
}
