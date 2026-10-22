package com.groupe8.workshop2.config;

/**
 * VULNERABILITE INTENTIONNELLE N°5 - Secrets hardcodes (CWE-798, OWASP A02/A07).
 *
 * Les cles et mots de passe sont fournis par l'environnement d'execution.
 */
public final class Credentials {

    private Credentials() {
    }

    public static boolean login(String user, String password) {
        return "admin".equals(user) && adminPassword().equals(password);
    }

    public static String adminPassword() {
        return required("ADMIN_PASSWORD");
    }

    public static String awsAccessKeyId() {
        return required("AWS_ACCESS_KEY_ID");
    }

    public static String awsSecretAccessKey() {
        return required("AWS_SECRET_ACCESS_KEY");
    }

    public static String apiToken() {
        return required("API_TOKEN");
    }

    private static String required(String variableName) {
        String value = System.getenv(variableName);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(variableName + " non configurée");
        }
        return value;
    }
}
