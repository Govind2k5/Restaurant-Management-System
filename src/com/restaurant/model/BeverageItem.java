package com.restaurant.model;

/**
 * BEVERAGE ITEM - Concrete subclass used by Factory Pattern (Member 1)
 */
public class BeverageItem extends MenuItem {

    public BeverageItem() {
        super();
    }

    public BeverageItem(int itemId, int menuId, String name, double price, boolean available) {
        super(itemId, menuId, name, price, available, "BEVERAGE");
    }
}
