package immobilier;

import immobilier.dao.ProprieteDaoPostgreSQL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        System.out.println(
                "Nombre de propriétés chargées : "
                        + new ProprieteDaoPostgreSQL().charger().size()
        );

        FXMLLoader loader = new FXMLLoader(
                MainFx.class.getResource("/fxml/vue-principale.fxml")
        );

        Scene scene = new Scene(
                loader.load(),
                1200,
                750
        );

        scene.getStylesheets().add(
                MainFx.class
                        .getResource("/css/style.css")
                        .toExternalForm()
        );

        stage.setTitle(
                "Catalogue Immobilier"
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}