package br.com.itau.challenge.balance.mapper;

import br.com.itau.challenge.balance.dto.BalanceResponse;
import br.com.itau.challenge.balance.model.Account;
import org.springframework.stereotype.Component;

@Component
public class BalanceMapper {

    public BalanceResponse toDto(Account account) {
        return new BalanceResponse(
                account.getId(),
                account.getOwner(),
                new BalanceResponse.Amount(account.getAmount(), account.getCurrency()),
                account.getUpdatedAt());
    }
}