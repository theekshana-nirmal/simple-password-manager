package com.example.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for managing application data directories and files
 */
public class DataManager { // Base data directory in project's resources folder
    private static final String RESOURCES_DIR = System.getProperty("user.dir") + File.separator + "src" + File.separator
            + "main" + File.separator + "resources";
    private static final String APP_DATA_DIR = RESOURCES_DIR + File.separator + "data";
    private static final String USERS_DIR = APP_DATA_DIR + File.separator + "users";
    private static final String PASSWORDS_DIR = APP_DATA_DIR + File.separator + "passwords";

    // File names
    private static final String USER_DATA_FILE = "user-data.csv";
    private static final String USER_DATA_HEADER = "Username,Email,PasswordHash,CreatedAt";
    private static final String PASSWORD_FILE_HEADER = "Website/App Name,Username/Email,Password";

    /**
     * Initialize the data directory structure
     */
    public static void initializeDataDirectories() {
        try {
            // Create main data directory
            Path appDataPath = Paths.get(APP_DATA_DIR);
            if (!Files.exists(appDataPath)) {
                Files.createDirectories(appDataPath);
                System.out.println("Created app data directory: " + APP_DATA_DIR);
            }

            // Create users directory
            Path usersPath = Paths.get(USERS_DIR);
            if (!Files.exists(usersPath)) {
                Files.createDirectories(usersPath);
                System.out.println("Created users directory: " + USERS_DIR);
            }

            // Create passwords directory
            Path passwordsPath = Paths.get(PASSWORDS_DIR);
            if (!Files.exists(passwordsPath)) {
                Files.createDirectories(passwordsPath);
                System.out.println("Created passwords directory: " + PASSWORDS_DIR);
            }

            // Create user data file if it doesn't exist
            Path userDataFile = Paths.get(USERS_DIR, USER_DATA_FILE);
            if (!Files.exists(userDataFile)) {
                Files.createFile(userDataFile);
                Files.write(userDataFile, USER_DATA_HEADER.getBytes());
                System.out.println("Created user data file: " + userDataFile);
            }

        } catch (IOException e) {
            System.err.println("Error initializing data directories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the path to the user data CSV file
     * 
     * @return Path to user-data.csv
     */
    public static Path getUserDataFilePath() {
        return Paths.get(USERS_DIR, USER_DATA_FILE);
    }

    /**
     * Get the path to a user's password CSV file
     * 
     * @param username The username
     * @return Path to the user's password CSV file
     */
    public static Path getUserPasswordFilePath(String username) {
        return Paths.get(PASSWORDS_DIR, "passwords_" + username + ".csv");
    }

    /**
     * Create an empty password file for a new user
     * 
     * @param username The username
     * @return true if file was created successfully
     */
    public static boolean createUserPasswordFile(String username) {
        try {
            Path userPasswordFile = getUserPasswordFilePath(username);
            if (!Files.exists(userPasswordFile)) {
                Files.createFile(userPasswordFile);
                Files.write(userPasswordFile, PASSWORD_FILE_HEADER.getBytes());
                System.out.println("Created password file for user: " + username);
                return true;
            }
            return true; // File already exists
        } catch (IOException e) {
            System.err.println("Error creating password file for user " + username + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if user password file exists
     * 
     * @param username The username
     * @return true if file exists
     */
    public static boolean userPasswordFileExists(String username) {
        return Files.exists(getUserPasswordFilePath(username));
    }

    /**
     * Get the app data directory path
     * 
     * @return App data directory path
     */
    public static String getAppDataDir() {
        return APP_DATA_DIR;
    }

    /**
     * Get the users directory path
     * 
     * @return Users directory path
     */
    public static String getUsersDir() {
        return USERS_DIR;
    }

    /**
     * Get the passwords directory path
     * 
     * @return Passwords directory path
     */
    public static String getPasswordsDir() {
        return PASSWORDS_DIR;
    }
}
