package com.groupe8.workshop2;

import com.fasterxml.jackson.databind.JsonNode;
import com.groupe8.workshop2.config.JsonConfigLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonConfigLoaderTest {

    private final JsonConfigLoader loader = new JsonConfigLoader();

    @Test
    void loadConfig_readsJsonAsTree() throws Exception {
        JsonNode config = loader.loadConfig("{\"name\":\"demo\",\"enabled\":true}");

        assertEquals("demo", config.get("name").asText());
        assertTrue(config.get("enabled").asBoolean());
    }

    @Test
    void loadConfig_doesNotInstantiatePolymorphicTypes() throws Exception {
        JsonNode config = loader.loadConfig(
                "{\"@class\":\"java.lang.String\",\"value\":\"not an object\"}");

        assertEquals("java.lang.String", config.get("@class").asText());
    }
}
