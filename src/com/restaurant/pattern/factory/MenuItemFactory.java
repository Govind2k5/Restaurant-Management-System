package com.restaurant.pattern.factory;

import com.restaurant.model.BeverageItem;
import com.restaurant.model.FoodItem;
import com.restaurant.model.MenuItem;

/**
 * FACTORY PATTERN (Member 1 - Admin)
 * Cleanly instantiates different types of MenuItem (FoodItem vs BeverageItem)
 * without exposing creation logic to the caller.
 *
 * Usage:
 *   MenuItem item = MenuItemFactory.createMenuItem(MenuItemType.FOOD, 0, 1, "Pasta", 10.99, true);
 */
public class MenuItemFactory {

    // Prevent instantiation - this is a utility/factory class
    private MenuItemFactory() {}

    /**
     * Creates a MenuItem of the specified type.
     *
     * @param type      FOOD or BEVERAGE
     * @param itemId    Database ID (0 for new items)
     * @param menuId    Parent menu ID
     * @param name      Item name
     * @param price     Item price
     * @param available Availability status
     * @return          Concrete MenuItem subclass
     */
    public static MenuItem createMenuItem(MenuItemType type, int itemId, int menuId,
                                          String name, double price, boolean available) {
        switch (type) {
            case FOOD:
                return new FoodItem(itemId, menuId, name, price, available);
            case BEVERAGE:
                return new BeverageItem(itemId, menuId, name, price, available);
            default:
                throw new IllegalArgumentException("Unknown menu item type: " + type);
        }
    }

    /**
     * Convenience method - creates MenuItem from a String type (for DB reads).
     */
    public static MenuItem createMenuItemFromString(String typeStr, int itemId, int menuId,
                                                     String name, double price, boolean available) {
        MenuItemType type = MenuItemType.valueOf(typeStr.toUpperCase());
        return createMenuItem(type, itemId, menuId, name, price, available);
    }
}
