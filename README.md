# dt-unibanco

Projeto desenvolvido em Java com Spring Boot para o desafio do Itaú Unibanco.

## Descrição
Este projeto é uma aplicação backend que expõe funcionalidades relacionadas a saldo bancário, seguindo boas práticas de arquitetura e desenvolvimento Java.

## Estrutura do Projeto

```
src/
  main/
    java/br/com/itau/challenge/balance/  # Código fonte principal
    resources/                           # Arquivos de configuração
  test/
    java/br/com/itau/challenge/balance/  # Testes automatizados
```

## Pré-requisitos
- Java 21 ou superior
- Maven 3.8+
- Docker (opcional, para execução via docker-compose)

## Como executar

### Usando Maven
```bash
./mvn spring-boot:run
```
ou
```bash
mvn spring-boot:run
```

### Usando Docker Compose
```bash
docker-compose up --build
```

## Executando os Testes
```bash
./mvn test
```
ou
```bash
mvn test
```

## Configuração
As configurações da aplicação estão no arquivo `src/main/resources/application.properties`.

## Estrutura dos Principais Arquivos
- `BalanceApplication.java`: Classe principal da aplicação Spring Boot.
- `application.properties`: Configurações da aplicação.
- `BalanceApplicationTests.java`: Testes automatizados.

## Contato
Em caso de dúvidas, entre em contato com o responsável pelo projeto.
