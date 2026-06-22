package com.alpa.utils;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebhookUtilsTest {

    private static String hmacHex(String payload, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(hash.length * 2);
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    @Test
    void verifiesValidSignatureWithPrefix() throws Exception {
        String secret = "test-secret";
        String payload = "{\"type\":\"transaction.completed\"}";
        String sig = hmacHex(payload, secret);
        assertTrue(WebhookUtils.verifySignature(payload, "sha256=" + sig, secret));
    }

    @Test
    void verifiesValidSignatureWithoutPrefix() throws Exception {
        String secret = "test-secret";
        String payload = "{\"a\":1}";
        assertTrue(WebhookUtils.verifySignature(payload, hmacHex(payload, secret), secret));
    }

    @Test
    void rejectsInvalidSignature() {
        assertFalse(WebhookUtils.verifySignature("{}", "bad", "secret"));
    }

    @Test
    void rejectsEmptyInputs() {
        assertFalse(WebhookUtils.verifySignature("", "sig", "secret"));
        assertFalse(WebhookUtils.verifySignature("{}", "", "secret"));
        assertFalse(WebhookUtils.verifySignature("{}", "sig", ""));
    }

    @Test
    void eventConstantsAlignedToBackend() {
        assertEquals("transaction.completed", WebhookUtils.EVENT_TRANSACTION_COMPLETED);
        assertEquals("balance.updated", WebhookUtils.EVENT_BALANCE_UPDATED);
        assertEquals("withdrawal.requested", WebhookUtils.EVENT_WITHDRAWAL_REQUESTED);
    }
}
