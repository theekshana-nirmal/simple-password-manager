package com.example.models;

import java.util.ArrayList;
import java.util.List;

/**
 * AdminUser class representing administrative users.
 * Demonstrates INHERITANCE by extending BaseUser.
 * Shows POLYMORPHISM by providing admin-specific implementations.
 */
public class AdminUser extends BaseUser {

    // Encapsulated fields specific to admin users
    private List<String> adminPrivileges;
    private List<String> managedUsers;
    private int totalUsersManaged;
    private boolean canDeleteUsers;
    private boolean canViewAllPasswords;
    private boolean canChangeSystemSettings;

    /**
     * Constructor for creating a new admin user
     */
    public AdminUser(String username, String email, String passwordHash) {
        super(username, email, passwordHash);
        initializeAdminPrivileges();
    }

    /**
     * Constructor for loading existing admin user from storage
     */
    public AdminUser(String username, String email, String passwordHash, String createdAtString) {
        super(username, email, passwordHash, createdAtString);
        initializeAdminPrivileges();
    }

    /**
     * Initialize admin privileges and capabilities
     */
    private void initializeAdminPrivileges() {
        this.adminPrivileges = new ArrayList<>();
        this.managedUsers = new ArrayList<>();
        this.totalUsersManaged = 0;
        this.canDeleteUsers = true;
        this.canViewAllPasswords = false; // For security, admins should not view user passwords
        this.canChangeSystemSettings = true;

        // Add default admin privileges
        adminPrivileges.add("USER_MANAGEMENT");
        adminPrivileges.add("SYSTEM_CONFIGURATION");
        adminPrivileges.add("DATA_BACKUP");
        adminPrivileges.add("USER_DELETION");
        adminPrivileges.add("AUDIT_LOGS");
    }

    // Polymorphic method implementations - different behavior for admin users

    @Override
    public String getUserType() {
        return "Admin";
    }

    @Override
    public boolean canManagePasswords() {
        return true; // Admins can manage their own passwords
    }

    @Override
    public boolean hasAdminPrivileges() {
        return true; // Admin users have admin privileges
    }

    @Override
    public int getMaxPasswordCount() {
        return -1; // Unlimited password storage for admins
    }

    @Override
    public void performLoginActions() {
        System.out.println("Admin user logged in: " + getUsername());
        loadManagedUsers();
        auditLog("Admin login", "Admin " + getUsername() + " logged into the system");
    }

    @Override
    public void performLogoutActions() {
        System.out.println("Admin user logged out: " + getUsername());
        auditLog("Admin logout", "Admin " + getUsername() + " logged out of the system");
        clearAdminSession();
    }

    // Admin-specific methods

    /**
     * Check if admin has a specific privilege
     * 
     * @param privilege The privilege to check
     * @return true if admin has the privilege
     */
    public boolean hasPrivilege(String privilege) {
        return adminPrivileges.contains(privilege);
    }

    /**
     * Add a privilege to the admin
     * 
     * @param privilege The privilege to add
     * @return true if privilege was added
     */
    public boolean addPrivilege(String privilege) {
        if (privilege != null && !privilege.trim().isEmpty() && !adminPrivileges.contains(privilege)) {
            adminPrivileges.add(privilege);
            auditLog("Privilege added", "Privilege '" + privilege + "' added to admin " + getUsername());
            return true;
        }
        return false;
    }

    /**
     * Remove a privilege from the admin
     * 
     * @param privilege The privilege to remove
     * @return true if privilege was removed
     */
    public boolean removePrivilege(String privilege) {
        if (adminPrivileges.remove(privilege)) {
            auditLog("Privilege removed", "Privilege '" + privilege + "' removed from admin " + getUsername());
            return true;
        }
        return false;
    }

    /**
     * Get all admin privileges
     * 
     * @return List of admin privileges
     */
    public List<String> getAdminPrivileges() {
        return new ArrayList<>(adminPrivileges); // Return copy to protect encapsulation
    }

