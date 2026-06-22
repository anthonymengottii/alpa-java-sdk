<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="logo/light.png">
    <source media="(prefers-color-scheme: light)" srcset="logo/dark.png">
    <img src="logo/dark.png" alt="Alpa" height="60">
  </picture>
</p>

# Alpa Java SDK

SDK oficial da Alpa para Java. Integre PIX, cartão de crédito e boleto. Compatível com Java 17+ e qualquer projeto Maven/Gradle.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 17+](https://img.shields.io/badge/java-17+-blue.svg)](https://www.java.com/downloads)

## 📦 Requisitos

- Java 17+
- Maven 3.8+
- Uma API Key da Alpa

## 🚀 Instalação

```bash
git clone https://github.com/anthonymengottii/alpa-java-sdk
cd alpa-java-sdk && mvn install
```

## 🔑 Configuração básica

```java
import com.alpa.AlpaClient;

AlpaClient alpa = new AlpaClient(System.getenv("ALPA_API_KEY"));
// Ou com opções:
// new AlpaClient(apiKey, "https://alpa-sistema-api.onrender.com", "v1", 30);
```

> O ambiente (desenvolvimento ou produção) é determinado pela **chave** usada.

## 💳 Payment Links

```java
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

JsonNode link = alpa.paymentLinks.create(
    "Produto Premium",
    9900, // amountCents (R$ 99,00)
    Map.of("description", "Acesso vitalício")
);

// A resposta traz o slug; monte a URL do checkout a partir dele:
System.out.println("Checkout: https://checkout.usealpa.com/pay/" + link.path("slug").asText());
```

## 📦 Produtos / 💳 Transações / 🎫 Cupons

```java
JsonNode products = alpa.products.list(1, 10);
JsonNode transactions = alpa.transactions.list(1, 10);
JsonNode validation = alpa.coupons.validate("CUPOM10", 9900);
```

## 🌐 Webhooks

A Alpa assina cada webhook com **HMAC-SHA256 (hex)** no header `X-Webhook-Signature`. O envelope tem o formato `{ id, type, data, timestamp, subscription }`.

```java
import com.alpa.utils.WebhookUtils;

String payload = requestBody;                              // JSON bruto recebido
String signature = request.getHeader("X-Webhook-Signature");
String secret = System.getenv("ALPA_WEBHOOK_SECRET");

if (!WebhookUtils.verifySignature(payload, signature, secret)) {
    response.setStatus(401);
    return;
}
// processar evento (WebhookUtils.EVENT_TRANSACTION_COMPLETED, etc.)
```

## 📝 Tratamento de erros

Erros HTTP são encapsulados em `AlpaException`:

```java
import com.alpa.utils.AlpaException;

try {
    JsonNode links = alpa.paymentLinks.list(1, 10);
} catch (AlpaException e) {
    System.err.println("Erro: " + e.getMessage());
    System.err.println("Status: " + e.getStatus());
    System.err.println("Code: " + e.getCode());
}
```

## 🧪 Testes

```bash
mvn test
```

## 🔗 Links úteis

- [Documentação](https://docs.usealpa.com)
- [Dashboard](https://app.usealpa.com)
- [Suporte](mailto:suporte@usealpa.com)
- [Repositório](https://github.com/anthonymengottii/alpa-java-sdk)

## 📄 Licença

MIT
