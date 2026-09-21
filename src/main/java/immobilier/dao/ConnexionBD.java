package immobilier.dao;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnexionBD {

    private static final String URL;
    private static final String UTILISATEUR;
    private static final String MOT_DE_PASSE;

    static {
        Properties props = new Properties();
        try (InputStream is = ConnexionBD.class.getResourceAsStream("/database.properties")) {
            if (is == null) {
                throw new RuntimeException("database.properties introuvable dans le classpath");
            }
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture database.properties", e);
        }
        URL = props.getProperty("db.url");
        UTILISATEUR = props.getProperty("db.utilisateur");
        MOT_DE_PASSE = props.getProperty("db.motdepasse");
    }

    private ConnexionBD() { }
    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
    }
}