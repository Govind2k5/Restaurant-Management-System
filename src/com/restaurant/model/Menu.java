package com.restaurant.model;

import java.util.ArrayList;
import java.util.List;

/**
 * MENU MODEL - Matches class diagram: Menu class
 */
public class Menu {
    private int menuId;
    private String name;
    private List<MenuItem> items;

    public Menu() { this.items = new ArrayList<>(); }

    public Menu(int menuId, String name) {
        this.menuId = menuId;
        this.name = name;
        this.items = new ArrayList<>();
    }

    // Methods from class diagram
    public void addItem(MenuItem item) { items.add(item); }
    public void editItem(MenuItem item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemId() == item.getItemId()) {
                items.set(i, item);
                break;
            }
        }
    }
    public void deleteItem(int itemId) {
        items.removeIf(i -> i.getItemId() == itemId);
    }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }
}
