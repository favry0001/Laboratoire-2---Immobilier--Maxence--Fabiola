package immobilier.service;

import immobilier.algorithmes.Algorithme;
import immobilier.model.Propriete;

import java.util.Comparator;
import java.util.List;

public interface ServiceCatalogue {

    void appliquerFiltres(CritereFiltre criteres);
    void rechercher(String texte);
    void trier(Comparator<Propriete> comparateur, Algorithme<Propriete> algorithme);

    List<Propriete> pageCourante();
    int numeroPage();
    int nombrePages();
    void pagePrecedente();
    void pageSuivante();
    void taillePage(int taille);

    int nombreResultats();
    List<Propriete> toutesLesDonnees();
}