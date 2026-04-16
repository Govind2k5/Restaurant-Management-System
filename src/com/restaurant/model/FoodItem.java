package com.restaurant.model;

/**
 * FOOD ITEM - Concrete subclass used by Factory Pattern (Member 1)
 */
public class FoodItem extends MenuItem {

    public FoodItem() {
        super();
    }

    public FoodItem(int itemId, int menuId, String name, double price, boolean available) {
        super(itemId, menuId, name, price, available, "FOOD");
    }
}
