package immobilier.util;

import immobilier.model.Condo;
import immobilier.model.Maison;
import immobilier.model.Propriete;
import immobilier.model.TypeCourtier;
import immobilier.model.TypeTransaction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LecteurCSV implements SourceDonnees {

    private static final String CHEMIN = "/data/proprietes.csv";

    @Override
    public List<Propriete> charger() {
        List<Propriete> proprietes = new ArrayList<>();

        try (BufferedReader lecteur = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(CHEMIN), StandardCharsets.UTF_8))) {

            lecteur.readLine();
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.isBlank()) continue;
                proprietes.add(construire(ligne.split(",")));
            }

        } catch (Exception e) {
            throw new RuntimeException("Echec du chargement du fichier " + CHEMIN, e);
        }
        return proprietes;
    }

    private Propriete construire(String[] c) {
        String id = c[0].trim();
        TypeTransaction transaction = TypeTransaction.valueOf(c[1].trim());
        double prix = Double.parseDouble(c[3].trim());
        int superficie = Integer.parseInt(c[4].trim());
        int chambres = Integer.parseInt(c[5].trim());
        double sallesBain = Double.parseDouble(c[6].trim());
        String ville = c[7].trim();
        String quartier = c[8].trim();
        int annee = Integer.parseInt(c[9].trim());
        TypeCourtier courtier = TypeCourtier.valueOf(c[10].trim());
        LocalDate datePubli = LocalDate.parse(c[11].trim());
        String description = c[18].trim();

        if ("MAISON".equals(c[2].trim())) {
            return new Maison(id, transaction, prix, superficie, chambres, sallesBain,
                    ville, quartier, annee, courtier, datePubli, description,
                    entier(c[12]), entier(c[13]), booleen(c[14]));
        }
        return new Condo(id, transaction, prix, superficie, chambres, sallesBain,
                ville, quartier, annee, courtier, datePubli, description,
                entier(c[15]), reel(c[16]), booleen(c[17]));
    }

    private static int entier(String valeur) {
        return (valeur == null || valeur.isBlank()) ? 0 : Integer.parseInt(valeur.trim());
    }

    private static double reel(String valeur) {
        return (valeur == null || valeur.isBlank()) ? 0 : Double.parseDouble(valeur.trim());
    }

    private static boolean booleen(String valeur) {
        return valeur != null && Boolean.parseBoolean(valeur.trim());
    }
}