package com.g4t2project.g4t2project.DTO;

public class LoginResponse {
    private String message;
    private String token;
    private String username;
    private String role;

    public LoginResponse(String message, String token, String username, String role) {
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}