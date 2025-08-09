package br.com.itau.challenge.balance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {
  @GetMapping("/__health")
  String ok() { return "ok"; }
}
