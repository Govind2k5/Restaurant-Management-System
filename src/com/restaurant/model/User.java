package com.restaurant.model;

/**
 * BASE USER MODEL - Matches class diagram: User class
 * Attributes: userId, name, password, username
 */
public class User {
    private int userId;
    private String name;
    private String password;
    private String username;
    private String role;

    public User() {}

    public User(int userId, String name, String username, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // login/logout as per class diagram
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        System.out.println("[User] " + this.name + " logged out.");
    }

    // Getters & Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", name='" + name + "', role='" + role + "'}";
    }
}
