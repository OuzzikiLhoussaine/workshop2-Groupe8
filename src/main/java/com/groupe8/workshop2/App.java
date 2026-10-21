package com.groupe8.workshop2;

import com.groupe8.workshop2.service.SearchService;
import com.groupe8.workshop2.service.UserRepository;

import java.util.List;

/**
 * Workshop 2 - DevSecOps (Chapitres 2 & 3).
 *
 * Application pedagogique contenant des vulnerabilites INTENTIONNELLES.
 * Objectif : les detecter avec SAST (SonarQube / Semgrep) et SCA
 * (OWASP Dependency-Check / Dependabot), puis les corriger.
 */
public class App {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Workshop 2 - DevSecOps : application vulnérable (demo) ===");

        // Injection SQL (SQLi) - voir UserRepository
        UserRepository repo = new UserRepository();
        repo.initSchema();
        repo.createSampleData();
        List<String> users = repo.findUsersByName("Alice");
        System.out.println("Utilisateurs trouves : " + users);

        // Log utilisateur avec Log4j (Log4Shell / log injection) - voir SearchService
        SearchService search = new SearchService();
        search.search("atelier devsecops");

        System.out.println("=== Fin de la demonstration ===");
    }
}
