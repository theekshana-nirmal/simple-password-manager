package com.example.models;

/**
 * This file contains the BasePasswordEntry abstract class that defines the
 * structure for password entries.
 * OOP Concepts: This class demonstrates ABSTRACTION and ENCAPSULATION by hiding
 * implementation details.
 */
public abstract class BasePasswordEntry {

    private String website;
    private String username;
    private String password;

    // Creates a base password entry with required fields
    public BasePasswordEntry(String website, String username, String password) {
        this.website = website;
        this.username = username;
        this.password = password;
    }

    // Returns the website or service name
    public String getWebsite() {
        return website;
    }

    // Returns the username for the password entry
    public String getUsername() {
        return username;
    }

    // Returns the password for this entry
    public String getPassword() {
        return password;
    }

    // Updates the website or service name
    public void setWebsite(String website) {
        this.website = website;
    }

    // Updates the username
    public void setUsername(String username) {
        this.username = username;
    }

    // Updates the password
    public void setPassword(String password) {
        this.password = password;
    }

    // Defines a method to check if the entry has valid data
    public abstract boolean isValidEntry();

    // Converts the password entry to CSV format
    @Override
    public String toString() {
        return website + "," + username + "," + password;
    }
}
