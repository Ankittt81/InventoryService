package com.smartcart.inventoryservice.services;

import com.smartcart.inventoryservice.dtos.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface InventoryService {
    InventoryResponseDto createInventory(CreateInventoryDto createInventoryDto);
    StockCheckDto checkStock(Long variantId);
    ReserveStockResponseDto reserveStock(ReserveStockRequestDto reserveStockRequestDto);
    InventoryResponseDto releaseStock(ReleaseStockRequestDto releaseStockRequestDto);
    InventoryResponseDto confirmReservation(ConfirmReservationDto dto);
}
