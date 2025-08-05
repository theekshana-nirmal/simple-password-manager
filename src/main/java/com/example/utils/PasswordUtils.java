package com.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification
 */
public class PasswordUtils {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generate a salt for password hashing
     * 
     * @return Base64 encoded salt
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash a password with salt
     * 
     * @param password The plain text password
     * @param salt     The salt to use
     * @return Base64 encoded hash
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Create a password hash with embedded salt
     * 
     * @param password The plain text password
     * @return String in format "salt:hash"
     */
    public static String createPasswordHash(String password) {
        String salt = generateSalt();
        String hash = hashPassword(password, salt);
        return salt + ":" + hash;
    }

    /**
     * Verify a password against a stored hash
     * 
     * @param password   The plain text password to verify
     * @param storedHash The stored hash in format "salt:hash"
     * @return true if password matches
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }
            String salt = parts[0];
            String hash = parts[1];
            String testHash = hashPassword(password, salt);
            return hash.equals(testHash);
        } catch (Exception e) {
            return false;
        }
    }
}
