package com.groupe8.workshop2.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * VULNERABILITE INTENTIONNELLE N°6 - Cryptographie faible (OWASP A04:2025, CWE-328).
 *
 * MD5 est un algorithme de hachage casse : il ne faut JAMAIS l'utiliser
 * pour stocker des mots de passe (rainbow tables, collisions).
 */
public class PasswordUtil {

    /** VULNERABLE : hachage MD5 pour les mots de passe. */
    public String hashPasswordWeak(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
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
