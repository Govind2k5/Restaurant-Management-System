package com.restaurant.controller;

import com.restaurant.dao.MenuDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.dao.TableDAO;
import com.restaurant.model.MenuItem;
import com.restaurant.model.Order;
import com.restaurant.model.Table;
import com.restaurant.pattern.observer.OrderManager;

import java.util.ArrayList;
import java.util.List;

/**
 * WAITER CONTROLLER (Member 2)
 * MVC Controller: handles all Waiter use cases from the Use Case Diagram:
 * - Place Order (Major Feature) — includes «includes» Verify Item Availability
 * - Cancel Order («extends» from Place Order)
 * - Reserve Table (Minor Feature)
 */
public class WaiterController {

    private OrderDAO orderDAO;
    private TableDAO tableDAO;
    private MenuDAO menuDAO;

    // Current working order (in-memory cart)
    private Order currentOrder;

    public WaiterController() {
        this.orderDAO = new OrderDAO();
        this.tableDAO = new TableDAO();
        this.menuDAO = new MenuDAO();
    }

    // ---- ORDER TAKING (Major Feature) ----

    public void startNewOrder(int tableId, int waiterId) {
        currentOrder = new Order();
        currentOrder.setTableId(tableId);
        currentOrder.setWaiterId(waiterId);
        currentOrder.setStatus("PENDING");
        System.out.println("[WaiterController] New order started for table " + tableId);
    }

    public void addItemToOrder(MenuItem item) {
        if (currentOrder == null) throw new IllegalStateException("No active order. Call startNewOrder() first.");
        currentOrder.addItem(item);
    }

    public void removeItemFromOrder(MenuItem item) {
        if (currentOrder != null) currentOrder.removeItem(item);
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Submits the order — includes «Verify item availability» (Use Case).
     * Also fires Observer notification to Chef's KDS.
     */
    public boolean placeOrder() {
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            return false;
        }
        // «includes» Verify item availability
        if (!verifyItemAvailability()) {
            System.out.println("[WaiterController] Order rejected: some items are unavailable.");
            return false;
        }
        int id = orderDAO.saveOrder(currentOrder);
        if (id > 0) {
            // OBSERVER PATTERN: notify Chef's KDS
            OrderManager.getInstance().notifyObservers(currentOrder);
            currentOrder = null; // reset cart
            return true;
        }
        return false;
    }

    /**
     * «includes» use case: Verify item availability
     */
    public boolean verifyItemAvailability() {
        if (currentOrder == null) return false;
        return currentOrder.verifyAvailability();
    }

    /**
     * Cancel Order — «extends» Place Order (Use Case Diagram)
     */
    public boolean cancelOrder(int orderId) {
        boolean result = orderDAO.updateOrderStatus(orderId, "CANCELLED");
        if (result) {
            Order order = orderDAO.getOrderById(orderId);
            if (order != null) OrderManager.getInstance().notifyStatusChange(order);
        }
        return result;
    }

    public List<MenuItem> getAvailableMenuItems() {
        return menuDAO.getAvailableMenuItems();
    }

    // ---- TABLE RESERVATION (Minor Feature) ----

    public List<Table> getAllTables() {
        return tableDAO.getAllTables();
    }

    public List<Table> getAvailableTables() {
        return tableDAO.getAvailableTables();
    }

    public boolean reserveTable(int tableId) {
        return tableDAO.updateTableReservation(tableId, true);
    }

    public boolean releaseTable(int tableId) {
        return tableDAO.updateTableReservation(tableId, false);
    }
}
