package com.safe.storage.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A utility class for encrypting and decrypting files and strings using AES encryption.
 */
public class Crypto {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256; // AES key size in bits
    private static final int IV_SIZE = 16; // IV size in bytes
    private static final int ITERATIONS = 65536; // PBKDF2 iterations
    private static final String SALT = "fB6snC3Z8j7vRx1Q5wD9pY2kA4uG0M3tN6hX8aV1bJ5qL7cW9eT2rF0zS8mK4yI7P3uH9oR5gY2nD8wC4vJ6lX1tQ5bM9kA2jG7sZ3eU0nF8yL7pR9vT6cW4qI1mK5oP7uH3xN8dJ9aB6gO2sV0rF5lY3tL7eZ4wC9nX1qD6kA8jS2mI5oT7vP0uR3bM9cF8yN6hG4xV1pW5lO7gJ2dE0nK8qI3sA9mU7tH6eZ5rB1wC4xN9oF2vT0pL3jY6mK8iJ5gR7aD1bS4uH9nM2qE0cV7tW5yO3xL8kF6zP1dN4eQ9rI7vU5gJ0mB2sA6hT8oW3lC1yK5iX9nR4uG7pV0bD6jM4aF8wQ2tL5xN3kO7eS9cI0hY6vR8uJ1mD2pB5gT7aW4nF9oE3qL1iK6zV0xC8rS2jM5yU9tH7dA0bN4lG6wQ3fP1eK9cV7uJ5oR2xT8mI0aL6hY4gN3qD5vW9bS1nF7kO0pE8zJ2tR5yC6uH3iM9aG1xL7jQ4wN8dV2rF0oI5sT9kP3eB6mU1cA9lW7yZ4gQ0hV5nJ8uO2tX3dR6bS9iM1pK7aL4vF2qN0xC8wY5jG3oD7eZ9rT1mH6kP4uI2nB0aJ8vL5yQ9xW7gR3oF6tE1dK2sU5bN4hM0pC8eJ9zV7iA3lO6qW2yT5nG1xR4mB0uH8vD3jL7kF9iS6oP1aN7zC5qE0gY4tI2rV8pW3mK9bX6dO0fQ1eJ7cA5uH9lS6nM4wT8yZ2oR3iG1vL7kD0pB9xF5aQ4tE7jN6mW2hC8oV5uI1rL3yP9gK7zD0bF6xJ4wN8eA2cS5qM3tH1vU7nR9oK6iG0lY8aJ2bV4xT7pW5mQ9zO6eC1dN3rI0hL7kF5uS8jM2qG4vA9tX1oP6nB3yD0wR5iZ7cE2lJ9gQ8mT4bU6aC0hV1xY3pK7oF9rL5sD6jN2eW0uH8zI4qG1vO9tM7wB3dA5xN6fP4yK2iJ0lC8rS7mE9gQ5bU3nT1aL4oW6kV7hX2pY0jR9eI5cF8dZ3qG1uM7tN9vO4wR5lX6yK2oP0aB3jS8mI7gD9zL1hE5nF4iU2cV6tQ0xR8kW3jY7bM9oT5pN2aG0vL4eC6dH1uS3wQ8rF7iZ9kO5mJ2xT0nP6bA1yD3lV8jI7qK4gW9eR5hC6oU2tL7nZ0vF8dM1aB3yS9kJ4xQ5pG6iN2wE0cR7uH1oT3vL8mD5bI6eA4qF9zK2jX0yP7gW3nO1rV5uC8hM6tB9lD2aN0jY4oI7xE3kQ5sR8iG1vL6wT9pZ7cJ0mH4fO2bU5aL8gN3eS6dV0rW9kF1iT7xC5qM2hP4oR8nJ6vD3bA0lY7tG9zI5uH1eK4xN2cS6mW8jQ3pF0dO9rT5iV7oL1aB3kN6hU4gY8eM2sX9bW0jC7dR5qP3lF1vK4nZ7tO6iA2uE9oH5mQ0xJ8rG1cV4yS3bD9kI7wL6pT8fN2eC0aB5qV9zJ1iG3oD7nM4tK6rW8yL5hO2uX0vR9mA3eS7lP1cF6gQ4jI8nT5bH9dJ2oL7wY3kZ0uR6iM4aN1vF8tB2xO5pG7eC9hD3qK6jS8lW0mQ1yU5zI7fV4bX2gT9oL3rN5wA0iE6dJ8cH7kP9sR2nY4vM1uZ5oO3tL9aQ6xF7jD0mK2eB8gW1pI4cS7hN0yV3lT5rU9fJ6bA2wG8dR7iC0nZ1kO5xM3qP9sF4vH6uE2oI7jT1aL0yB5gQ8dV3rK9mW6pN2eS4cX1lJ7hO9tF0uA3bY6iM2vZ5xR7wC8nD1gQ4jK0oT9fJ6eL3yI1pU7mA5rS8hO2nB4vW0xN7kC6dE9qG5lF3iT1oR8jM2uY9bV0aZ4gQ6cH5sW7nD3pK1rL8mF9tJ6eI2oO0xA7vB4uS5yN1iP3lK9wM7qG8dT2hE6cF0jR5mV1kL3bD6nW9oJ4fH7zI8pQ2rT5vC1yU4aN0eS9gM3iK7oL5xJ2wR8tP6hD4bF0qV1cZ9mA7uN8lG2dE3jT6sW4kO5rH1yB9xP0vQ7fM1aU6iL2nJ3gS5oT8eD0bY9kR4wC7vN1qF6mI3zX8lA5uH7pG4rV2tB0dJ6oK9cS1yW3fN8eM5iQ7hL0nU4xT2vG9jR6kO3bD1mP0aZ7sC8qL5yE9wJ4iF2uH3tV1gK0rN7oX6dB5lM9aP8eI4fQ2cS3vW1jT7kO6hY0xL9nG5bR8zU7iD1mJ3oP5wA6tB4qF2nC0vR9lS8yK5gE9xT6uM1iL4rJ7pH0aD3cV2bO9eG7fN8mQ5kI6oT4wZ3yX1gL0dS2bJ9qR8nF5uH7vI4pM2oA3tL1yW6hC0rV9eK7jD5xO8mB6aQ4gS3iP0uN2tF1zL9yJ8kR7wH5cE6nM4oV3dA0bS2iT8qG6pU7vR9lC4xF5jD3nN2mW0aO1eI7zK6tQ8yL3hB9sP5gJ1rM4uN0dL2kX7oV8wC6iF9tH5aJ3bG1pQ6eZ7kO0mR4vS2lY8nT9uD5xW0iC3oL7qF2fA6gK1jP8bV9yN4hM7dE0rT5sW3iI2nL6kO9vX0mN4uY5jR7eG1qB8pC9dH3lS6aF2tZ5wV0oM7xK8nJ1rQ6iU4gA3bD9lT2yF7kE5cS0hP9vN8oR5jM4wI1uY3xL6zK2aT7nQ0pG8dV9fB5rC7eJ4mO6sN1iH0tW2lP9xJ3kQ7yR5uA8bV6cL4oF2nI0dM7eH1gS3zK9rT6vW8jB5pY2aG4oD0fC7mN6xQ9lI1kU8hP2iR4tL3eS5vF7nJ0yZ9bO6wM8dA3gK1uC5jQ7oT2rV9lE4sN0hP8kF6mX3iY1nB7cW5aR4zD2qG0tL8jO9vM6eH7yK5xW3pU2iI6rF1oL4bA9sT8dJ0cN5nX7gV3mQ2eP9kZ1wY6uB4hO5tR8oS7lF0qC9vN2gW5jI6aK1pM8rD3nT4yE7iJ0xH1bL9uF2cQ5kO6tV3mZ8eA4oR7gL5sD0nI9pJ7wT6lX1yM2hU8bC4aF5vK0qN3jR9gP6eO7tL5iM3dV8kN1yJ4nS0bA2uH9wC6oX7rT5qF0gE3cZ8mP9lB1iO7vY2tW4hK6jR1aN8xJ5dQ0sM3nU2pL7fB9oV6iG4cT5yF8eD9kS1rH0mJ3lP6qN7tW4vA8bR5oX2yK1uI9gC0dO3jQ6nL5zM7eH8pT4sR2iV1aJ9wY0fB6kN3xD5mU0oL7cG4vF2tQ8rE9hI6pJ9lK2yS3nM5dW0aO8gR7tV1mC4fU6iH3oB9eP7qN0jX2kZ5wL4yD8rA5cT3nW6vI0mL9uF7jE2sK1oO9gQ4aR5pV3lJ6dH8bC7nD0tY2fM1xW5kN4iT9eJ2oL7vS3mU8rG5cQ0aF6hK1bZ9yT4wP7nJ3dR8iM5uO2kX0qA6gV9lC7sH4eN1pI8tB3oJ2rF6wD9mY0uL5jQ7vK4zN3aP1gM8xE5nB2cS7oT0iR9lW6hF4kO3tG8eD1yQ5uA9jV7mX2bN0pC6sR4wK3iT8hL9aD5oF6vJ1nY7gM0xE2kN9uH3bI4lT5qW1cO8mJ6rS0zP2eL7iV9aF5dR3gY8kU4tB1oX0nS7jQ6pM9wL2vC5hI3rT0fA8eD9bO6gP4sN7yK1uL5iJ2mR9oV8wQ3nX5kT6aY4lH7eS0cF1dZ2pB9tN3vW8uI6jM5oL0rA7gK9xQ4fS3nC5yD8zV2hR6bI7mT0oJ1pW9kF4lE6iU3vN2yF9aG5jM7cT8rO1bP6dK0sL4xH9eR2wQ3iV7nY5gJ0oD6mU8pL1tC9zB4aQ7fK2vN5yT3jI6rE0nS8lW9hO4dR5mJ1xA7qG2uF6iP3kZ0bV7oT8rM9lJ5eY1cS2wN4gQ6aD7pB0hO8kU3vF9iL2tT4nR8eM5yL7xP0uI6gC3jH9zK1qV8oN5dW7cR4lF0bD3iA2sE9kZ6pG1yJ5mT7wH0nO4vL6xQ9rB8aU2tS3fK1oI7jN0dL5eC4mY5bM9wT8pA7gJ6vR1kX2rQ3nO0uF4hD6tB8yY9zV7jJ5oI3mC6eH1lK9fO0dS2wP4tL3aG7bR8nT5kN6vU1xQ9pF0iM2rW7yE6uB5oV8hI4cN3lJ6sA1tD9gZ0aP7mK5jL2rW8eH4vT6pQ3xJ9";

