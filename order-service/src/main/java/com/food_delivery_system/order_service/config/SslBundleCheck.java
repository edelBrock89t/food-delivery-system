package com.food_delivery_system.order_service.config;

import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.stereotype.Component;

import javax.net.ssl.X509TrustManager;
import java.util.Arrays;

@Component
class SslBundleCheck {
    SslBundleCheck(SslBundles sslBundles) {
        SslBundle bundle = sslBundles.getBundle("payment-service-client");
        Arrays.stream(bundle.getManagers().getTrustManagerFactory().getTrustManagers())
                .forEach(tm -> {
                    if (tm instanceof X509TrustManager x509) {
                        Arrays.stream(x509.getAcceptedIssuers())
                                .forEach(cert -> System.out.println("Trusted issuer: " + cert.getSubjectX500Principal()));
                    }
                });
    }
}
