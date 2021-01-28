package com.chandan.billing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chandan.billing.config.BillingServiceConfig;
import com.chandan.billing.config.TaxConfig;
import com.chandan.billing.contants.TaxType;
import com.chandan.billing.entity.ItemRequest;
import com.chandan.billing.entity.ItemResponse;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingServiceConfig config;

    @Override
    public ItemResponse getItemsPrice(final ItemRequest request) {
        log.debug("BillingServiceImpl.getItemsPrice");
        final List<TaxConfig> percentageTaxConfigList = new ArrayList<>();
        final List<TaxConfig> fixedTaxConfigList = new ArrayList<>();
        config.getTaxConfig().entrySet().stream().forEach(t -> {
            final TaxConfig taxConfigValue = t.getValue();
            if (taxConfigValue.getType().equals(TaxType.percentage)) {
                percentageTaxConfigList.add(taxConfigValue);
            } else if (taxConfigValue.getType().equals(TaxType.fixed)) {
                fixedTaxConfigList.add(taxConfigValue);
            }
        });
        final Double percentageTaxPrice = setPercentageTaxPrice(request.getBasePrice(), percentageTaxConfigList);
        log.info("percentageTaxPrice {}", percentageTaxPrice);

        final Double fixedTaxPrice = setFixedTaxPrice(percentageTaxPrice, fixedTaxConfigList);
        log.info("fixedTaxPrice {}", fixedTaxPrice);

        final Double discountPrice = setDiscountPrice(request.getBasePrice(), request.getDiscount());
        log.info("discountPrice {}", discountPrice);

        final double finalPrice = fixedTaxPrice - discountPrice;
        log.info("finalPrice {}", finalPrice);
        return ItemResponse.builder().item(request.getItemName()).basePrice(request.getBasePrice()).finalPrice(finalPrice).build();
    }

    private Double setPercentageTaxPrice(final Double basePrice, final List<TaxConfig> percentageTaxConfigs) {
        final List<TaxConfig> taxConfigs = percentageTaxConfigs.stream().filter(taxConfig -> {
            if (Objects.isNull(taxConfig.getMaxPrice()) && taxConfig.getMinPrice() <= basePrice) {
                return true;
            } else if (taxConfig.getMinPrice() <= basePrice && basePrice <= taxConfig.getMaxPrice()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        final TaxConfig taxConfig = taxConfigs.get(0);
        if (Objects.isNull(taxConfig)) {
            return basePrice;
        }
        final Double percentage = (taxConfig.getTax() / Double.valueOf(100));
        final Double result = basePrice * percentage;
        return basePrice + result;
    }

    private Double setFixedTaxPrice(final Double basePrice, final List<TaxConfig> fixedTaxConfigs) {
        final TaxConfig taxConfig = fixedTaxConfigs.get(0);
        if (Objects.isNull(taxConfig)) {
            return basePrice;
        }
        return taxConfig.getTax() + basePrice;
    }

    private Double setDiscountPrice(final Double basePrice, final Double discount) {
        if (Objects.isNull(discount)) {
            return Double.valueOf(0);
        }
        final Double percentage = (discount / Double.valueOf(100));
        return basePrice * percentage;
    }
}
