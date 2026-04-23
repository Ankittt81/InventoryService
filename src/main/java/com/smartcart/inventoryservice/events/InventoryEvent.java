package com.smartcart.inventoryservice.events;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InventoryEvent {
    private Long orderId;
    private String paymentStatus; // SUCCESS / FAILED
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long variantId;
        private int quantity;

        public Item() {}

        public Item(Long variantId, int quantity) {
            this.variantId = variantId;
            this.quantity = quantity;
        }

    }

    public InventoryEvent() {}

    public InventoryEvent(String paymentStatus, List<Item> items) {
        this.paymentStatus = paymentStatus;
        this.items = items;
    }

}
