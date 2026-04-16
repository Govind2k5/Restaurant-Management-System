package com.restaurant.model;

import java.util.Date;

/**
 * RECEIPT MODEL - Matches class diagram: Receipt class
 */
public class Receipt {
    private int receiptId;
    private int billId;
    private Date date;

    public Receipt() { this.date = new Date(); }

    public Receipt(int receiptId, int billId, Date date) {
        this.receiptId = receiptId;
        this.billId = billId;
        this.date = date;
    }

    public void printReceipt() {
        System.out.println("=== RECEIPT #" + receiptId + " ===");
        System.out.println("Bill ID: " + billId);
        System.out.println("Date: " + date);
    }

    public int getReceiptId() { return receiptId; }
    public void setReceiptId(int receiptId) { this.receiptId = receiptId; }
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
