package com.restaurant.dao;

import com.restaurant.db.DBConnection;
import com.restaurant.model.Bill;
import com.restaurant.model.Receipt;
import com.restaurant.model.SalesReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BILL DAO (Member 4 - Manager)
 */
public class BillDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    public int saveBill(Bill bill) {
        String sql = "INSERT INTO bills (orderId, totalAmount, discount, tax, serviceCharge, finalAmount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bill.getOrderId());
            ps.setDouble(2, bill.getTotalAmount());
            ps.setDouble(3, bill.getDiscount());
            ps.setDouble(4, bill.getTax());
            ps.setDouble(5, bill.getServiceCharge());
            ps.setDouble(6, bill.getFinalAmount());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[BillDAO] saveBill: " + e.getMessage());
        }
        return -1;
    }

    public Bill getBillByOrderId(int orderId) {
        String sql = "SELECT * FROM bills WHERE orderId=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bill b = new Bill();
                    b.setBillId(rs.getInt("billId"));
                    b.setOrderId(rs.getInt("orderId"));
                    b.setTotalAmount(rs.getDouble("totalAmount"));
                    b.setDiscount(rs.getDouble("discount"));
                    b.setTax(rs.getDouble("tax"));
                    b.setServiceCharge(rs.getDouble("serviceCharge"));
                    b.setFinalAmount(rs.getDouble("finalAmount"));
                    return b;
                }
            }
        } catch (SQLException e) {
            System.err.println("[BillDAO] getBillByOrderId: " + e.getMessage());
        }
        return null;
    }

    public int saveReceipt(Receipt receipt) {
        String sql = "INSERT INTO receipts (billId, date) VALUES (?, NOW())";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, receipt.getBillId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[BillDAO] saveReceipt: " + e.getMessage());
        }
        return -1;
    }

    public double getTodayTotalSales() {
        String sql = "SELECT SUM(finalAmount) FROM bills WHERE DATE(createdAt) = CURDATE()";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("[BillDAO] getTodayTotalSales: " + e.getMessage());
        }
        return 0.0;
    }

    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills ORDER BY createdAt DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bill b = new Bill();
                b.setBillId(rs.getInt("billId"));
                b.setOrderId(rs.getInt("orderId"));
                b.setTotalAmount(rs.getDouble("totalAmount"));
                b.setDiscount(rs.getDouble("discount"));
                b.setTax(rs.getDouble("tax"));
                b.setServiceCharge(rs.getDouble("serviceCharge"));
                b.setFinalAmount(rs.getDouble("finalAmount"));
                bills.add(b);
            }
        } catch (SQLException e) {
            System.err.println("[BillDAO] getAllBills: " + e.getMessage());
        }
        return bills;
    }
}
