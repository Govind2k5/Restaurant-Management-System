package com.restaurant.dao;

import com.restaurant.db.DBConnection;
import com.restaurant.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * INVENTORY DAO (Member 3 - Chef)
 */
public class InventoryDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    public List<Inventory> getAllInventory() {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory ORDER BY itemName";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Inventory(rs.getInt("inventoryId"), rs.getString("itemName"), rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            System.err.println("[InventoryDAO] getAllInventory: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStock(int inventoryId, int qty) {
        String sql = "UPDATE inventory SET quantity=? WHERE inventoryId=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, inventoryId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[InventoryDAO] updateStock: " + e.getMessage());
        }
        return false;
    }

    public int addInventoryItem(String itemName, int quantity) {
        String sql = "INSERT INTO inventory (itemName, quantity) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, itemName);
            ps.setInt(2, quantity);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[InventoryDAO] addInventoryItem: " + e.getMessage());
        }
        return -1;
    }
}
