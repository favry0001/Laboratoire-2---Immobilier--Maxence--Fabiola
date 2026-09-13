package immobilier.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public abstract class Propriete {

    private final String id;
    private final TypeTransaction typeTransaction;
    private final double prix;
    private final int superficie;
    private final int chambres;
    private final double sallesBain;
    private final String ville;
    private final String quartier;
    private final int anneeConstruction;
    private final TypeCourtier typeCourtier;
    private final LocalDate datePubli;
    private final String description;

    public Propriete(String id, TypeTransaction typeTransaction, double prix, int superficie,
                     int chambres, double sallesBain, String ville, String quartier,
                     int anneeConstruction, TypeCourtier typeCourtier, LocalDate datePubli,
                     String description) {
        this.id = Objects.requireNonNull(id);
        this.typeTransaction = Objects.requireNonNull(typeTransaction);
        if (prix <= 0) throw new IllegalArgumentException("prix doit etre positif");
        this.prix = prix;
        this.superficie = superficie;
        this.chambres = chambres;
        this.sallesBain = sallesBain;
        this.ville = ville;
        this.quartier = quartier;
        this.anneeConstruction = anneeConstruction;
        this.typeCourtier = typeCourtier;
        this.datePubli = datePubli;
        this.description = description;
    }

    public double prixAuPiedCarre() {
        return superficie == 0 ? 0 : prix / superficie;
    }

    public abstract Map<String, String> attributsSpecifiques();

    public abstract String typeBien();
}