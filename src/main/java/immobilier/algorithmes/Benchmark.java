package immobilier.algorithmes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Mesure le temps d'execution des algorithmes de tri. */
public class Benchmark<T> {

    /**
     * Mesure un algorithme sur une COPIE de la liste.
     * Sans la copie, la 2e mesure porterait sur une liste deja triee.
     *
     * @return le temps d'execution en millisecondes
     */
    public long mesurer(Algorithme<T> algorithme, List<T> donnees, Comparator<T> comparateur) {
        List<T> copie = new ArrayList<>(donnees);
        long debut = System.nanoTime();
        algorithme.trier(copie, comparateur);
        long fin = System.nanoTime();
        return (fin - debut) / 1_000_000;
    }
}