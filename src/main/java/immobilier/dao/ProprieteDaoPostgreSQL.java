package immobilier.dao;

import immobilier.model.Condo;
import immobilier.model.Maison;
import immobilier.model.Propriete;
import immobilier.model.TypeCourtier;
import immobilier.model.TypeTransaction;
import immobilier.util.SourceDonnees;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// DAo
public class ProprieteDaoPostgreSQL implements ProprieteDao, SourceDonnees {

    // -- Requêtes SQL

    private static final String SQL_TROUVER_TOUS =
            "SELECT p.id, p.type_transaction, p.type_bien, p.prix, "
                    + "p.superficie, p.chambres, p.salles_bain, "
                    + "q.nom AS quartier, q.ville, "
                    + "p.annee_construction, p.type_courtier, p.date_publi, "
                    + "p.description, "
                    + "p.terrain_pi2, p.etages, p.garage, "
                    + "p.etage_unite, p.frais_condo, p.ascenseur "
                    + "FROM propriete p "
                    + "JOIN quartier q ON p.quartier_id = q.id";

    private static final String SQL_TROUVER_PAR_ID =
            SQL_TROUVER_TOUS + " WHERE p.id = ?";

    private static final String SQL_AJOUTER =
            "INSERT INTO propriete "
                    + "(id, type_transaction, type_bien, prix, superficie, chambres, "
                    + "salles_bain, quartier_id, annee_construction, type_courtier, "
                    + "date_publi, description, "
                    + "terrain_pi2, etages, garage, etage_unite, frais_condo, ascenseur) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, "
                    + "(SELECT id FROM quartier WHERE nom = ? AND ville = ?), "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_MODIFIER =
            "UPDATE propriete SET "
                    + "type_transaction = ?, type_bien = ?, prix = ?, "
                    + "superficie = ?, chambres = ?, salles_bain = ?, "
                    + "quartier_id = (SELECT id FROM quartier WHERE nom = ? AND ville = ?), "
                    + "annee_construction = ?, type_courtier = ?, date_publi = ?, "
                    + "description = ?, "
                    + "terrain_pi2 = ?, etages = ?, garage = ?, "
                    + "etage_unite = ?, frais_condo = ?, ascenseur = ? "
                    + "WHERE id = ?";

    private static final String SQL_SUPPRIMER =
            "DELETE FROM propriete WHERE id = ?";

    private static final String SQL_INSERER_QUARTIER =
            "INSERT INTO quartier (nom, ville) VALUES (?, ?) "
                    + "ON CONFLICT (nom, ville) DO NOTHING";

    // -- CRUD
    @Override
    public List<Propriete> trouverTous() throws SQLException {
        List<Propriete> proprietes = new ArrayList<>();

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_TROUVER_TOUS)) {

            while (rs.next()) {
                proprietes.add(construire(rs));
            }
        }

        return proprietes;
    }

    @Override
    public Propriete trouverParId(String id) throws SQLException {

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL_TROUVER_PAR_ID)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construire(rs);
                }
            }
        }

        return null;
    }

    @Override
    public String ajouter(Propriete propriete) throws SQLException {

        try (Connection conn = ConnexionBD.getConnexion()) {

            assurerQuartier(conn, propriete.getQuartier(), propriete.getVille());
            try (PreparedStatement ps = conn.prepareStatement(SQL_AJOUTER)) {
                int i = 1;
                ps.setString(i++, propriete.getId());
                remplirParametresCommuns(ps, propriete, i);
                ps.executeUpdate();
            }
        }

        return propriete.getId();
    }

    @Override
    public boolean modifier(Propriete propriete) throws SQLException {

        try (Connection conn = ConnexionBD.getConnexion()) {

            assurerQuartier(conn, propriete.getQuartier(), propriete.getVille());
            try (PreparedStatement ps = conn.prepareStatement(SQL_MODIFIER)) {
                int dernier = remplirParametresCommuns(ps, propriete, 1);
                ps.setString(dernier, propriete.getId());
                return ps.executeUpdate() > 0;
            }
        }
    }

    @Override
    public boolean supprimer(String id) throws SQLException {

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL_SUPPRIMER)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // -- SourceDonnees

    @Override
    public List<Propriete> charger() {
        try {
            return trouverTous();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement des propriétés", e);
        }
    }

    // -- Méthodes privées

    private Propriete construire(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        TypeTransaction transaction = TypeTransaction.valueOf(rs.getString("type_transaction"));
        String typeBien = rs.getString("type_bien");
        double prix = rs.getDouble("prix");
        int superficie = rs.getInt("superficie");
        int chambres = rs.getInt("chambres");
        double sallesBain = rs.getDouble("salles_bain");
        String quartier = rs.getString("quartier");
        String ville = rs.getString("ville");
        int annee = rs.getInt("annee_construction");
        TypeCourtier courtier = TypeCourtier.valueOf(rs.getString("type_courtier"));
        LocalDate datePubli = rs.getDate("date_publi").toLocalDate();
        String description = rs.getString("description");

        if ("MAISON".equals(typeBien)) {
            return new Maison(id, transaction, prix, superficie, chambres,
                    sallesBain, ville, quartier, annee, courtier, datePubli,
                    description,
                    rs.getInt("terrain_pi2"),
                    rs.getInt("etages"),
                    rs.getBoolean("garage"));
        }
        return new Condo(id, transaction, prix, superficie, chambres,
                sallesBain, ville, quartier, annee, courtier, datePubli,
                description,
                rs.getInt("etage_unite"),
                rs.getDouble("frais_condo"),
                rs.getBoolean("ascenseur"));
    }

    private int remplirParametresCommuns(PreparedStatement ps, Propriete p,
                                         int i) throws SQLException {
        ps.setString(i++, p.getTypeTransaction().name());
        ps.setString(i++, p.typeBien().toUpperCase());
        ps.setDouble(i++, p.getPrix());
        ps.setInt(i++, p.getSuperficie());
        ps.setInt(i++, p.getChambres());
        ps.setDouble(i++, p.getSallesBain());
        // quartier_id via sous-requête
        ps.setString(i++, p.getQuartier());
        ps.setString(i++, p.getVille());
        ps.setInt(i++, p.getAnneeConstruction());
        ps.setString(i++, p.getTypeCourtier().name());
        ps.setDate(i++, Date.valueOf(p.getDatePubli()));
        ps.setString(i++, p.getDescription());

        // Colonnes Maison
        if (p instanceof Maison) {
            Maison m = (Maison) p;
            ps.setInt(i++, m.getTerrainPi2());
            ps.setInt(i++, m.getEtages());
            ps.setBoolean(i++, m.isGarage());
            ps.setNull(i++, Types.INTEGER);    // etage_unite
            ps.setNull(i++, Types.NUMERIC);    // frais_condo
            ps.setNull(i++, Types.BOOLEAN);    // ascenseur
        } else if (p instanceof Condo) {
            Condo c = (Condo) p;
            ps.setNull(i++, Types.INTEGER);    //  terrain_pi2
            ps.setNull(i++, Types.INTEGER);    // etages
            ps.setNull(i++, Types.BOOLEAN);    // garage
            ps.setInt(i++, c.getEtageUnite());
            ps.setDouble(i++, c.getFraisCondo());
            ps.setBoolean(i++, c.isAscenseur());
        }

        return i;
    }
    // quartier
    private void assurerQuartier(Connection conn, String nom, String ville)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERER_QUARTIER)) {
            ps.setString(1, nom);
            ps.setString(2, ville);
            ps.executeUpdate();
        }
    }
}