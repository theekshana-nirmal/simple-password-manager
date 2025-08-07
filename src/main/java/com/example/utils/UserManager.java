package com.example.utils;

import com.example.models.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the UserManager utility class that handles user
 * authentication and data management.
 * OOP Concept: This class demonstrates UTILITY PATTERN by providing static
 * methods for user operations.
 */
public class UserManager {

    private static final String USER_DATA_HEADER = "Username,Email,PasswordHash";
    private static User currentUser = null;

    // Initialize data directories when class is loaded
    static {
        DataManager.initializeDataDirectories();
    } // Creates a new user account in the system

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
    } // Verifies user credentials and logs them in

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
    } // Returns the currently logged-in user

    public static User getCurrentUser() {
        return currentUser;
    } // Logs out the current user by clearing the reference

    public static void logout() {
        currentUser = null;
    } // Checks if a user is currently logged into the system

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    } // Searches for and returns a user by their username

    public static User findUserByUsername(String username) {
        List<User> users = loadUsersFromCSV();
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    } // Searches for and returns a user by their email address

    public static User findUserByEmail(String email) {
        List<User> users = loadUsersFromCSV();
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    } // Loads all user records from the CSV file

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
    } // Adds a new user to the CSV storage

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
    } // Saves all users to the CSV file

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
    } // Returns a list of all registered users

    public static List<User> getAllUsers() {
        return loadUsersFromCSV();
    } // Removes a user from the system by their email

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
