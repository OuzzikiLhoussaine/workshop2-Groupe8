package com.groupe8.workshop2.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * VULNERABILITE INTENTIONNELLE N°7 - Deserialisation non sure
 * (OWASP A08:2025 - Software or Data Integrity Failures, CWE-502).
 *
 * enableDefaultTyping() active le typage polymorphe de Jackson :
 * un attaquant peut fournir un JSON malveillant qui instancie
 * des classes dangereuses (gadget chains) => execution de code.
 * De plus, jackson-databind 2.9.8 est lui-meme vulnerable (SCA).
 */
public class JsonConfigLoader {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonConfigLoader() {
        // VULNERABLE : typage polymorphe active (deserialisation non sure)
        mapper.enableDefaultTyping();
    }

    /** VULNERABLE : deserialisation de donnees non fiables sans validation. */
    public Object loadConfig(String json) throws IOException {
        return mapper.readValue(json, Object.class);
    }
}
