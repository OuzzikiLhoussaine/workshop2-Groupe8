package com.groupe8.workshop2.web;

/**
 * VULNERABILITE INTENTIONNELLE N°3 - Cross-Site Scripting / XSS (CWE-79).
 *
 * La saisie utilisateur est inseree dans le HTML sans echappement :
 * une charge comme  &lt;script&gt;alert(document.cookie)&lt;/script&gt;
 * sera executee par le navigateur de la victime.
 */
public class HtmlRenderer {

    /** VULNERABLE : aucune validation / aucun echappement du contenu HTML. */
    public String renderWelcomePage(String username) {
        return "<!DOCTYPE html><html><head><title>Profil</title></head>"
                + "<body><h1>Bonjour " + username + "</h1>"
                + "<p>Bienvenue sur votre espace personnel.</p>"
                + "</body></html>";
    }

    /** VULNERABLE : reflet direct de la saisie dans la reponse HTML. */
    public String renderSearchResult(String userQuery) {
        return "<p>Resultats pour : " + userQuery + "</p>";
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
