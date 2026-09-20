package immobilier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainFx.class.getResource("/fxml/vue-principale.fxml")
            );

            Scene scene = new Scene(loader.load(), 1200, 750);

            scene.getStylesheets().add(
                    MainFx.class
                            .getResource("/css/style.css")
                            .toExternalForm()
            );

            stage.setTitle("Catalogue Immobilier");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            afficherErreur(e);
        }
    }

    private void afficherErreur(Throwable erreur) {
        Throwable cause = erreur;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        String message = cause.getMessage();

        if (message == null || message.isBlank()) {
            message = "Une erreur inattendue s’est produite.";
        }

        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle("Erreur de démarrage");
        alerte.setHeaderText("Impossible de démarrer l’application");
        alerte.setContentText(
                message
                        + "\n\nVérifie PostgreSQL et le fichier database.properties."
        );
        alerte.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
