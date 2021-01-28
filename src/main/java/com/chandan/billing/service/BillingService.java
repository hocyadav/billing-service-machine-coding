package com.chandan.billing.service;

import com.chandan.billing.entity.ItemRequest;
import com.chandan.billing.entity.ItemResponse;

/**
 * @author HariomYadav
 * @since 28/01/21
 */
public interface BillingService {
    ItemResponse getItemsPrice(final ItemRequest request);
}
