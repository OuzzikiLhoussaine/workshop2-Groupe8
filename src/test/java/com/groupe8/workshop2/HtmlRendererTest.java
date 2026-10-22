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

    @Test
    void renderWelcomePage_escapesUsername() {
        String rendered = renderer.renderWelcomePage("<script>alert(1)</script>");

        assertFalse(rendered.contains("<script>"));
        assertTrue(rendered.contains("&lt;script&gt;alert(1)&lt;/script&gt;"));
    }

    @Test
    void renderSearchResult_escapesUserQuery() {
        String rendered = renderer.renderSearchResult("<img src=x onerror=alert(1)>");

        assertFalse(rendered.contains("<img"));
        assertTrue(rendered.contains("&lt;img src=x onerror=alert(1)&gt;"));
    }
}
