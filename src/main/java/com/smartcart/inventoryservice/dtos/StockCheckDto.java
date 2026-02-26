package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockCheckDto {
    private Long variantId;
    private Integer availableStock;
    private boolean active;
}
