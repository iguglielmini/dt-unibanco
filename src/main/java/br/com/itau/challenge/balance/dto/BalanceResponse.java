package br.com.itau.challenge.balance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(name = "BalanceResponse")
public class BalanceResponse {
  public UUID id;
  public UUID owner;
  public Amount balance;
  public OffsetDateTime updated_at;

  @Schema(name = "Amount")
  public static class Amount {
    public BigDecimal amount;
    public String currency;
    public Amount() {}
    public Amount(BigDecimal amount, String currency) {
      this.amount = amount; this.currency = currency;
    }
  }

  public BalanceResponse() {}
  public BalanceResponse(UUID id, UUID owner, Amount balance, OffsetDateTime updated_at) {
    this.id = id; this.owner = owner; this.balance = balance; this.updated_at = updated_at;
  }
}
