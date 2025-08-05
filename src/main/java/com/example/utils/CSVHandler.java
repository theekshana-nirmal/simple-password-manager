package com.example.utils;

import com.example.controllers.UserController.PasswordEntry;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {

    private static final String CSV_HEADER = "Website/App Name,Username/Email,Password";
    private static final String CSV_FILE_PATH = "/sample-passwords.csv";

    /**
     * Load password entries from CSV file
     * 
     * @return List of PasswordEntry objects, empty if file is empty or doesn't
     *         exist
     */
    public static List<PasswordEntry> loadPasswordsFromCSV() {
        List<PasswordEntry> passwords = new ArrayList<>();

        try {
            InputStream inputStream = CSVHandler.class.getResourceAsStream(CSV_FILE_PATH);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                boolean firstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false; // Skip header
                        continue;
                    }

                    // Check if line is not empty or just whitespace
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length >= 3 && !data[0].trim().isEmpty()) {
                        passwords.add(new PasswordEntry(
                                data[0].trim(),
                                data[1].trim(),
                                data[2].trim()));
                    }
                }
                reader.close();
                System.out.println("Loaded " + passwords.size() + " password entries from CSV");
            } else {
                System.err.println("Could not find CSV file: " + CSV_FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Error loading passwords from CSV: " + e.getMessage());
        }

        return passwords;
    }

    /**
     * Save password entries to CSV file
     * Note: This is a simplified version for demonstration.
     * In a real application, you would need proper file handling for resources.
     * 
     * @param passwords List of PasswordEntry objects to save
     */
    public static void savePasswordsToCSV(ObservableList<PasswordEntry> passwords) {
        try {
            // Get the path to the resources directory
            URL resourceUrl = CSVHandler.class.getResource(CSV_FILE_PATH);
            if (resourceUrl != null) {
                Path csvPath = Paths.get(resourceUrl.toURI());

                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(csvPath))) {
                    // Write header
                    writer.println(CSV_HEADER);

                    // Write data
                    for (PasswordEntry entry : passwords) {
                        writer.printf("%s,%s,%s%n",
                                entry.getWebsite(),
                                entry.getUsername(),
                                entry.getPassword());
                    }
                    System.out.println("Saved " + passwords.size() + " password entries to CSV");
                }
            } else {
                System.err.println("Could not find CSV file path for saving");
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error saving passwords to CSV: " + e.getMessage());
        }
    }

    /**
     * Check if CSV file exists and is readable
     * 
     * @return true if file exists and can be read
     */
    public static boolean isCSVFileAvailable() {
        InputStream inputStream = CSVHandler.class.getResourceAsStream(CSV_FILE_PATH);
        if (inputStream != null) {
            try {
                inputStream.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Check if CSV file is empty (has only header or no content)
     * 
     * @return true if file is empty or has no data rows
     */
    public static boolean isCSVFileEmpty() {
        try {
            InputStream inputStream = CSVHandler.class.getResourceAsStream(CSV_FILE_PATH);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                boolean firstLine = true;
                boolean hasData = false;

                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false; // Skip header
                        continue;
                    }

                    if (!line.trim().isEmpty()) {
                        hasData = true;
                        break;
                    }
                }
                reader.close();
                return !hasData;
            }
        } catch (IOException e) {
            System.err.println("Error checking if CSV is empty: " + e.getMessage());
        }
        return true; // Consider empty if there's an error
    }

    /**
     * Load password entries from user-specific CSV file
     * 
     * @param username The username to load passwords for
     * @return List of PasswordEntry objects
     */
    public static List<PasswordEntry> loadUserPasswordsFromCSV(String username) {
        List<PasswordEntry> passwords = new ArrayList<>();

        try {
            Path userPasswordFile = DataManager.getUserPasswordFilePath(username);
            if (Files.exists(userPasswordFile)) {
                List<String> lines = Files.readAllLines(userPasswordFile);
                boolean firstLine = true;

                for (String line : lines) {
                    if (firstLine) {
                        firstLine = false; // Skip header
                        continue;
                    }

                    // Check if line is not empty or just whitespace
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length >= 3 && !data[0].trim().isEmpty()) {
                        passwords.add(new PasswordEntry(
                                data[0].trim(),
                                data[1].trim(),
                                data[2].trim()));
                    }
                }
                System.out.println("Loaded " + passwords.size() + " password entries for user: " + username);
            } else {
                System.out.println("No password file found for user: " + username + ". Creating empty file.");
                DataManager.createUserPasswordFile(username);
            }
        } catch (IOException e) {
            System.err.println("Error loading passwords for user " + username + ": " + e.getMessage());
        }

        return passwords;
    }

    /**
     * Save password entries to user-specific CSV file
     * 
     * @param username  The username to save passwords for
     * @param passwords List of PasswordEntry objects to save
     */
    public static void saveUserPasswordsToCSV(String username, ObservableList<PasswordEntry> passwords) {
        try {
            Path userPasswordFile = DataManager.getUserPasswordFilePath(username);

            // Ensure parent directories exist
            Files.createDirectories(userPasswordFile.getParent());

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(userPasswordFile))) {
                // Write header
                writer.println(CSV_HEADER);

                // Write data
                for (PasswordEntry entry : passwords) {
                    writer.printf("%s,%s,%s%n",
                            entry.getWebsite(),
                            entry.getUsername(),
                            entry.getPassword());
                }
                System.out.println("Saved " + passwords.size() + " password entries for user: " + username);
            }
        } catch (IOException e) {
            System.err.println("Error saving passwords for user " + username + ": " + e.getMessage());
        }
    }
}
