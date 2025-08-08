package br.com.itau.challenge.balance.mapper;

import br.com.itau.challenge.balance.dto.BalanceResponse;
import br.com.itau.challenge.balance.model.Account;
import org.springframework.stereotype.Component;

@Component
public class BalanceMapper {

    public BalanceResponse toDto(Account account) {
        return BalanceResponse.builder()
                .id(account.getId())
                .owner(account.getOwner())
                .updated_at(account.getUpdatedAt())
                .balance(BalanceResponse.Amount.builder()
                        .amount(account.getAmount())
                        .currency(account.getCurrency())
                        .build())
                .build();
    }
}
