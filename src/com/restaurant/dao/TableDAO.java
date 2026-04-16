package com.restaurant.dao;

import com.restaurant.db.DBConnection;
import com.restaurant.model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TABLE DAO (Member 2 - Waiter)
 */
public class TableDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM tables ORDER BY tableId";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tables.add(new Table(rs.getInt("tableId"), rs.getInt("capacity"), rs.getBoolean("isReserved")));
            }
        } catch (SQLException e) {
            System.err.println("[TableDAO] getAllTables: " + e.getMessage());
        }
        return tables;
    }

    public List<Table> getAvailableTables() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM tables WHERE isReserved = FALSE ORDER BY tableId";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tables.add(new Table(rs.getInt("tableId"), rs.getInt("capacity"), false));
            }
        } catch (SQLException e) {
            System.err.println("[TableDAO] getAvailableTables: " + e.getMessage());
        }
        return tables;
    }

    public boolean updateTableReservation(int tableId, boolean reserved) {
        String sql = "UPDATE tables SET isReserved=? WHERE tableId=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setBoolean(1, reserved);
            ps.setInt(2, tableId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TableDAO] updateTableReservation: " + e.getMessage());
        }
        return false;
    }
}
