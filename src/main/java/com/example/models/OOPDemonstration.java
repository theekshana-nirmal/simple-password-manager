package com.example.models;

import java.util.List;

/**
 * Demonstration class showing how to use the new OOP model structure.
 * This class provides examples of how the 4 core OOP principles are implemented
 * and demonstrates the benefits of the redesigned architecture.
 */
public class OOPDemonstration {

    /**
     * Demonstrate POLYMORPHISM - same method calls work differently based on object
     * type
     */
    public static void demonstratePolymorphism() {
        System.out.println("=== POLYMORPHISM DEMONSTRATION ===");

        // Create different user types
        BaseUser[] users = {
                UserFactory.createGuestUser(),
                UserFactory.createNormalUser("john_doe", "john@email.com", "hashedPassword123"),
                UserFactory.createAdminUser("admin_user", "admin@system.com", "hashedAdminPass456")
        };

        // Same method calls, different behaviors (POLYMORPHISM)
        for (BaseUser user : users) {
            System.out.println("\n--- User Type: " + user.getUserType() + " ---");
            System.out.println("Can manage passwords: " + user.canManagePasswords());
            System.out.println("Has admin privileges: " + user.hasAdminPrivileges());
            System.out.println("Max password count: " + user.getMaxPasswordCount());

            // Polymorphic method calls - different behavior for each user type
            user.performLoginActions();

            // Demonstrate runtime type checking and casting
            if (user instanceof NormalUser) {
                NormalUser normalUser = (NormalUser) user;
                System.out.println("Normal user can add more passwords: " + normalUser.canAddMorePasswords());
            } else if (user instanceof AdminUser) {
                AdminUser adminUser = (AdminUser) user;
                System.out.println("Admin privileges: " + adminUser.getAdminPrivileges());
            } else if (user instanceof GuestUser) {
                GuestUser guestUser = (GuestUser) user;
                System.out.println("Can view sample data: " + guestUser.canViewSampleData());
            }
        }
    }

    /**
     * Demonstrate INHERITANCE - child classes inherit and extend parent
     * functionality
     */
    public static void demonstrateInheritance() {
        System.out.println("\n=== INHERITANCE DEMONSTRATION ===");

        // Create a normal user (inherits from BaseUser)
        NormalUser normalUser = UserFactory.createNormalUser("jane_smith", "jane@email.com", "password789");

        System.out.println("--- Inherited Properties and Methods ---");
        System.out.println("Username (inherited): " + normalUser.getUsername());
        System.out.println("Email (inherited): " + normalUser.getEmail());
        System.out.println("Created At (inherited): " + normalUser.getCreatedAtString());
        System.out.println("User Type (overridden): " + normalUser.getUserType());

        System.out.println("\n--- Extended Functionality ---");
        System.out.println("Password count (extended): " + normalUser.getPasswordCount());
        System.out.println("Remaining slots (extended): " + normalUser.getRemainingPasswordSlots());
        System.out.println("User statistics (extended): " + normalUser.getUserStatistics());

        // Demonstrate password entry inheritance
        System.out.println("\n--- Password Entry Inheritance ---");
        PasswordEntry userEntry = new PasswordEntry("GitHub", "jane_dev", "secureCode123");
        SamplePasswordEntry sampleEntry = new SamplePasswordEntry("Demo Site", "demo_user", "demo_pass");

        BasePasswordEntry[] entries = { userEntry, sampleEntry };

        for (BasePasswordEntry entry : entries) {
            System.out.println("Entry Type: " + entry.getEntryType());
            System.out.println("Can be edited: " + entry.canBeEdited());
            System.out.println("Can be deleted: " + entry.canBeDeleted());
            System.out.println("Display password: " + entry.getDisplayPassword());
            System.out.println("---");
        }
    }

    /**
     * Demonstrate ENCAPSULATION - controlled access to object data
     */
    public static void demonstrateEncapsulation() {
        System.out.println("\n=== ENCAPSULATION DEMONSTRATION ===");

        // Create a normal user
        NormalUser user = UserFactory.createNormalUser("test_user", "test@email.com", "testPassword123");

        System.out.println("--- Controlled Data Access ---");

        // Direct field access is not possible (fields are private)
        // user.username = "hacker"; // This would not compile

        // Access must go through controlled methods
        System.out.println("Current username: " + user.getUsername());

        // Setters include validation (ENCAPSULATION)
        user.setUsername("  valid_username  "); // Automatically trimmed
        System.out.println("Updated username: " + user.getUsername());

        user.setUsername(""); // Invalid input, no change
        System.out.println("Username after invalid input: " + user.getUsername());

        // Demonstrate password entry encapsulation
        System.out.println("\n--- Password Entry Encapsulation ---");
        PasswordEntry entry = new PasswordEntry("SecureBank", "user123", "secretPassword456");

        System.out.println("Masked password: " + entry.getDisplayPassword());
        System.out.println("Access count before: " + entry.getAccessCount());

        // Accessing actual password is tracked (ENCAPSULATION)
        String actualPassword = entry.getActualPassword();
        System.out.println("Actual password accessed: " + actualPassword);
        System.out.println("Access count after: " + entry.getAccessCount());

        // Password strength is calculated internally
        System.out.println("Password strength: " + entry.getPasswordStrength() + "/5");
    }

