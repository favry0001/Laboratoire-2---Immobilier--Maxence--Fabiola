package immobilier;

import immobilier.algorithmes.Comparateurs;
import immobilier.algorithmes.TriBulle;
import immobilier.algorithmes.TriFusion;
import immobilier.algorithmes.TriInsertion;
import immobilier.model.Propriete;
import immobilier.util.LecteurCSV;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) {




        Label label = new Label("Catalogue immobilier - Lab 2");
        Scene scene = new Scene(label, 600, 400);
        stage.setTitle("Immobilier");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}