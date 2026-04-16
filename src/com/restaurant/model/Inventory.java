package com.restaurant.model;

/**
 * INVENTORY MODEL - Matches class diagram: Inventory class
 */
public class Inventory {
    private int inventoryId;
    private String itemName;
    private int quantity;

    public Inventory() {}

    public Inventory(int inventoryId, String itemName, int quantity) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    // Methods from class diagram
    public void updateStock(int qty) {
        this.quantity = qty;
        System.out.println("[Inventory] " + itemName + " stock updated to: " + qty);
    }

    public int checkStock() {
        return this.quantity;
    }

    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return itemName + " (qty: " + quantity + ")";
    }
}
