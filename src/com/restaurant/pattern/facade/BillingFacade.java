package com.restaurant.pattern.facade;

import com.restaurant.dao.BillDAO;
import com.restaurant.model.Bill;
import com.restaurant.model.Order;
import com.restaurant.model.Receipt;

import java.util.Date;

/**
 * FACADE PATTERN (Member 4 - Manager)
 * Hides the complex billing math (tax, service charge, discounts) behind
 * one simple generateFinalBill() method.
 *
 * The Manager's view just calls: BillingFacade.generateFinalBill(order, discountPct)
 * and gets a complete Bill back — no need to know the internals.
 */
public class BillingFacade {

    private static final double TAX_RATE = 0.08;            // 8% tax
    private static final double SERVICE_CHARGE_RATE = 0.05; // 5% service charge

    private BillDAO billDAO;

    public BillingFacade() {
        this.billDAO = new BillDAO();
    }

    /**
     * Main facade method — generates a complete, final bill for an order.
     *
     * @param order       The completed Order object
     * @param discountPct Discount percentage (0.0 to 1.0, e.g., 0.10 = 10%)
     * @return            Fully calculated Bill object (also saved to DB)
     */
    public Bill generateFinalBill(Order order, double discountPct) {
        Bill bill = new Bill();
        bill.setOrderId(order.getOrderId());

        double subtotal    = calculateSubtotal(order);
        double discount    = calculateDiscount(subtotal, discountPct);
        double tax         = calculateTax(subtotal - discount);
        double serviceChg  = calculateServiceCharge(subtotal - discount);
        double finalAmt    = subtotal - discount + tax + serviceChg;

        bill.setTotalAmount(subtotal);
        bill.setDiscount(discount);
        bill.setTax(tax);
        bill.setServiceCharge(serviceChg);
        bill.setFinalAmount(finalAmt);

        // Persist to database
        int billId = billDAO.saveBill(bill);
        bill.setBillId(billId);

        System.out.println("[BillingFacade] Bill generated. Total: $" + String.format("%.2f", finalAmt));
        return bill;
    }

    /**
     * Generates a Receipt from an existing Bill.
     */
    public Receipt generateReceipt(Bill bill) {
        Receipt receipt = new Receipt(0, bill.getBillId(), new Date());
        billDAO.saveReceipt(receipt);
        System.out.println("[BillingFacade] Receipt generated for Bill #" + bill.getBillId());
        return receipt;
    }

    // ---- Private calculation helpers (hidden from Manager's view) ----

    private double calculateSubtotal(Order order) {
        return order.getTotalAmount();
    }

    private double calculateDiscount(double subtotal, double discountPct) {
        return subtotal * discountPct;
    }

    private double calculateTax(double taxableAmount) {
        return taxableAmount * TAX_RATE;
    }

    private double calculateServiceCharge(double amount) {
        return amount * SERVICE_CHARGE_RATE;
    }
}
