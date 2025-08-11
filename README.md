# Balance API

Balance API é um serviço Java 21 com Spring Boot que consome mensagens de uma fila AWS SQS, processa transações e atualiza saldos em um banco PostgreSQL.

## 📌 Funcionalidades

- Consumo de mensagens de uma fila SQS (LocalStack ou AWS real)
- Processamento de transações financeiras
- Persistência dos saldos no banco de dados PostgreSQL
- Exposição de API REST com documentação Swagger

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- AWS SDK v2 (SQS)
- PostgreSQL
- LocalStack
- Maven
- SpringDoc OpenAPI (Swagger UI)

## 📦 Pré-requisitos

Antes de iniciar, instale:

- [Java 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- [AWS CLI](https://aws.amazon.com/cli/)

## ⚙️ Configuração do Ambiente

### 1. Subir serviços com Docker Compose
```bash
docker-compose up -d
```

Isso iniciará:
- PostgreSQL na porta 5432
- LocalStack na porta 4566

### 2. Criar fila SQS no LocalStack
```bash
export AWS_DEFAULT_REGION=sa-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test

aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name transacoes-financeiras-processadas
```

### 3. Configuração de variáveis de ambiente
No terminal:
```bash
export AWS_DEFAULT_REGION=sa-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
```

Ou configure no `application.yml`.

## ▶️ Rodando a Aplicação

### 1. Instalar dependências e compilar
```bash
mvn -s .mvn/settings.xml clean install
```

### 2. Executar a aplicação
```bash
mvn -s .mvn/settings.xml spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## 📖 Swagger UI

Após iniciar, acesse:
```
http://localhost:8080/swagger-ui.html
```

## 📨 Enviando mensagens para a fila

Crie um arquivo `msg.json`:
```json
{
    "id": "00035d0e-a72c-46c5-8706-4fcb702c3ac1",
    "amount": 50.00,
    "currency": "BRL"
}
```

Envie para a fila:
```bash
aws --endpoint-url=http://localhost:4566 sqs send-message   --queue-url http://sqs.sa-east-1.localhost.localstack.cloud:4566/000000000000/transacoes-financeiras-processadas   --message-body file://msg.json
```

## 📂 Estrutura do Projeto
```
src/
 ├── main/
 │   ├── java/br/com/itau/challenge/balance
 │   └── resources/
 ├── test/
 │   └── java/
 └── README.md
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie sua feature branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Faça push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📝 Licença
Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.
