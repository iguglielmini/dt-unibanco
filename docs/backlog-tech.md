# Backlog técnico / Trade-offs

## Implementado

- API GET /balances/{accountId} (idempotente)
- Consumer SQS (assíncrono, delete-on-success, DLQ sugerida)
- Persistência Postgres (Hikari, índices por account_id)
- Observabilidade: /healthz e /readyz; logs estruturados (opcional)

## Não implementado / Próximos passos

- **DLQ SQS**: configurar redrive policy (3 tentativas).
- **Idempotência**: chave natural (transaction_id) para ignorar duplicados.
- **Circuit breaker** no consumer (Resilience4j).
- **Métricas custom** (Micrometer) por fila/latência.
- **Cache de leitura** (Redis) para GET muito frequente.
- **Migração Flyway** (substituir `ddl-auto=update`).
- **Teste de carga** (Gatling/k6) + tuning Hikari.
- **Security**: tokens de serviço, WAF no API Gateway.

## Decisões (ADR)

- **DB relacional (Postgres)**: necessidade de consistência e queries simples.  
- **SQS**: desacoplamento produtor/consumidor e burst control.  
- **Spring Boot 3 + Java 21**: suporte LTS e nativo a Observabilidade/Records.
