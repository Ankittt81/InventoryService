package com.smartcart.inventoryservice.repositories;

import com.smartcart.inventoryservice.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
     Optional<Inventory> findByVariantId(Long variantId);
     Inventory save(Inventory inventory);
}
