package immobilier;

import immobilier.util.LecteurCSV;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println(new LecteurCSV().charger().size() + " proprietes chargees");

        Label label = new Label("Catalogue immobilier - Lab 2");
        Scene scene = new Scene(label, 600, 400);
        stage.setTitle("Immobilier");
        stage.setScene(scene);
        System.out.println(new LecteurCSV().charger().size() + " proprietes chargees");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}