package immobilier.service;

import immobilier.algorithmes.Algorithme;
import immobilier.model.Propriete;
import immobilier.util.SourceDonnees;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServiceCatalogueImpl implements ServiceCatalogue {

    private static final int TAILLE_PAGE_DEFAUT = 25;

    private final List<Propriete> donnees;
    private List<Propriete> resultats;
    private CritereFiltre criteres = new CritereFiltre();
    private String texteRecherche = "";
    private int taillePage = TAILLE_PAGE_DEFAUT;
    private int page = 0;

    public ServiceCatalogueImpl(SourceDonnees source) {
        this.donnees = source.charger();
        this.resultats = new ArrayList<>(donnees);
    }

    // --------- filtres et recherche -------

    @Override
    public void appliquerFiltres(CritereFiltre criteres) {
        this.criteres = criteres;
        recalculer();
    }

    @Override
    public void rechercher(String texte) {
        this.texteRecherche = (texte == null) ? "" : texte;
        recalculer();
    }

    private void recalculer() {
        List<Propriete> filtres = new ArrayList<>();
        for (Propriete p : donnees) {
            if (passeLesFiltres(p) && passeLaRecherche(p)) {
                filtres.add(p);
            }
        }
        this.resultats = filtres;
        this.page = 0;
    }

    private boolean passeLesFiltres(Propriete p) {
        if (criteres.getTransaction() != null
                && p.getTypeTransaction() != criteres.getTransaction()) {
            return false;
        }
        if (criteres.getTypeBien() != null
                && !p.typeBien().equals(criteres.getTypeBien())) {
            return false;
        }
        if (criteres.getPrixMax() != null && p.getPrix() > criteres.getPrixMax()) {
            return false;
        }
        if (criteres.getChambresMin() != null && p.getChambres() < criteres.getChambresMin()) {
            return false;
        }
        if (criteres.getVille() != null && !p.getVille().equals(criteres.getVille())) {
            return false;
        }
        return true;
    }

    private boolean passeLaRecherche(Propriete p) {
        if (texteRecherche.isBlank()) {
            return true;
        }
        String cible = normaliser(p.getQuartier() + " " + p.getVille());
        return cible.contains(normaliser(texteRecherche));
    }

    /** Minuscules + suppression des accents : "Sainte-Catherine" trouve "sainte-catherine". */
    private static String normaliser(String texte) {
        String sansAccent = Normalizer.normalize(texte, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sansAccent.toLowerCase();
    }

    // ------- trier --------

    @Override
    public void trier(Comparator<Propriete> comparateur, Algorithme<Propriete> algorithme) {
        algorithme.trier(resultats, comparateur);   // NOS algorithmes, jamais Collections.sort
        this.page = 0;
    }

    // ------ pagination ---------

    @Override
    public List<Propriete> pageCourante() {
        int debut = page * taillePage;
        int fin = Math.min(debut + taillePage, resultats.size());
        if (debut >= resultats.size()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(resultats.subList(debut, fin));
    }

    @Override
    public int numeroPage() {
        return page + 1;
    }

    @Override
    public int nombrePages() {
        if (resultats.isEmpty()) {
            return 1;
        }
        return (resultats.size() + taillePage - 1) / taillePage;
    }

    @Override
    public void pagePrecedente() {
        if (page > 0) {
            page--;
        }
    }

    @Override
    public void pageSuivante() {
        if (page < nombrePages() - 1) {
            page++;
        }
    }

    @Override
    public void taillePage(int taille) {
        this.taillePage = taille;
        this.page = 0;
    }



    @Override
    public int nombreResultats() {
        return resultats.size();
    }

    @Override
    public List<Propriete> toutesLesDonnees() {
        return new ArrayList<>(donnees);
    }
}