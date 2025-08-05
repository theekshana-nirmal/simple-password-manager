package com.example.models;

import java.util.ArrayList;
import java.util.List;

/**
 * GuestUser class representing guest access to the application.
 * Demonstrates INHERITANCE by extending BaseUser.
 * Shows POLYMORPHISM by providing guest-specific implementations.
 */
public class GuestUser extends BaseUser {

    private List<PasswordEntry> samplePasswords;

    /**
     * Constructor for guest user
     */
    public GuestUser() {
        super("Guest", "guest@example.com", "");
        initializeSamplePasswords();
    }

    /**
     * Initialize sample passwords for demonstration
     */
    private void initializeSamplePasswords() {
        samplePasswords = new ArrayList<>();
        samplePasswords.add(new PasswordEntry("github.com", "john_doe", "password123"));
        samplePasswords.add(new PasswordEntry("google.com", "john.doe@email.com", "mypassword"));
        samplePasswords.add(new PasswordEntry("facebook.com", "johndoe", "pass1234"));
    }

    /**
     * Get sample passwords for demo purposes
     */
    public List<PasswordEntry> getSamplePasswords() {
        return new ArrayList<>(samplePasswords);
    }

    // Polymorphic method implementations

    @Override
    public String getUserType() {
        return "Guest";
    }

    @Override
    public boolean canManagePasswords() {
        return false; // Guests cannot manage passwords
    }

    @Override
    public boolean hasAdminPrivileges() {
        return false;
    }

    @Override
    public int getMaxPasswordCount() {
        return 0; // Guests cannot save passwords
    }

    @Override
    public void performLoginActions() {
        System.out.println("Guest user accessed the system");
    }

    @Override
    public void performLogoutActions() {
        System.out.println("Guest user left the system");
    }
}
