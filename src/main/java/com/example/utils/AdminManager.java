package com.example.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AdminManager {

    private static final String ADMIN_DATA_FILE = "src/main/resources/data/admin/admin-data.csv";
    private static final String DEFAULT_EMAIL = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    public static boolean authenticateAdmin(String email, String password) {
        try {
            // Create admin directory if it doesn't exist
            java.io.File adminDir = new java.io.File("src/main/resources/data/admin");
            if (!adminDir.exists()) {
                adminDir.mkdirs();
            }

            java.io.File adminFile = new java.io.File(ADMIN_DATA_FILE);
            String storedEmail = DEFAULT_EMAIL;
            String storedPassword = DEFAULT_PASSWORD;

            // Read admin credentials if file exists
            if (adminFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(adminFile))) {
                    String line = reader.readLine();
                    if (line != null && !line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length >= 2) {
                            storedEmail = parts[0];
                            storedPassword = parts[1];

                            // Decrypt password if it's encrypted
                            if (EncryptionUtils.isEncrypted(storedPassword)) {
                                storedPassword = EncryptionUtils.decryptPassword(storedPassword);
                            }
                        }
                    }
                }
            } else {
                // Create default admin file with encrypted password
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(adminFile))) {
                    String encryptedPassword = EncryptionUtils.encryptPassword(DEFAULT_PASSWORD);
                    writer.write(DEFAULT_EMAIL + "," + encryptedPassword);
                    writer.newLine();
                }
            }

            return email.equals(storedEmail) && password.equals(storedPassword);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getAdminPassword() {
        try {
            java.io.File adminFile = new java.io.File(ADMIN_DATA_FILE);

            if (adminFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(adminFile))) {
                    String line = reader.readLine();
                    if (line != null && !line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length >= 2) {
                            String storedPassword = parts[1];
                            // Decrypt password if it's encrypted
                            if (EncryptionUtils.isEncrypted(storedPassword)) {
                                return EncryptionUtils.decryptPassword(storedPassword);
                            }
                            return storedPassword;
                        }
                    }
                }
            }

            return DEFAULT_PASSWORD;

        } catch (IOException e) {
            e.printStackTrace();
            return DEFAULT_PASSWORD;
        }
    }

    public static String getAdminEmail() {
        try {
            java.io.File adminFile = new java.io.File(ADMIN_DATA_FILE);

            if (adminFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(adminFile))) {
                    String line = reader.readLine();
                    if (line != null && !line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length >= 1) {
                            return parts[0];
                        }
                    }
                }
            }

            return DEFAULT_EMAIL;

        } catch (IOException e) {
            e.printStackTrace();
            return DEFAULT_EMAIL;
        }
    }

    public static boolean updateAdminCredentials(String currentPassword, String newEmail, String newPassword) {
        try {
            // Verify current password first
            String storedPassword = getAdminPassword();
            if (!currentPassword.equals(storedPassword)) {
                return false;
            }

            // Create admin directory if it doesn't exist
            java.io.File adminDir = new java.io.File("src/main/resources/data/admin");
            if (!adminDir.exists()) {
                adminDir.mkdirs();
            }

            java.io.File adminFile = new java.io.File(ADMIN_DATA_FILE);

            // Write updated credentials with encrypted password
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(adminFile))) {
                String encryptedPassword = EncryptionUtils.encryptPassword(newPassword);
                writer.write(newEmail + "," + encryptedPassword);
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Migrates existing plain text admin passwords to encrypted format
     * This method should be called during application startup
     */
    public static void migrateToEncryptedPasswords() {
        try {
            java.io.File adminFile = new java.io.File(ADMIN_DATA_FILE);

            if (!adminFile.exists()) {
                return; // No admin file exists, nothing to migrate
            }

            String adminEmail = DEFAULT_EMAIL;
            String adminPassword = DEFAULT_PASSWORD;
            boolean needsMigration = false;

            // Read current credentials
            try (BufferedReader reader = new BufferedReader(new FileReader(adminFile))) {
                String line = reader.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        adminEmail = parts[0];
                        adminPassword = parts[1];

                        // Check if password is already encrypted
                        if (!EncryptionUtils.isEncrypted(adminPassword)) {
                            needsMigration = true;
                        }
                    }
                }
            }

            // Migrate if necessary
            if (needsMigration) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(adminFile))) {
                    String encryptedPassword = EncryptionUtils.encryptPassword(adminPassword);
                    writer.write(adminEmail + "," + encryptedPassword);
                    writer.newLine();
                }
                System.out.println("Admin password successfully migrated to encrypted format.");
            }

        } catch (IOException e) {
            System.err.println("Error during admin password migration: " + e.getMessage());
        }
    }
}
