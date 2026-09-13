package immobilier.algorithmes;

import java.util.Comparator;
import java.util.List;

public class TriBulle<T> implements Algorithme<T> {

    @Override
    public String nom() {
        return "Bubble Sort";
    }

    @Override
    public String complexiteTheorique() {
        return "O(n²)";
    }

    @Override
    public void trier(List<T> liste, Comparator<T> comparateur) {
        int n = liste.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparateur.compare(liste.get(j), liste.get(j + 1)) > 0) {
                    T temp = liste.get(j);
                    liste.set(j, liste.get(j + 1));
                    liste.set(j + 1, temp);
                }
            }
        }
    }
}