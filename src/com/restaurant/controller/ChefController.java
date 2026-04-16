package com.restaurant.controller;

import com.restaurant.dao.InventoryDAO;
import com.restaurant.dao.MenuDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.model.Inventory;
import com.restaurant.model.Order;
import com.restaurant.pattern.observer.KitchenObserver;
import com.restaurant.pattern.observer.OrderManager;
import com.restaurant.view.chef.KitchenDisplayView;

import java.util.List;

/**
 * CHEF CONTROLLER (Member 3)
 * MVC Controller: handles all Chef use cases from the Use Case Diagram:
 * - Process Kitchen Order / KDS (Major Feature)  — OBSERVER PATTERN
 * - Inventory Tracking (Minor Feature)
 */
public class ChefController {

    private OrderDAO orderDAO;
    private InventoryDAO inventoryDAO;
    private MenuDAO menuDAO;

    public ChefController() {
        this.orderDAO = new OrderDAO();
        this.inventoryDAO = new InventoryDAO();
        this.menuDAO = new MenuDAO();
    }

    /**
     * Register the Chef's KDS view as an observer.
     * Called once when the KDS window opens.
     * OBSERVER PATTERN: links KitchenDisplayView to OrderManager.
     */
    public void registerKitchenDisplay(KitchenDisplayView view) {
        KitchenObserver observer = new KitchenObserver(view);
        OrderManager.getInstance().addObserver(observer);
        System.out.println("[ChefController] Kitchen display registered as observer.");
    }

    // ---- KITCHEN DISPLAY SYSTEM (Major Feature) ----

    public List<Order> getActiveOrders() {
        return orderDAO.getAllActiveOrders();
    }

    public List<Order> getPendingOrders() {
        return orderDAO.getOrdersByStatus("PENDING");
    }

    public List<Order> getInProgressOrders() {
        return orderDAO.getOrdersByStatus("IN_PROGRESS");
    }

    /**
     * Chef starts processing an order (PENDING → IN_PROGRESS)
     */
    public boolean startProcessingOrder(int orderId) {
        boolean result = orderDAO.updateOrderStatus(orderId, "IN_PROGRESS");
        if (result) {
            Order order = orderDAO.getOrderById(orderId);
            if (order != null) OrderManager.getInstance().notifyStatusChange(order);
        }
        return result;
    }

    /**
     * Chef marks order as ready (IN_PROGRESS → READY)
     */
    public boolean markOrderReady(int orderId) {
        boolean result = orderDAO.updateOrderStatus(orderId, "READY");
        if (result) {
            Order order = orderDAO.getOrderById(orderId);
            if (order != null) OrderManager.getInstance().notifyStatusChange(order);
        }
        return result;
    }

    // ---- INVENTORY TRACKING (Minor Feature) ----

    public List<Inventory> getAllInventory() {
        return inventoryDAO.getAllInventory();
    }

    public boolean updateInventoryStock(int inventoryId, int quantity) {
        return inventoryDAO.updateStock(inventoryId, quantity);
    }

    public int addInventoryItem(String itemName, int quantity) {
        return inventoryDAO.addInventoryItem(itemName, quantity);
    }

    /**
     * Toggle a menu item's availability (Out of Stock feature).
     */
    public boolean toggleMenuItemAvailability(int itemId, boolean available) {
        return menuDAO.updateAvailability(itemId, available);
    }
}
