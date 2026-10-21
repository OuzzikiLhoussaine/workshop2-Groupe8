package com.groupe8.workshop2.web;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

/**
 * VULNERABILITE INTENTIONNELLE N°8 - Text4Shell (CVE-2022-42889, CVSS 9.8).
 *
 * commons-text 1.9 interprete les expressions ${...} dans les chaines
 * substituees. En soumettant du contenu utilisateur a StringSubstitutor,
 * un attaquant peut executer : ${script:js:java.lang.Runtime...}
 */
public class MessageTemplate {

    /** VULNERABLE : substitution d'une saisie utilisateur avec commons-text 1.9. */
    public String render(String template, Map<String, String> values) {
        StringSubstitutor substitutor = new StringSubstitutor(values);
        return substitutor.replace(template);
    }
}
