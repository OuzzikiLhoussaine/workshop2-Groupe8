package com.groupe8.workshop2;

import com.groupe8.workshop2.crypto.PasswordUtil;
import com.groupe8.workshop2.web.HtmlRenderer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests unitaires des methodes SURES du projet.
 * Ils garantissent que "mvn verify" passe sans erreur.
 */
class PasswordUtilTest {

    private final PasswordUtil passwordUtil = new PasswordUtil();

    @Test
    void hashPasswordSecure_producesDeterministicSha256() {
        String hash1 = passwordUtil.hashPasswordSecure("motdepasse");
        String hash2 = passwordUtil.hashPasswordSecure("motdepasse");
        assertEquals(hash1, hash2);
        assertEquals(64, hash1.length(), "SHA-256 produit 64 caracteres hexadecimaux");
    }

    @Test
    void hashPasswordSecure_differsForDifferentInputs() {
        assertNotEquals(
                passwordUtil.hashPasswordSecure("abc"),
                passwordUtil.hashPasswordSecure("abd"));
    }

    @Test
    void hashPasswordWeak_isMd5Length() {
        // Documente la faiblesse : MD5 = 32 caracteres seulement
        assertEquals(32, passwordUtil.hashPasswordWeak("motdepasse").length());
    }
}
