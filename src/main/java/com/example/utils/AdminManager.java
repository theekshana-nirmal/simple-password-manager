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
                        }
                    }
                }
            } else {
                // Create default admin file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(adminFile))) {
                    writer.write(DEFAULT_EMAIL + "," + DEFAULT_PASSWORD);
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
                            return parts[1];
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

    public static boolean changePassword(String currentPassword, String newPassword) {
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
            String adminEmail = DEFAULT_EMAIL;

            // Read current email if file exists
            if (adminFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(adminFile))) {
                    String line = reader.readLine();
                    if (line != null && !line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length >= 1) {
                            adminEmail = parts[0];
                        }
                    }
                }
            }

            // Write updated credentials
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(adminFile))) {
                writer.write(adminEmail + "," + newPassword);
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
