package com.restaurant.model;

import java.util.Date;

/**
 * BILL MODEL - Matches class diagram: Bill class
 */
public class Bill {
    private int billId;
    private int orderId;
    private double totalAmount;
    private double discount;
    private double tax;
    private double serviceCharge;
    private double finalAmount;
    private Date createdAt;

    public Bill() { this.createdAt = new Date(); }

    public Bill(int billId, int orderId, double totalAmount, double discount, double tax, double serviceCharge, double finalAmount) {
        this.billId = billId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.tax = tax;
        this.serviceCharge = serviceCharge;
        this.finalAmount = finalAmount;
        this.createdAt = new Date();
    }

    // Methods from class diagram
    public Bill generateBill(Order order) {
        this.orderId = order.getOrderId();
        this.totalAmount = order.getTotalAmount();
        return this;
    }

    public void applyDiscount(double amount) {
        this.discount = amount;
        recalculateFinal();
    }

    public double calculateFinalAmount() {
        recalculateFinal();
        return finalAmount;
    }

    private void recalculateFinal() {
        this.finalAmount = totalAmount - discount + tax + serviceCharge;
    }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(double serviceCharge) { this.serviceCharge = serviceCharge; }
    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
