package immobilier.controller;

import immobilier.model.Propriete;
import immobilier.model.TypeTransaction;
import immobilier.util.LecteurCSV;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControleurPrincipal {

    @FXML
    private TextField champRecherche;

    @FXML
    private ComboBox<String> comboTri;

    @FXML
    private ComboBox<String> comboTransaction;

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private TextField champPrixMax;

    @FXML
    private ComboBox<String> comboChambres;

    @FXML
    private TextField champSuperficieMin;

    @FXML
    private ComboBox<String> comboVille;

    @FXML
    private TextField champAnneeMin;

    @FXML
    private TableView<Propriete> tableProprietes;

    @FXML
    private TableColumn<Propriete, String> colPrix;

    @FXML
    private TableColumn<Propriete, String> colType;

    @FXML
    private TableColumn<Propriete, String> colVille;

    @FXML
    private TableColumn<Propriete, String> colChambres;

    @FXML
    private TableColumn<Propriete, String> colSuperficie;

    @FXML
    private Button btnPrecedent;

    @FXML
    private Button btnSuivant;

    @FXML
    private Button btnFavoris;

    @FXML
    private Label labelPage;

    @FXML
    private Label labelPrix;

    @FXML
    private Label labelTransaction;

    @FXML
    private Label labelAdresse;

    @FXML
    private Label labelInfos;

    private final List<Propriete> toutesProprietes = new ArrayList<>();
    private final List<Propriete> proprietesFiltrees = new ArrayList<>();

    private int pageActuelle = 1;

    private static final int TAILLE_PAGE = 25;

    @FXML
    public void initialize() {

        preparerMenus();

        preparerTableau();

        chargerProprietes();

        preparerRechercheEtFiltres();

        preparerPagination();

        preparerSelection();

        appliquerFiltres();
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
                "Toutes",
                "Vente",
                "Location"
        );

        comboType.getItems().addAll(
                "Toutes",
                "Maison",
                "Condo"
        );

        comboChambres.getItems().addAll(
                "Toutes",
                "1+",
                "2+",
                "3+",
                "4+",
                "5+"
        );

        comboVille.getItems().add("Toutes");

        comboTransaction.setValue("Toutes");
        comboType.setValue("Toutes");
        comboChambres.setValue("Toutes");
        comboVille.setValue("Toutes");
    }

    private void preparerTableau() {

        colPrix.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.format("%.0f $", data.getValue().getPrix())
                )
        );

        colType.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().typeBien()
                )
        );

        colVille.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getVille()
                )
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

    private void chargerProprietes() {

        LecteurCSV lecteurCSV = new LecteurCSV();

        toutesProprietes.addAll(
                lecteurCSV.charger()
        );

        for (Propriete propriete : toutesProprietes) {

            String ville = propriete.getVille();

            if (!comboVille.getItems().contains(ville)) {
                comboVille.getItems().add(ville);
            }
        }

        System.out.println(
                toutesProprietes.size() + " propriétés chargées."
        );
    }

    private void preparerRechercheEtFiltres() {

        champRecherche.textProperty().addListener(
                (observable, ancien, nouveau) -> appliquerFiltres()
        );

        champPrixMax.textProperty().addListener(
                (observable, ancien, nouveau) -> appliquerFiltres()
        );

        champSuperficieMin.textProperty().addListener(
                (observable, ancien, nouveau) -> appliquerFiltres()
        );

        champAnneeMin.textProperty().addListener(
                (observable, ancien, nouveau) -> appliquerFiltres()
        );

        comboTransaction.setOnAction(event -> appliquerFiltres());

        comboType.setOnAction(event -> appliquerFiltres());

        comboChambres.setOnAction(event -> appliquerFiltres());

        comboVille.setOnAction(event -> appliquerFiltres());
    }

    private void appliquerFiltres() {

        proprietesFiltrees.clear();

        String recherche = champRecherche.getText()
                .trim()
                .toLowerCase();

        for (Propriete propriete : toutesProprietes) {

            if (!recherche.isEmpty()) {

                String texte = (
                        propriete.getVille()
                                + " "
                                + propriete.getQuartier()
                                + " "
                                + propriete.getDescription()
                ).toLowerCase();

                if (!texte.contains(recherche)) {
                    continue;
                }
            }

            if (!transactionValide(propriete)) {
                continue;
            }

            if (!typeValide(propriete)) {
                continue;
            }

            if (!prixValide(propriete)) {
                continue;
            }

            if (!chambresValides(propriete)) {
                continue;
            }

            if (!superficieValide(propriete)) {
                continue;
            }

            if (!villeValide(propriete)) {
                continue;
            }

            if (!anneeValide(propriete)) {
                continue;
            }

            proprietesFiltrees.add(propriete);
        }

        pageActuelle = 1;

        afficherPage();
    }

    private boolean transactionValide(Propriete propriete) {

        String choix = comboTransaction.getValue();

        if (choix == null || choix.equals("Toutes")) {
            return true;
        }

        if (choix.equals("Vente")) {
            return propriete.getTypeTransaction() == TypeTransaction.VENTE;
        }

        if (choix.equals("Location")) {
            return propriete.getTypeTransaction() == TypeTransaction.LOCATION;
        }

        return true;
    }

    private boolean typeValide(Propriete propriete) {

        String choix = comboType.getValue();

        if (choix == null || choix.equals("Toutes")) {
            return true;
        }

        return propriete.typeBien().equalsIgnoreCase(choix);
    }

    private boolean prixValide(Propriete propriete) {

        String texte = champPrixMax.getText().trim();

        if (texte.isEmpty()) {
            return true;
        }

        try {

            double prixMax = Double.parseDouble(texte);

            return propriete.getPrix() <= prixMax;

        } catch (NumberFormatException e) {

            return true;
        }
    }

    private boolean chambresValides(Propriete propriete) {

        String choix = comboChambres.getValue();

        if (choix == null || choix.equals("Toutes")) {
            return true;
        }

        int minimum = Integer.parseInt(
                choix.replace("+", "")
        );

        return propriete.getChambres() >= minimum;
    }

    private boolean superficieValide(Propriete propriete) {

        String texte = champSuperficieMin.getText().trim();

        if (texte.isEmpty()) {
            return true;
        }

        try {

            int superficieMin = Integer.parseInt(texte);

            return propriete.getSuperficie() >= superficieMin;

        } catch (NumberFormatException e) {

            return true;
        }
    }

    private boolean villeValide(Propriete propriete) {

        String choix = comboVille.getValue();

        if (choix == null || choix.equals("Toutes")) {
            return true;
        }

        return propriete.getVille().equalsIgnoreCase(choix);
    }

    private boolean anneeValide(Propriete propriete) {

        String texte = champAnneeMin.getText().trim();

        if (texte.isEmpty()) {
            return true;
        }

        try {

            int anneeMin = Integer.parseInt(texte);

            return propriete.getAnneeConstruction() >= anneeMin;

        } catch (NumberFormatException e) {

            return true;
        }
    }

    private void preparerPagination() {

        btnPrecedent.setOnAction(event -> {

            if (pageActuelle > 1) {

                pageActuelle--;

                afficherPage();
            }
        });

        btnSuivant.setOnAction(event -> {

            int nombrePages = nombrePages();

            if (pageActuelle < nombrePages) {

                pageActuelle++;

                afficherPage();
            }
        });
    }

    private void afficherPage() {

        int debut = (pageActuelle - 1) * TAILLE_PAGE;

        int fin = Math.min(
                debut + TAILLE_PAGE,
                proprietesFiltrees.size()
        );

        if (debut > proprietesFiltrees.size()) {
            debut = 0;
        }

        List<Propriete> page = new ArrayList<>();

        for (int i = debut; i < fin; i++) {
            page.add(proprietesFiltrees.get(i));
        }

        tableProprietes.setItems(
                FXCollections.observableArrayList(page)
        );

        int totalPages = nombrePages();

        labelPage.setText(
                "Page " + pageActuelle + " / " + totalPages
        );

        btnPrecedent.setDisable(pageActuelle <= 1);

        btnSuivant.setDisable(pageActuelle >= totalPages);
    }

    private int nombrePages() {

        if (proprietesFiltrees.isEmpty()) {
            return 1;
        }

        return (int) Math.ceil(
                (double) proprietesFiltrees.size() / TAILLE_PAGE
        );
    }

    private void preparerSelection() {

        tableProprietes.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, ancien, nouveau) -> {

                            if (nouveau != null) {
                                afficherDetails(nouveau);
                            }
                        }
                );
    }

    private void afficherDetails(Propriete propriete) {

        labelPrix.setText(
                "Prix : "
                        + String.format("%.0f $", propriete.getPrix())
        );

        labelTransaction.setText(
                "Transaction : "
                        + propriete.getTypeTransaction()
        );

        labelAdresse.setText(
                "Adresse : "
                        + propriete.getQuartier()
                        + ", "
                        + propriete.getVille()
        );

        String infos = "";

        infos += "Type : " + propriete.typeBien() + "\n";

        infos += "Chambres : "
                + propriete.getChambres()
                + "\n";

        infos += "Salles de bain : "
                + propriete.getSallesBain()
                + "\n";

        infos += "Superficie : "
                + propriete.getSuperficie()
                + " pi²\n";

        infos += "Année : "
                + propriete.getAnneeConstruction()
                + "\n";

        infos += "Courtier : "
                + propriete.getTypeCourtier()
                + "\n";

        infos += "Prix au pi² : "
                + String.format(
                "%.2f $",
                propriete.prixAuPiedCarre()
        )
                + "\n\n";

        for (Map.Entry<String, String> entree
                : propriete.attributsSpecifiques().entrySet()) {

            infos += entree.getKey()
                    + " : "
                    + entree.getValue()
                    + "\n";
        }

        infos += "\n"
                + propriete.getDescription();

        labelInfos.setText(infos);
    }
}