package com.smartcart.inventoryservice.services;

import com.smartcart.inventoryservice.dtos.*;
import com.smartcart.inventoryservice.exceptions.InventoryNotFoundException;
import com.smartcart.inventoryservice.exceptions.NotEnoughStockException;
import com.smartcart.inventoryservice.mappers.InventoryMapper;
import com.smartcart.inventoryservice.models.Inventory;
import com.smartcart.inventoryservice.repositories.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{
    private InventoryRepository inventoryRepository;
    private InventoryMapper  inventoryMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }


    @Transactional
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

    @Transactional
    @Override
    public InventoryResponseDto reserveStock(StockOperationRequestDto stockOperationRequestDto) {
        Optional<Inventory>  existing=inventoryRepository.findByVariantId(stockOperationRequestDto.getVariantId());
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }
        Inventory inventory=existing.get();
        if(!inventory.isActive()){
            throw new RuntimeException("Inactive inventory");
        }
        Integer stock=inventory.getAvailableStock();
        Integer quantity=stockOperationRequestDto.getQuantity();
        if(stock<quantity){
            throw new NotEnoughStockException("Not enough stock");
        }
        inventory.setAvailableStock(stock-quantity);
        inventory.setReservedStock(inventory.getReservedStock()+ quantity);
        inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(inventory);
    }

    @Transactional
    @Override
    public InventoryResponseDto releaseStock(StockOperationRequestDto stockOperationRequestDto) {
        Optional<Inventory>  existing=inventoryRepository.findByVariantId(stockOperationRequestDto.getVariantId());
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }
        Inventory inventory=existing.get();
        Integer quantity=stockOperationRequestDto.getQuantity();
        if(inventory.getReservedStock()<quantity){
            throw new IllegalStateException("Invalid release request");
        }
        inventory.setReservedStock(inventory.getReservedStock()-quantity);
        inventory.setAvailableStock(inventory.getAvailableStock()+quantity);
        return  inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }

    @Transactional
    @Override
    public InventoryResponseDto confirmReservation(StockOperationRequestDto dto) {
        Optional<Inventory>  existing=inventoryRepository.findByVariantId(dto.getVariantId());
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }
        Inventory inventory=existing.get();
        if(inventory.getReservedStock()< dto.getQuantity()){
            throw new IllegalStateException("Invalid confirm");
        }
        inventory.setReservedStock(inventory.getReservedStock()-dto.getQuantity());
        return inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }

    @Override
    public InventoryResponseDto Restock(Long variantId, StockOperationRequestDto dto) {
        Optional<Inventory>  existing=inventoryRepository.findByVariantId(variantId);
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }
        Inventory inventory=existing.get();
        inventory.setAvailableStock(inventory.getAvailableStock()+dto.getQuantity());
        return inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }

    @Override
    public InventoryResponseDto Deactivate(Long variantId) {
        Optional<Inventory>  existing=inventoryRepository.findByVariantId(variantId);
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }
        Inventory inventory=existing.get();
        inventory.setActive(false);
        return inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }


}
