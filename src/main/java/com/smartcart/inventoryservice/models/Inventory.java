package com.smartcart.inventoryservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "inventroy")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "variant_id", nullable = false, unique = true)
    private Long variantId;
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;
    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;
    @Column(name = "reserved_stock",nullable = false)
    private Integer reservedStock=0;
    @Column(name = "isActive", nullable = false)
    private boolean active;
    @Version
    private Integer version;
    
    private LocalDateTime  createdAt;
    private LocalDateTime updatedAt;
}
