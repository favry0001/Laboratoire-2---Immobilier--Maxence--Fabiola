package immobilier.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private TableView<Object> tableProprietes;

    @FXML
    private TableColumn<Object, String> colPrix;

    @FXML
    private TableColumn<Object, String> colType;

    @FXML
    private TableColumn<Object, String> colVille;

    @FXML
    private TableColumn<Object, String> colChambres;

    @FXML
    private TableColumn<Object, String> colSuperficie;

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

    @FXML
    public void initialize() {

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
                "1+",
                "2+",
                "3+",
                "4+",
                "5+"
        );

        comboVille.getItems().addAll(
                "Toutes",
                "Montréal",
                "Laval",
                "Québec",
                "Sherbrooke"
        );
    }
}