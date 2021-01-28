package com.chandan.billing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chandan.billing.config.BillingServiceConfig;
import com.chandan.billing.config.TaxType;
import com.chandan.billing.entity.ItemRequest;
import com.chandan.billing.entity.ItemResponse;

@Service
public class BillingService {
    @Autowired
    private BillingServiceConfig config;

    public ItemResponse getPrice(ItemRequest itemRequest) {
        final Map<String, BillingServiceConfig.TaxEntity> taxConfig = config.getTaxConfig();
        final List<BillingServiceConfig.TaxEntity> percentageTaxEntityList = new ArrayList<>();
        final List<BillingServiceConfig.TaxEntity> fixedTaxEntityList = new ArrayList<>();
        taxConfig.entrySet().stream().forEach(c -> {
            final BillingServiceConfig.TaxEntity value = c.getValue();
            if (value.getType().equals(TaxType.percentage)) {
                percentageTaxEntityList.add(value);
            }
            if (value.getType().equals(TaxType.fixed)) {
                fixedTaxEntityList.add(value);
            }
        });
        final Double percentageTaxPrice = setPercentageTaxPrice(itemRequest.getBasePrice(), percentageTaxEntityList);
        System.out.println("percentageTaxPrice = " + percentageTaxPrice);

        final Double fixedTaxPrice = setFixedTaxPrice(percentageTaxPrice, fixedTaxEntityList);
        System.out.println("fixedTaxPrice = " + fixedTaxPrice);

        final Double discountPrice = setDiscountPrice(itemRequest.getBasePrice(), itemRequest.getDiscount());
        System.out.println("priceAfterDiscount = " + discountPrice);

        final double finalPrice = fixedTaxPrice - discountPrice;

        return ItemResponse.builder().item(itemRequest.getItemName()).basePrice(itemRequest.getBasePrice()).finalPrice(finalPrice).build();
    }

    private Double setPercentageTaxPrice(Double basePrice, List<BillingServiceConfig.TaxEntity> percentageTaxEntityList) {
        final List<BillingServiceConfig.TaxEntity> taxEntities = percentageTaxEntityList.stream().filter(i -> {
            final BillingServiceConfig.TaxEntity taxEntity = i;
            if (i.getMaxPrice() == null && i.getMinPrice() <= basePrice) {
                return true;
            } else if (i.getMinPrice() <= basePrice && basePrice <= i.getMaxPrice()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        final BillingServiceConfig.TaxEntity taxEntity = taxEntities.get(0);

        if (taxEntities != null) {
            final Double tax = taxEntity.getTax();
            final Double percentage = (tax / Double.valueOf(100));
            final Double result = basePrice * percentage;
            return basePrice + result;
        }
        return basePrice;
    }

    private Double setFixedTaxPrice(Double basePrice, List<BillingServiceConfig.TaxEntity> fixedTaxEntityList) {
        final BillingServiceConfig.TaxEntity taxEntity = fixedTaxEntityList.get(0);
        if (taxEntity != null) {
            return taxEntity.getTax() + basePrice;
        }
        return basePrice;
    }

    private Double setDiscountPrice(Double basePrice, Double discount) {
        final Double percentage = (discount / Double.valueOf(100));
        final double discountPrice = basePrice * percentage;
        return discountPrice;
    }
}
