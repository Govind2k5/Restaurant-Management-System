package com.restaurant.dao;

import com.restaurant.db.DBConnection;
import com.restaurant.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ORDER DAO - All database operations for Orders (Members 2, 3)
 */
public class OrderDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    public int saveOrder(Order order) {
        String sql = "INSERT INTO orders (tableId, waiterId, status, orderTime, totalAmount) VALUES (?, ?, ?, NOW(), ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getTableId());
            ps.setInt(2, order.getWaiterId());
            ps.setString(3, order.getStatus());
            ps.setDouble(4, order.getTotalAmount());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    order.setOrderId(id);
                    saveOrderItems(order);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] saveOrder: " + e.getMessage());
        }
        return -1;
    }

    private void saveOrderItems(Order order) {
        String sql = "INSERT INTO order_items (orderId, itemId, quantity) VALUES (?, ?, 1)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            for (var item : order.getItems()) {
                ps.setInt(1, order.getOrderId());
                ps.setInt(2, item.getItemId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            System.err.println("[OrderDAO] saveOrderItems: " + e.getMessage());
        }
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status=? WHERE orderId=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[OrderDAO] updateOrderStatus: " + e.getMessage());
        }
        return false;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE orderId=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToOrder(rs);
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getOrderById: " + e.getMessage());
        }
        return null;
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status=? ORDER BY orderTime DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) orders.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getOrdersByStatus: " + e.getMessage());
        }
        return orders;
    }

    public List<Order> getAllActiveOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status NOT IN ('SERVED','CANCELLED') ORDER BY orderTime DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) orders.add(mapRowToOrder(rs));
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getAllActiveOrders: " + e.getMessage());
        }
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY orderTime DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) orders.add(mapRowToOrder(rs));
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getAllOrders: " + e.getMessage());
        }
        return orders;
    }

    public List<Order> getTodayOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE DATE(orderTime) = CURDATE() ORDER BY orderTime DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) orders.add(mapRowToOrder(rs));
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getTodayOrders: " + e.getMessage());
        }
        return orders;
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("orderId"),
                rs.getInt("tableId"),
                rs.getInt("waiterId"),
                rs.getString("status"),
                rs.getTimestamp("orderTime"),
                rs.getDouble("totalAmount")
        );
    }
}
