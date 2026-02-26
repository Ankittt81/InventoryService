package com.smartcart.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmReservationDto {
   private Long variantId;
   private Integer quantity;
}
