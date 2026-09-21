# Catalogue immobilier — Laboratoire 3

**Cours :** 420-930-MA — Algorithmes et modèles de programmation  
**Session :** Été 2026, groupe 25604  
**Laboratoire :** 3 — Base de données et pattern DAO  
**Date de remise :** 20 septembre 2026, 23 h 59

## Équipe

| Nom complet | Adresse courriel | Contribution principale |
|---|---|---|
| Maxence Zeka | zkmance@gmail.com | Interface JavaFX, FXML, CSS, contrôleurs, formulaires et intégration du CRUD |
| Fabiola Sainvry | favry0001@gmail.com | Modèles, services, DAO PostgreSQL, scripts SQL et intégration JDBC |

## Présentation orale

**Journée choisie :** mercredi 23 septembre 2026

## Sujet choisi

**Numéro du sujet :** 5  
**Nom du sujet :** Immobilier

## Dépôt GitHub public

https://github.com/favry0001/Laboratoire-2---Immobilier--Maxence--Fabiola.git

## Fonctionnalités

- Chargement de 350 propriétés depuis PostgreSQL
- Connexion JDBC configurée dans un fichier externe
- DAO PostgreSQL utilisant des requêtes préparées
- Ajout, modification et suppression d’une propriété
- Confirmation avant suppression
- Validation des formulaires et messages d’erreur
- Recherche en temps réel par ville ou quartier
- Filtres combinables par transaction, type, prix, chambres et ville
- Pagination de 25 propriétés par page
- Tris Java codés à la main
- Benchmark du tri à bulles, du tri par insertion et du tri fusion
- Affichage détaillé adapté aux maisons et aux condos
- Calcul du prix au pied carré
- Gestion locale des favoris sans doublons
- Lecteur CSV conservé derrière l’interface `SourceDonnees`

## Prérequis

- JDK 21
- Maven 3.x
- PostgreSQL 17
- pgAdmin 4 ou un autre client PostgreSQL

## Installation de la base de données

1. Créer une base PostgreSQL nommée `immobilier`.
2. Ouvrir `schema.sql` dans cette base et l’exécuter.
3. Ouvrir `donnees.sql` et l’exécuter.
4. Vérifier l’importation avec :

```sql
SELECT COUNT(*) FROM propriete;
```

Le résultat attendu est `350`.

Le script `schema.sql` supprime puis recrée les tables. Pour reconstruire complètement la base, exécuter d’abord `schema.sql`, puis `donnees.sql`.

## Configuration de la connexion

Copier :

```text
src/main/resources/database.properties.example
```

vers :

```text
src/main/resources/database.properties
```

Modifier ensuite les valeurs selon l’installation locale :

```properties
db.url=jdbc:postgresql://localhost:5432/immobilier
db.utilisateur=postgres
db.motdepasse=votre_mot_de_passe
```

Le fichier `database.properties` est ignoré par Git afin de ne pas publier le mot de passe.

## Lancement

Depuis la racine du projet :

```bash
mvn clean javafx:run
```

Dans IntelliJ IDEA :

1. Ouvrir le projet.
2. Attendre le chargement Maven.
3. Ouvrir la fenêtre Maven.
4. Lancer `Plugins > javafx > javafx:run`.

## Structure du projet

```text
src/main/java/immobilier/
├── algorithmes/
│   ├── Algorithme.java
│   ├── Benchmark.java
│   ├── Comparateurs.java
│   ├── TriBulle.java
│   ├── TriFusion.java
│   └── TriInsertion.java
├── controller/
│   ├── ControleurBenchmark.java
│   ├── ControleurFormulairePropriete.java
│   └── ControleurPrincipal.java
├── dao/
│   ├── ConnexionBD.java
│   ├── ProprieteDao.java
│   └── ProprieteDaoPostgreSQL.java
├── model/
├── service/
├── util/
│   ├── LecteurCSV.java
│   └── SourceDonnees.java
└── MainFx.java

src/main/resources/
├── css/
├── data/
├── fxml/
└── database.properties.example
```

## Architecture et choix techniques

Le contrôleur JavaFX communique avec `ServiceCatalogue`. Le service reçoit une `SourceDonnees`. En fonctionnement normal, la source utilisée est `ProprieteDaoPostgreSQL`, qui isole les requêtes SQL et les opérations CRUD dans le package `dao`.

Les objets `Maison` et `Condo` sont construits à partir des résultats JDBC. Toutes les ressources JDBC sont fermées automatiquement avec `try-with-resources`. Les requêtes d’ajout, de modification et de suppression utilisent `PreparedStatement`.

Le lecteur CSV du Lab 2 reste utilisable. Pour tester la source CSV, remplacer temporairement dans les contrôleurs :

```java
new ServiceCatalogueImpl(new ProprieteDaoPostgreSQL())
```

par :

```java
new ServiceCatalogueImpl(new LecteurCSV())
```

Les opérations de modification sont volontairement indisponibles avec la source CSV.

## Algorithmes de tri

| Algorithme | Classe | Complexité |
|---|---|---|
| Tri à bulles | `TriBulle` | O(n²) |
| Tri par insertion | `TriInsertion` | O(n²) |
| Tri fusion | `TriFusion` | O(n log n) |

Les tris Java du Lab 2 sont conservés. PostgreSQL fournit les données, mais ne remplace pas les algorithmes de tri de l’application.

## Dépendances principales

- JavaFX Controls 21.0.4
- JavaFX FXML 21.0.4
- Pilote PostgreSQL JDBC 42.7.4

## Difficultés rencontrées

### Maxence

L’intégration du travail réalisé sur plusieurs branches Git a demandé de vérifier les versions des contrôleurs, des vues FXML et des services. L’ajout du CRUD a aussi nécessité de relier les résultats des formulaires JavaFX au service, puis d’actualiser le tableau après chaque opération.

### Fabiola

La mise en place du DAO a demandé de convertir chaque ligne du `ResultSet` vers le bon sous-type, `Maison` ou `Condo`, et de gérer les colonnes spécifiques qui peuvent être nulles. Il a également fallu centraliser la connexion et conserver le lecteur CSV derrière une abstraction commune.

## Répartition du travail

| Membre  | Contribution estimée | Travail principal |
|---------|---:|---|
| Fabiola | 50 % | Modèles, services, algorithmes, DAO, connexion JDBC et scripts SQL |
| Maxance | 50 % | Contrôleurs, FXML, CSS, formulaires, messages d’erreur et intégration du CRUD |

## Vérifications finales

- L’application démarre avec `mvn javafx:run`.
- Les 350 propriétés initiales sont chargées.
- Les recherches, filtres, tris et pages fonctionnent.
- Une propriété peut être ajoutée, modifiée et supprimée.
- Les changements sont toujours présents après le redémarrage.
- Une suppression demande une confirmation.
- Une erreur de connexion affiche un message à l’utilisateur.
- `database.properties` n’est pas suivi par Git.
- `database.properties.example`, `schema.sql` et `donnees.sql` sont présents dans le dépôt.
