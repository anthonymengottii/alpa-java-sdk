package com.alpa;

import com.alpa.http.HttpClientWrapper;
import com.alpa.resources.PaymentLinksResource;
import com.alpa.resources.TransactionsResource;
import com.alpa.resources.ProductsResource;
import com.alpa.resources.CouponsResource;
import com.alpa.resources.SubscriptionsResource;
import com.alpa.resources.CheckoutsResource;
import com.alpa.resources.OffersResource;
import com.alpa.resources.WithdrawalsResource;
import com.alpa.resources.WalletResource;
import com.alpa.utils.WebhookUtils;

public class AlpaClient {

    private final HttpClientWrapper http;

    public final PaymentLinksResource paymentLinks;
    public final TransactionsResource transactions;
    public final ProductsResource products;
    public final CouponsResource coupons;
    public final SubscriptionsResource subscriptions;
    public final CheckoutsResource checkouts;
    public final OffersResource offers;
    public final WithdrawalsResource withdrawals;
    public final WalletResource wallet;

    public AlpaClient(String apiKey, String baseUrl, String version, int timeoutSeconds) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is required");
        }
        String url = baseUrl != null ? baseUrl : "https://alpa-sistema-api.onrender.com";
        this.http = new HttpClientWrapper(apiKey, url, version, timeoutSeconds);

        this.paymentLinks  = new PaymentLinksResource(http);
        this.transactions  = new TransactionsResource(http);
        this.products      = new ProductsResource(http);
        this.coupons       = new CouponsResource(http);
        this.subscriptions = new SubscriptionsResource(http);
        this.checkouts     = new CheckoutsResource(http);
        this.offers        = new OffersResource(http);
        this.withdrawals   = new WithdrawalsResource(http);
        this.wallet        = new WalletResource(http);
    }

    public AlpaClient(String apiKey) {
        this(apiKey, null, "v1", 30);
    }

    public boolean verifyWebhookSignature(String payload, String signature, String secret) {
        if (payload == null) {
            throw new IllegalArgumentException("payload must not be null");
        }
        if (signature == null) {
            throw new IllegalArgumentException("signature must not be null");
        }
        if (secret == null) {
            throw new IllegalArgumentException("secret must not be null");
        }
        return WebhookUtils.verifySignature(payload, signature, secret);
    }
}
