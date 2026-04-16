-- Restaurant Management System - Database Schema
-- Run this in MySQL before starting the application

CREATE DATABASE IF NOT EXISTS restaurant_ms;
USE restaurant_ms;

-- Users table (base for all roles)
CREATE TABLE IF NOT EXISTS users (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'WAITER', 'CHEF', 'MANAGER') NOT NULL
);

-- Employees table
CREATE TABLE IF NOT EXISTS employees (
    employeeId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    salary DOUBLE NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
);

-- Menu table
CREATE TABLE IF NOT EXISTS menu (
    menuId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Menu items table
CREATE TABLE IF NOT EXISTS menu_items (
    itemId INT AUTO_INCREMENT PRIMARY KEY,
    menuId INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    itemType ENUM('FOOD', 'BEVERAGE') NOT NULL,
    FOREIGN KEY (menuId) REFERENCES menu(menuId) ON DELETE CASCADE
);

-- Tables table
CREATE TABLE IF NOT EXISTS tables (
    tableId INT AUTO_INCREMENT PRIMARY KEY,
    capacity INT NOT NULL,
    isReserved BOOLEAN DEFAULT FALSE
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    tableId INT NOT NULL,
    waiterId INT NOT NULL,
    status ENUM('PENDING', 'IN_PROGRESS', 'READY', 'SERVED', 'CANCELLED') DEFAULT 'PENDING',
    orderTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    totalAmount DOUBLE DEFAULT 0.0,
    FOREIGN KEY (tableId) REFERENCES tables(tableId),
    FOREIGN KEY (waiterId) REFERENCES users(userId)
);

-- Order items (many-to-many: orders & menu_items)
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL,
    itemId INT NOT NULL,
    quantity INT DEFAULT 1,
    FOREIGN KEY (orderId) REFERENCES orders(orderId) ON DELETE CASCADE,
    FOREIGN KEY (itemId) REFERENCES menu_items(itemId)
);

-- Bills table
CREATE TABLE IF NOT EXISTS bills (
    billId INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL UNIQUE,
    totalAmount DOUBLE NOT NULL,
    discount DOUBLE DEFAULT 0.0,
    tax DOUBLE DEFAULT 0.0,
    serviceCharge DOUBLE DEFAULT 0.0,
    finalAmount DOUBLE NOT NULL,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (orderId) REFERENCES orders(orderId)
);

-- Receipts table
CREATE TABLE IF NOT EXISTS receipts (
    receiptId INT AUTO_INCREMENT PRIMARY KEY,
    billId INT NOT NULL,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (billId) REFERENCES bills(billId)
);

-- Sales reports table
CREATE TABLE IF NOT EXISTS sales_reports (
    reportId INT AUTO_INCREMENT PRIMARY KEY,
    totalSales DOUBLE NOT NULL,
    date DATE NOT NULL
);

-- Inventory table
CREATE TABLE IF NOT EXISTS inventory (
    inventoryId INT AUTO_INCREMENT PRIMARY KEY,
    itemName VARCHAR(100) NOT NULL,
    quantity INT NOT NULL
);

-- Seed default admin user (password: admin123)
INSERT IGNORE INTO users (name, username, password, role)
VALUES ('Admin User', 'admin', 'admin123', 'ADMIN');

-- Seed default tables
INSERT IGNORE INTO tables (tableId, capacity, isReserved) VALUES
(1, 4, FALSE), (2, 4, FALSE), (3, 6, FALSE),
(4, 2, FALSE), (5, 8, FALSE), (6, 4, FALSE);

-- Seed default menu
INSERT IGNORE INTO menu (menuId, name) VALUES (1, 'Main Menu');

-- Seed some menu items
INSERT IGNORE INTO menu_items (menuId, name, price, available, itemType) VALUES
(1, 'Grilled Chicken', 12.99, TRUE, 'FOOD'),
(1, 'Pasta Carbonara', 10.99, TRUE, 'FOOD'),
(1, 'Caesar Salad', 7.99, TRUE, 'FOOD'),
(1, 'Margherita Pizza', 11.99, TRUE, 'FOOD'),
(1, 'Coca Cola', 2.99, TRUE, 'BEVERAGE'),
(1, 'Orange Juice', 3.99, TRUE, 'BEVERAGE'),
(1, 'Mineral Water', 1.99, TRUE, 'BEVERAGE'),
(1, 'Lemonade', 3.49, TRUE, 'BEVERAGE');
