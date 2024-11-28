package com.safe.storage.security;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Password {

    private static final int SALT_LENGTH = 16; // Length of the salt in bytes
    private static final int HASH_LENGTH = 32; // Length of the hash in bytes
    private static final int ITERATIONS = 65536; // PBKDF2 iterations

    // Hashes a password with a generated salt
    public static String hashPassword(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt); // Generate a random salt

        // Hash the password with the salt
        String hash = hash(password, salt);

        // Combine salt and hash for storage
        return Base64.getEncoder().encodeToString(salt) + ":" + hash;
    }

    // Hashes the password using PBKDF2 with the provided salt
    private static String hash(String password, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH * 8);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Verifies a password against a stored hash
    public static boolean verifyPassword(String password, String storedHash) {
        String[] parts = storedHash.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String hash = parts[1];

        // Hash the provided password with the stored salt
        String hashToVerify = hash(password, salt);

        // Compare the hashes
        return hashToVerify.equals(hash);
    }
}