    /**
     * Demonstrate ABSTRACTION - hiding complex implementation details
     */
    public static void demonstrateAbstraction() {
        System.out.println("\n=== ABSTRACTION DEMONSTRATION ===");

        System.out.println("--- User Factory Abstraction ---");
        // Factory pattern hides complex object creation logic
        BaseUser user1 = UserFactory.createUser("normal", "user1", "user1@email.com", "password123");
        BaseUser user2 = UserFactory.createUser("admin", "admin1", "admin1@system.com", "adminpass456");

        System.out.println("Created " + user1.getUserType() + " user: " + user1.getUsername());
        System.out.println("Created " + user2.getUserType() + " user: " + user2.getUsername());

        System.out.println("\n--- Enhanced User Manager Abstraction ---");
        // Complex authentication logic is hidden behind simple method calls
        BaseUser authenticatedUser = EnhancedUserManager.authenticateUser("user1@email.com", "password123");
        if (authenticatedUser != null) {
            System.out.println("Authentication successful for: " + authenticatedUser.getUsername());
            System.out.println("User type determined automatically: " + authenticatedUser.getUserType());
        }

        // Abstract base classes hide implementation complexity
        System.out.println("\n--- Abstract Class Benefits ---");
        System.out.println("All BaseUser implementations must provide:");
        System.out.println("- getUserType() method");
        System.out.println("- canManagePasswords() method");
        System.out.println("- hasAdminPrivileges() method");
        System.out.println("- performLoginActions() method");
        System.out.println("But each can implement them differently (ABSTRACTION + POLYMORPHISM)");
    }

    /**
     * Demonstrate real-world usage scenario
     */
    public static void demonstrateRealWorldUsage() {
        System.out.println("\n=== REAL-WORLD USAGE SCENARIO ===");

        // Scenario: User registration and password management
        System.out.println("--- User Registration ---");
        boolean registered = EnhancedUserManager.registerUser("real_user", "real@email.com", "securePassword123",
                "normal");
        System.out.println("User registration successful: " + registered);

        // Scenario: User authentication
        System.out.println("\n--- User Authentication ---");
        BaseUser currentUser = EnhancedUserManager.authenticateUser("real@email.com", "securePassword123");

        if (currentUser != null && currentUser instanceof NormalUser) {
            NormalUser normalUser = (NormalUser) currentUser;

            System.out.println("User logged in: " + normalUser.getUsername());

            // Scenario: Password management
            System.out.println("\n--- Password Management ---");

            // Add password entries
            PasswordEntry entry1 = new PasswordEntry("Facebook", "real_user", "fbPassword123", "Social Media",
                    "Personal account");
            PasswordEntry entry2 = new PasswordEntry("Work Email", "real.user@company.com", "workPass456", "Work",
                    "Company email");

            normalUser.addPasswordEntry(entry1);
            normalUser.addPasswordEntry(entry2);

            System.out.println("Password entries added: " + normalUser.getPasswordCount());

            // Find specific password
            PasswordEntry foundEntry = normalUser.findPasswordEntryByWebsite("Facebook");
            if (foundEntry != null) {
                System.out.println("Found Facebook entry: " + foundEntry.getUsername());
                System.out.println("Entry details: " + foundEntry.getEntrySummary());
            }

            // Update password
            entry1.updatePassword("newFbPassword789");
            System.out.println("Password updated for: " + entry1.getWebsite());

            // Show user statistics
            System.out.println("\n--- User Statistics ---");
            System.out.println(normalUser.getUserStatistics());
        }

        // Clean up
        EnhancedUserManager.logout();
        System.out.println("User logged out successfully");
    }

    /**
     * Demonstrate admin functionality
     */
    public static void demonstrateAdminFunctionality() {
        System.out.println("\n=== ADMIN FUNCTIONALITY DEMONSTRATION ===");

        // Create and authenticate admin
        AdminUser admin = EnhancedUserManager.authenticateAdmin("admin", "admin");

        if (admin != null) {
            System.out.println("Admin authenticated: " + admin.getUsername());

            // Demonstrate admin privileges
            System.out.println("\n--- Admin Privileges ---");
            List<String> privileges = admin.getAdminPrivileges();
            System.out.println("Admin privileges: " + String.join(", ", privileges));

            // Demonstrate user management
            System.out.println("\n--- User Management ---");
            List<BaseUser> allUsers = admin.getAllUsers();
            System.out.println("Total users in system: " + allUsers.size());

            for (BaseUser user : allUsers) {
                System.out.println("- " + user.getUsername() + " (" + user.getUserType() + ")");
            }

            // Generate system report
            System.out.println("\n--- System Report ---");
            String report = admin.generateSystemReport();
            System.out.println(report);

            // Demonstrate backup functionality
            System.out.println("\n--- Data Backup ---");
            boolean backupSuccess = admin.backupUserData();
            System.out.println("Backup completed: " + backupSuccess);

            // Show admin statistics
            System.out.println("\n--- Admin Statistics ---");
            System.out.println(admin.getAdminStatistics());
        }

        EnhancedUserManager.logout();
    }

    /**
     * Main method to run all demonstrations
     */
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("OOP PASSWORD MANAGER DEMONSTRATION");
        System.out.println("Showcasing 4 Core Principles of OOP");
        System.out.println("==================================================");

        demonstratePolymorphism();
        demonstrateInheritance();
        demonstrateEncapsulation();
        demonstrateAbstraction();
        demonstrateRealWorldUsage();
        demonstrateAdminFunctionality();

        System.out.println("\n==================================================");
        System.out.println("DEMONSTRATION COMPLETE");
        System.out.println("All 4 OOP principles successfully demonstrated!");
        System.out.println("==================================================");
    }
}
