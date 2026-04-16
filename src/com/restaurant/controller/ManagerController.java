package com.restaurant.controller;

import com.restaurant.dao.BillDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.model.Bill;
import com.restaurant.model.Order;
import com.restaurant.model.Receipt;
import com.restaurant.model.SalesReport;
import com.restaurant.pattern.facade.BillingFacade;

import java.util.Date;
import java.util.List;

/**
 * MANAGER CONTROLLER (Member 4)
 * MVC Controller: handles all Manager use cases from the Use Case Diagram:
 * - Generate Bill (Major Feature) — uses FACADE PATTERN
 * - Apply Discount («extends» Generate Bill)
 * - Print Receipt («extends» Generate Bill)
 * - View Sales Report (Minor Feature)
 */
public class ManagerController {

    private BillingFacade billingFacade;
    private OrderDAO orderDAO;
    private BillDAO billDAO;

    public ManagerController() {
        this.billingFacade = new BillingFacade();
        this.orderDAO = new OrderDAO();
        this.billDAO = new BillDAO();
    }

    // ---- BILLING & INVOICING (Major Feature) ----

    /**
     * FACADE PATTERN: One call generates the entire bill
     * (calculates subtotal, tax, service charge, discount, final amount).
     *
     * @param orderId     The order to bill
     * @param discountPct 0.0 = no discount, 0.1 = 10% off
     */
    public Bill generateBill(int orderId, double discountPct) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            System.err.println("[ManagerController] Order not found: " + orderId);
            return null;
        }
        // Mark order as SERVED
        orderDAO.updateOrderStatus(orderId, "SERVED");

        // Facade hides all the tax/discount math
        return billingFacade.generateFinalBill(order, discountPct);
    }

    /**
     * «extends» Generate Bill — Apply Discount use case.
     */
    public Bill generateBillWithDiscount(int orderId, double discountPct) {
        return generateBill(orderId, discountPct);
    }

    /**
     * «extends» Generate Bill — Print Receipt use case.
     */
    public Receipt printReceipt(Bill bill) {
        return billingFacade.generateReceipt(bill);
    }

    public Bill getBillByOrderId(int orderId) {
        return billDAO.getBillByOrderId(orderId);
    }

    // ---- SALES REPORT (Minor Feature) ----

    public SalesReport viewSalesReport() {
        double todaySales = billDAO.getTodayTotalSales();
        return new SalesReport(0, todaySales, new Date());
    }

    public List<Order> getTodayOrders() {
        return orderDAO.getTodayOrders();
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }

    public List<Order> getReadyOrders() {
        return orderDAO.getOrdersByStatus("READY");
    }
}
