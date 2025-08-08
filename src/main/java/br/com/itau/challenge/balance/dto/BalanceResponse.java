package br.com.itau.challenge.balance.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record BalanceResponse(
        UUID id,
        UUID owner,
        Amount balance,
        OffsetDateTime updated_at
) {
    @Builder
    public record Amount(BigDecimal amount, String currency) {}
}