    // Encrypts a file using the specified key
    public static Optional<File> encryptFile(String key, File inputFile, File outputFile) {
        return processFile(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    // Decrypts a file using the specified key
    public static Optional<File> decryptFile(String key, File inputFile, File outputFile) {
        return processFile(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    // Encrypts a string using the specified key
    public static Optional<String> encryptString(String key, String input) {
        return processBytes(Cipher.ENCRYPT_MODE, key, input.getBytes()).map(bytes -> java.util.Base64.getEncoder().encodeToString(bytes));
    }

    // Decrypts a string using the specified key
    public static Optional<String> decryptString(String key, String input) {
        return processBytes(Cipher.DECRYPT_MODE, key, java.util.Base64.getDecoder().decode(input)).map(String::new);
    }

    // Encodes a byte array using the specified key
    public static Optional<byte[]> encryptBytes(String key, byte[] inputBytes) {
        return processBytes(Cipher.ENCRYPT_MODE, key, inputBytes);
    }

    // Decodes a byte array using the specified key
    public static Optional<byte[]> decryptBytes(String key, byte[] inputBytes) {
        return processBytes(Cipher.DECRYPT_MODE, key, inputBytes);
    }

    // Processes the file for encryption or decryption
    private static Optional<File> processFile(int cipherMode, String key, File inputFile, File outputFile) {
        try {
            // Read the entire file into a byte array
            byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
            // Process the bytes (encrypt or decrypt)
            Optional<byte[]> outputBytesOpt = processBytes(cipherMode, key, inputBytes);
            if (outputBytesOpt.isPresent()) {
                // Write the output bytes to the output file
                Files.write(outputFile.toPath(), outputBytesOpt.get());

                return Optional.of(outputFile); // Return the output file
            } else {
                return Optional.empty(); // Return empty Optional if processing failed
            }
        } catch (IOException e) {
            System.out.println("Error in Crypto.processFile");
            e.printStackTrace();

            return Optional.empty(); // Return empty Optional in case of error
        }
    }

    // Processes the byte array for encryption or decryption
    public static Optional<byte[]> processBytes(int cipherMode, String key, byte[] inputBytes) {
        Optional<Key> secretKeyOpt = generateKey(key);
        if (secretKeyOpt.isEmpty()) {
            return Optional.empty(); // Return empty Optional if key generation failed
        }

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] iv = new byte[IV_SIZE];
            new SecureRandom().nextBytes(iv); // Generate a random IV
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
            cipher.init(cipherMode, secretKeyOpt.get(), paramSpec);

            if (cipherMode == Cipher.ENCRYPT_MODE) {
                // Encrypt the input bytes
                byte[] outputBytes = cipher.doFinal(inputBytes);
                // Prepend the IV to the output bytes
                byte[] combined = new byte[IV_SIZE + outputBytes.length];
                System.arraycopy(iv, 0, combined, 0, IV_SIZE);
                System.arraycopy(outputBytes, 0, combined, IV_SIZE, outputBytes.length);

                return Optional.of(combined); // Return the combined byte array with IV prepended
            } else {
                // Decrypt the input bytes
                System.arraycopy(inputBytes, 0, iv, 0, IV_SIZE); // Extract the IV
                paramSpec = new IvParameterSpec(iv);
                cipher.init(cipherMode, secretKeyOpt.get(), paramSpec);
                byte[] outputBytes = cipher.doFinal(inputBytes, IV_SIZE, inputBytes.length - IV_SIZE);

                return Optional.of(outputBytes); // Return the decrypted byte array
            }
        } catch (Exception e) {
            System.out.println("Error in Crypto.processBytes");
            e.printStackTrace();

            return Optional.empty(); // Return empty Optional in case of error
        }
    }

    // Generates a secret key from the provided key string
    private static Optional<Key> generateKey(String key) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), SALT.getBytes(), ITERATIONS, KEY_SIZE);

            return Optional.of(new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ALGORITHM));
        } catch (Exception e) {
            System.out.println("Error in Crypto.generateKey");
            e.printStackTrace();

            return Optional.empty(); // Return empty Optional in case of error
        }
    }
}