package com.chandan.billing.resorce;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.chandan.billing.entity.ItemRequest;
import com.chandan.billing.entity.ItemResponse;
import com.chandan.billing.service.BillingService;

@RestController
public class BillingResource {

    @Autowired
    private BillingService billingService;

    @PostMapping ("/")
    public List<ItemResponse> getPrice(@RequestBody List<ItemRequest> itemRequest) {
        List<ItemResponse> itemResponses = new ArrayList<>();
        itemRequest.forEach(i -> {
            final ItemResponse price = billingService.getPrice(i);
            itemResponses.add(price);
        });
        return itemResponses;
    }

}
