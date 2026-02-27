package com.smartcart.inventoryservice.services;

import com.smartcart.inventoryservice.dtos.*;

public interface InventoryService {
    InventoryResponseDto createInventory(CreateInventoryDto createInventoryDto);
    StockCheckDto checkStock(Long variantId);
    InventoryResponseDto reserveStock(StockOperationRequestDto stockOperationRequestDto);
    InventoryResponseDto releaseStock(StockOperationRequestDto stockOperationRequestDto);
    InventoryResponseDto confirmReservation(StockOperationRequestDto dto);
    InventoryResponseDto Restock(Long variantId, StockOperationRequestDto dto);
    InventoryResponseDto Deactivate(Long variantId);
}
