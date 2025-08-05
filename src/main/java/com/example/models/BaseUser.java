package com.example.models;

/**
 * This file contains the BaseUser abstract class that defines the common
 * structure for all user types.
 * OOP Concepts: This class demonstrates ABSTRACTION and ENCAPSULATION by hiding
 * implementation details.
 */
public abstract class BaseUser {

    private String username;
    private String email;
    private String passwordHash;

    // Creates a base user with required credentials
    public BaseUser(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Returns the username of the user
    public String getUsername() {
        return username;
    }

    // Returns the email address of the user
    public String getEmail() {
        return email;
    }

    // Returns the hashed password of the user
    public String getPasswordHash() {
        return passwordHash;
    }

    // Defines a method for getting the user type identifier
    public abstract String getUserType();

    // Defines a method to check if user can manage passwords
    public abstract boolean canManagePasswords();

    // Defines actions to perform when user logs in
    public abstract void performLoginActions();

    // Defines actions to perform when user logs out
    public abstract void performLogoutActions();
}
