package com.restaurant.controller;

import com.restaurant.dao.EmployeeDAO;
import com.restaurant.dao.MenuDAO;
import com.restaurant.model.Employee;
import com.restaurant.model.MenuItem;
import com.restaurant.pattern.factory.MenuItemFactory;
import com.restaurant.pattern.factory.MenuItemType;

import java.util.List;

/**
 * ADMIN CONTROLLER (Member 1)
 * MVC Controller: handles all Admin use cases from the Use Case Diagram:
 * - Manage Menu (Major Feature)
 * - Manage Employees (Minor Feature)
 * - Manage Inventory (shared with Chef)
 */
public class AdminController {

    private MenuDAO menuDAO;
    private EmployeeDAO employeeDAO;

    public AdminController() {
        this.menuDAO = new MenuDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    // ---- MENU MANAGEMENT (Major Feature) ----

    public List<MenuItem> getAllMenuItems() {
        return menuDAO.getAllMenuItems();
    }

    /**
     * Uses FACTORY PATTERN to create the correct MenuItem subtype.
     */
    public boolean addMenuItem(String name, double price, boolean available, String type, int menuId) {
        MenuItemType itemType = MenuItemType.valueOf(type.toUpperCase());
        MenuItem item = MenuItemFactory.createMenuItem(itemType, 0, menuId, name, price, available);
        int id = menuDAO.addMenuItem(item);
        return id > 0;
    }

    public boolean updateMenuItem(int itemId, String name, double price, boolean available, String type, int menuId) {
        MenuItemType itemType = MenuItemType.valueOf(type.toUpperCase());
        MenuItem item = MenuItemFactory.createMenuItem(itemType, itemId, menuId, name, price, available);
        return menuDAO.updateMenuItem(item);
    }

    public boolean deleteMenuItem(int itemId) {
        return menuDAO.deleteMenuItem(itemId);
    }

    // ---- EMPLOYEE MANAGEMENT (Minor Feature) ----

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    /**
     * Adds employee + creates login credentials in one transaction.
     */
    public boolean addEmployee(String name, String role, double salary, String username, String password) {
        Employee emp = new Employee(0, 0, name, role, salary);
        // Map role name to user role enum
        String userRole = role.toUpperCase().replace(" ", "_");
        if (!userRole.equals("WAITER") && !userRole.equals("CHEF") && !userRole.equals("MANAGER")) {
            userRole = "WAITER"; // default
        }
        int id = employeeDAO.addEmployee(emp, username, password, userRole);
        return id > 0;
    }

    public boolean removeEmployee(int employeeId) {
        return employeeDAO.removeEmployee(employeeId);
    }
}
