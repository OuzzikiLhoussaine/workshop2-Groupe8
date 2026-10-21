package com.groupe8.workshop2.config;

/**
 * VULNERABILITE INTENTIONNELLE N°5 - Secrets hardcodes (CWE-798, OWASP A02/A07).
 *
 * Des cles et mots de passe sont ecrits en dur dans le code source.
 * Ils fuient des qu'un tiers accede au depot Git (historique compris).
 * Les outils SAST / secrets-scanning doivent les detecter.
 */
public final class Credentials {

    private Credentials() {
    }

    // VULNERABLE : mot de passe administrateur hardcode
    public static final String ADMIN_PASSWORD = "Admin@12345";

    // VULNERABLE : cle API stockee en clair (exemple AWS)
    public static final String AWS_ACCESS_KEY_ID = "AKIA7Q2M4RXK9TPVWXYZ";
    public static final String AWS_SECRET_ACCESS_KEY = "k8R2mQ9vLpX4nT6wY1zB3cD5eF7gH9jK0lM2nP4qR6s";

    // VULNERABLE : token JWT / API key hardcode
    public static final String API_TOKEN = "ghp_9Xb2Km4Qp7Rt1Vw3Yz5Ad8Fh0Jn2Ls4Mu6Ox";

    /** VULNERABLE : connexion avec identifiants en dur. */
    public static boolean login(String user, String password) {
        return "admin".equals(user) && ADMIN_PASSWORD.equals(password);
    }
}
