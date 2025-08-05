package com.example.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for all user types in the password manager system.
 * Demonstrates ABSTRACTION by defining common user properties and behaviors
 * while leaving specific implementations to subclasses.
 */
public abstract class BaseUser {
    // Encapsulated fields - private access with controlled getter/setter methods
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
    private boolean isLoggedIn;

    /**
     * Constructor for creating a new user
     * 
     * @param username     The username
     * @param email        The email address
     * @param passwordHash The hashed password
     */
    protected BaseUser(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
        this.isLoggedIn = false;
    }

    /**
     * Constructor for loading existing user from storage
     * 
     * @param username        The username
     * @param email           The email address
     * @param passwordHash    The hashed password
     * @param createdAtString The creation timestamp as string
     */
    protected BaseUser(String username, String email, String passwordHash, String createdAtString) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isLoggedIn = false;
        try {
            this.createdAt = LocalDateTime.parse(createdAtString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Abstract methods that must be implemented by subclasses (ABSTRACTION)

    /**
     * Get the user type - implemented differently by each subclass
     * 
     * @return The user type as string
     */
    public abstract String getUserType();

    /**
     * Check if user can manage passwords - different for each user type
     * 
     * @return true if user can manage passwords
     */
    public abstract boolean canManagePasswords();

    /**
     * Check if user has admin privileges - different for each user type
     * 
     * @return true if user has admin privileges
     */
    public abstract boolean hasAdminPrivileges();

    /**
     * Get maximum number of passwords user can store
     * 
     * @return maximum password count, -1 for unlimited
     */
    public abstract int getMaxPasswordCount();

    /**
     * Perform user-specific login actions
     */
    public abstract void performLoginActions();

    /**
     * Perform user-specific logout actions
     */
    public abstract void performLogoutActions();

    // Encapsulated getter and setter methods for controlled access

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username.trim();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim().toLowerCase();
        }
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash != null && !passwordHash.trim().isEmpty()) {
            this.passwordHash = passwordHash;
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        if (createdAt != null) {
            this.createdAt = createdAt;
        }
    }

    public String getCreatedAtString() {
        return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Set login status and perform login/logout actions
     * 
     * @param loggedIn true to log in, false to log out
     */
    public void setLoggedIn(boolean loggedIn) {
        if (loggedIn && !this.isLoggedIn) {
            this.isLoggedIn = true;
            performLoginActions();
        } else if (!loggedIn && this.isLoggedIn) {
            this.isLoggedIn = false;
            performLogoutActions();
        }
    }

    /**
     * Validate user credentials - common method for all user types
     * 
     * @param password The password to verify
     * @return true if credentials are valid
     */
    public boolean validateCredentials(String password) {
        return com.example.utils.PasswordUtils.verifyPassword(password, this.passwordHash);
    }

    /**
     * Update user password - common method for all user types
     * 
     * @param newPassword The new password
     */
    public void updatePassword(String newPassword) {
        this.passwordHash = com.example.utils.PasswordUtils.createPasswordHash(newPassword);
    }

    @Override
    public String toString() {
        return String.format("%s{username='%s', email='%s', type='%s'}",
                getClass().getSimpleName(), username, email, getUserType());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        BaseUser baseUser = (BaseUser) obj;
        return email.equals(baseUser.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
