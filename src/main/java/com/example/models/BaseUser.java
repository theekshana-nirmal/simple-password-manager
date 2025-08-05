package com.example.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for all user types.
 * Demonstrates ABSTRACTION and ENCAPSULATION principles.
 * Contains common user attributes and abstract methods for polymorphic
 * behavior.
 */
public abstract class BaseUser {

    // Encapsulated core user attributes
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;

    /**
     * Constructor for creating a new user
     */
    public BaseUser(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor for loading existing user from storage
     */
    public BaseUser(String username, String email, String passwordHash, String createdAtString) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.parse(createdAtString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Getters for encapsulated fields
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedAtString() {
        return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Abstract methods for polymorphic behavior
    public abstract String getUserType();

    public abstract boolean canManagePasswords();

    public abstract boolean hasAdminPrivileges();

    public abstract int getMaxPasswordCount();

    public abstract void performLoginActions();

    public abstract void performLogoutActions();
}
