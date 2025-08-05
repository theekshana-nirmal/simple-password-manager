package com.example.models;

/**
 * PasswordEntry class representing user-managed password entries.
 * Demonstrates INHERITANCE by extending BasePasswordEntry.
 * Shows POLYMORPHISM by providing specific implementations for user passwords.
 */
public class PasswordEntry extends BasePasswordEntry {

    private boolean isEncrypted;
    private boolean isFavorite;
    private int accessCount;

    /**
     * Constructor for creating a new password entry
     */
    public PasswordEntry(String website, String username, String password) {
        super(website, username, password);
        this.isEncrypted = false;
        this.isFavorite = false;
        this.accessCount = 0;
    }

    /**
     * Constructor with additional fields
     */
    public PasswordEntry(String website, String username, String password, String category, String notes) {
        super(website, username, password, category, notes);
        this.isEncrypted = false;
        this.isFavorite = false;
        this.accessCount = 0;
    }

    /**
     * Constructor with all fields
     */
    public PasswordEntry(String website, String username, String password, String category, String notes,
            boolean isFavorite, boolean isEncrypted) {
        super(website, username, password, category, notes);
        this.isEncrypted = isEncrypted;
        this.isFavorite = isFavorite;
        this.accessCount = 0;
    }

    // Polymorphic method implementations

    @Override
    public String getEntryType() {
        return "User Password";
    }

    @Override
    public boolean canBeEdited() {
        return true; // User password entries can always be edited
    }

    @Override
    public boolean canBeDeleted() {
        return true; // User password entries can always be deleted
    }

    @Override
    public boolean isValid() {
        return getWebsite() != null && !getWebsite().trim().isEmpty() &&
                getUsername() != null && !getUsername().trim().isEmpty() &&
                getPassword() != null && !getPassword().trim().isEmpty();
    }

    @Override
    public String getDisplayPassword() {
        return getMaskedPassword(); // Always show masked password for security
    }

    // Additional methods specific to user password entries

    /**
     * Get the actual password (for editing/viewing)
     * This should only be called when user explicitly requests to view password
     * 
     * @return the actual password
     */
    public String getActualPassword() {
        accessCount++;
        return getPassword();
    }

    /**
     * Encrypt the password entry
     * 
     * @return true if encryption successful
     */
    public boolean encrypt() {
        if (!isEncrypted) {
            try {
                // In a real application, you would use proper encryption here
                // For this demo, we'll just mark it as encrypted
                isEncrypted = true;
                updateLastModified();
                System.out.println("Password entry encrypted for: " + getWebsite());
                return true;
            } catch (Exception e) {
                System.err.println("Failed to encrypt password entry: " + e.getMessage());
                return false;
            }
        }
        return true; // Already encrypted
    }

    /**
     * Decrypt the password entry
     * 
     * @return true if decryption successful
     */
    public boolean decrypt() {
        if (isEncrypted) {
            try {
                // In a real application, you would use proper decryption here
                // For this demo, we'll just mark it as decrypted
                isEncrypted = false;
                updateLastModified();
                System.out.println("Password entry decrypted for: " + getWebsite());
                return true;
            } catch (Exception e) {
                System.err.println("Failed to decrypt password entry: " + e.getMessage());
                return false;
            }
        }
        return true; // Already decrypted
    }

    /**
     * Toggle favorite status
     */
    public void toggleFavorite() {
        this.isFavorite = !this.isFavorite;
        updateLastModified();
        System.out.println(
                "Password entry " + (isFavorite ? "added to" : "removed from") + " favorites: " + getWebsite());
    }

    /**
     * Check if this entry is encrypted
     * 
     * @return true if encrypted
     */
    public boolean isEncrypted() {
        return isEncrypted;
    }

    /**
     * Check if this entry is marked as favorite
     * 
     * @return true if favorite
     */
    public boolean isFavorite() {
        return isFavorite;
    }

    /**
     * Set favorite status
     * 
     * @param favorite true to mark as favorite
     */
    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
        updateLastModified();
    }

    /**
     * Get access count (how many times password was viewed)
     * 
     * @return access count
     */
    public int getAccessCount() {
        return accessCount;
    }

    /**
     * Reset access count
     */
    public void resetAccessCount() {
        this.accessCount = 0;
    }

    /**
     * Clone this password entry
     * 
     * @return a copy of this password entry
     */
    public PasswordEntry clone() {
        PasswordEntry clone = new PasswordEntry(getWebsite(), getUsername(), getPassword(),
                getCategory(), getNotes(), isFavorite, isEncrypted);
        clone.accessCount = this.accessCount;
        return clone;
    }

    /**
     * Get entry summary for display
     * 
     * @return formatted entry summary
     */
    public String getEntrySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Website: ").append(getWebsite()).append("\n");
        summary.append("Username: ").append(getUsername()).append("\n");
        summary.append("Category: ").append(getCategory()).append("\n");
        summary.append("Created: ").append(getCreatedAtString()).append("\n");
        summary.append("Modified: ").append(getLastModifiedString()).append("\n");
        summary.append("Encrypted: ").append(isEncrypted ? "Yes" : "No").append("\n");
        summary.append("Favorite: ").append(isFavorite ? "Yes" : "No").append("\n");
        summary.append("Password Strength: ").append(getPasswordStrength()).append("/5\n");
        summary.append("Access Count: ").append(accessCount);
        return summary.toString();
    }

    /**
     * Update password with validation
     * 
     * @param newPassword the new password
     * @return true if password was updated successfully
     */
    public boolean updatePassword(String newPassword) {
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            setPassword(newPassword);
            System.out.println("Password updated for: " + getWebsite());
            return true;
        }
        return false;
    }

    /**
     * Update entry details
     * 
     * @param website  new website
     * @param username new username
     * @param password new password
     * @param category new category
     * @param notes    new notes
     * @return true if update successful
     */
    public boolean updateEntry(String website, String username, String password, String category, String notes) {
        if (website == null || website.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return false;
        }

        setWebsite(website);
        setUsername(username);
        setPassword(password);
        setCategory(category);
        setNotes(notes);

        System.out.println("Password entry updated: " + getWebsite());
        return true;
    }
}
