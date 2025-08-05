package com.example.models;

import com.example.utils.EncryptionUtils;

/**
 * This file contains the PasswordEntry class that stores a user's password
 * information with encryption support.
 * OOP Concept: This class demonstrates INHERITANCE by extending
 * BasePasswordEntry and adds SECURITY through password encryption.
 */
public class PasswordEntry extends BasePasswordEntry {

    // Creates a new password entry with website, username and encrypted password
    public PasswordEntry(String website, String username, String password) {
        // Encrypt the password before storing
        super(website, username, EncryptionUtils.encryptPassword(password));
    }

    // Checks if all required fields have valid data
    @Override
    public boolean isValidEntry() {
        return getWebsite() != null && !getWebsite().trim().isEmpty() &&
                getUsername() != null && !getUsername().trim().isEmpty() &&
                getPassword() != null && !getPassword().trim().isEmpty();
    }    // Creates a password entry from a CSV line format
    public PasswordEntry(String csvLine) {
        super("", "", ""); // Call parent constructor with empty values first
        
        // When loading from CSV, assume password might already be encrypted
        String website = parseWebsite(csvLine);
        String username = parseUsername(csvLine);
        String password = parsePassword(csvLine);
        
        // Set the website and username
        setWebsite(website);
        setUsername(username);
        
        // Check if the password is already encrypted
        if (com.example.utils.EncryptionUtils.isEncrypted(password)) {
            setPassword(password); // Already encrypted
        } else {
            // Needs encryption (legacy data)
            setPassword(com.example.utils.EncryptionUtils.encryptPassword(password));
        }
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

    // Returns the decrypted password for display/use
    public String getDecryptedPassword() {
        return EncryptionUtils.decryptPassword(getPassword());
    }

    // Special constructor that takes pre-encrypted password (for loading from storage)
    public PasswordEntry(String website, String username, String encryptedPassword, boolean isAlreadyEncrypted) {
        super(website, username, encryptedPassword);
    }
}
