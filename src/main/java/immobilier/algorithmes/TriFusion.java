package immobilier.algorithmes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TriFusion<T> implements Algorithme<T> {

    @Override
    public String nom() {
        return "Merge Sort";
    }

    @Override
    public String complexiteTheorique() {
        return "O(n log n)";
    }

    @Override
    public void trier(List<T> liste, Comparator<T> comparateur) {
        trier(liste, 0, liste.size() - 1, comparateur);
    }

    private void trier(List<T> liste, int debut, int fin, Comparator<T> comparateur) {
        if (debut >= fin) {
            return;                                   
        }
        int milieu = (debut + fin) / 2;
        trier(liste, debut, milieu, comparateur);
        trier(liste, milieu + 1, fin, comparateur);
        fusionner(liste, debut, milieu, fin, comparateur);
    }

    private void fusionner(List<T> liste, int debut, int milieu, int fin,
                           Comparator<T> comparateur) {
        List<T> gauche = new ArrayList<>(liste.subList(debut, milieu + 1));
        List<T> droite = new ArrayList<>(liste.subList(milieu + 1, fin + 1));

        int i = 0, j = 0, k = debut;

        while (i < gauche.size() && j < droite.size()) {
            if (comparateur.compare(gauche.get(i), droite.get(j)) <= 0) {
                liste.set(k++, gauche.get(i++));
            } else {
                liste.set(k++, droite.get(j++));
            }
        }
        while (i < gauche.size()) {
            liste.set(k++, gauche.get(i++));
        }
        while (j < droite.size()) {
            liste.set(k++, droite.get(j++));
        }
    }
}