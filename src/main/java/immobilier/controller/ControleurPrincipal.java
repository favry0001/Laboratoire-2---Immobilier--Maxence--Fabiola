package immobilier.controller;

import immobilier.algorithmes.Comparateurs;
import immobilier.algorithmes.TriFusion;
import immobilier.model.Favoris;
import immobilier.model.Propriete;
import immobilier.model.TypeTransaction;
import immobilier.service.CritereFiltre;
import immobilier.service.ServiceCatalogue;
import immobilier.service.ServiceCatalogueImpl;
import immobilier.dao.ProprieteDaoPostgreSQL;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class ControleurPrincipal {

    @FXML private TextField champRecherche;
    @FXML private TextField champPrixMax;

    @FXML private ComboBox<String> comboTri;
    @FXML private ComboBox<String> comboTransaction;
    @FXML private ComboBox<String> comboType;
    @FXML private ComboBox<String> comboChambres;
    @FXML private ComboBox<String> comboVille;

    @FXML private TableView<Propriete> tableProprietes;
    @FXML private TableColumn<Propriete, String> colPrix;
    @FXML private TableColumn<Propriete, String> colType;
    @FXML private TableColumn<Propriete, String> colVille;
    @FXML private TableColumn<Propriete, String> colChambres;
    @FXML private TableColumn<Propriete, String> colSuperficie;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnPrecedent;
    @FXML private Button btnSuivant;
    @FXML private Button btnFavoris;
    @FXML private Button btnBenchmark;

    @FXML private Label labelPage;
    @FXML private Label labelPrix;
    @FXML private Label labelTransaction;
    @FXML private Label labelAdresse;
    @FXML private Label labelInfos;

    private final ServiceCatalogue service =
            new ServiceCatalogueImpl(new ProprieteDaoPostgreSQL());

    private final Favoris favoris = new Favoris();

    @FXML
    public void initialize() {
        preparerMenus();
        preparerTableau();
        preparerRechercheEtFiltres();
        preparerPagination();
        preparerSelection();
        preparerFavoris();
        preparerFormulaire();
        preparerSuppression();
        afficherPage();
    }

    private void preparerMenus() {
        comboTri.getItems().addAll(
                "Prix croissant",
                "Prix décroissant",
                "Superficie décroissante",
                "Année de construction",
                "Date de publication",
                "Prix au pied carré"
        );

        comboTransaction.getItems().addAll(
                "Transactions", "Vente", "Location"
        );

        comboType.getItems().addAll(
                "Type", "Maison", "Condo"
        );

        comboChambres.getItems().addAll(
                "Nbr chambres", "1+", "2+", "3+", "4+", "5+"
        );

        actualiserVilles();

        comboTransaction.setValue("Transactions");
        comboType.setValue("Type");
        comboChambres.setValue("Nbr chambres");
        comboVille.setValue("Villes");
    }

    private void preparerTableau() {
        colPrix.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.format("%.0f $", data.getValue().getPrix())
                )
        );

        colType.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().typeBien())
        );

        colVille.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getVille())
        );

        colChambres.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.valueOf(data.getValue().getChambres())
                )
        );

        colSuperficie.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getSuperficie() + " pi²"
                )
        );
    }

    private void preparerRechercheEtFiltres() {
        champRecherche.textProperty().addListener(
                (observable, ancien, nouveau) -> {
                    service.rechercher(nouveau);
                    afficherPage();
                }
        );

        champPrixMax.textProperty().addListener(
                (observable, ancien, nouveau) -> appliquerFiltres()
        );

        comboTransaction.setOnAction(event -> appliquerFiltres());
        comboType.setOnAction(event -> appliquerFiltres());
        comboChambres.setOnAction(event -> appliquerFiltres());
        comboVille.setOnAction(event -> appliquerFiltres());
        comboTri.setOnAction(event -> appliquerTri());
    }

    private void appliquerFiltres() {
        CritereFiltre criteres = new CritereFiltre();

        String transaction = comboTransaction.getValue();

        if ("Vente".equals(transaction)) {
            criteres.setTransaction(TypeTransaction.VENTE);
        } else if ("Location".equals(transaction)) {
            criteres.setTransaction(TypeTransaction.LOCATION);
        }

        String type = comboType.getValue();

        if (type != null && !"Toutes".equals(type)) {
            criteres.setTypeBien(type);
        }

        String prix = champPrixMax.getText().trim();

        if (!prix.isEmpty()) {
            try {
                criteres.setPrixMax(Double.parseDouble(prix));
            } catch (NumberFormatException ignored) {
            }
        }

        String chambres = comboChambres.getValue();

        if (chambres != null && !"Toutes".equals(chambres)) {
            criteres.setChambresMin(
                    Integer.parseInt(chambres.replace("+", ""))
            );
        }

        String ville = comboVille.getValue();

        if (ville != null && !"Toutes".equals(ville)) {
            criteres.setVille(ville);
        }

        service.appliquerFiltres(criteres);
        afficherPage();
    }

    private void appliquerTri() {
        String choix = comboTri.getValue();

        if (choix == null) {
            return;
        }

        Comparator<Propriete> comparateur;

        switch (choix) {
            case "Prix croissant" ->
                    comparateur = Comparateurs.parPrixCroissant();

            case "Prix décroissant" ->
                    comparateur = Comparateurs.parPrixDecroissant();

            case "Superficie décroissante" ->
                    comparateur = Comparateurs.parSuperficieDecroissante();

            case "Année de construction" ->
                    comparateur = Comparator.comparingInt(
                            Propriete::getAnneeConstruction
                    );

            case "Date de publication" ->
                    comparateur = Comparator.comparing(
                            Propriete::getDatePubli
                    );

            case "Prix au pied carré" ->
                    comparateur = Comparateurs.parPrixAuPiedCarre();

            default -> {
                return;
            }
        }

        service.trier(comparateur, new TriFusion<>());
        afficherPage();
    }

    private void preparerPagination() {
        btnPrecedent.setOnAction(event -> {
            service.pagePrecedente();
            afficherPage();
        });

        btnSuivant.setOnAction(event -> {
            service.pageSuivante();
            afficherPage();
        });
    }

    private void afficherPage() {
        tableProprietes.setItems(
                FXCollections.observableArrayList(service.pageCourante())
        );

        labelPage.setText(
                "Page " + service.numeroPage()
                        + " / " + service.nombrePages()
        );

        btnPrecedent.setDisable(service.numeroPage() <= 1);
        btnSuivant.setDisable(
                service.numeroPage() >= service.nombrePages()
        );

        mettreAJourSelection(
                tableProprietes.getSelectionModel().getSelectedItem()
        );
    }

    private void preparerSelection() {
        mettreAJourSelection(null);

        tableProprietes.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, ancien, nouveau) ->
                                mettreAJourSelection(nouveau)
                );
    }

    private void mettreAJourSelection(Propriete propriete) {
        boolean aucuneSelection = propriete == null;

        btnModifier.setDisable(aucuneSelection);
        btnSupprimer.setDisable(aucuneSelection);
        btnFavoris.setDisable(aucuneSelection);

        if (aucuneSelection) {
            effacerDetails();
        } else {
            afficherDetails(propriete);
            mettreAJourBoutonFavoris(propriete);
        }
    }

    private void effacerDetails() {
        labelPrix.setText("Prix :");
        labelTransaction.setText("Transaction :");
        labelAdresse.setText("Adresse :");
        labelInfos.setText("");
        btnFavoris.setText("Ajouter aux favoris");
    }

    private void preparerFavoris() {
        btnFavoris.setOnAction(event -> {
            Propriete propriete =
                    tableProprietes.getSelectionModel().getSelectedItem();

            if (propriete == null) {
                return;
            }

            favoris.basculer(propriete);
            mettreAJourBoutonFavoris(propriete);
        });
    }

    private void mettreAJourBoutonFavoris(Propriete propriete) {
        btnFavoris.setDisable(false);

        if (favoris.contient(propriete)) {
            btnFavoris.setText("Retirer des favoris");
        } else {
            btnFavoris.setText("Ajouter aux favoris");
        }
    }

    private void afficherDetails(Propriete propriete) {
        labelPrix.setText(
                "Prix : " + String.format("%.0f $", propriete.getPrix())
        );

        labelTransaction.setText(
                "Transaction : " + propriete.getTypeTransaction()
        );

        labelAdresse.setText(
                "Adresse : " + propriete.getQuartier()
                        + ", " + propriete.getVille()
        );

        StringBuilder infos = new StringBuilder();

        infos.append("Type : ")
                .append(propriete.typeBien())
                .append("\n");

        infos.append("Chambres : ")
                .append(propriete.getChambres())
                .append("\n");

        infos.append("Salles de bain : ")
                .append(propriete.getSallesBain())
                .append("\n");

        infos.append("Superficie : ")
                .append(propriete.getSuperficie())
                .append(" pi²\n");

        infos.append("Année : ")
                .append(propriete.getAnneeConstruction())
                .append("\n");

        infos.append("Courtier : ")
                .append(propriete.getTypeCourtier())
                .append("\n");

        infos.append("Prix au pi² : ")
                .append(String.format(
                        "%.2f $",
                        propriete.prixAuPiedCarre()
                ))
                .append("\n\n");

        for (Map.Entry<String, String> entree
                : propriete.attributsSpecifiques().entrySet()) {
            infos.append(entree.getKey())
                    .append(" : ")
                    .append(entree.getValue())
                    .append("\n");
        }

        infos.append("\n").append(propriete.getDescription());
        labelInfos.setText(infos.toString());
    }

    private void preparerFormulaire() {
        btnAjouter.setOnAction(event -> ajouterPropriete());

        btnModifier.setOnAction(event -> {
            Propriete selection =
                    tableProprietes.getSelectionModel().getSelectedItem();

            if (selection != null) {
                modifierPropriete(selection);
            }
        });
    }

    private void ajouterPropriete() {
        Optional<Propriete> resultat = ouvrirFormulaire(null);

        if (resultat.isEmpty()) {
            return;
        }

        try {
            service.ajouter(resultat.get());
            actualiserVilles();
            afficherPage();
            afficherInformation(
                    "Ajout réussi",
                    "La propriété a été ajoutée à la base de données."
            );
        } catch (RuntimeException e) {
            afficherErreur(
                    "Impossible d’ajouter la propriété",
                    obtenirMessageErreur(e)
            );
        }
    }

    private void modifierPropriete(Propriete propriete) {
        Optional<Propriete> resultat = ouvrirFormulaire(propriete);

        if (resultat.isEmpty()) {
            return;
        }

        try {
            boolean modifiee = service.modifier(resultat.get());

            if (!modifiee) {
                afficherErreur(
                        "Modification impossible",
                        "La propriété n’existe plus dans la base de données."
                );
                return;
            }

            actualiserVilles();
            afficherPage();
            afficherInformation(
                    "Modification réussie",
                    "La propriété a été modifiée dans la base de données."
            );
        } catch (RuntimeException e) {
            afficherErreur(
                    "Impossible de modifier la propriété",
                    obtenirMessageErreur(e)
            );
        }
    }

    private Optional<Propriete> ouvrirFormulaire(
            Propriete propriete
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/vue-formulaire-propriete.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass()
                            .getResource("/css/style.css")
                            .toExternalForm()
            );

            ControleurFormulairePropriete controleur =
                    loader.getController();

            if (propriete != null) {
                controleur.preparerModification(propriete);
            }

            Stage fenetre = new Stage();

            fenetre.setTitle(
                    propriete == null
                            ? "Ajouter une propriété"
                            : "Modifier une propriété"
            );

            fenetre.initOwner(
                    tableProprietes.getScene().getWindow()
            );
            fenetre.initModality(Modality.WINDOW_MODAL);
            fenetre.setScene(scene);
            fenetre.showAndWait();

            return controleur.getResultat();

        } catch (IOException | RuntimeException e) {
            afficherErreur(
                    "Impossible d’ouvrir le formulaire",
                    "Détail : " + e.getMessage()
            );

            return Optional.empty();
        }
    }

    private void preparerSuppression() {
        btnSupprimer.setOnAction(
                event -> demanderSuppression()
        );
    }

    private void demanderSuppression() {
        Propriete selection =
                tableProprietes.getSelectionModel().getSelectedItem();

        if (selection == null) {
            return;
        }

        ButtonType boutonSupprimer = new ButtonType(
                "Supprimer",
                ButtonBar.ButtonData.OK_DONE
        );

        ButtonType boutonAnnuler = new ButtonType(
                "Annuler",
                ButtonBar.ButtonData.CANCEL_CLOSE
        );

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "",
                boutonSupprimer,
                boutonAnnuler
        );

        confirmation.initOwner(
                tableProprietes.getScene().getWindow()
        );

        confirmation.setTitle("Confirmer la suppression");
        confirmation.setHeaderText(
                "Supprimer cette propriété ?"
        );

        confirmation.setContentText(
                selection.typeBien()
                        + " à " + selection.getVille()
                        + "\nQuartier : "
                        + selection.getQuartier()
                        + "\nPrix : "
                        + String.format(
                        "%.0f $",
                        selection.getPrix()
                )
                        + "\n\nCette action sera définitive."
        );

        Optional<ButtonType> reponse =
                confirmation.showAndWait();

        if (reponse.isEmpty()
                || reponse.get() != boutonSupprimer) {
            return;
        }

        try {
            boolean supprimee = service.supprimer(selection.getId());

            if (!supprimee) {
                afficherErreur(
                        "Suppression impossible",
                        "La propriété n’existe plus dans la base de données."
                );
                return;
            }

            favoris.retirer(selection);
            actualiserVilles();
            afficherPage();
            afficherInformation(
                    "Suppression réussie",
                    "La propriété a été supprimée de la base de données."
            );
        } catch (RuntimeException e) {
            afficherErreur(
                    "Impossible de supprimer la propriété",
                    obtenirMessageErreur(e)
            );
        }
    }

    @FXML
    private void ouvrirBenchmark() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/vue-benchmark.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass()
                            .getResource("/css/style.css")
                            .toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Benchmark des algorithmes");
            stage.setScene(scene);
            stage.show();

        } catch (IOException | RuntimeException e) {
            afficherErreur(
                    "Impossible d’ouvrir le benchmark",
                    "Détail : " + e.getMessage()
            );
        }
    }

    private void afficherErreur(
            String titre,
            String message
    ) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);

        alerte.initOwner(
                tableProprietes.getScene().getWindow()
        );

        alerte.setTitle("Erreur");
        alerte.setHeaderText(titre);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    private void afficherInformation(String titre, String message) {
        Alert alerte = new Alert(Alert.AlertType.INFORMATION);

        alerte.initOwner(
                tableProprietes.getScene().getWindow()
        );

        alerte.setTitle("Catalogue immobilier");
        alerte.setHeaderText(titre);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    private void actualiserVilles() {
        String selection = comboVille.getValue();

        comboVille.getItems().clear();
        comboVille.getItems().add("Villes");

        for (Propriete propriete : service.toutesLesDonnees()) {
            String ville = propriete.getVille();

            if (!comboVille.getItems().contains(ville)) {
                comboVille.getItems().add(ville);
            }
        }

        if (selection != null && comboVille.getItems().contains(selection)) {
            comboVille.setValue(selection);
        } else {
            comboVille.setValue("Villes");
        }
    }

    private String obtenirMessageErreur(Throwable erreur) {
        Throwable cause = erreur;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        if (cause.getMessage() == null || cause.getMessage().isBlank()) {
            return "Une erreur inattendue s’est produite.";
        }

        return cause.getMessage();
    }
}
