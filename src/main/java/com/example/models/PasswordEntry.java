package com.example.models;

/**
 * PasswordEntry class representing a user's password entry.
 * Demonstrates INHERITANCE by extending BasePasswordEntry.
 * Contains only the core functionality needed for password management.
 */
public class PasswordEntry extends BasePasswordEntry {

    /**
     * Constructor for creating a password entry
     */
    public PasswordEntry(String website, String username, String password) {
        super(website, username, password);
    }

    /**
     * Implementation of abstract method from BasePasswordEntry
     */
    @Override
    public boolean isValidEntry() {
        return getWebsite() != null && !getWebsite().trim().isEmpty() &&
                getUsername() != null && !getUsername().trim().isEmpty() &&
                getPassword() != null && !getPassword().trim().isEmpty();
    }

    /**
     * Legacy constructor for backward compatibility with existing CSV format
     */
    public PasswordEntry(String csvLine) {
        this(parseWebsite(csvLine), parseUsername(csvLine), parsePassword(csvLine));
    }

    // Helper methods for parsing CSV format
    private static String parseWebsite(String csvLine) {
        String[] parts = csvLine.split(",");
        return parts.length > 0 ? parts[0] : "";
    }

    private static String parseUsername(String csvLine) {
        String[] parts = csvLine.split(",");
        return parts.length > 1 ? parts[1] : "";
    }

    private static String parsePassword(String csvLine) {
        String[] parts = csvLine.split(",");
        return parts.length > 2 ? parts[2] : "";
    }
}
