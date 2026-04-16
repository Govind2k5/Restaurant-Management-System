package com.restaurant;

import com.restaurant.view.LoginView;
import com.restaurant.view.UITheme;

import javax.swing.*;

/**
 * MAIN ENTRY POINT — Restaurant Management System
 *
 * Stack   : Java Swing (Desktop MVC)
 * DB      : MySQL via JDBC (Singleton DBConnection)
 * Patterns: Factory | Singleton | Observer | Facade
 *
 * Setup:
 *  1. Run database/schema.sql in MySQL
 *  2. Set your MySQL password in DBConnection.java
 *  3. Add mysql-connector-java.jar to classpath
 *  4. Compile all sources under src/
 *  5. Run this class
 *
 * Default login: admin / admin123
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        UITheme.applyGlobalDefaults();

        SwingUtilities.invokeLater(() -> {
            System.out.println("=== Restaurant Management System ===");
            new LoginView();
        });
    }
}
