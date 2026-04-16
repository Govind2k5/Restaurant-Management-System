-- ============================================================
-- Restaurant Management System — Demo Seed Data
-- Run this AFTER schema.sql on a fresh database
-- ============================================================
USE restaurant_ms;

-- ── Staff Users ───────────────────────────────────────────────────────────

-- Waiters
INSERT IGNORE INTO users (name, username, password, role) VALUES
('James Carter',  'james',   'waiter123', 'WAITER'),
('Sofia Reyes',   'sofia',   'waiter123', 'WAITER'),
('Marcus Bell',   'marcus',  'waiter123', 'WAITER'),
('Nina Patel',    'nina',    'waiter123', 'WAITER');

-- Chefs
INSERT IGNORE INTO users (name, username, password, role) VALUES
('Antonio Russo',  'antonio', 'chef123', 'CHEF'),
('Priya Sharma',   'priya',   'chef123', 'CHEF');

-- Managers
INSERT IGNORE INTO users (name, username, password, role) VALUES
('Diana Ortega',  'diana',  'manager123', 'MANAGER'),
('Kevin Zhao',    'kevin',  'manager123', 'MANAGER');

-- ── Employee Records ──────────────────────────────────────────────────────

INSERT IGNORE INTO employees (userId, name, role, salary)
SELECT userId, name, 'WAITER', 2200.00 FROM users WHERE username = 'james';

INSERT IGNORE INTO employees (userId, name, role, salary)
SELECT userId, name, 'WAITER', 2200.00 FROM users WHERE username = 'sofia';

INSERT IGNORE INTO employees (userId, name, role, salary)
SELECT userId, name, 'WAITER', 2200.00 FROM users WHERE username = 'marcus';

INSERT IGNORE INTO employees (userId, name, role, salary)
SELECT userId, name, 'WAITER', 2200.00 FROM users WHERE username = 'nina';

INSERT IGNORE INTO employees (userId, name, role, salary)
SELECT userId, name, 'CHEF', 3500.00 FROM users WHERE username = 'antonio';

INSERT IGNORE INTO employees (userId, name, role, salary)
SELECT userId, name, 'CHEF', 3500.00 FROM users WHERE username = 'priya';

INSERT IGNORE INTO employees (userId, name, role, salary)
SELECT userId, name, 'MANAGER', 4500.00 FROM users WHERE username = 'diana';

INSERT IGNORE INTO employees (userId, name, role, salary)
SELECT userId, name, 'MANAGER', 4500.00 FROM users WHERE username = 'kevin';

-- ── Extended Menu Items ───────────────────────────────────────────────────

INSERT IGNORE INTO menu_items (menuId, name, price, available, itemType) VALUES
-- Starters
(1, 'Bruschetta',            6.99,  TRUE,  'FOOD'),
(1, 'Garlic Bread',          4.99,  TRUE,  'FOOD'),
(1, 'Tomato Soup',           7.49,  TRUE,  'FOOD'),
(1, 'Crispy Calamari',       9.99,  TRUE,  'FOOD'),
-- Mains
(1, 'Beef Burger',          14.99,  TRUE,  'FOOD'),
(1, 'Fish & Chips',         13.99,  TRUE,  'FOOD'),
(1, 'Vegetable Stir-Fry',   11.49,  TRUE,  'FOOD'),
(1, 'BBQ Ribs',             18.99,  TRUE,  'FOOD'),
(1, 'Club Sandwich',         9.99,  TRUE,  'FOOD'),
-- Desserts
(1, 'Tiramisu',              6.99,  TRUE,  'FOOD'),
(1, 'Chocolate Lava Cake',   7.49,  TRUE,  'FOOD'),
(1, 'Cheesecake',            5.99,  TRUE,  'FOOD'),
-- Hot drinks
(1, 'Espresso',              2.49,  TRUE,  'BEVERAGE'),
(1, 'Cappuccino',            3.49,  TRUE,  'BEVERAGE'),
-- Cold drinks
(1, 'Sparkling Water',       2.49,  TRUE,  'BEVERAGE'),
(1, 'Mango Smoothie',        4.49,  TRUE,  'BEVERAGE'),
(1, 'Iced Tea',              2.99,  TRUE,  'BEVERAGE'),
(1, 'Strawberry Lemonade',   3.79,  TRUE,  'BEVERAGE');

-- ── More Tables ───────────────────────────────────────────────────────────

INSERT IGNORE INTO tables (tableId, capacity, isReserved) VALUES
(7, 4, FALSE), (8, 6, FALSE), (9, 2, FALSE), (10, 8, FALSE);

-- ── Inventory ─────────────────────────────────────────────────────────────

