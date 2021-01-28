package com.chandan.billing.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties (prefix = "billing")
public class BillingServiceConfig {
    private Map<String, TaxConfig> taxConfig;
}
