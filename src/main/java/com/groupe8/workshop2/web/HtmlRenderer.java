package com.groupe8.workshop2.web;

/**
 * VULNERABILITE INTENTIONNELLE N°3 - Cross-Site Scripting / XSS (CWE-79).
 *
 * La saisie utilisateur est echappee avant insertion dans le HTML.
 */
public class HtmlRenderer {

    public String renderWelcomePage(String username) {
        return "<!DOCTYPE html><html><head><title>Profil</title></head>"
                + "<body><h1>Bonjour " + escapeHtml(username) + "</h1>"
                + "<p>Bienvenue sur votre espace personnel.</p>"
                + "</body></html>";
    }

    public String renderSearchResult(String userQuery) {
        return "<p>Resultats pour : " + escapeHtml(userQuery) + "</p>";
    }

    /** CORRECT : echappement HTML (utilise par les tests unitaires). */
    public String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
