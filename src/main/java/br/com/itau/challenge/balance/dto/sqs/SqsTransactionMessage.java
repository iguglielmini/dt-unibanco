package br.com.itau.challenge.balance.dto.sqs;

import java.math.BigDecimal;

public record SqsTransactionMessage(
        Transaction transaction,
        Account account
) {
    public record Transaction(String id, String type, BigDecimal amount, String currency,
                              String status, long timestamp) {}
    public record Account(String id, String owner, String created_at, String status,
                          Balance balance) {
        public record Balance(BigDecimal amount, String currency) {}
    }
}
