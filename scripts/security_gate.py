#!/usr/bin/env python3
"""
Security Gate - Workshop 2 (Groupe 8)
Implemente la "risk acceptance gate" du Chapitre 2 (SAST) :

    Build FAIL si :
      - 1 vulnerabilite ou plus de severite CRITIQUE
      - plus de 5 vulnerabilites HIGH
      - des secrets commits dans le code
    Build PASS sinon

Lit les rapports produits par les jobs SAST (Semgrep) et SCA
(OWASP Dependency-Check), genere un resume Markdown dans le
job summary GitHub, et bloque (exit 1) si la politique est violee.
"""

import json
import sys
from pathlib import Path

# --- Politique de securite (seuils) ---------------------------------
MAX_CRITICAL = 0   # aucun critique tolere
MAX_HIGH = 5       # strictement plus de 5 HIGH => FAIL
BLOCK_ON_SECRETS = True

semgrep_json = Path("reports/semgrep/semgrep-results.json")
odc_json = Path("reports/odc/dependency-check-report.json")

critical_count = 0
high_count = 0
medium_low_count = 0
secret_count = 0
details = []


def add_detail(tool, severity, message):
    details.append((tool, severity, message))


# --- 1. Analyse du rapport Semgrep (SAST) --------------------------
if semgrep_json.exists():
    with open(semgrep_json, encoding="utf-8") as f:
        data = json.load(f)
    for r in data.get("results", []):
        check_id = r.get("check_id", "")
        severity = (r.get("extra", {}).get("severity") or "INFO").upper()
        location = f"{r.get('path')}:{r.get('start', {}).get('line', '?')}"
        is_secret = "secret" in check_id.lower() or "secrets" in check_id
        if is_secret:
            secret_count += 1
            add_detail("Semgrep", "SECRET", f"{location} - {check_id}")
        elif severity == "ERROR":
            critical_count += 1
            add_detail("Semgrep", "CRITICAL", f"{location} - {check_id}")
        elif severity == "WARNING":
            high_count += 1
            add_detail("Semgrep", "HIGH", f"{location} - {check_id}")
        else:
            medium_low_count += 1
            add_detail("Semgrep", "MEDIUM/LOW", f"{location} - {check_id}")
else:
    print("ATTENTION : rapport Semgrep introuvable.")

# --- 2. Analyse du rapport OWASP Dependency-Check (SCA) -------------
if odc_json.exists():
    with open(odc_json, encoding="utf-8") as f:
        data = json.load(f)
    for dep in data.get("dependencies", []):
        for v in dep.get("vulnerabilities", []):
            name = v.get("name", "?")
            score = 0.0
            cvss3 = v.get("cvssv3") or {}
            if isinstance(cvss3, dict):
                score = float(cvss3.get("baseScore") or 0)
            elif isinstance(v.get("cvssv2"), dict):
                score = float(v["cvssv2"].get("baseScore") or 0)
            pkg = dep.get("packages", [{}])[0].get("id", dep.get("fileName", "?"))
            msg = f"{name} (CVSS {score}) sur {pkg}"
            if score >= 9.0:
                critical_count += 1
                add_detail("Dependency-Check", "CRITICAL", msg)
            elif score >= 7.0:
                high_count += 1
                add_detail("Dependency-Check", "HIGH", msg)
            else:
                medium_low_count += 1
                add_detail("Dependency-Check", "MEDIUM/LOW", msg)
else:
    print("ATTENTION : rapport Dependency-Check introuvable.")

# --- 3. Decision (gate) --------------------------------------------
failed = (
    critical_count > MAX_CRITICAL
    or high_count > MAX_HIGH
    or (BLOCK_ON_SECRETS and secret_count > 0)
)

summary = [
    "## \U0001f6e1\ufe0f Security Gate - Resultat",
    "",
    "| Metrique | Nombre | Politique |",
    "|---|---|---|",
    f"| CRITICAL | {critical_count} | max {MAX_CRITICAL} |",
    f"| HIGH | {high_count} | max {MAX_HIGH} |",
    f"| MEDIUM/LOW | {medium_low_count} | informatif |",
    f"| Secrets commits | {secret_count} | 0 tolere |",
    "",
]

if details:
    summary.append("### \U0001f4cb Details des findings")
    summary.append("")
    summary.append("| Outil | Severite | Finding |")
    summary.append("|---|---|---|")
    for tool, sev, msg in details[:40]:
        summary.append(f"| {tool} | {sev} | {msg.replace('|', '/')} |")
    if len(details) > 40:
        summary.append(f"\n_(... {len(details) - 40} autres findings dans les artefacts)_")
    summary.append("")

if failed:
    summary.append("### \u274c GATE BLOQUANT")
    summary.append("Le niveau de risque est **inacceptable** : corrigez les vulnerabilites "
                   "(voir `docs/CORRECTIONS.md`), puis poussez un nouveau commit "
                   "pour relancer automatiquement le pipeline (Shift Left).")
else:
    summary.append("### \u2705 GATE PASSE")
    summary.append("Aucun risque critique detecte : le build peut continuer (deploiement).")

report = "\n".join(summary)
print(report)

# Publication dans le resume du job GitHub Actions
import os
summary_path = os.environ.get("GITHUB_STEP_SUMMARY")
if summary_path:
    with open(summary_path, "a", encoding="utf-8") as f:
        f.write(report + "\n")

sys.exit(1 if failed else 0)
