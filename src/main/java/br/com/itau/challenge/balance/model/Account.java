package br.com.itau.challenge.balance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Account {

  @Id
  private UUID id;

  private UUID owner;
  private BigDecimal amount;
  private String currency;
  private OffsetDateTime updatedAt;

  public Account(UUID id) { this.id = id; }
}
