package com.groupe8.workshop2.web;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * VULNERABILITE INTENTIONNELLE N°9 - Mauvaise gestion des erreurs
 * (OWASP A09/A10:2025 - Logging & Alerting Failures / Exceptional Conditions).
 *
 * Les exceptions sont avalees silencieusement (pas de journalisation,
 * pas d'alerte) et les stack traces completes sont renvoyees a l'utilisateur
 * final (fuite d'informations internes).
 */
public class ErrorHandler {

    /** VULNERABLE : la stack trace complete est exposee au client. */
    public String renderErrorPage(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return "<html><body><h1>Erreur</h1><pre>" + sw.toString() + "</pre></body></html>";
    }

    /** VULNERABLE : exception avalee sans trace ni alerte. */
    public void saveData(String data) {
        try {
            if (data == null) {
                throw new IllegalArgumentException("data est null");
            }
            // ... sauvegarde ...
        } catch (Exception e) {
            // TODO: rien faire, ce n'est pas grave (FAUX : il faut journaliser !)
        }
    }
}
