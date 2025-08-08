package br.com.itau.challenge.balance.repository;

import br.com.itau.challenge.balance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
