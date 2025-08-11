package br.com.itau.challenge.balance.service;

public interface BalanceUpdateService {
  void applyMessage(String sqsBodyJson);
}
