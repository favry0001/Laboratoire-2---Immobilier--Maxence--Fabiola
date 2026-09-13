package immobilier.algorithmes;

import immobilier.model.Propriete;
import java.util.Comparator;

public final class Comparateurs {

    private Comparateurs() { }

    public static Comparator<Propriete> parPrixCroissant() {
        return (a, b) -> Double.compare(a.getPrix(), b.getPrix());
    }

    public static Comparator<Propriete> parPrixDecroissant() {
        return (a, b) -> Double.compare(b.getPrix(), a.getPrix());
    }

    public static Comparator<Propriete> parSuperficieDecroissante() {
        return (a, b) -> Integer.compare(b.getSuperficie(), a.getSuperficie());
    }

    public static Comparator<Propriete> parPrixAuPiedCarre() {
        return (a, b) -> Double.compare(a.prixAuPiedCarre(), b.prixAuPiedCarre());
    }
}