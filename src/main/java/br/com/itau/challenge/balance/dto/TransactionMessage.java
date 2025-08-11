package br.com.itau.challenge.balance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionMessage {
  public Transaction transaction;
  public Account account;

  public static class Transaction {
    public UUID id;
    public String type;     // "CREDIT" | "DEBIT"
    public BigDecimal amount;
    public String currency; // "BRL"
    public String status;   // "APPROVED" | "REJECTED"
    public long timestamp;  // micros
  }

  public static class Account {
    public UUID id;
    public UUID owner;
    @JsonProperty("created_at")
    public String createdAt;
    public String status;   // "ENABLED"...
    public Balance balance;
  }

  public static class Balance {
    public BigDecimal amount;
    public String currency;
  }
}
