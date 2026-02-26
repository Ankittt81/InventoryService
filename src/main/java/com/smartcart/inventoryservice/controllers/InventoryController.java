package com.smartcart.inventoryservice.controllers;

import com.smartcart.inventoryservice.dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @PostMapping()
    public ResponseEntity<InventoryResponseDto> createInventory(@RequestBody CreateInventoryDto createInventoryDto) {
        return null;
    }

    @GetMapping("/{variantId}")
    public StockCheckDto checkStock(@PathVariable Long variantId) {
        return null;
    }

    @PostMapping("/reserve")
    public ReserveStockResponseDto reserveStock(@RequestBody ReserveStockRequestDto  reserveStockRequestDto) {
        return null;
    }

    @PostMapping("/release")
    public InventoryResponseDto releaseStock(@RequestBody ReleaseStockRequestDto releaseStockRequestDto) {
        return null;
    }

    @PostMapping("/confirm")
    public InventoryResponseDto confirmReservation(@RequestBody ConfirmReservationDto confirmReservationDto) {
        return null;
    }

}
