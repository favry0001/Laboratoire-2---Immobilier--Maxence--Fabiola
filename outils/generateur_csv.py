"""
Generateur du jeu de donnees pour le Laboratoire 2 - Sujet 5 (Immobilier).

Produit proprietes.csv : 350 annonces coherentes (60% vente / 40% location),
5 villes du Quebec, maisons et condos.

Les champs specifiques au type sont laisses VIDES selon le type :
  - maison -> terrain_pi2, etages, garage remplis ; etage_unite, frais_condo, ascenseur vides
  - condo  -> l'inverse

Usage : python generateur_csv.py
"""

import csv
import random
from datetime import date, timedelta

random.seed(2026)

NB_ANNONCES = 350
PART_VENTE = 0.60

# prix au pi2 (vente) et loyer mensuel au pi2 (location), par ville
VILLES = {
    "Montreal":  {"maison": 550, "condo": 620, "loyer": 2.20, "poids": 40},
    "Laval":     {"maison": 450, "condo": 480, "loyer": 1.80, "poids": 20},
    "Longueuil": {"maison": 430, "condo": 460, "loyer": 1.75, "poids": 15},
    "Quebec":    {"maison": 380, "condo": 400, "loyer": 1.50, "poids": 15},
    "Sherbrooke": {"maison": 300, "condo": 320, "loyer": 1.30, "poids": 10},
}

RUES = {
    "Montreal": ["Rue Sainte-Catherine", "Rue Saint-Denis", "Avenue du Parc",
                 "Boulevard Saint-Laurent", "Rue Ontario", "Avenue Mont-Royal"],
    "Laval": ["Boulevard Saint-Martin", "Rue Principale", "Avenue des Perron",
              "Boulevard des Laurentides"],
    "Longueuil": ["Rue Saint-Charles", "Chemin de Chambly", "Boulevard Taschereau"],
    "Quebec": ["Rue Sainte-Catherine", "Rue Saint-Jean", "Avenue Cartier",
               "Boulevard Charest"],
    "Sherbrooke": ["Rue King", "Rue Wellington", "Boulevard Portland"],
}

QUARTIERS = {
    "Montreal": ["Le Plateau", "Ville-Marie", "Rosemont", "Verdun", "Ahuntsic", "Villeray"],
    "Laval": ["Sainte-Rose", "Chomedey", "Vimont", "Duvernay"],
    "Longueuil": ["Vieux-Longueuil", "Saint-Hubert", "Greenfield Park"],
    "Quebec": ["Saint-Roch", "Limoilou", "Sainte-Foy", "Montcalm"],
    "Sherbrooke": ["Centre-ville", "Rock Forest", "Fleurimont"],
}

DESC_MAISON = [
    "Maison familiale avec cour arriere amenagee",
    "Cottage renove avec cuisine moderne et sous-sol fini",
    "Maison de plain-pied pres des ecoles et des parcs",
    "Propriete lumineuse avec grande salle de sejour",
    "Maison spacieuse dans un secteur tranquille",
    "Belle residence avec foyer au bois et veranda",
]

DESC_CONDO = [
    "Condo lumineux a distance de marche du metro",
    "Unite renovee avec balcon et rangement",
    "Appartement au coeur du quartier avec transport a proximite",
    "Condo moderne avec aire ouverte et grandes fenetres",
    "Unite bien entretenue dans un immeuble recent",
    "Logement pratique et ideal pour un premier achat",
]


def choisir_ville():
    noms = list(VILLES.keys())
    poids = [VILLES[v]["poids"] for v in noms]
    return random.choices(noms, weights=poids, k=1)[0]


def facteur_annee(annee):
    """Un batiment recent vaut plus cher au pi2 qu'un batiment ancien."""
    if annee >= 2015:
        return 1.15
    if annee >= 2000:
        return 1.05
    if annee >= 1980:
        return 1.00
    return 0.90


