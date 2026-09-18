package immobilier.controller;

import immobilier.model.Condo;
import immobilier.model.Maison;
import immobilier.model.Propriete;
import immobilier.model.TypeCourtier;
import immobilier.model.TypeTransaction;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class ControleurFormulairePropriete {

    @FXML private Label labelTitre;

    @FXML private ComboBox<String> comboTypeBien;
    @FXML private ComboBox<TypeTransaction> comboTransaction;
    @FXML private ComboBox<TypeCourtier> comboCourtier;

    @FXML private TextField champPrix;
    @FXML private TextField champSuperficie;
    @FXML private TextField champChambres;
    @FXML private TextField champSallesBain;
    @FXML private TextField champVille;
    @FXML private TextField champQuartier;
    @FXML private TextField champAnnee;

    @FXML private DatePicker datePublication;
    @FXML private TextArea champDescription;

    @FXML private VBox blocMaison;
    @FXML private TextField champTerrain;
    @FXML private TextField champEtages;
    @FXML private CheckBox checkGarage;

    @FXML private VBox blocCondo;
    @FXML private TextField champEtageUnite;
    @FXML private TextField champFraisCondo;
    @FXML private CheckBox checkAscenseur;

    @FXML private Button btnAnnuler;
    @FXML private Button btnEnregistrer;

    private Propriete proprieteInitiale;
    private Propriete resultat;

    @FXML
    public void initialize() {
        comboTypeBien.getItems().addAll("Maison", "Condo");
        comboTransaction.getItems().addAll(TypeTransaction.values());
        comboCourtier.getItems().addAll(TypeCourtier.values());

        comboTypeBien.setValue("Maison");
        comboTransaction.setValue(TypeTransaction.VENTE);
        comboCourtier.setValue(TypeCourtier.COURTIER);
        datePublication.setValue(LocalDate.now());

        comboTypeBien.setOnAction(event -> afficherBlocType());
        btnAnnuler.setOnAction(event -> fermer());
        btnEnregistrer.setOnAction(event -> enregistrer());

        afficherBlocType();
    }

    private void afficherBlocType() {
        boolean maison = "Maison".equals(comboTypeBien.getValue());

        blocMaison.setVisible(maison);
        blocMaison.setManaged(maison);

        blocCondo.setVisible(!maison);
        blocCondo.setManaged(!maison);
    }

    public void preparerModification(Propriete propriete) {
        proprieteInitiale = propriete;
        labelTitre.setText("Modifier une propriété");

        comboTypeBien.setValue(propriete.typeBien());
        comboTransaction.setValue(propriete.getTypeTransaction());
        comboCourtier.setValue(propriete.getTypeCourtier());

        champPrix.setText(String.valueOf(propriete.getPrix()));
        champSuperficie.setText(String.valueOf(propriete.getSuperficie()));
        champChambres.setText(String.valueOf(propriete.getChambres()));
        champSallesBain.setText(String.valueOf(propriete.getSallesBain()));
        champVille.setText(propriete.getVille());
        champQuartier.setText(propriete.getQuartier());
        champAnnee.setText(
                String.valueOf(propriete.getAnneeConstruction())
        );

        datePublication.setValue(propriete.getDatePubli());
        champDescription.setText(propriete.getDescription());

        if (propriete instanceof Maison maison) {
            champTerrain.setText(String.valueOf(maison.getTerrainPi2()));
            champEtages.setText(String.valueOf(maison.getEtages()));
            checkGarage.setSelected(maison.isGarage());

        } else if (propriete instanceof Condo condo) {
            champEtageUnite.setText(String.valueOf(condo.getEtageUnite()));
            champFraisCondo.setText(String.valueOf(condo.getFraisCondo()));
            checkAscenseur.setSelected(condo.isAscenseur());
        }

        afficherBlocType();
    }

    private void enregistrer() {
        try {
            verifierChoix();

            double prix = lireDecimal(champPrix, "Prix", true);
            int superficie = lireEntier(champSuperficie, "Superficie", 1);
            int chambres = lireEntier(champChambres, "Chambres", 0);
            double sallesBain =
                    lireDecimal(champSallesBain, "Salles de bain", false);

            String ville = lireTexte(champVille, "Ville");
            String quartier = lireTexte(champQuartier, "Quartier");
            int annee = lireEntier(champAnnee, "Année de construction", 1);

            if (annee > LocalDate.now().getYear()) {
                throw new IllegalArgumentException(
                        "L’année de construction ne peut pas être dans le futur."
                );
            }

            // Identifiant provisoire, à adapter au DAO lors de l'intégration.
            String id = proprieteInitiale == null
                    ? UUID.randomUUID().toString()
                    : proprieteInitiale.getId();

            TypeTransaction transaction = comboTransaction.getValue();
            TypeCourtier courtier = comboCourtier.getValue();
            LocalDate date = datePublication.getValue();
            String description = champDescription.getText().trim();

            if ("Maison".equals(comboTypeBien.getValue())) {
                int terrain = lireEntier(champTerrain, "Terrain", 0);
                int etages = lireEntier(champEtages, "Nombre d’étages", 1);

                resultat = new Maison(
                        id, transaction, prix, superficie, chambres,
                        sallesBain, ville, quartier, annee, courtier,
                        date, description, terrain, etages,
                        checkGarage.isSelected()
                );

            } else {
                int etage = lireEntier(
                        champEtageUnite, "Étage de l’unité", 0
                );

                double frais = lireDecimal(
                        champFraisCondo, "Frais de condo", false
                );

                resultat = new Condo(
                        id, transaction, prix, superficie, chambres,
                        sallesBain, ville, quartier, annee, courtier,
                        date, description, etage, frais,
                        checkAscenseur.isSelected()
                );
            }

            fermer();

        } catch (IllegalArgumentException e) {
            afficherErreur(e.getMessage());
        }
    }

    private void verifierChoix() {
        if (comboTypeBien.getValue() == null
                || comboTransaction.getValue() == null
                || comboCourtier.getValue() == null) {
            throw new IllegalArgumentException(
                    "Sélectionne le type de bien, la transaction "
                            + "et le type de vendeur."
            );
        }

        try {
            datePublication.commitValue();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(
                    "La date de publication est invalide."
            );
        }

        if (datePublication.getValue() == null) {
            throw new IllegalArgumentException(
                    "La date de publication est obligatoire."
            );
        }
    }

    private String lireTexte(TextField champ, String nom) {
        String texte = champ.getText().trim();

        if (texte.isEmpty()) {
            throw new IllegalArgumentException(
                    "Le champ « " + nom + " » est obligatoire."
            );
        }

        return texte;
    }

    private int lireEntier(TextField champ, String nom, int minimum) {
        String texte = lireTexte(champ, nom);
        int valeur;

        try {
            valeur = Integer.parseInt(texte);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Le champ « " + nom + " » doit être un nombre entier."
            );
        }

        if (valeur < minimum) {
            throw new IllegalArgumentException(
                    "Le champ « " + nom + " » doit être au moins "
                            + minimum + "."
            );
        }

        return valeur;
    }

    private double lireDecimal(
            TextField champ, String nom, boolean strictementPositif
    ) {
        String texte = lireTexte(champ, nom).replace(',', '.');
        double valeur;

        try {
            valeur = Double.parseDouble(texte);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Le champ « " + nom + " » doit être numérique."
            );
        }

        if (!Double.isFinite(valeur)) {
            throw new IllegalArgumentException(
                    "Le champ « " + nom + " » contient une valeur invalide."
            );
        }

        if (strictementPositif && valeur <= 0) {
            throw new IllegalArgumentException(
                    "Le champ « " + nom + " » doit être supérieur à zéro."
            );
        }

        if (!strictementPositif && valeur < 0) {
            throw new IllegalArgumentException(
                    "Le champ « " + nom + " » doit être positif ou nul."
            );
        }

        return valeur;
    }

    private void afficherErreur(String message) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.initOwner(btnEnregistrer.getScene().getWindow());
        alerte.setTitle("Saisie invalide");
        alerte.setHeaderText("Vérifie les informations");
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    public Optional<Propriete> getResultat() {
        return Optional.ofNullable(resultat);
    }

    private void fermer() {
        Stage fenetre = (Stage) btnAnnuler.getScene().getWindow();
        fenetre.close();
    }
}