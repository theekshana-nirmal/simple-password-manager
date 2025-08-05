package com.example.models;

/**
 * This file contains the PasswordEntry class that stores a user's password
 * information.
 * OOP Concept: This class demonstrates INHERITANCE by extending
 * BasePasswordEntry.
 */
public class PasswordEntry extends BasePasswordEntry {

    // Creates a new password entry with website, username and password
    public PasswordEntry(String website, String username, String password) {
        super(website, username, password);
    }

    // Checks if all required fields have valid data
    @Override
    public boolean isValidEntry() {
        return getWebsite() != null && !getWebsite().trim().isEmpty() &&
                getUsername() != null && !getUsername().trim().isEmpty() &&
                getPassword() != null && !getPassword().trim().isEmpty();
    }

    // Creates a password entry from a CSV line format
    public PasswordEntry(String csvLine) {
        this(parseWebsite(csvLine), parseUsername(csvLine), parsePassword(csvLine));
    }

    // Extracts website from a CSV line
    private static String parseWebsite(String csvLine) {
        String[] parts = csvLine.split(",");
        return parts.length > 0 ? parts[0] : "";
    }

    // Extracts username from a CSV line
    private static String parseUsername(String csvLine) {
        String[] parts = csvLine.split(",");
        return parts.length > 1 ? parts[1] : "";
    }

    // Extracts password from a CSV line
    private static String parsePassword(String csvLine) {
        String[] parts = csvLine.split(",");
        return parts.length > 2 ? parts[2] : "";
    }
}
