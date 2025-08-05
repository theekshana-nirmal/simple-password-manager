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
     * Constructor for loading existing user from storage
     */
    public User(String username, String email, String passwordHash, String createdAtString) {
        super(username, email, passwordHash, createdAtString);
    }

    // Implement abstract methods from BaseUser

    @Override
    public String getUserType() {
        // Determine type based on username/email patterns
        if (getUsername().toLowerCase().contains("admin") ||
                getEmail().toLowerCase().contains("admin")) {
            return "Admin";
        }
        return "Normal";
    }

    @Override
    public boolean canManagePasswords() {
        return true; // All registered users can manage passwords
    }

    @Override
    public boolean hasAdminPrivileges() {
        return getUserType().equals("Admin");
    }

    @Override
    public int getMaxPasswordCount() {
        return hasAdminPrivileges() ? -1 : 100; // Unlimited for admin, 100 for normal users
    }

    @Override
    public void performLoginActions() {
        System.out.println("User logged in: " + getUsername() + " (Type: " + getUserType() + ")");
    }

    @Override
    public void performLogoutActions() {
        System.out.println("User logged out: " + getUsername() + " (Type: " + getUserType() + ")");
    }
}
