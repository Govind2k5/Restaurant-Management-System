package com.restaurant.dao;

import com.restaurant.db.DBConnection;
import com.restaurant.model.Menu;
import com.restaurant.model.MenuItem;
import com.restaurant.pattern.factory.MenuItemFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MENU DAO - All database operations for Menu and MenuItems (Member 1 - Admin)
 */
public class MenuDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    // ---- MENU ITEMS ----

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items ORDER BY itemType, name";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                items.add(mapRowToMenuItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] getAllMenuItems: " + e.getMessage());
        }
        return items;
    }

    public List<MenuItem> getAvailableMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE available = TRUE ORDER BY itemType, name";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                items.add(mapRowToMenuItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] getAvailableMenuItems: " + e.getMessage());
        }
        return items;
    }

    public MenuItem getMenuItemById(int itemId) {
        String sql = "SELECT * FROM menu_items WHERE itemId = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToMenuItem(rs);
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] getMenuItemById: " + e.getMessage());
        }
        return null;
    }

    public int addMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu_items (menuId, name, price, available, itemType) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getMenuId());
            ps.setString(2, item.getName());
            ps.setDouble(3, item.getPrice());
            ps.setBoolean(4, item.isAvailable());
            ps.setString(5, item.getItemType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] addMenuItem: " + e.getMessage());
        }
        return -1;
    }

    public boolean updateMenuItem(MenuItem item) {
        String sql = "UPDATE menu_items SET name=?, price=?, available=?, itemType=? WHERE itemId=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setBoolean(3, item.isAvailable());
            ps.setString(4, item.getItemType());
            ps.setInt(5, item.getItemId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MenuDAO] updateMenuItem: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteMenuItem(int itemId) {
        String sql = "DELETE FROM menu_items WHERE itemId=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MenuDAO] deleteMenuItem: " + e.getMessage());
        }
        return false;
    }

    public boolean updateAvailability(int itemId, boolean available) {
        String sql = "UPDATE menu_items SET available=? WHERE itemId=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MenuDAO] updateAvailability: " + e.getMessage());
        }
        return false;
    }

    // ---- MENU ----

    public List<Menu> getAllMenus() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menu";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                menus.add(new Menu(rs.getInt("menuId"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] getAllMenus: " + e.getMessage());
        }
        return menus;
    }

    // ---- MAPPER ----

    private MenuItem mapRowToMenuItem(ResultSet rs) throws SQLException {
        return MenuItemFactory.createMenuItemFromString(
                rs.getString("itemType"),
                rs.getInt("itemId"),
                rs.getInt("menuId"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getBoolean("available")
        );
    }
}
