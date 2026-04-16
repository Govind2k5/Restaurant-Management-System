package com.restaurant.model;

/**
 * MENU ITEM BASE MODEL - Matches class diagram: MenuItem class
 * Used with Factory Pattern
 */
public abstract class MenuItem {
    private int itemId;
    private int menuId;
    private String name;
    private double price;
    private boolean available;
    private String itemType;

    public MenuItem() {}

    public MenuItem(int itemId, int menuId, String name, double price, boolean available, String itemType) {
        this.itemId = itemId;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.available = available;
        this.itemType = itemType;
    }

    // Method from class diagram
    public void updateAvailability(boolean status) {
        this.available = status;
        System.out.println("[MenuItem] " + name + " availability set to: " + status);
    }

    // Getters & Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price) + (available ? "" : " [OUT OF STOCK]");
    }
}
