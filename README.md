
# Catalogue Immobilier - TP2

**Cours** : 420-930-MA — Algorithmes et modèles de programmation
**Session** : Été 2026, groupe 25604
**Laboratoire** : 2 (Application JavaFX v1)
**Date de remise** : 11 septembre 2026, 23h59

---

## Équipe

| Nom complet | Adresse courriel | Contribution principale |
|-------------|------------------|--------------------------|
|  Maxance Zeka    | zkmance@gmail.com  | Frontend:  UI FXML, Controller, CSS
| Fabiola  Sainvry | favry0001@gmail.com | Backend: Modèle, Service, Tris|



---

## Sujet choisi

**Numéro du sujet** :  5
**Nom du sujet** : Immobilier

---

## 🔗 Lien du dépôt GitHub PUBLIC

**URL** : https://github.com/favry0001/Laboratoire-2---Immobilier--Maxence--Fabiola.git

> ⚠️ Vérifier que le dépôt est **PUBLIC** et accessible sans authentification.
> Tester le lien dans un navigateur privé avant la remise.

---

## Fonctionnalités implémentées

### ✅ Obligatoires (cocher ce qui est fait)

- [x] Architecture MVC avec packages séparés (model / service / algorithmes / controller / util)
- [x] Chargement des données depuis fichier CSV (nombre de lignes : 350)
- [x] Interface JavaFX principale avec liste/tableau
- [x] Panneau détail affichant l'élément sélectionné
- [x] Pagination fonctionnelle (taille de page : 25)
- [ ] Filtres multi-critères combinables (nombre implémentés : 5 / 5 demandés)
- [x] Recherche par texte en temps réel
- [x] Interface Algorithme définie
- [x] Tri #1 implémenté : Tri à bulles (Bubble Sort) — O(n²)
- [x] Tri #2 implémenté : Tri par insertion (Insertion Sort) — O(n²)
- [x] Tri #3 implémenté : Tri fusion (Merge Sort) — O(n log n)
- [x] Benchmark des 3 tris avec System.nanoTime()
- [ ] Comparateur/benchmark des tris avec mesure du temps
- [x] Wishlist / Favoris (ajout, retrait, pas de doublons)
- [x] CSS appliqué (thème visuel du projet)

### 🎁 Bonus (cocher ce qui est fait)

- [ ] Calcul du prix au pied carré affiché dans le panneau détail
- [ ] Affichage adapté au type de bien (maison vs condo) dans le panneau détail
- [ ] Jeu de données généré par script Python reproductible (`outils/generateur_csv.py`)

### ❌ Non implémenté (assumer honnêtement)

 ### Limite connue

`ControleurPrincipal` dépasse la limite de 300 lignes fixée par les consignes.

---

## Structure du projet

```
 src/main/java/immobilier/
├── MainFx.java                  
├── model/                      
│   ├── Propriete.java            12 attributs private final
│   ├── Maison.java              + terrain, étages, garage
│   ├── Condo.java               + étage de l'unité, frais de condo, ascenseur
│   ├── TypeTransaction.java     enum VENTE / LOCATION
│   ├── TypeCourtier.java        enum COURTIER / PROPRIETAIRE_DIRECT
│   └── Favoris.java             liste 
├── service/                     logique métier
│   ├── CritereFiltre.java       porteur des valeurs de filtre
│   ├── ServiceCatalogue.java    interface
│   └── ServiceCatalogueImpl.java filtrage, recherche, pagination
├── algorithmes/                 tris implémentés à la main
│   ├── Algorithme.java          interface : nom, complexiteTheorique, trier
│   ├── TriBulle.java            
│   ├── TriInsertion.java       
│   ├── TriFusion.java           
│   ├── Comparateurs.java        fabrique de Comparator
│   └── Benchmark.java           
├── controller/                  contrôleurs JavaFX
└── util/
    ├── SourceDonnees.java       interface 
    └── LecteurCSV.java          fabrique Maison / Condo selon type_bien

src/main/resources/
├── data/proprietes.csv          350 annonces
├── fxml/                        vues dessinées avec Scene Builder
└── css/style.css

outils/
└── generateur_csv.py            script de génération de la base de donné
```

---

## Instructions pour lancer le projet

### Prérequis

- JDK [A COMPLETER : 17 ou 21]
- Maven [A COMPLETER : version 3.x]
- (optionnel) IntelliJ IDEA / Eclipse

### Étapes

```bash
# 1. Cloner le dépôt
git clone https://github.com/favry0001/Laboratoire-2---Immobilier--Maxence--Fabiola.git

cd Laboratoire-2---Maxence--Fabiola

# 2. Compiler
mvn clean compile

# 3. Lancer l'application
mvn javafx:run
```

### Alternative dans IntelliJ

1. Ouvrir le projet dans IntelliJ (File > Open > dossier du projet)
2. Attendre que Maven télécharge les dépendances
3. Ouvrir `MainFx.java`
4. Cliquer sur le bouton Run

