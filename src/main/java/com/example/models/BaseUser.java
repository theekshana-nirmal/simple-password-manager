package com.example.models;

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

    /**
     * Constructor for creating a user
     */
    public BaseUser(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
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

    // Abstract methods for polymorphic behavior
    public abstract String getUserType();

    public abstract boolean canManagePasswords();

    public abstract void performLoginActions();

    public abstract void performLogoutActions();
}
