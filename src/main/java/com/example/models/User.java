package com.example.models;

/**
 * User model class representing a registered user.
 * Extends BaseUser to implement OOP principles.
 * Contains only the core functionality needed for the password manager.
 */
public class User extends BaseUser {
    /**
     * Constructor for creating a new user
     */
    public User(String username, String email, String passwordHash) {
        super(username, email, passwordHash);
    }

    /**
     * Constructor for loading existing user from storage (backward compatibility)
     */
    public User(String username, String email, String passwordHash, String createdAtString) {
        super(username, email, passwordHash);
        // We ignore the createdAtString since we removed that field for simplification
    }

    /**
     * Get created at string for backward compatibility
     */
    public String getCreatedAtString() {
        return ""; // Return empty string since we removed the createdAt field
    }

    // Implement abstract methods from BaseUser

    @Override
    public String getUserType() {
        return "Normal"; // All created users are normal users
    }

    @Override
    public boolean canManagePasswords() {
        return true; // All registered users can manage passwords
    }

    @Override
    public void performLoginActions() {
        System.out.println("User logged in: " + getUsername());
    }

    @Override
    public void performLogoutActions() {
        System.out.println("User logged out: " + getUsername());
    }
}
