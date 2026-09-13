package immobilier.model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Condo extends Propriete {

    private final int etageUnite;
    private final double fraisCondo;
    private final boolean ascenseur;

    public Condo(String id, TypeTransaction typeTransaction, double prix, int superficie,
                 int chambres, double sallesBain, String ville, String quartier,
                 int anneeConstruction, TypeCourtier typeCourtier, LocalDate datePubli,
                 String description, int etageUnite, double fraisCondo, boolean ascenseur) {
        super(id, typeTransaction, prix, superficie, chambres, sallesBain, ville, quartier,
                anneeConstruction, typeCourtier, datePubli, description);
        this.etageUnite = etageUnite;
        this.fraisCondo = fraisCondo;
        this.ascenseur = ascenseur;
    }

    @Override
    public Map<String, String> attributsSpecifiques() {
        Map<String, String> attributs = new LinkedHashMap<>();
        attributs.put("Étage de l'unité", String.valueOf(etageUnite));
        attributs.put("Frais de condo", fraisCondo + " $/mois");
        attributs.put("Ascenseur", ascenseur ? "Oui" : "Non");
        return attributs;
    }

    @Override
    public String typeBien() {
        return "Condo";
    }
}