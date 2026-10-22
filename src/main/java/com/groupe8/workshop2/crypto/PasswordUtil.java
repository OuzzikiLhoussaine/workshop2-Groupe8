package com.groupe8.workshop2.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * VULNERABILITE INTENTIONNELLE N°6 - Cryptographie faible (OWASP A04:2025, CWE-328).
 *
 * Les mots de passe sont haches avec SHA-256 et un sel aleatoire.
 */
public class PasswordUtil {

    private static final int SALT_LENGTH = 16;
    private final SecureRandom secureRandom = new SecureRandom();

    public String hashPasswordWeak(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return toHex(salt) + ":" + toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /** CORRECT : SHA-256 (minimum) - utilise par les tests unitaires. */
    public String hashPasswordSecure(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
