package immobilier;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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