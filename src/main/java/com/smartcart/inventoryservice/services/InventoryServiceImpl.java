package com.smartcart.inventoryservice.services;

import com.smartcart.inventoryservice.dtos.*;
import com.smartcart.inventoryservice.exceptions.InventoryNotFoundException;
import com.smartcart.inventoryservice.exceptions.NotEnoughStockException;
import com.smartcart.inventoryservice.mappers.InventoryMapper;
import com.smartcart.inventoryservice.models.Inventory;
import com.smartcart.inventoryservice.repositories.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{
    private InventoryRepository inventoryRepository;
    private InventoryMapper  inventoryMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }


    @Override
    public InventoryResponseDto createInventory(CreateInventoryDto dto) {
        Optional<Inventory> existing=inventoryRepository.findByVariantId(dto.getVariantId());
        //Idempotent
        if(existing.isPresent()){
           return inventoryMapper.toResponse(existing.get());
        }

        Inventory inventory=inventoryMapper.toEntity(dto);
        inventory=inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public StockCheckDto checkStock(Long variantId) {
        Optional<Inventory> existing=inventoryRepository.findByVariantId(variantId);
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }

        Inventory inventory=existing.get();
        return inventoryMapper.toStockDto(inventory);
    }

    @Override
    public ReserveStockResponseDto reserveStock(ReserveStockRequestDto reserveStockRequestDto) {
        Optional<Inventory>  existing=inventoryRepository.findByVariantId(reserveStockRequestDto.getVariantId());
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }
        Inventory inventory=existing.get();
        Integer stock=inventory.getAvailableStock();
        Integer quantity=reserveStockRequestDto.getQuantity();
        if(stock<quantity){
            throw new NotEnoughStockException("Not enough stock");
        }
        inventory.setAvailableStock(stock-quantity);
        inventory.setReservedStock(inventory.getReservedStock()+ quantity);
        inventoryRepository.save(inventory);
        return inventoryMapper.toReserveDto(inventory);
    }

    @Override
    public InventoryResponseDto releaseStock(ReleaseStockRequestDto releaseStockRequestDto) {
        Optional<Inventory>  existing=inventoryRepository.findByVariantId(releaseStockRequestDto.getVariantId());
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }
        Inventory inventory=existing.get();
        Integer quantity=releaseStockRequestDto.getQuantity();
        inventory.setReservedStock(inventory.getReservedStock()-quantity);
        inventory.setAvailableStock(inventory.getAvailableStock()+quantity);
        inventory=inventoryRepository.save(inventory);
        return  inventoryMapper.toResponse(inventory);
    }

    @Override
    public InventoryResponseDto confirmReservation(ConfirmReservationDto dto) {
        Optional<Inventory>  existing=inventoryRepository.findByVariantId(dto.getVariantId());
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }
        Inventory inventory=existing.get();
        inventory.setReservedStock(inventory.getReservedStock()-dto.getQuantity());
        return inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }
}
