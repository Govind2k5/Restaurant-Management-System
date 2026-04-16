package com.restaurant.model;

/**
 * EMPLOYEE MODEL - Matches class diagram: Employees class
 * Attributes: employeeId, name, role, salary
 */
public class Employee {
    private int employeeId;
    private int userId;
    private String name;
    private String role;
    private double salary;

    public Employee() {}

    public Employee(int employeeId, int userId, String name, String role, double salary) {
        this.employeeId = employeeId;
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.salary = salary;
    }

    // Methods as per class diagram
    public void addEmployee() {
        System.out.println("[Employee] Adding employee: " + name);
    }

    public void removeEmployee() {
        System.out.println("[Employee] Removing employee: " + name);
    }

    // Getters & Setters
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public String toString() {
        return "Employee{id=" + employeeId + ", name='" + name + "', role='" + role + "'}";
    }
}
