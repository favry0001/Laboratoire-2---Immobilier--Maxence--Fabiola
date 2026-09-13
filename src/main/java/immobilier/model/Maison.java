package immobilier.model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Maison extends Propriete {

    private final int terrainPi2;
    private final int etages;
    private final boolean garage;

    public Maison(String id, TypeTransaction typeTransaction, double prix, int superficie,
                  int chambres, double sallesBain, String ville, String quartier,
                  int anneeConstruction, TypeCourtier typeCourtier, LocalDate datePubli,
                  String description, int terrainPi2, int etages, boolean garage) {
        super(id, typeTransaction, prix, superficie, chambres, sallesBain, ville, quartier,
                anneeConstruction, typeCourtier, datePubli, description);
        this.terrainPi2 = terrainPi2;
        this.etages = etages;
        this.garage = garage;
    }

    @Override
    public Map<String, String> attributsSpecifiques() {
        Map<String, String> attributs = new LinkedHashMap<>();
        attributs.put("Terrain", terrainPi2 + " pi²");
        attributs.put("Étages", String.valueOf(etages));
        attributs.put("Garage", garage ? "Oui" : "Non");
        return attributs;
    }

    @Override
    public String typeBien() {
        return "Maison";
    }
}