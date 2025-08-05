package com.example.models;

/**
 * Abstract base class for password entries.
 * Demonstrates ABSTRACTION and ENCAPSULATION principles.
 * Contains common password entry attributes and methods.
 */
public abstract class BasePasswordEntry {

    // Encapsulated core password entry attributes
    private String website;
    private String username;
    private String password;

    /**
     * Constructor for creating a password entry
     */
    public BasePasswordEntry(String website, String username, String password) {
        this.website = website;
        this.username = username;
        this.password = password;
    }

    // Getters for encapsulated fields
    public String getWebsite() {
        return website;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters for updating entries
    public void setWebsite(String website) {
        this.website = website;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Abstract method for polymorphic behavior
    public abstract boolean isValidEntry();

    @Override
    public String toString() {
        return website + "," + username + "," + password;
    }
}
