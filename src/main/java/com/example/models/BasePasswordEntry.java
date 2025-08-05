package com.example.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for password entries in the password manager.
 * Demonstrates ABSTRACTION by defining common password entry properties
 * while allowing different types of password entries.
 */
public abstract class BasePasswordEntry {

    // Encapsulated fields - private access with controlled getter/setter methods
    private String website;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private String category;
    private String notes;

    /**
     * Constructor for creating a new password entry
     */
    protected BasePasswordEntry(String website, String username, String password) {
        this.website = website;
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.category = "General";
        this.notes = "";
    }

    /**
     * Constructor with additional fields
     */
    protected BasePasswordEntry(String website, String username, String password, String category, String notes) {
        this.website = website;
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.category = category != null ? category : "General";
        this.notes = notes != null ? notes : "";
    }

    // Abstract methods that must be implemented by subclasses

    /**
     * Get the entry type - implemented differently by each subclass
     * 
     * @return The entry type as string
     */
    public abstract String getEntryType();

    /**
     * Check if this entry can be edited
     * 
     * @return true if entry can be edited
     */
    public abstract boolean canBeEdited();

    /**
     * Check if this entry can be deleted
     * 
     * @return true if entry can be deleted
     */
    public abstract boolean canBeDeleted();

    /**
     * Validate the password entry data
     * 
     * @return true if entry is valid
     */
    public abstract boolean isValid();

    /**
     * Get display format for password (masked or plain)
     * 
     * @return formatted password string
     */
    public abstract String getDisplayPassword();

    // Encapsulated getter and setter methods

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        if (website != null && !website.trim().isEmpty()) {
            this.website = website.trim();
            updateLastModified();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username.trim();
            updateLastModified();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && !password.trim().isEmpty()) {
            this.password = password;
            updateLastModified();
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category != null && !category.trim().isEmpty()) {
            this.category = category.trim();
            updateLastModified();
        }
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
        updateLastModified();
    }

    /**
     * Update the last modified timestamp
     */
    protected void updateLastModified() {
        this.lastModified = LocalDateTime.now();
    }

    /**
     * Get formatted creation date
     * 
     * @return formatted creation date string
     */
    public String getCreatedAtString() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * Get formatted last modified date
     * 
     * @return formatted last modified date string
     */
    public String getLastModifiedString() {
        return lastModified.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * Generate a masked version of the password
     * 
     * @return masked password string
     */
    protected String getMaskedPassword() {
        if (password == null || password.isEmpty()) {
            return "••••••";
        }
        return "•".repeat(Math.min(password.length(), 12));
    }

    /**
     * Check if password meets minimum security requirements
     * 
     * @return true if password is secure enough
     */
    public boolean isPasswordSecure() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Get password strength score (1-5)
     * 
     * @return password strength score
     */
    public int getPasswordStrength() {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int score = 0;
        if (password.length() >= 8)
            score++;
        if (password.length() >= 12)
            score++;
        if (password.chars().anyMatch(Character::isUpperCase))
            score++;
        if (password.chars().anyMatch(Character::isLowerCase))
            score++;
        if (password.chars().anyMatch(Character::isDigit))
            score++;
        if (password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0))
            score++;

        return Math.min(5, score);
    }

    @Override
    public String toString() {
        return String.format("%s{website='%s', username='%s', category='%s'}",
                getClass().getSimpleName(), website, username, category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        BasePasswordEntry that = (BasePasswordEntry) obj;
        return website.equals(that.website) && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return (website + username).hashCode();
    }
}
