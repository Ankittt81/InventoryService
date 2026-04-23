package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockOperationRequestDto {
   private Long variantId;
   private Integer quantity;

   public StockOperationRequestDto(Long variantId, Integer quantity) {
       this.variantId = variantId;
       this.quantity = quantity;
   }
}
