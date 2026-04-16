package com.restaurant.dao;

import com.restaurant.db.DBConnection;
import com.restaurant.model.User;

import java.sql.*;

/**
 * USER DAO - Authentication (all roles)
 */
public class UserDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("userId"),
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] login: " + e.getMessage());
        }
        return null;
    }
}
