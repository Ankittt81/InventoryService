package com.smartcart.inventoryservice.controllers;

import com.smartcart.inventoryservice.dtos.*;
import com.smartcart.inventoryservice.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private InventoryService inventoryService;


    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping()
    public ResponseEntity<InventoryResponseDto> createInventory(@RequestBody CreateInventoryDto createInventoryDto) {
        return ResponseEntity.ok(inventoryService.createInventory(createInventoryDto));
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<StockCheckDto>  checkStock(@PathVariable Long variantId) {
        return ResponseEntity.ok(inventoryService.checkStock(variantId));
    }

    @PostMapping("/reserve")
    public ResponseEntity<InventoryResponseDto> reserveStock(@RequestBody StockOperationRequestDto stockOperationRequestDto) {
        return ResponseEntity.ok(inventoryService.reserveStock(stockOperationRequestDto));
    }

    @PostMapping("/release")
    public ResponseEntity<InventoryResponseDto> releaseStock(@RequestBody StockOperationRequestDto stockOperationRequestDto) {
        return ResponseEntity.ok(inventoryService.releaseStock(stockOperationRequestDto));
    }

    @PostMapping("/confirm")
    public  ResponseEntity<InventoryResponseDto> confirmReservation(@RequestBody StockOperationRequestDto stockOperationRequestDto) {
        return ResponseEntity.ok(inventoryService.confirmReservation(stockOperationRequestDto));
    }

    @PatchMapping("/{variantId}/restock")
    public ResponseEntity<InventoryResponseDto> Restock(@PathVariable Long variantId, @RequestBody StockOperationRequestDto stockOperationRequestDto) {
        return ResponseEntity.ok(inventoryService.Restock(variantId, stockOperationRequestDto));
    }

    @PostMapping("/{variantId}/deactivate")
    public ResponseEntity<InventoryResponseDto> Deactivate(@PathVariable Long variantId) {
        return ResponseEntity.ok(inventoryService.Deactivate(variantId));
    }
}
