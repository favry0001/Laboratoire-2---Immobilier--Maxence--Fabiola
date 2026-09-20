package immobilier.controller;

import immobilier.algorithmes.Benchmark;
import immobilier.algorithmes.Comparateurs;
import immobilier.algorithmes.TriBulle;
import immobilier.algorithmes.TriFusion;
import immobilier.algorithmes.TriInsertion;
import immobilier.dao.ProprieteDaoPostgreSQL;
import immobilier.model.Propriete;
import immobilier.service.ServiceCatalogue;
import immobilier.service.ServiceCatalogueImpl;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ControleurBenchmark {

    @FXML
    private TableView<ResultatBenchmark> tableBenchmark;

    @FXML
    private TableColumn<ResultatBenchmark, String> colAlgorithme;

    @FXML
    private TableColumn<ResultatBenchmark, String> colTemps;

    @FXML
    private TableColumn<ResultatBenchmark, String> colElements;

    @FXML
    private Button btnLancer;

    @FXML
    private Button btnFermer;

    private final ServiceCatalogue service =
            new ServiceCatalogueImpl(
                    new ProprieteDaoPostgreSQL()
            );

    private final Benchmark<Propriete> benchmark =
            new Benchmark<>();

    @FXML
    public void initialize() {
        preparerTableau();

        btnLancer.setOnAction(
                event -> lancerBenchmark()
        );

        btnFermer.setOnAction(
                event -> fermerFenetre()
        );
    }

    private void preparerTableau() {
        colAlgorithme.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().algorithme()
                )
        );

        colTemps.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().temps()
                )
        );

        colElements.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.valueOf(
                                data.getValue().elements()
                        )
                )
        );
    }

    private void lancerBenchmark() {
        List<Propriete> donnees =
                service.toutesLesDonnees();

        List<ResultatBenchmark> resultats =
                new ArrayList<>();

        TriBulle<Propriete> triBulle =
                new TriBulle<>();

        long tempsBulle = benchmark.mesurer(
                triBulle,
                donnees,
                Comparateurs.parPrixCroissant()
        );

        resultats.add(
                new ResultatBenchmark(
                        triBulle.nom()
                                + " - "
                                + triBulle.complexiteTheorique(),
                        tempsBulle + " ms",
                        donnees.size()
                )
        );

        TriInsertion<Propriete> triInsertion =
                new TriInsertion<>();

        long tempsInsertion = benchmark.mesurer(
                triInsertion,
                donnees,
                Comparateurs.parPrixCroissant()
        );

        resultats.add(
                new ResultatBenchmark(
                        triInsertion.nom()
                                + " - "
                                + triInsertion.complexiteTheorique(),
                        tempsInsertion + " ms",
                        donnees.size()
                )
        );

        TriFusion<Propriete> triFusion =
                new TriFusion<>();

        long tempsFusion = benchmark.mesurer(
                triFusion,
                donnees,
                Comparateurs.parPrixCroissant()
        );

        resultats.add(
                new ResultatBenchmark(
                        triFusion.nom()
                                + " - "
                                + triFusion.complexiteTheorique(),
                        tempsFusion + " ms",
                        donnees.size()
                )
        );

        tableBenchmark.getItems().setAll(resultats);
    }

    private void fermerFenetre() {
        Stage stage =
                (Stage) btnFermer
                        .getScene()
                        .getWindow();

        stage.close();
    }

    public record ResultatBenchmark(
            String algorithme,
            String temps,
            int elements
    ) {
    }
}