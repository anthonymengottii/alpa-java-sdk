package com.alpa.examples;

import com.fasterxml.jackson.databind.JsonNode;
import com.alpa.AlpaClient;

public class Main {
    public static void main(String[] args) throws Exception {
        // Nunca deixe a API Key hardcoded em código-fonte.
        // Defina a variável de ambiente ALPA_API_KEY antes de executar.
        String apiKey = System.getenv("ALPA_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Environment variable ALPA_API_KEY is required to run this example.");
        }
        String baseUrl = System.getenv("ALPA_BASE_URL");
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://alpa-sistema-api.onrender.com";
        }

        System.out.println("Testando SDK Alpa Java...\n");
        System.out.println("Base URL: " + baseUrl + "\n");

        AlpaClient alpa = new AlpaClient(apiKey, baseUrl, "v1", 30);

        // Teste 1: Listar Payment Links
        System.out.println("Teste 1: Listar Payment Links...");
        JsonNode links = alpa.paymentLinks.list(1, 5);
        System.out.println(links.toPrettyString());

        // Teste 2: Listar Produtos
        System.out.println("\nTeste 2: Listar Produtos...");
        JsonNode products = alpa.products.list(1, 5);
        System.out.println(products.toPrettyString());

        System.out.println("\n[OK] Testes do SDK Java concluídos.");
    }
}
