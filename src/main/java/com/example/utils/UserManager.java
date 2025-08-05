package com.example.utils;

import com.example.models.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static final String USER_DATA_HEADER = "Username,Email,PasswordHash";
    private static User currentUser = null;

    // Initialize data directories when class is loaded
    static {
        DataManager.initializeDataDirectories();
    }

    /**
     * Register a new user
     * 
     * @param username The username
     * @param email    The email
     * @param password The plain text password
     * @return true if registration successful, false if user already exists
     */
    public static boolean registerUser(String username, String email, String password) {
        // Check if user already exists
        if (findUserByUsername(username) != null || findUserByEmail(email) != null) {
            return false;
        }

        // Create new user with hashed password
        String passwordHash = PasswordUtils.createPasswordHash(password);
        User newUser = new User(username, email, passwordHash);

        // Save to CSV
        return saveUserToCSV(newUser);
    }

    /**
     * Authenticate a user
     * 
     * @param usernameOrEmail Username or email
     * @param password        Plain text password
     * @return true if authentication successful
     */
    public static boolean authenticateUser(String usernameOrEmail, String password) {
        User user = findUserByUsername(usernameOrEmail);
        if (user == null) {
            user = findUserByEmail(usernameOrEmail);
        }

        if (user != null && PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            currentUser = user;
            return true;
        }

        return false;
    }

    /**
     * Get the currently logged-in user
     * 
     * @return Current user or null if not logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Log out the current user
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Check if a user is currently logged in
     * 
     * @return true if user is logged in
     */
    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /**
     * Find user by username
     * 
     * @param username The username to search for
     * @return User object or null if not found
     */
    public static User findUserByUsername(String username) {
        List<User> users = loadUsersFromCSV();
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find user by email
     * 
     * @param email The email to search for
     * @return User object or null if not found
     */
    public static User findUserByEmail(String email) {
        List<User> users = loadUsersFromCSV();
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Load all users from CSV file
     * 
     * @return List of User objects
     */
    private static List<User> loadUsersFromCSV() {
        List<User> users = new ArrayList<>();
        try {
            Path userDataFile = DataManager.getUserDataFilePath();
            if (Files.exists(userDataFile)) {
                List<String> lines = Files.readAllLines(userDataFile);
                boolean firstLine = true;

                for (String line : lines) {
                    if (firstLine) {
                        firstLine = false; // Skip header
                        continue;
                    }

                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] data = line.split(",");
                    if (data.length >= 3) {
                        // Only use the 3-parameter constructor - we no longer track creation date
                        users.add(new User(
                                data[0].trim(),
                                data[1].trim(),
                                data[2].trim()));
                    }
                }
                System.out.println("Loaded " + users.size() + " users");
            } else {
                System.out.println("User data file does not exist, starting with empty user list");
            }
        } catch (IOException e) {
            System.err.println("Error loading users from CSV: " + e.getMessage());
        }

        return users;
    }

    /**
     * Save a user to CSV file
     * 
     * @param user The user to save
     * @return true if successful
     */
    private static boolean saveUserToCSV(User user) {
        try {
            // First, load existing users
            List<User> existingUsers = loadUsersFromCSV();
            existingUsers.add(user);

            // Save all users back to CSV
            return saveAllUsersToCSV(existingUsers);
        } catch (Exception e) {
            System.err.println("Error saving user to CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Save all users to CSV file
     * 
     * @param users List of users to save
     * @return true if successful
     */
    private static boolean saveAllUsersToCSV(List<User> users) {
        try {
            Path userDataFile = DataManager.getUserDataFilePath();

            // Ensure parent directories exist
            Files.createDirectories(userDataFile.getParent());

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(userDataFile))) {
                // Write header
                writer.println(USER_DATA_HEADER); // Write users (only core fields)
                for (User user : users) {
                    writer.printf("%s,%s,%s%n",
                            user.getUsername(),
                            user.getEmail(),
                            user.getPasswordHash());
                }
            }

            System.out.println("Saved " + users.size() + " users to: " + userDataFile);

            // Create password file for the new user if it doesn't exist
            if (!users.isEmpty()) {
                User lastUser = users.get(users.size() - 1);
                DataManager.createUserPasswordFile(lastUser.getUsername());
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error saving users to CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all registered users
     * 
     * @return List of all users
     */
    public static List<User> getAllUsers() {
        return loadUsersFromCSV();
    }

    /**
     * Delete a user by email
     * 
     * @param email The email of the user to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteUser(String email) {
        List<User> users = loadUsersFromCSV();

        // Find the user before removing to get username for password file deletion
        User userToDelete = null;
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                userToDelete = user;
                break;
            }
        }

        boolean removed = users.removeIf(user -> user.getEmail().equals(email));
        if (removed && userToDelete != null) {
            // Delete the user's password file first
            try {
                Path passwordFilePath = DataManager.getUserPasswordFilePath(userToDelete.getUsername());
                if (Files.exists(passwordFilePath)) {
                    Files.delete(passwordFilePath);
                    System.out.println("Deleted password file: " + passwordFilePath);
                }
            } catch (IOException e) {
                System.err.println("Error deleting user password file: " + e.getMessage());
                // Continue with user deletion even if password file deletion fails
            }

            // Save updated user list
            String userDataFile = DataManager.getUserDataFilePath().toString();
            try (PrintWriter writer = new PrintWriter(new FileWriter(userDataFile))) {
                // Write header
                writer.println(USER_DATA_HEADER); // Write remaining users (only core fields)
                for (User user : users) {
                    writer.printf("%s,%s,%s%n",
                            user.getUsername(),
                            user.getEmail(),
                            user.getPasswordHash());
                }

                System.out.println("User and all associated data deleted: " + email);
                return true;
            } catch (IOException e) {
                System.err.println("Error deleting user from CSV: " + e.getMessage());
                return false;
            }
        }

        return false;
    }
}
