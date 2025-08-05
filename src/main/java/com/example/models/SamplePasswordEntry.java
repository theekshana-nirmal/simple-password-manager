package com.example.models;

/**
 * SamplePasswordEntry class representing demo password entries for guest users.
 * Demonstrates INHERITANCE by extending BasePasswordEntry.
 * Shows POLYMORPHISM by providing specific implementations for sample
 * passwords.
 */
public class SamplePasswordEntry extends BasePasswordEntry {

    private boolean isDemo;

    /**
     * Constructor for creating a sample password entry
     */
    public SamplePasswordEntry(String website, String username, String password) {
        super(website, username, password);
        this.isDemo = true;
    }

    /**
     * Constructor with category
     */
    public SamplePasswordEntry(String website, String username, String password, String category) {
        super(website, username, password, category, "Demo entry for guest viewing");
        this.isDemo = true;
    }

    // Polymorphic method implementations

    @Override
    public String getEntryType() {
        return "Sample Password";
    }

    @Override
    public boolean canBeEdited() {
        return false; // Sample entries cannot be edited
    }

    @Override
    public boolean canBeDeleted() {
        return false; // Sample entries cannot be deleted
    }

    @Override
    public boolean isValid() {
        return isDemo && getWebsite() != null && !getWebsite().trim().isEmpty();
    }

    @Override
    public String getDisplayPassword() {
        return getMaskedPassword(); // Always show masked password for samples
    }

    /**
     * Sample entries cannot reveal actual passwords
     */
    public String getActualPassword() {
        return getMaskedPassword(); // Never reveal actual password for samples
    }

    /**
     * Check if this is a demo entry
     * 
     * @return true if this is a demo entry
     */
    public boolean isDemo() {
        return isDemo;
    }

    /**
     * Sample entries cannot be encrypted
     */
    public boolean encrypt() {
        System.out.println("Sample entries cannot be encrypted");
        return false;
    }

    /**
     * Sample entries cannot be decrypted
     */
    public boolean decrypt() {
        System.out.println("Sample entries cannot be decrypted");
        return false;
    }

    /**
     * Get demo message for guest users
     * 
     * @return demo message
     */
    public String getDemoMessage() {
        return "This is a sample password entry for demonstration purposes. " +
                "Register as a user to manage your own passwords.";
    }

    /**
     * Override password setter to prevent changes
     */
    @Override
    public void setPassword(String password) {
        // Do nothing - sample passwords cannot be changed
        System.out.println("Sample password entries cannot be modified");
    }

    /**
     * Override website setter to prevent changes
     */
    @Override
    public void setWebsite(String website) {
        // Do nothing - sample entries cannot be changed
        System.out.println("Sample password entries cannot be modified");
    }

    /**
     * Override username setter to prevent changes
     */
    @Override
    public void setUsername(String username) {
        // Do nothing - sample entries cannot be changed
        System.out.println("Sample password entries cannot be modified");
    }

    /**
     * Override category setter to prevent changes
     */
    @Override
    public void setCategory(String category) {
        // Do nothing - sample entries cannot be changed
        System.out.println("Sample password entries cannot be modified");
    }

    /**
     * Override notes setter to prevent changes
     */
    @Override
    public void setNotes(String notes) {
        // Do nothing - sample entries cannot be changed
        System.out.println("Sample password entries cannot be modified");
    }

    /**
     * Get sample entry information
     * 
     * @return formatted sample entry info
     */
    public String getSampleEntryInfo() {
        return String.format("Sample Entry - Website: %s, Username: %s, Type: %s",
                getWebsite(), getUsername(), getEntryType());
    }
}
