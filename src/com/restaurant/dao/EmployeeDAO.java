package com.restaurant.dao;

import com.restaurant.db.DBConnection;
import com.restaurant.model.Employee;
import com.restaurant.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EMPLOYEE DAO (Member 1 - Admin)
 */
public class EmployeeDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, u.username FROM employees e JOIN users u ON e.userId = u.userId";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employeeId"),
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getDouble("salary")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[EmployeeDAO] getAllEmployees: " + e.getMessage());
        }
        return employees;
    }

    /**
     * Adds both a User login and an Employee record (atomic operation).
     */
    public int addEmployee(Employee employee, String username, String password, String userRole) {
        Connection conn = getConn();
        try {
            conn.setAutoCommit(false);
            // 1. Create user login
            String userSql = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)";
            int userId = -1;
            try (PreparedStatement ps = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, employee.getName());
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setString(4, userRole);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) userId = keys.getInt(1);
                }
            }
            if (userId == -1) { conn.rollback(); return -1; }

            // 2. Create employee record
            String empSql = "INSERT INTO employees (userId, name, role, salary) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(empSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setString(2, employee.getName());
                ps.setString(3, employee.getRole());
                ps.setDouble(4, employee.getSalary());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        conn.commit();
                        return keys.getInt(1);
                    }
                }
            }
            conn.rollback();
        } catch (SQLException e) {
            System.err.println("[EmployeeDAO] addEmployee: " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
        return -1;
    }

    public boolean removeEmployee(int employeeId) {
        // Get userId first to cascade delete user too
        String getUser = "SELECT userId FROM employees WHERE employeeId=?";
        try (PreparedStatement ps = getConn().prepareStatement(getUser)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("userId");
                    String delUser = "DELETE FROM users WHERE userId=?";
                    try (PreparedStatement ps2 = getConn().prepareStatement(delUser)) {
                        ps2.setInt(1, userId);
                        return ps2.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[EmployeeDAO] removeEmployee: " + e.getMessage());
        }
        return false;
    }
}
