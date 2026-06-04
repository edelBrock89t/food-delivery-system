package com.food_delivery_system.order_service.config.http_clients;

import com.food_delivery_system.order_service.external.PaymentHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class PaymentHttpClientConfig {

    @Value("${payment-service.base_url}")
    private String paymentServiceBaseUrl;

    @Bean
    public RestClient paymentRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(paymentServiceBaseUrl)
                .build();
    }

    @Bean
    public PaymentHttpClient paymentHttpClient(RestClient paymentRestClient) {
        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(paymentRestClient))
                .build()
                .createClient(PaymentHttpClient.class);
    }
}
