package immobilier.algorithmes;

import java.util.Comparator;
import java.util.List;

public interface Algorithme<T> {
    String nom();
    String complexiteTheorique();
    void trier(List<T> liste, Comparator<T> comparateur);
}