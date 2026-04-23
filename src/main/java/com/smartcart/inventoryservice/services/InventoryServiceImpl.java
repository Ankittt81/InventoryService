package com.smartcart.inventoryservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcart.inventoryservice.dtos.*;
import com.smartcart.inventoryservice.events.InventoryEvent;
import com.smartcart.inventoryservice.exceptions.InventoryNotFoundException;
import com.smartcart.inventoryservice.exceptions.NotEnoughStockException;
import com.smartcart.inventoryservice.mappers.InventoryMapper;
import com.smartcart.inventoryservice.models.Inventory;
import com.smartcart.inventoryservice.repositories.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{
    private InventoryRepository inventoryRepository;
    private InventoryMapper  inventoryMapper;
    private ObjectMapper objectMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper, ObjectMapper objectMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.objectMapper = objectMapper;
    }
@KafkaListener(topics = "Variant-Created", groupId = "inventory-group")
public void consumerVariantCreated(String message){
        try{
            CreateInventoryDto dto = objectMapper.readValue(message, CreateInventoryDto.class);
            createInventory(dto);
        }catch (Exception e){
            throw new RuntimeException("Message cannot be parsed!");
        }

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
        System.out.println("Create Inventory Successfully");
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public InventoryResponseDto checkStock(Long variantId) {
        Optional<Inventory> existing=inventoryRepository.findByVariantId(variantId);
        if(existing.isEmpty()){
            throw new InventoryNotFoundException("Inventory not found");
        }

        Inventory inventory=existing.get();
        System.out.println("Check Stock Successfully");
        return inventoryMapper.toResponse(inventory);
    }

    @Transactional
    @Override
    public boolean reserveStock(StockOperationRequestDto stockOperationRequestDto) {
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
        System.out.println("Reserve "+quantity+" of variant "+inventory.getVariantId()+" Stock Successfully");
        System.out.println("Now! variant "+inventory.getVariantId()+" Inventory have "+inventory.getReservedStock()+" stocks reserved");
        return true;
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


    @KafkaListener(topics = "inventory-topic", groupId = "inventory-group")
    public void handleInventory(String message) throws Exception {
        try{
            InventoryEvent event = objectMapper.readValue(message, InventoryEvent.class);

            for (InventoryEvent.Item item : event.getItems()) {

                if ("SUCCESS".equals(event.getPaymentStatus())) {
                    confirmReservation(new StockOperationRequestDto(item.getVariantId(), item.getQuantity()));
                } else {
                    releaseStock(new StockOperationRequestDto(item.getVariantId(), item.getQuantity()));
                }
            }
            System.out.println("Inventory updated Successfully for orderId: "+event.getOrderId());
        }catch (Exception e){
            throw new Exception("message cannot be parsed!");
        }
    }
}
