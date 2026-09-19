#!/usr/bin/env python3
"""
Génère donnees.sql à partir de proprietes.csv.
Insère d'abord les quartiers uniques, puis les propriétés avec quartier_id.
"""
import csv
import os

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
CSV_PATH = os.path.join(SCRIPT_DIR, '..', 'src', 'main', 'resources', 'data', 'proprietes.csv')
OUT_PATH = os.path.join(SCRIPT_DIR, '..', 'donnees.sql')


def escape(val):
    """Échappe les apostrophes pour SQL."""
    if val is None:
        return 'NULL'
    return val.replace("'", "''")


def sql_val(val, quote=True):
    """Retourne la valeur SQL : NULL si vide, sinon quotée ou brute."""
    if val is None or val.strip() == '':
        return 'NULL'
    v = val.strip()
    return f"'{escape(v)}'" if quote else v


def main():
    # --- Lire le CSV ---
    with open(CSV_PATH, encoding='utf-8') as f:
        reader = csv.DictReader(f)
        rows = list(reader)

    # --- Extraire les quartiers uniques ---
    quartiers = {}  # (nom, ville) -> id
    qid = 1
    for row in rows:
        nom = row['quartier'].strip()
        ville = row['ville'].strip()
        key = (nom, ville)
        if key not in quartiers:
            quartiers[key] = qid
            qid += 1

    # --- Écrire le fichier SQL ---
    with open(OUT_PATH, 'w', encoding='utf-8') as out:
        out.write("-- ================================================\n")
        out.write("-- Données générées automatiquement depuis proprietes.csv\n")
        out.write("-- Jouer APRÈS schema.sql\n")
        out.write("-- ================================================\n\n")

        # Quartiers
        out.write("-- Quartiers\n")
        for (nom, ville), q_id in sorted(quartiers.items(), key=lambda x: x[1]):
            out.write(
                f"INSERT INTO quartier (id, nom, ville) "
                f"VALUES ({q_id}, '{escape(nom)}', '{escape(ville)}');\n"
            )

        out.write(f"\n-- Réinitialiser la séquence quartier_id_seq\n")
        out.write(f"SELECT setval('quartier_id_seq', {len(quartiers)});\n\n")

        # Propriétés
        out.write(f"-- Propriétés ({len(rows)} lignes)\n")
        for row in rows:
            pid = row['id'].strip()
            tt = row['type_transaction'].strip()
            tb = row['type_bien'].strip()
            prix = row['prix'].strip()
            sup = row['superficie'].strip()
            ch = row['chambres'].strip()
            sb = row['salles_bain'].strip()
            nom_q = row['quartier'].strip()
            ville = row['ville'].strip()
            q_id = quartiers[(nom_q, ville)]
            annee = row['annee_construction'].strip()
            tc = row['type_courtier'].strip()
            dp = row['date_publi'].strip()
            desc_val = sql_val(row['description'])

            terrain = sql_val(row.get('terrain_pi2', ''), quote=False)
            etages = sql_val(row.get('etages', ''), quote=False)
            garage = sql_val(row.get('garage', ''), quote=False)
            etage_u = sql_val(row.get('etage_unite', ''), quote=False)
            frais = sql_val(row.get('frais_condo', ''), quote=False)
            asc = sql_val(row.get('ascenseur', ''), quote=False)

            out.write(
                f"INSERT INTO propriete VALUES ("
                f"'{pid}', '{tt}', '{tb}', {prix}, {sup}, {ch}, {sb}, "
                f"{q_id}, {annee}, '{tc}', '{dp}', {desc_val}, "
                f"{terrain}, {etages}, {garage}, {etage_u}, {frais}, {asc}"
                f");\n"
            )

    print(f"donnees.sql généré : {len(quartiers)} quartiers, {len(rows)} propriétés")


if __name__ == '__main__':
    main()