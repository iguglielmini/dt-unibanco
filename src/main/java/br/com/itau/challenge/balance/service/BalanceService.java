package br.com.itau.challenge.balance.service;

import br.com.itau.challenge.balance.dto.BalanceResponse;

import java.util.UUID;

public interface BalanceService {
    BalanceResponse getBalanceByAccountId(UUID accountId);
}
