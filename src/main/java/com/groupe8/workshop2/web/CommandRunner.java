package com.groupe8.workshop2.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * VULNERABILITE INTENTIONNELLE N°4 - Command Injection (CWE-78, OWASP A05:2025).
 *
 * La saisie utilisateur est concatenee dans une commande systeme :
 * un attaquant peut saisir  8.8.8.8; rm -rf /  ou  $(whoami)
 * pour executer des commandes arbitraires sur le serveur.
 */
public class CommandRunner {

    /** VULNERABLE : concatenation de la saisie dans Runtime.exec(). */
    public String pingHost(String host) throws IOException {
        String command = "ping -c 1 " + host;
        Process process = Runtime.getRuntime().exec(command);
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace(); // VULNERABLE : stack trace exposee (mauvaise gestion des erreurs)
        }
        return output.toString();
    }
}
