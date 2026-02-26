package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveStockRequestDto {
    private Long variantId;
    private Integer quantity;
}
