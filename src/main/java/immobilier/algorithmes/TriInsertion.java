package immobilier.algorithmes;

import java.util.Comparator;
import java.util.List;

public class TriInsertion<T> implements Algorithme<T> {

    @Override
    public String nom() {
        return "Insertion Sort";
    }

    @Override
    public String complexiteTheorique() {
        return "O(n²)";
    }

    @Override
    public void trier(List<T> liste, Comparator<T> comparateur) {
        int n = liste.size();
        for (int i = 1; i < n; i++) {
            T cle = liste.get(i);
            int j = i - 1;
            while (j >= 0 && comparateur.compare(liste.get(j), cle) > 0) {
                liste.set(j + 1, liste.get(j));
                j--;
            }
            liste.set(j + 1, cle);
        }
    }
}