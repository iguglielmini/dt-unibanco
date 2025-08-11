package br.com.itau.challenge.balance.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final HealthCheckService health;

    public HealthController(HealthCheckService health) {
        this.health = health;
    }

    // Liveness: app está de pé (sem dependências externas)
    @GetMapping("/healthz")
    public ResponseEntity<Map<String, Object>> healthz() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    // Readiness: app pronto para tráfego (checa DB e SQS)
    @GetMapping("/readyz")
    public ResponseEntity<Map<String, Object>> readyz() {
        var db = health.checkDb();
        var sqs = health.checkSqs();

        boolean up = "UP".equals(db.get("status")) && "UP".equals(sqs.get("status"));
        var body = Map.of("status", up ? "UP" : "DOWN", "db", db, "sqs", sqs);

        return new ResponseEntity<>(body, up ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE);
    }
}
