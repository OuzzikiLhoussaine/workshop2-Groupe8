package com.groupe8.workshop2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Chargement sûr de configuration JSON
 * (OWASP A08:2025 - Software or Data Integrity Failures, CWE-502).
 *
 * La configuration est lue comme un arbre JSON sans typage polymorphe.
 */
public class JsonConfigLoader {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode loadConfig(String json) throws IOException {
        return mapper.readTree(json);
    }
}