def generer(index):
    ville = choisir_ville()
    est_maison = random.random() < 0.45
    est_vente = random.random() < PART_VENTE
    annee = random.randint(1955, 2025)

    if est_maison:
        superficie = random.randint(1100, 3200)
        chambres = random.choice([3, 3, 4, 4, 5])
        salles_bain = random.choice([1.0, 1.5, 2.0, 2.5, 3.0])
    else:
        superficie = random.randint(480, 1400)
        chambres = random.choice([1, 2, 2, 3])
        salles_bain = random.choice([1.0, 1.0, 1.5, 2.0])

    cle = "maison" if est_maison else "condo"

    if est_vente:
        base = VILLES[ville][cle] * superficie * facteur_annee(annee)
        prix = round(base * random.uniform(0.88, 1.12), -3)  # arrondi au millier
    else:
        base = VILLES[ville]["loyer"] * superficie * facteur_annee(annee)
        prix = round(base * random.uniform(0.85, 1.15), -1)  # arrondi a la dizaine

    jours = random.randint(0, 120)
    date_publi = date(2026, 9, 1) - timedelta(days=jours)

    ligne = {
        "id": f"P{index:04d}",
        "type_transaction": "VENTE" if est_vente else "LOCATION",
        "type_bien": "MAISON" if est_maison else "CONDO",
        "prix": int(prix),
        "superficie": superficie,
        "chambres": chambres,
        "salles_bain": salles_bain,
        "ville": ville,
        "quartier": f"{random.choice(QUARTIERS[ville])} - {random.choice(RUES[ville])}",
        "annee_construction": annee,
        "type_courtier": random.choices(
            ["COURTIER", "PROPRIETAIRE_DIRECT"], weights=[75, 25], k=1)[0],
        "date_publi": date_publi.isoformat(),
        "terrain_pi2": "",
        "etages": "",
        "garage": "",
        "etage_unite": "",
        "frais_condo": "",
        "ascenseur": "",
        "description": random.choice(DESC_MAISON if est_maison else DESC_CONDO),
    }

    if est_maison:
        ligne["terrain_pi2"] = superficie + random.randint(1500, 5500)
        ligne["etages"] = random.choice([1, 1, 2, 2, 3])
        ligne["garage"] = random.choice(["true", "false"])
    else:
        ligne["etage_unite"] = random.randint(1, 14)
        ligne["frais_condo"] = 0 if not est_vente else round(superficie * random.uniform(0.30, 0.55))
        ligne["ascenseur"] = random.choice(["true", "false"])

    return ligne


COLONNES = ["id", "type_transaction", "type_bien", "prix", "superficie", "chambres",
            "salles_bain", "ville", "quartier", "annee_construction", "type_courtier",
            "date_publi", "terrain_pi2", "etages", "garage", "etage_unite",
            "frais_condo", "ascenseur", "description"]


def main():
    lignes = [generer(i) for i in range(1, NB_ANNONCES + 1)]


    petits = [l for l in lignes
              if l["type_bien"] == "CONDO" and l["type_transaction"] == "LOCATION"
              and l["ville"] == "Montreal" and l["prix"] <= 1500]
    if len(petits) < 5:
        for i in range(5):
            l = generer(NB_ANNONCES + i + 1)
            l["type_bien"] = "CONDO"
            l["type_transaction"] = "LOCATION"
            l["ville"] = "Montreal"
            l["superficie"] = random.randint(480, 620)
            l["prix"] = random.randint(1050, 1480)
            l["quartier"] = f"{random.choice(QUARTIERS['Montreal'])} - {random.choice(RUES['Montreal'])}"
            l["terrain_pi2"] = l["etages"] = l["garage"] = ""
            l["etage_unite"] = random.randint(1, 9)
            l["frais_condo"] = 0
            l["ascenseur"] = random.choice(["true", "false"])
            l["description"] = random.choice(DESC_CONDO)
            lignes.append(l)

    with open("proprietes.csv", "w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=COLONNES, lineterminator="\n")
        writer.writeheader()
        writer.writerows(lignes)

    ventes = sum(1 for l in lignes if l["type_transaction"] == "VENTE")
    maisons = sum(1 for l in lignes if l["type_bien"] == "MAISON")
    print(f"{len(lignes)} annonces ecrites dans proprietes.csv")
    print(f"  ventes   : {ventes}  ({ventes * 100 // len(lignes)}%)")
    print(f"  locations: {len(lignes) - ventes}")
    print(f"  maisons  : {maisons} | condos : {len(lignes) - maisons}")
    print(f"  villes   : {len(VILLES)}")


if __name__ == "__main__":
    main()