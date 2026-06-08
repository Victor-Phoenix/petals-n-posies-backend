package com.victor.petalsnposies.config;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class StripeConfig {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    @PostConstruct
    public void init() {
    	  System.out.println("Stripe key starts with: " + stripeSecretKey.substring(0, 15));
        Stripe.apiKey = stripeSecretKey;
    }
}