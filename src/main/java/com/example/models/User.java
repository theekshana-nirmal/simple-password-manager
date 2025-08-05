package com.example.models;

/**
 * User model class representing a registered user.
 */
public class User extends BaseUser {
    public User(String username, String email, String passwordHash) {
        super(username, email, passwordHash);
    }

    @Override
    public String getUserType() {
        return "Normal";
    }

    @Override
    public boolean canManagePasswords() {
        return true;
    }

    @Override
    public void performLoginActions() {
        System.out.println("User logged in: " + getUsername());
    }

    @Override
    public void performLogoutActions() {
        System.out.println("User logged out: " + getUsername());
    }
}
