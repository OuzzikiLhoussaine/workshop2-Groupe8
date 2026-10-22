package com.groupe8.workshop2.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * VULNERABILITE INTENTIONNELLE N°4 - Command Injection (CWE-78, OWASP A05:2025).
 *
 * Seules les adresses IPv4 sont acceptees avant execution de la commande.
 */
public class CommandRunner {

    public String pingHost(String host) throws IOException {
        if (host == null || !host.matches("^\\d{1,3}(\\.\\d{1,3}){3}$")) {
            throw new IllegalArgumentException("Hôte invalide : " + host);
        }

        ProcessBuilder processBuilder = new ProcessBuilder("ping", "-c", "1", host);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }
}
