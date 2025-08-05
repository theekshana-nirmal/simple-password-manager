package com.example.models;

/**
 * User model class representing a registered user.
 * Now extends BaseUser to maintain compatibility while implementing OOP
 * principles.
 * This class bridges the gap between the old and new model structures.
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
    // Default to NormalUser behavior for backward compatibility

    @Override
    public String getUserType() {
        // Determine type based on username/email patterns for backward compatibility
        if (getUsername().toLowerCase().contains("admin") ||
                getEmail().toLowerCase().contains("admin")) {
            return "Admin";
        }
        return "Normal";
    }

    @Override
    public boolean canManagePasswords() {
        return true; // Backward compatibility - assume all User objects can manage passwords
    }

    @Override
    public boolean hasAdminPrivileges() {
        // Check if this is an admin user based on naming patterns
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

    /**
     * Convert this User to a more specific BaseUser type
     * 
     * @return Appropriate BaseUser subclass instance
     */
    public BaseUser toSpecificUserType() {
        if (hasAdminPrivileges()) {
            return UserFactory.createAdminUser(getUsername(), getEmail(), getPasswordHash(), getCreatedAtString());
        } else {
            return UserFactory.createNormalUser(getUsername(), getEmail(), getPasswordHash(), getCreatedAtString());
        }
    }

    /**
     * Legacy method for backward compatibility
     * 
     * @return User type as determined by the enhanced model
     */
    public String determineUserType() {
        return getUserType();
    }
}
