package com.example.models;

/**
 * GuestUser class representing users in guest mode.
 * Demonstrates INHERITANCE by extending BaseUser.
 * Shows POLYMORPHISM by providing guest-specific implementations.
 */
public class GuestUser extends BaseUser {

    /**
     * Create a guest user (temporary, no persistent storage)
     */
    public GuestUser() {
        super("Guest", "guest@temp.com", "no-password");
    }

    // Polymorphic method implementations - different behavior for guest users

    @Override
    public String getUserType() {
        return "Guest";
    }

    @Override
    public boolean canManagePasswords() {
        return false; // Guest users cannot manage real passwords
    }

    @Override
    public boolean hasAdminPrivileges() {
        return false; // Guest users have no admin privileges
    }

    @Override
    public int getMaxPasswordCount() {
        return 0; // Guest users cannot store any passwords
    }

    @Override
    public void performLoginActions() {
        System.out.println("Guest user accessing demo mode - limited functionality");
    }

    @Override
    public void performLogoutActions() {
        System.out.println("Guest user session ended");
    }

    /**
     * Guest users cannot validate credentials (always false)
     */
    @Override
    public boolean validateCredentials(String password) {
        return false; // Guest users don't have real credentials
    }

    /**
     * Guest users cannot update passwords
     */
    @Override
    public void updatePassword(String newPassword) {
        throw new UnsupportedOperationException("Guest users cannot update passwords");
    }

    /**
     * Check if user can view sample data
     * 
     * @return true for guest users
     */
    public boolean canViewSampleData() {
        return true;
    }

    /**
     * Get sample password data for demo purposes
     * 
     * @return Array of sample password entries
     */
    public String[][] getSamplePasswordData() {
        return new String[][] {
                { "Facebook", "demo@email.com", "••••••••" },
                { "Gmail", "demouser123", "••••••••" },
                { "GitHub", "demo_developer", "••••••••" },
                { "Netflix", "demo.user", "••••••••" },
                { "Amazon", "demo@example.com", "••••••••" }
        };
    }
}