    /**
     * Delete a user (admin action)
     * 
     * @param userEmail The email of the user to delete
     * @return true if user was deleted successfully
     */
    public boolean deleteUser(String userEmail) {
        if (!canDeleteUsers || !hasPrivilege("USER_DELETION")) {
            System.out.println("Admin does not have permission to delete users");
            return false;
        }

        if (userEmail != null && !userEmail.trim().isEmpty()) {
            boolean deleted = com.example.utils.UserManager.deleteUser(userEmail);
            if (deleted) {
                managedUsers.remove(userEmail);
                totalUsersManaged++;
                auditLog("User deleted", "Admin " + getUsername() + " deleted user: " + userEmail);
                System.out.println("Admin " + getUsername() + " deleted user: " + userEmail);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all users in the system (admin action)
     * 
     * @return List of all users
     */
    public List<BaseUser> getAllUsers() {
        if (!hasPrivilege("USER_MANAGEMENT")) {
            System.out.println("Admin does not have permission to view all users");
            return new ArrayList<>();
        }

        // Convert User objects to BaseUser objects
        List<com.example.models.User> users = com.example.utils.UserManager.getAllUsers();
        List<BaseUser> baseUsers = new ArrayList<>();

        for (com.example.models.User user : users) {
            // Create NormalUser objects from User data
            NormalUser normalUser = new NormalUser(user.getUsername(), user.getEmail(),
                    user.getPasswordHash(), user.getCreatedAtString());
            baseUsers.add(normalUser);
        }

        auditLog("User list accessed", "Admin " + getUsername() + " accessed user list");
        return baseUsers;
    }

    /**
     * Change admin password with additional validation
     * 
     * @param currentPassword Current password
     * @param newPassword     New password
     * @return true if password was changed successfully
     */
    public boolean changeAdminPassword(String currentPassword, String newPassword) {
        if (!validateCredentials(currentPassword)) {
            auditLog("Password change failed", "Invalid current password for admin " + getUsername());
            return false;
        }

        if (newPassword == null || newPassword.length() < 8) {
            System.out.println("New password must be at least 8 characters long");
            return false;
        }

        updatePassword(newPassword);
        auditLog("Password changed", "Admin " + getUsername() + " changed password");

        // Update admin credentials in AdminManager
        return com.example.utils.AdminManager.changePassword(currentPassword, newPassword);
    }

    /**
     * Generate system report (admin action)
     * 
     * @return system report as string
     */
    public String generateSystemReport() {
        if (!hasPrivilege("AUDIT_LOGS")) {
            return "Insufficient privileges to generate system report";
        }

        StringBuilder report = new StringBuilder();
        report.append("=== SYSTEM REPORT ===\n");
        report.append("Generated by: ").append(getUsername()).append("\n");
        report.append("Generated at: ").append(java.time.LocalDateTime.now()).append("\n\n");

        List<BaseUser> allUsers = getAllUsers();
        report.append("Total Users: ").append(allUsers.size()).append("\n");
        report.append("Users Managed by Admin: ").append(totalUsersManaged).append("\n");
        report.append("Admin Privileges: ").append(String.join(", ", adminPrivileges)).append("\n");

        auditLog("System report generated", "Admin " + getUsername() + " generated system report");
        return report.toString();
    }

    /**
     * Backup user data (admin action)
     * 
     * @return true if backup was successful
     */
    public boolean backupUserData() {
        if (!hasPrivilege("DATA_BACKUP")) {
            System.out.println("Admin does not have permission to backup data");
            return false;
        }

        try {
            // In a real implementation, this would create actual backups
            System.out.println("Creating user data backup...");
            auditLog("Data backup", "Admin " + getUsername() + " initiated data backup");
            System.out.println("Backup completed successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Backup failed: " + e.getMessage());
            auditLog("Backup failed", "Data backup failed for admin " + getUsername() + ": " + e.getMessage());
            return false;
        }
    }

    // Getter methods for admin properties

    public boolean canDeleteUsers() {
        return canDeleteUsers;
    }

    public void setCanDeleteUsers(boolean canDeleteUsers) {
        this.canDeleteUsers = canDeleteUsers;
    }

    public boolean canViewAllPasswords() {
        return canViewAllPasswords;
    }

    public void setCanViewAllPasswords(boolean canViewAllPasswords) {
        this.canViewAllPasswords = canViewAllPasswords;
    }

    public boolean canChangeSystemSettings() {
        return canChangeSystemSettings;
    }

    public void setCanChangeSystemSettings(boolean canChangeSystemSettings) {
        this.canChangeSystemSettings = canChangeSystemSettings;
    }

    public int getTotalUsersManaged() {
        return totalUsersManaged;
    }

    public List<String> getManagedUsers() {
        return new ArrayList<>(managedUsers);
    }

    /**
     * Get admin statistics
     * 
     * @return String containing admin statistics
     */
    public String getAdminStatistics() {
        return String.format("Admin: %s | Users Managed: %d | Privileges: %d | Can Delete Users: %s",
                getUsername(), totalUsersManaged, adminPrivileges.size(), canDeleteUsers ? "Yes" : "No");
    }

    // Private helper methods

    private void loadManagedUsers() {
        // Load list of users managed by this admin
        managedUsers.clear();
        List<BaseUser> allUsers = getAllUsers();
        for (BaseUser user : allUsers) {
            managedUsers.add(user.getEmail());
        }
    }

    private void clearAdminSession() {
        // Clear admin session data
        managedUsers.clear();
    }

    private void auditLog(String action, String details) {
        // In a real implementation, this would write to an audit log file
        String logEntry = String.format("[%s] %s: %s",
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                action, details);
        System.out.println("AUDIT LOG: " + logEntry);
    }
}
