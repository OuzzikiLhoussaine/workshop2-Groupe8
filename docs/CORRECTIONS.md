# 🔧 Corrections des vulnérabilités — Workshop 2 (Groupe 8)

Ce document montre **comment corriger** chaque vulnérabilité détectée par les
outils SAST/SCA. Appliquez ces correctifs, committez, et observez le
**Security Gate** passer de ❌ à ✅ (Shift Left / ré-scan automatique).

---

## 1. Injection SQL — `UserRepository.findUsersByName`

**Avant (vulnérable)** — concaténation dans la requête :

```java
String query = "SELECT name, email FROM users WHERE name = '" + name + "'";
ResultSet rs = st.executeQuery(query);
```

**Après (corrigé)** — requête paramétrée :

```java
public List<String> findUsersByName(String name) throws SQLException {
    String query = "SELECT name, email FROM users WHERE name = ?";
    List<String> results = new ArrayList<>();
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(query)) {
        ps.setString(1, name);           // paramètre lié → injection impossible
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(rs.getString("name") + " <" + rs.getString("email") + ">");
            }
        }
    }
    return results;
}
```

---

## 2. XSS — `HtmlRenderer.renderWelcomePage` / `renderSearchResult`

**Avant** :

```java
return "<h1>Bonjour " + username + "</h1>";
```

**Après** :

```java
return "<h1>Bonjour " + escapeHtml(username) + "</h1>";
// (utiliser la méthode escapeHtml déjà présente dans la classe)
```

---

## 3. Command Injection — `CommandRunner.pingHost`

**Avant** :

```java
String command = "ping -c 1 " + host;
Process process = Runtime.getRuntime().exec(command);
```

**Après** — `ProcessBuilder` + validation (liste blanche) :

```java
public String pingHost(String host) throws IOException {
    if (!host.matches("^\\d{1,3}(\\.\\d{1,3}){3}$")) {   // IP uniquement
        throw new IllegalArgumentException("Hôte invalide : " + host);
    }
    ProcessBuilder pb = new ProcessBuilder("ping", "-c", "1", host);  // sans shell
    pb.redirectErrorStream(true);
    Process process = pb.start();
    // ... lecture de la sortie ...
}
```

---

## 4. Secrets hardcodés — `Credentials`

**Avant** :

```java
public static final String ADMIN_PASSWORD = "Admin@12345";
```

**Après** — variables d'environnement / vault :

```java
public static String adminPassword() {
    String pwd = System.getenv("ADMIN_PASSWORD");
    if (pwd == null || pwd.isEmpty()) {
        throw new IllegalStateException("ADMIN_PASSWORD non configurée");
    }
    return pwd;
}
```

> ⚠️ Pensez aussi à **révoquer** les clés déjà commitées et à purger l'historique Git
> (ou regénérer les clés) : un secret commité est considéré comme compromis.

---

## 5. Crypto faible — `PasswordUtil.hashPasswordWeak`

**Avant** :

```java
MessageDigest md = MessageDigest.getInstance("MD5");
```

**Après** — SHA-256 + sel (idéalement **BCrypt/Argon2** pour les mots de passe) :

```java
MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(salt);                      // sel aléatoire de 16 octets
byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
```

---

## 6. Désérialisation non sûre — `JsonConfigLoader`

**Avant** :

```java
mapper.enableDefaultTyping();          // typage polymorphe dangereux
```

**Après** :

```java
// Supprimer enableDefaultTyping() et désérialiser vers des types précis :
mapper.readValue(json, MonConfig.class);
// + mettre jackson-databind à jour (2.17+)
```

---

## 7. Log4Shell / injection de logs — `SearchService` + dépendances

**Code — Avant** :

```java
logger.info("Recherche utilisateur : " + query);
```

**Code — Après** :

```java
logger.info("Recherche utilisateur : {}", query);   // paramètre lié, pas de concaténation
```

**Dépendance — `pom.xml`** :

```xml
<log4j2.version>2.17.1</log4j2.version>   <!-- était 2.14.1 (Log4Shell) -->
```

---

## 8. Text4Shell — `MessageTemplate` + dépendances

**Dépendance — `pom.xml`** :

```xml
<commons-text.version>1.10.0</commons-text.version>   <!-- était 1.9 -->
```

**Code** : ne jamais appliquer `StringSubstitutor` sur du contenu utilisateur ;
séparer les gabarits (fiables) des valeurs (non fiables).

---

## 9. Mauvaise gestion des erreurs — `ErrorHandler`

**Avant** :

```java
e.printStackTrace();                       // stack trace exposée
catch (Exception e) { /* avalée */ }
```

**Après** :

```java
private static final Logger log = LogManager.getLogger(ErrorHandler.class);

public String renderErrorPage(Exception e) {
    log.error("Erreur interne", e);                    // journalisée (pas exposée)
    return "<html><body><h1>Erreur</h1>"
         + "<p>Une erreur interne est survenue. Veuillez réessayer.</p>"
         + "</body></html>";
}
```

---

## 10. Montée de version des dépendances (SCA)

| Bibliothèque | Version vulnérable | Version corrigée |
|---|---|---|
| `log4j-core` / `log4j-api` | 2.14.1 | **2.17.1+** |
| `commons-text` | 1.9 | **1.10.0+** |
| `jackson-databind` | 2.9.8 | **2.17+** |
| `h2` | 1.4.200 | **2.2.224+** |

> 💡 **Dependabot** peut faire ces montées de version automatiquement via des Pull
> Requests : c'est tout l'objet de `.github/dependabot.yml`.

---

## 10. Durcissement de la CI (findings Semgrep sur le workflow)

- **Pinner les actions** par commit SHA (au lieu des tags `@v4` mutables) :

```yaml
- uses: actions/checkout@11bd71901bbe5b1630ceea73d27597364c9af683 # v4.2.2
```

- **Ajouter un cooldown Dependabot** dans `.github/dependabot.yml`
  (évite d'accepter un package fraîchement publié et potentiellement compromis) :

```yaml
updates:
  - package-ecosystem: "maven"
    directory: "/"
    cooldown:
      default-days: 7
```

---

## ✅ Validation finale

1. `mvn clean verify` → compile sans erreur
2. `mvn -Psecurity-sca dependency-check:check` → 0 CVE critique
3. Pipeline GitHub Actions → **Security Gate ✅ PASS**
4. Capture d'écran du gate vert → dans le README