---

## Choix techniques

### Version Java utilisée
[Java 21  avec JavaFX 21.0.4, compilé via Maven.

### Format des données
CSV, séparateur virgule, encodage UTF-8, 19 colonnes, 350 lignes

### Algorithmes de tri implémentés
| Algorithme | Classe | Complexité |
|---|---|---|
| Tri à bulles | `TriBulle` | O(n²) |
| Tri par insertion | `TriInsertion` | O(n²) |
| Tri fusion | `TriFusion` | O(n log n) |

### Bibliothèques externes utilisées
[A COMPLETER : liste des dépendances Maven au-delà de JavaFX]

---

## Difficultés rencontrées
```markdown
Maxence : Une des principales difficultés rencontrées a été l’intégration du travail réalisé sur plusieurs branches Git. Certaines modifications concernaient les mêmes fichiers, notamment MainFx.java et Propriete.java, ce qui a provoqué des conflits lors des fusions. Il a fallu identifier les bonnes versions à conserver, résoudre les conflits manuellement et vérifier que les fonctionnalités déjà réalisées n’étaient pas perdues. Ça m’a permis de mieux comprendre le fonctionnement des branches, des merges et l’importance de faire des commits réguliers et bien séparés.

----
Fabiola : 

J'ai utilisé l'internet et AI comme tuteur, surtout pour débloquer des erreurs que je ne
comprenais pas. J'ai écrit le code en repartant de ce qui a été vu en classe : l'interface `Algorithme`
du projet BigOLab et la structure de `FilmDAO` pour le chargement des données, et
les algorithmes de tri de la séance 11 ou 12.

Les élemets sur lesquels j'ai vraiment bloqué :

**Sortir `target/` et `.idea/` du dépôt.** J'avais bien mis les dossiers dans le
`.gitignore`, mais Git continuait de les suivre. J'ai cherché longtemps avant de
comprendre que le `.gitignore` ne fait rien sur un fichier déjà suivi — il faut
faire `git rm -r --cached` pour arrêter le suivi. Trouvé après plusieurs
tutoriels YouTube et de l'aide en ligne.

**Le CSV introuvable.** Je l'avais mis dans `src/resources/` au lieu de
`src/main/resources/`. L'arborescence avait l'air correcte, mais Maven ne
regarde que sous `main/`, alors le fichier n'était jamais chargé.


**Les champs vides du CSV.** Comme les colonnes changent selon le type
(`garage` vide pour un condo, `frais_condo` vide pour une maison),
`Integer.parseInt("")` faisait planter le chargement à la deuxième ligne. D'où
les petites méthodes `entier()`, `reel()` et `booleen()` dans `LecteurCSV`.

```



---

## Répartition du travail (auto-évaluation)

| Membre | % contribution estimée | Ce sur quoi j'ai travaillé |
|--------|-----------------------|------------------------------|
| Fabiola |  50% | packages model, service, algorithmes, util ; génération du jeu de données ; interfaces partagées |
| Maxence | 50% | Packages controller, ressources fxml et css ; point d'entrée MainFx. |


---

## Notes pour le correcteur

[A COMPLETER (optionnel) : commentaires utiles pour le correcteur, ex. "Le benchmark est accessible via le menu Outils > Comparer les tris"]

---

## Captures d'écran (fortement recommandé)

[A COMPLETER (fortement recommandé) : mettre 2-3 captures d'écran de l'application dans un dossier `screenshots/` du dépôt et les référencer ici]

Exemple :
```markdown
### Écran principal
![Écran principal](screenshots/principal.png)

### Écran de benchmark
![Benchmark](screenshots/benchmark.png)
```

---

## Historique Git

**Nombre total de commits** : 
**Date du premier commit** : [A COMPLETER]
**Date du dernier commit** : [A COMPLETER]

Voir l'onglet **Insights > Contributors** de GitHub pour voir la contribution de chacun.

---

<!--
====================================================================
  CHECKLIST FINALE AVANT LA REMISE (a supprimer avant remise)
====================================================================

[ ] Tous les [A COMPLETER] ont ete remplaces par de vrais contenus
[ ] Tous les commentaires HTML <!-- ... --> ont ete supprimes
[x] Le lien GitHub est valide (teste dans un navigateur prive)
[x] Le depot est PUBLIC (pas Prive)
[x] Le README.md est bien present a la RACINE du depot
[ ] Le projet compile avec "mvn clean compile" sans erreur
[x] Le projet lance avec "mvn javafx:run" sans erreur
[x] Les donnees (CSV) sont dans src/main/resources/data/
[x] Le .gitignore exclut target/, .idea/, out/
[x] Chaque membre de l'equipe a des commits a son nom
[ ] Ce fichier README rempli a ete deposé sur Teams

DATE LIMITE : 11 septembre 2026, 23h59
====================================================================
-->
