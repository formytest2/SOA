package com.github.bluecatlee.gs4d.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "config")
public class PaymentConfig {

    private Map<String, String> paymentTypes;

}
