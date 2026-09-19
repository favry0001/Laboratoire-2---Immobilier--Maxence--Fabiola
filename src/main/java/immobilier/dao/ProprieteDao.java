package immobilier.dao;

import immobilier.model.Propriete;

import java.sql.SQLException;
import java.util.List;


public interface ProprieteDao {

    List<Propriete> trouverTous() throws SQLException;

    Propriete trouverParId(String id) throws SQLException;

    String ajouter(Propriete propriete) throws SQLException;

    boolean modifier(Propriete propriete) throws SQLException;

    boolean supprimer(String id) throws SQLException;
}