Catalogue immobilier — Laboratoire 3

Application JavaFX de consultation et de gestion d’annonces immobilières avec PostgreSQL, JDBC et le pattern DAO.

Équipe

Membre

Contribution principale

Maxence Zeka

Interface JavaFX, FXML, CSS, contrôleurs, formulaires et intégration du CRUD

Fabiola Sainvry

Modèles, services, DAO PostgreSQL, scripts SQL et intégration JDBC

Fonctionnalités

Chargement de 350 propriétés depuis PostgreSQL

Recherche par ville ou quartier

Filtres combinables par transaction, type, prix, chambres et ville

Pagination de 25 propriétés par page

Tris Java codés à la main

Benchmark du tri à bulles, du tri par insertion et du tri fusion

Affichage détaillé des maisons et des condos

Ajout, modification et suppression avec confirmation

Validation des formulaires et messages d’erreur

Gestion locale des favoris

Lecteur CSV conservé derrière l’interface SourceDonnees

Prérequis

JDK 21

Maven 3.x

PostgreSQL 17

pgAdmin 4 ou un autre client PostgreSQL

Installation de la base de données

Créer une base PostgreSQL nommée immobilier.

Ouvrir schema.sql dans la base immobilier et l’exécuter.

Ouvrir donnees.sql et l’exécuter.

Vérifier l’importation :

SELECT COUNT(*) FROM propriete;

Le résultat attendu est 350.

Configuration de la connexion

Copier :

src/main/resources/database.properties.example

vers :

src/main/resources/database.properties

Adapter ensuite les valeurs :

db.url=jdbc:postgresql://localhost:5432/immobilier
db.utilisateur=postgres
db.motdepasse=votre_mot_de_passe

Le fichier database.properties est ignoré par Git afin de ne pas publier le mot de passe.

Lancement

Depuis la racine du projet :

mvn clean javafx:run

Dans IntelliJ IDEA, ouvrir la fenêtre Maven puis lancer :

Plugins > javafx > javafx:run

Architecture

src/main/java/immobilier/
├── algorithmes
├── controller
├── dao
├── model
├── service
├── util
└── MainFx.java

src/main/resources/
├── css
├── data
├── fxml
└── database.properties.example

Le contrôleur utilise ServiceCatalogue. Le service reçoit une SourceDonnees. En fonctionnement normal, cette source est ProprieteDaoPostgreSQL, qui encapsule les requêtes JDBC et les opérations CRUD.

Le lecteur CSV reste disponible. Pour vérifier le chargement CSV, remplacer temporairement dans les contrôleurs :

new ServiceCatalogueImpl(new ProprieteDaoPostgreSQL())

par :

new ServiceCatalogueImpl(new LecteurCSV())

Les modifications sont volontairement indisponibles avec la source CSV.

Scripts fournis

schema.sql recrée les tables quartier et propriete.

donnees.sql insère les quartiers et les 350 propriétés.

Pour reconstruire complètement la base, exécuter schema.sql, puis donnees.sql.

Dépendances principales

JavaFX Controls 21.0.4

JavaFX FXML 21.0.4

Pilote PostgreSQL JDBC 42.7.4

Dépôt

https://github.com/favry0001/Laboratoire-2---Immobilier--Maxence--Fabiola
