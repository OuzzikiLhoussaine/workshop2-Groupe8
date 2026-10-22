package com.groupe8.workshop2.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * VULNERABILITE INTENTIONNELLE N°2 - Injection de logs / Log4Shell (contexte).
 *
 * Les donnees utilisateur sont transmises comme parametres de log.
 */
public class SearchService {

    private static final Logger logger = LogManager.getLogger(SearchService.class);

    private final List<String> catalog = List.of("atelier devsecops", "pipeline ci/cd", "owasp top 10");

    public List<String> search(String query) {
        logger.info("Recherche utilisateur : {}", query);
        List<String> hits = new ArrayList<>();
        for (String item : catalog) {
            if (item.contains(query.toLowerCase())) {
                hits.add(item);
            }
        }
        logger.debug("Nombre de resultats : {}", hits.size());
        return hits;
    }
}
