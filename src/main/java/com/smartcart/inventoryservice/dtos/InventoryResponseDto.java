package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponseDto {
    private Long inventoryId;
    private Long variantId;
    private String sku;
    private Integer availableStock;
    private Integer reservedStock;
    private boolean active;
}
