package com.example.models;

import java.util.ArrayList;
import java.util.List;

/**
 * NormalUser class representing regular registered users.
 * Demonstrates INHERITANCE by extending BaseUser.
 * Shows POLYMORPHISM by providing normal user-specific implementations.
 */
public class NormalUser extends BaseUser {

    // Encapsulated fields specific to normal users
    private List<PasswordEntry> passwordEntries;
    private int passwordCount;
    private boolean emailVerified;

    /**
     * Constructor for creating a new normal user
     */
    public NormalUser(String username, String email, String passwordHash) {
        super(username, email, passwordHash);
        this.passwordEntries = new ArrayList<>();
        this.passwordCount = 0;
        this.emailVerified = false;
    }

    /**
     * Constructor for loading existing normal user from storage
     */
    public NormalUser(String username, String email, String passwordHash, String createdAtString) {
        super(username, email, passwordHash, createdAtString);
        this.passwordEntries = new ArrayList<>();
        this.passwordCount = 0;
        this.emailVerified = false;
    }

    // Polymorphic method implementations - different behavior for normal users

    @Override
    public String getUserType() {
        return "Normal";
    }

    @Override
    public boolean canManagePasswords() {
        return true; // Normal users can manage their own passwords
    }

    @Override
    public boolean hasAdminPrivileges() {
        return false; // Normal users don't have admin privileges
    }

    @Override
    public int getMaxPasswordCount() {
        return 100; // Normal users can store up to 100 passwords
    }

    @Override
    public void performLoginActions() {
        System.out.println("Normal user logged in: " + getUsername());
        loadPasswordEntries();
    }

    @Override
    public void performLogoutActions() {
        System.out.println("Normal user logged out: " + getUsername());
        savePasswordEntries();
        clearPasswordEntries();
    }

    // Password management methods specific to normal users

    /**
     * Add a new password entry
     * 
     * @param entry The password entry to add
     * @return true if added successfully
     */
    public boolean addPasswordEntry(PasswordEntry entry) {
        if (entry == null) {
            return false;
        }

        if (passwordCount >= getMaxPasswordCount()) {
            System.out.println("Cannot add password: Maximum limit reached (" + getMaxPasswordCount() + ")");
            return false;
        }

        passwordEntries.add(entry);
        passwordCount++;
        System.out.println("Password entry added for website: " + entry.getWebsite());
        return true;
    }

    /**
     * Remove a password entry
     * 
     * @param entry The password entry to remove
     * @return true if removed successfully
     */
    public boolean removePasswordEntry(PasswordEntry entry) {
        if (passwordEntries.remove(entry)) {
            passwordCount--;
            System.out.println("Password entry removed for website: " + entry.getWebsite());
            return true;
        }
        return false;
    }

    /**
     * Update an existing password entry
     * 
     * @param oldEntry The entry to update
     * @param newEntry The new entry data
     * @return true if updated successfully
     */
    public boolean updatePasswordEntry(PasswordEntry oldEntry, PasswordEntry newEntry) {
        int index = passwordEntries.indexOf(oldEntry);
        if (index >= 0) {
            passwordEntries.set(index, newEntry);
            System.out.println("Password entry updated for website: " + newEntry.getWebsite());
            return true;
        }
        return false;
    }

    /**
     * Get all password entries for this user
     * 
     * @return List of password entries
     */
    public List<PasswordEntry> getPasswordEntries() {
        return new ArrayList<>(passwordEntries); // Return copy to protect encapsulation
    }

    /**
     * Find password entry by website
     * 
     * @param website The website to search for
     * @return PasswordEntry if found, null otherwise
     */
    public PasswordEntry findPasswordEntryByWebsite(String website) {
        return passwordEntries.stream()
                .filter(entry -> entry.getWebsite().equalsIgnoreCase(website))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get current password count
     * 
     * @return Number of stored passwords
     */
    public int getPasswordCount() {
        return passwordCount;
    }

    /**
     * Check if user can add more passwords
     * 
     * @return true if user can add more passwords
     */
    public boolean canAddMorePasswords() {
        return passwordCount < getMaxPasswordCount();
    }

    /**
     * Get remaining password slots
     * 
     * @return Number of remaining password slots
     */
    public int getRemainingPasswordSlots() {
        return getMaxPasswordCount() - passwordCount;
    } // Email verification methods

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    // Private helper methods for password management

    private void loadPasswordEntries() {
        // Load password entries from CSV file
        try {
            List<com.example.controllers.UserController.PasswordEntry> loadedEntries = com.example.utils.CSVHandler
                    .loadUserPasswordsFromCSV(getUsername());

            passwordEntries.clear();

            // Convert from controller PasswordEntry to model PasswordEntry
            for (com.example.controllers.UserController.PasswordEntry controllerEntry : loadedEntries) {
                PasswordEntry modelEntry = new PasswordEntry(
                        controllerEntry.getWebsite(),
                        controllerEntry.getUsername(),
                        controllerEntry.getPassword());
                passwordEntries.add(modelEntry);
            }

            passwordCount = passwordEntries.size();
            System.out.println("Loaded " + passwordCount + " password entries for user: " + getUsername());
        } catch (Exception e) {
            System.err.println("Error loading password entries: " + e.getMessage());
        }
    }

    private void savePasswordEntries() {
        // Save password entries to CSV file
        try {
            javafx.collections.ObservableList<com.example.controllers.UserController.PasswordEntry> controllerEntries = javafx.collections.FXCollections
                    .observableArrayList();

            for (PasswordEntry entry : passwordEntries) {
                controllerEntries.add(new com.example.controllers.UserController.PasswordEntry(
                        entry.getWebsite(), entry.getUsername(), entry.getPassword()));
            }

            com.example.utils.CSVHandler.saveUserPasswordsToCSV(getUsername(), controllerEntries);
            System.out.println("Saved " + passwordCount + " password entries for user: " + getUsername());
        } catch (Exception e) {
            System.err.println("Error saving password entries: " + e.getMessage());
        }
    }

    private void clearPasswordEntries() {
        passwordEntries.clear();
        passwordCount = 0;
    }

    /**
     * Get user statistics
     * 
     * @return String containing user statistics
     */
    public String getUserStatistics() {
        return String.format("User: %s | Passwords: %d/%d | Email Verified: %s",
                getUsername(), passwordCount, getMaxPasswordCount(), emailVerified ? "Yes" : "No");
    }
}
