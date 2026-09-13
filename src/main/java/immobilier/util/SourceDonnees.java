package immobilier.util;

import immobilier.model.Propriete;
import java.util.List;

public interface SourceDonnees {
    List<Propriete> charger();
}