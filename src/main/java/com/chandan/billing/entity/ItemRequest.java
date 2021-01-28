package com.chandan.billing.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequest {
    String itemName;

    Double basePrice;

    Double discount;
}
