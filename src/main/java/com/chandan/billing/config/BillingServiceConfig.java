package com.chandan.billing.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties (prefix = "billing")
@Getter
@Setter
@ToString
public class BillingServiceConfig {
    Map<String, TaxEntity> taxConfig;

    @Getter
    @Setter
    @ToString
    public static class TaxEntity {
        Double minPrice;

        Double maxPrice;

        Double tax;

        TaxType type;
    }
}
