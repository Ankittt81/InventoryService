package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReleaseStockRequestDto {
    private Long variantId;
    private Integer quantity;
}
