package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeactivateInventoryRequest {
    private Long variantId;
}
