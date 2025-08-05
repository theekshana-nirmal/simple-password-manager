package com.example.models;

/**
 * This file contains the User class that represents a registered user in the
 * password manager.
 * OOP Concept: This class demonstrates INHERITANCE by extending the BaseUser
 * abstract class.
 */
public class User extends BaseUser {

    // Creates a new user with basic credentials
    public User(String username, String email, String passwordHash) {
        super(username, email, passwordHash);
    }

    // Returns the type of user for identification purposes
    @Override
    public String getUserType() {
        return "Normal";
    }

    // Determines if user has permission to manage passwords
    @Override
    public boolean canManagePasswords() {
        return true;
    }

    // Performs actions when a user logs into the system
    @Override
    public void performLoginActions() {
        System.out.println("User logged in: " + getUsername());
    }

    // Performs cleanup actions when a user logs out
    @Override
    public void performLogoutActions() {
        System.out.println("User logged out: " + getUsername());
    }
}
