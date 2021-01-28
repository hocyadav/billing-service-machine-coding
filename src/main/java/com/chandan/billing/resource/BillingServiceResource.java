package com.chandan.billing.resource;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.chandan.billing.entity.ItemRequest;
import com.chandan.billing.entity.ItemResponse;
import com.chandan.billing.service.BillingService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BillingServiceResource {

    @Autowired
    private BillingService billingService;

    @PostMapping ("/")
    public List<ItemResponse> getItemsPrice(@RequestBody final List<ItemRequest> itemRequests) {
        log.debug("BillingServiceResource.getItemsPrice");
        return itemRequests.stream().map(itemRequest -> billingService.getItemsPrice(itemRequest))
                           .collect(Collectors.toList());
    }
}
