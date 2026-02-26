package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveStockResponseDto {
    private Long variantId;
    private Integer reservedQuantity;
    private Integer remainingStock;
}
