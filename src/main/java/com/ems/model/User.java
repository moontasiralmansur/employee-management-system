package com.ems.model;

// Model class representing a login user.
// Encapsulation: credentials are private and validated through this class.
public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Validate credentials
    public boolean validate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}