INSERT IGNORE INTO inventory (itemName, quantity) VALUES
('Chicken Breast (kg)',      25),
('Pasta (kg)',               40),
('Romaine Lettuce (heads)',  30),
('Pizza Dough (balls)',      20),
('Coca Cola (cans)',         60),
('Orange Juice (liters)',    15),
('Mineral Water (bottles)', 50),
('Lemons',                  35),
('Beef Patties',            18),
('Fish Fillets (kg)',        12),
('Tomatoes (kg)',            45),
('Garlic (bulbs)',           80),
('Baguettes',               22),
('Cooking Cream (liters)',   10),
('Coffee Beans (kg)',         8),
('Eggs (dozen)',             15),
('Butter (kg)',               6),
('Olive Oil (liters)',        9),
('Parmesan (kg)',             4),
('Chocolate (kg)',            5);

-- ── Sample Orders ─────────────────────────────────────────────────────────
-- These use INSERT IGNORE so they won't error if orders already exist.

INSERT IGNORE INTO orders (orderId, tableId, waiterId, status, orderTime, totalAmount)
SELECT 101, 1, u.userId, 'SERVED',      DATE_SUB(NOW(), INTERVAL 3 HOUR),  38.96 FROM users u WHERE u.username = 'james';

INSERT IGNORE INTO orders (orderId, tableId, waiterId, status, orderTime, totalAmount)
SELECT 102, 2, u.userId, 'SERVED',      DATE_SUB(NOW(), INTERVAL 2 HOUR),  27.97 FROM users u WHERE u.username = 'sofia';

INSERT IGNORE INTO orders (orderId, tableId, waiterId, status, orderTime, totalAmount)
SELECT 103, 3, u.userId, 'READY',       DATE_SUB(NOW(), INTERVAL 1 HOUR),  48.97 FROM users u WHERE u.username = 'marcus';

INSERT IGNORE INTO orders (orderId, tableId, waiterId, status, orderTime, totalAmount)
SELECT 104, 4, u.userId, 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 40 MINUTE), 22.48 FROM users u WHERE u.username = 'nina';

INSERT IGNORE INTO orders (orderId, tableId, waiterId, status, orderTime, totalAmount)
SELECT 105, 5, u.userId, 'PENDING',     DATE_SUB(NOW(), INTERVAL 10 MINUTE), 18.98 FROM users u WHERE u.username = 'james';

-- ── Order Items ───────────────────────────────────────────────────────────

-- Order 101: Grilled Chicken x2, Coca Cola x2, Caesar Salad x1
INSERT IGNORE INTO order_items (orderId, itemId, quantity) VALUES
(101, 1, 2), (101, 5, 2), (101, 3, 1);

-- Order 102: Pasta Carbonara x1, Orange Juice x2, Margherita Pizza x1
INSERT IGNORE INTO order_items (orderId, itemId, quantity) VALUES
(102, 2, 1), (102, 6, 2), (102, 4, 1);

-- Order 103: BBQ Ribs x1, Garlic Bread x2, Lemonade x2, Cheesecake x1
INSERT IGNORE INTO order_items (orderId, itemId, quantity) VALUES
(103, 1, 1), (103, 2, 2), (103, 8, 2), (103, 3, 1);

-- Order 104: Fish & Chips x1, Caesar Salad x1, Mineral Water x2
INSERT IGNORE INTO order_items (orderId, itemId, quantity) VALUES
(104, 1, 1), (104, 3, 1), (104, 7, 2);

-- Order 105: Beef Burger x1, Espresso x2
INSERT IGNORE INTO order_items (orderId, itemId, quantity) VALUES
(105, 1, 1), (105, 5, 2);

-- ── Bills ─────────────────────────────────────────────────────────────────
-- tax = 8%, serviceCharge = 5%, discount = 0%

INSERT IGNORE INTO bills (billId, orderId, totalAmount, discount, tax, serviceCharge, finalAmount, createdAt)
VALUES
(101, 101, 38.96, 0.00,  3.12, 1.95,  44.03, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(102, 102, 27.97, 2.80,  2.24, 1.40,  28.81, DATE_SUB(NOW(), INTERVAL 2 HOUR));

-- ── Historical Sales Reports (last 7 days) ────────────────────────────────

INSERT IGNORE INTO sales_reports (totalSales, date) VALUES
(842.50,  DATE_SUB(CURDATE(), INTERVAL 6 DAY)),
(915.75,  DATE_SUB(CURDATE(), INTERVAL 5 DAY)),
(763.20,  DATE_SUB(CURDATE(), INTERVAL 4 DAY)),
(1024.90, DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
(887.40,  DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
(932.15,  DATE_SUB(CURDATE(), INTERVAL 1 DAY));

-- ── Quick Reference: Login Credentials ────────────────────────────────────
-- Role       Username    Password
-- ADMIN      admin       admin123
-- WAITER     james       waiter123
-- WAITER     sofia       waiter123
-- WAITER     marcus      waiter123
-- WAITER     nina        waiter123
-- CHEF       antonio     chef123
-- CHEF       priya       chef123
-- MANAGER    diana       manager123
-- MANAGER    kevin       manager123
