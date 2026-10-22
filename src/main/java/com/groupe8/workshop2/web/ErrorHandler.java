package com.groupe8.workshop2.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * VULNERABILITE INTENTIONNELLE N°9 - Mauvaise gestion des erreurs
 * (OWASP A09/A10:2025 - Logging & Alerting Failures / Exceptional Conditions).
 *
 * Les erreurs sont journalisees cote serveur et un message generique est
 * renvoye a l'utilisateur final.
 */
public class ErrorHandler {

    private static final Logger log = LogManager.getLogger(ErrorHandler.class);

    public String renderErrorPage(Exception e) {
        log.error("Erreur interne", e);
        return "<html><body><h1>Erreur</h1>"
                + "<p>Une erreur interne est survenue. Veuillez réessayer.</p>"
                + "</body></html>";
    }

    public void saveData(String data) {
        try {
            if (data == null) {
                throw new IllegalArgumentException("data est null");
            }
            // ... sauvegarde ...
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde des donnees", e);
        }
    }
}
