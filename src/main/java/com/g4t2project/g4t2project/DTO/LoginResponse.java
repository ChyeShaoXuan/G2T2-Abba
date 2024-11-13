package com.g4t2project.g4t2project.DTO;

import java.util.List;

public class LoginResponse {
    private String message;
    private String token;
    private String username;
    private String role;
    private List<String> roles;

    public LoginResponse(String message, String token, String username, String role, List<String> roles) {
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
        this.roles = roles;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}