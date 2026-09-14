package immobilier.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Favoris {

    private final Map<String, Propriete> proprietesFavorites =
            new LinkedHashMap<>();

    public void ajouter(Propriete propriete) {

        if (propriete == null) {
            return;
        }

        proprietesFavorites.put(
                propriete.getId(),
                propriete
        );
    }

    public void retirer(Propriete propriete) {

        if (propriete == null) {
            return;
        }

        proprietesFavorites.remove(
                propriete.getId()
        );
    }

    public void basculer(Propriete propriete) {

        if (propriete == null) {
            return;
        }

        if (contient(propriete)) {
            retirer(propriete);
        } else {
            ajouter(propriete);
        }
    }

    public boolean contient(Propriete propriete) {

        if (propriete == null) {
            return false;
        }

        return proprietesFavorites.containsKey(
                propriete.getId()
        );
    }

    public List<Propriete> tous() {

        return new ArrayList<>(
                proprietesFavorites.values()
        );
    }

    public int taille() {

        return proprietesFavorites.size();
    }

    public void vider() {

        proprietesFavorites.clear();
    }
}