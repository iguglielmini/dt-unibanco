package br.com.itau.challenge.balance.controller;

import br.com.itau.challenge.balance.dto.BalanceResponse;
import br.com.itau.challenge.balance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService service;

    @GetMapping("/{accountId}")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable UUID accountId) {
        return ResponseEntity.ok(service.getBalanceByAccountId(accountId));
    }
}
