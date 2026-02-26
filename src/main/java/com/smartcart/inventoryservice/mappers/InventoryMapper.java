package com.smartcart.inventoryservice.mappers;

import com.smartcart.inventoryservice.dtos.CreateInventoryDto;
import com.smartcart.inventoryservice.dtos.InventoryResponseDto;
import com.smartcart.inventoryservice.dtos.ReserveStockResponseDto;
import com.smartcart.inventoryservice.dtos.StockCheckDto;
import com.smartcart.inventoryservice.models.Inventory;
import org.springframework.stereotype.Component;


@Component
public class InventoryMapper {

    public Inventory toEntity(CreateInventoryDto dto) {
        Inventory inventory = new Inventory();
        inventory.setVariantId(dto.getVariantId());
        inventory.setSku(dto.getSku());
        inventory.setAvailableStock(dto.getInitialStock());
        inventory.setActive(true);

        return inventory;
    }

    public InventoryResponseDto toResponse(Inventory inventory) {
        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
        inventoryResponseDto.setInventoryId(inventory.getId());
        inventoryResponseDto.setVariantId(inventory.getVariantId());
        inventoryResponseDto.setSku(inventory.getSku());
        inventoryResponseDto.setAvailableStock(inventory.getAvailableStock());
        inventoryResponseDto.setActive(inventory.isActive());
        return inventoryResponseDto;
    }

    public StockCheckDto toStockDto(Inventory inventory) {
        StockCheckDto stockCheckDto = new StockCheckDto();
        stockCheckDto.setActive(inventory.isActive());
        stockCheckDto.setAvailableStock(inventory.getAvailableStock());
        stockCheckDto.setVariantId(inventory.getVariantId());
        return stockCheckDto;
    }

    public ReserveStockResponseDto toReserveDto(Inventory inventory) {
        ReserveStockResponseDto reserveStockResponseDto = new ReserveStockResponseDto();
        reserveStockResponseDto.setVariantId(inventory.getVariantId());
        reserveStockResponseDto.setReservedQuantity(inventory.getReservedStock());
        reserveStockResponseDto.setRemainingStock(inventory.getAvailableStock());
        return reserveStockResponseDto;
    }
}
