package com.groupe8.workshop2.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * VULNERABILITE INTENTIONNELLE N°1 - Injection SQL (OWASP A05:2025, CWE-89).
 *
 * La requete SQL est construite par concatenation de la saisie utilisateur.
 * Un attaquant peut injecter :  ' OR '1'='1
 * ou supprimer la table :      '; DROP TABLE users; --
 */
public class UserRepository {

    private static final String DB_URL = "jdbc:h2:mem:workshop;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    // VULNERABILITE : mot de passe hardcode en clair (CWE-798)
    private static final String PASSWORD = "password123";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public void initSchema() throws SQLException {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100))");
        }
    }

    public void createSampleData() throws SQLException {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("DELETE FROM users");
            st.execute("INSERT INTO users VALUES (1, 'Alice', 'alice@example.com')");
            st.execute("INSERT INTO users VALUES (2, 'Bob', 'bob@example.com')");
        }
    }

    /**
     * VULNERABLE : concatenation directe de {@code name} dans la requete SQL.
     */
    public List<String> findUsersByName(String name) throws SQLException {
        String query = "SELECT name, email FROM users WHERE name = '" + name + "'";
        List<String> results = new ArrayList<>();
        try (Connection c = getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                results.add(rs.getString("name") + " <" + rs.getString("email") + ">");
            }
        }
        return results;
    }
}
