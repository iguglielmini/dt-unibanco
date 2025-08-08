package br.com.itau.challenge.balance.service.impl;

import br.com.itau.challenge.balance.dto.BalanceResponse;
import br.com.itau.challenge.balance.exception.NotFoundException;
import br.com.itau.challenge.balance.mapper.BalanceMapper;
import br.com.itau.challenge.balance.repository.AccountRepository;
import br.com.itau.challenge.balance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final AccountRepository repository;
    private final BalanceMapper mapper;

    @Override
    public BalanceResponse getBalanceByAccountId(UUID accountId) {
        return repository.findById(accountId)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }
}
