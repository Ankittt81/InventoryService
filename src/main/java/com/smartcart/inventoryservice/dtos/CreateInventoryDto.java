package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInventoryDto {
    private Long variantId;
    private String sku;
    private Integer initialStock;
}
