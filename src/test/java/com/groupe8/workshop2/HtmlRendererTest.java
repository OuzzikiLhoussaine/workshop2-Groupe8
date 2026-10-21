package com.groupe8.workshop2;

import com.groupe8.workshop2.web.HtmlRenderer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests unitaires de la methode d'echappement HTML (methode SURE).
 */
class HtmlRendererTest {

    private final HtmlRenderer renderer = new HtmlRenderer();

    @Test
    void escapeHtml_neutralizesScriptTags() {
        String escaped = renderer.escapeHtml("<script>alert(1)</script>");
        assertFalse(escaped.contains("<script>"));
        assertTrue(escaped.contains("&lt;script&gt;"));
    }

    @Test
    void escapeHtml_handlesNullInput() {
        assertEquals("", renderer.escapeHtml(null));
    }

    @Test
    void escapeHtml_escapesQuotesAndAmpersand() {
        assertEquals("a &amp; b &quot;c&quot; &#39;d&#39;", renderer.escapeHtml("a & b \"c\" 'd'"));
    }
}
