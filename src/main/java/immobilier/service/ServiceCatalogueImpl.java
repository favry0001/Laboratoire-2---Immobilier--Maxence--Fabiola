package immobilier.service;

import immobilier.algorithmes.Algorithme;
import immobilier.dao.ProprieteDao;
import immobilier.model.Propriete;
import immobilier.util.SourceDonnees;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServiceCatalogueImpl implements ServiceCatalogue {

    private static final int TAILLE_PAGE_DEFAUT = 25;

    private final SourceDonnees sourceDonnees;
    private final ProprieteDao dao;
    private List<Propriete> donnees;
    private List<Propriete> resultats;
    private CritereFiltre criteres = new CritereFiltre();
    private String texteRecherche = "";
    private int taillePage = TAILLE_PAGE_DEFAUT;
    private int page = 0;

    public ServiceCatalogueImpl(SourceDonnees sourceDonnees) {
        this.sourceDonnees = sourceDonnees;
        this.dao = sourceDonnees instanceof ProprieteDao proprieteDao
                ? proprieteDao
                : null;
        this.donnees = chargerDonnees();
        this.resultats = new ArrayList<>(donnees);
    }

    @Override
    public void ajouter(Propriete propriete) {
        try {
            obtenirDao().ajouter(propriete);
            recharger();
        } catch (Exception e) {
            throw new RuntimeException("Erreur ajout", e);
        }
    }

    @Override
    public boolean modifier(Propriete propriete) {
        try {
            boolean modifiee = obtenirDao().modifier(propriete);
            recharger();
            return modifiee;
        } catch (Exception e) {
            throw new RuntimeException("Erreur modification", e);
        }
    }

    @Override
    public boolean supprimer(String id) {
        try {
            boolean supprimee = obtenirDao().supprimer(id);
            recharger();
            return supprimee;
        } catch (Exception e) {
            throw new RuntimeException("Erreur suppression", e);
        }
    }

    private ProprieteDao obtenirDao() {
        if (dao == null) {
            throw new IllegalStateException(
                    "Les modifications sont indisponibles avec la source CSV."
            );
        }

        return dao;
    }

    private List<Propriete> chargerDonnees() {
        try {
            return sourceDonnees.charger();
        } catch (Exception e) {
            throw new RuntimeException("Erreur chargement des données", e);
        }
    }

    private void recharger() {
        donnees = chargerDonnees();
        recalculer();
    }

    @Override
    public void appliquerFiltres(CritereFiltre criteres) {
        this.criteres = criteres;
        recalculer();
    }

    @Override
    public void rechercher(String texte) {
        this.texteRecherche = texte == null ? "" : texte;
        recalculer();
    }

    private void recalculer() {
        List<Propriete> filtres = new ArrayList<>();

        for (Propriete propriete : donnees) {
            if (passeLesFiltres(propriete)
                    && passeLaRecherche(propriete)) {
                filtres.add(propriete);
            }
        }

        resultats = filtres;
        page = 0;
    }

    private boolean passeLesFiltres(Propriete propriete) {
        if (criteres.getTransaction() != null
                && propriete.getTypeTransaction()
                != criteres.getTransaction()) {
            return false;
        }

        if (criteres.getTypeBien() != null
                && !propriete.typeBien().equals(criteres.getTypeBien())) {
            return false;
        }

        if (criteres.getPrixMax() != null
                && propriete.getPrix() > criteres.getPrixMax()) {
            return false;
        }

        if (criteres.getChambresMin() != null
                && propriete.getChambres() < criteres.getChambresMin()) {
            return false;
        }

        return criteres.getVille() == null
                || propriete.getVille().equals(criteres.getVille());
    }

    private boolean passeLaRecherche(Propriete propriete) {
        if (texteRecherche.isBlank()) {
            return true;
        }

        String cible = normaliser(
                propriete.getQuartier() + " " + propriete.getVille()
        );

        return cible.contains(normaliser(texteRecherche));
    }

    private static String normaliser(String texte) {
        String sansAccent = Normalizer.normalize(
                        texte,
                        Normalizer.Form.NFD
                )
                .replaceAll("\\p{M}", "");

        return sansAccent.toLowerCase();
    }

    @Override
    public void trier(
            Comparator<Propriete> comparateur,
            Algorithme<Propriete> algorithme
    ) {
        algorithme.trier(resultats, comparateur);
        page = 0;
    }

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
        if (taille <= 0) {
            throw new IllegalArgumentException(
                    "La taille de page doit être supérieure à zéro."
            );
        }

        taillePage = taille;
        page = 0;
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
