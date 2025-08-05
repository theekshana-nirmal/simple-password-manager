package com.example.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * User model class representing a registered user
 */
public class User {
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;

    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String email, String passwordHash, String createdAtString) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        try {
            this.createdAt = LocalDateTime.parse(createdAtString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtString() {
        return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', email='" + email + "'}";
    }
}
