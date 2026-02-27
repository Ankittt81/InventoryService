package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockOperationRequestDto {
   private Long variantId;
   private Integer quantity;
}
