package immobilier.service;

import immobilier.algorithmes.Algorithme;
import immobilier.model.Propriete;

import java.util.Comparator;
import java.util.List;

// Interface du service catalogue
public interface ServiceCatalogue {

    // CRUD
    void ajouter(Propriete propriete);
    boolean modifier(Propriete propriete);
    boolean supprimer(String id);

    // Filtres et recherche
    void appliquerFiltres(CritereFiltre criteres);
    void rechercher(String texte);
    void trier(Comparator<Propriete> comparateur, Algorithme<Propriete> algorithme);

    // Pagination
    List<Propriete> pageCourante();
    int numeroPage();
    int nombrePages();
    void pagePrecedente();
    void pageSuivante();
    void taillePage(int taille);

    // Données
    int nombreResultats();
    List<Propriete> toutesLesDonnees();
}