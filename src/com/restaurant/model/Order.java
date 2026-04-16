package com.restaurant.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ORDER MODEL - Matches class diagram: Order class
 */
public class Order {
    private int orderId;
    private int tableId;
    private int waiterId;
    private String status; // PENDING, IN_PROGRESS, READY, SERVED, CANCELLED
    private Date orderTime;
    private double totalAmount;
    private List<MenuItem> items;

    public Order() {
        this.items = new ArrayList<>();
        this.orderTime = new Date();
        this.status = "PENDING";
    }

    public Order(int orderId, int tableId, int waiterId, String status, Date orderTime, double totalAmount) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.waiterId = waiterId;
        this.status = status;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.items = new ArrayList<>();
    }

    // Methods from class diagram
    public void addItem(MenuItem item) {
        items.add(item);
        recalculateTotal();
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
        recalculateTotal();
    }

    public boolean verifyAvailability() {
        for (MenuItem item : items) {
            if (!item.isAvailable()) return false;
        }
        return true;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    private void recalculateTotal() {
        totalAmount = items.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    // Getters & Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public int getWaiterId() { return waiterId; }
    public void setWaiterId(int waiterId) { this.waiterId = waiterId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getOrderTime() { return orderTime; }
    public void setOrderTime(Date orderTime) { this.orderTime = orderTime; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }

    @Override
    public String toString() {
        return "Order{id=" + orderId + ", table=" + tableId + ", status='" + status + "', total=$" + String.format("%.2f", totalAmount) + "}";
    }
}
