-- Schéma relationnel
-- ================================================

DROP TABLE IF EXISTS propriete CASCADE;
DROP TABLE IF EXISTS quartier  CASCADE;

-- quartier
CREATE TABLE quartier (
                          id    SERIAL       PRIMARY KEY,
                          nom   VARCHAR(120) NOT NULL,
                          ville VARCHAR(80)  NOT NULL,
                          UNIQUE (nom, ville)
);

-- propriete
CREATE TABLE propriete (
                           id                 VARCHAR(10)    PRIMARY KEY,
                           type_transaction   VARCHAR(10)    NOT NULL
                               CHECK (type_transaction IN ('VENTE', 'LOCATION')),
                           type_bien          VARCHAR(10)    NOT NULL
                               CHECK (type_bien IN ('MAISON', 'CONDO')),
                           prix               NUMERIC(12,2)  NOT NULL
                               CHECK (prix > 0),
                           superficie         INTEGER        NOT NULL
                               CHECK (superficie > 0),
                           chambres           INTEGER        NOT NULL
                               CHECK (chambres >= 0),
                           salles_bain        NUMERIC(3,1)   NOT NULL
                               CHECK (salles_bain >= 0),
                           quartier_id        INTEGER        NOT NULL
                               REFERENCES quartier(id) ON DELETE RESTRICT,
                           annee_construction INTEGER        NOT NULL,
                           type_courtier      VARCHAR(25)    NOT NULL
                               CHECK (type_courtier IN ('COURTIER', 'PROPRIETAIRE_DIRECT')),
                           date_publi         DATE           NOT NULL,
                           description        VARCHAR(500),

    -- Maison
                           terrain_pi2        INTEGER,
                           etages             INTEGER,
                           garage             BOOLEAN,

    -- Condo
                           etage_unite        INTEGER,
                           frais_condo        NUMERIC(8,2),
                           ascenseur          BOOLEAN
);