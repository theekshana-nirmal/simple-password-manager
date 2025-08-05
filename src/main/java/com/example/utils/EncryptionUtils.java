package com.example.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * This file contains the EncryptionUtils utility class for encrypting and
 * decrypting password data.
 * OOP Concept: This class demonstrates the UTILITY pattern by providing static
 * methods for encryption operations.
 */
public class EncryptionUtils {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;
    private static final String MASTER_PASSWORD_SALT = "PasswordManagerSalt123!"; // Fixed salt for app master key
    private static final String MASTER_PASSWORD = "S3cur3P@ssw0rdM@n@ger"; // Hard-coded master password for demo

    private static SecretKey masterKey;
    private static final SecureRandom RANDOM = new SecureRandom();

    static {
        try {
            // Initialize master key on class load
            masterKey = generateMasterKey();
        } catch (Exception e) {
            System.err.println("Failed to initialize encryption: " + e.getMessage());
            throw new RuntimeException("Encryption initialization failed", e);
        }
    }

    // Generates the application master key
    private static SecretKey generateMasterKey() throws Exception {
        PBEKeySpec spec = new PBEKeySpec(
                MASTER_PASSWORD.toCharArray(),
                MASTER_PASSWORD_SALT.getBytes(StandardCharsets.UTF_8),
                ITERATION_COUNT,
                KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Encrypts a plaintext password and returns a Base64-encoded string with IV
    // prepended
    public static String encryptPassword(String plainPassword) {
        try {
            // Generate random IV
            byte[] iv = new byte[16];
            RANDOM.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, masterKey, ivSpec);

            // Encrypt password
            byte[] encryptedBytes = cipher.doFinal(plainPassword.getBytes(StandardCharsets.UTF_8));

            // Combine IV and encrypted password and encode to Base64
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            System.err.println("Encryption failed: " + e.getMessage());
            return plainPassword; // Fallback to unencrypted if encryption fails
        }
    }

    // Decrypts a Base64-encoded encrypted password with prepended IV
    public static String decryptPassword(String encryptedPassword) {
        try {
            // Decode from Base64
            byte[] combined = Base64.getDecoder().decode(encryptedPassword);

            // Extract IV and encrypted password
            byte[] iv = new byte[16];
            byte[] encrypted = new byte[combined.length - 16];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encrypted, 0, encrypted.length);

            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, masterKey, new IvParameterSpec(iv));

            // Decrypt password
            byte[] decryptedBytes = cipher.doFinal(encrypted);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Decryption failed: " + e.getMessage());
            return encryptedPassword; // Return as-is if decryption fails
        }
    }

    // Checks if a string is likely encrypted (Base64 format)
    public static boolean isEncrypted(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Base64.getDecoder().decode(str);
            // If it successfully decodes and is longer than basic encryption overhead (IV +
            // minimal content)
            return str.length() >= 24; // Minimum size for our encryption format
        } catch (IllegalArgumentException e) {
            // Not valid Base64
            return false;
        }
    }
}
