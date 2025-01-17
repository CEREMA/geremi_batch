DROP SCHEMA IF EXISTS geremi_batch CASCADE;

CREATE SCHEMA geremi_batch;
ALTER SCHEMA geremi_batch OWNER TO geremi;

-- Création de la table TraitementDechet
CREATE TABLE geremi_batch.traitement_dechet (
    id INTEGER PRIMARY KEY DEFAULT nextval('geremi.traitement_dechet_id_seq'),
    id_ligne_excel VARCHAR(10),
    annee TEXT,
    code_etablissement TEXT,
    code_dechet TEXT,
    libelle_dechet TEXT,
    dangereux TEXT,
    statut_sortie_dechet TEXT,
    departement_origine TEXT,
    pays_origine TEXT,
    quantite_admise_tpa TEXT,
    quantite_traitee_tpa TEXT,
    code_ope TEXT,
    libelle_ope TEXT,
    numero_notification TEXT
);
alter table geremi_batch.traitement_dechet owner to geremi;

create index traitement_dechet_idx
on geremi_batch.traitement_dechet (code_etablissement);


-- Création de la table Declaration
CREATE TABLE geremi_batch.declaration (
    id INTEGER PRIMARY KEY DEFAULT nextval('geremi.declaration_id_seq'),
    id_ligne_excel VARCHAR(10),
    annee TEXT,
    code_etablissement TEXT,
    code_insee TEXT,
    commune TEXT,
    nom_etablissement TEXT,
    service_inspection TEXT,
    region TEXT,
    departement TEXT,
    statut_declaration TEXT,
    statut_quota_emission TEXT,
    statut_quota_niveaux_activites TEXT,
    progression TEXT,
    date_derniere_action_declarant TEXT,
    date_derniere_action_inspecteur TEXT,
    carriere TEXT,
    quotas TEXT,
    isdi TEXT,
    isdnd TEXT,
    date_init_declaration TEXT
);

alter table geremi_batch.declaration owner to geremi;

create unique index declaration_idx
on geremi_batch.declaration (code_etablissement);


-- Création de la table Etablissement
CREATE TABLE geremi_batch.etablissement (
    id INTEGER PRIMARY KEY DEFAULT nextval('geremi.etablissement_id_seq'),
    id_ligne_excel VARCHAR(10),
    annee TEXT,
    code_etablissement TEXT,
    nom_etablissement TEXT,
    adresseSite TEXT,
    codePostal TEXT,
    commune TEXT,
    numeroSiret TEXT,
    codeAPE TEXT,
    activitePrincipale TEXT,
    coordonneesGeographiques TEXT,
    abscisseLongitudeX TEXT,
    ordonneeLatitudeY TEXT,
    volumeProduction TEXT,
    unite TEXT,
    matiereProduite TEXT,
    nbHeuresExploitation TEXT,
    nbEmployes TEXT,
    adressesiteInternet TEXT,
    informationsComplementaires TEXT,
    commentairesSection TEXT
);
alter table geremi_batch.etablissement owner to geremi;

create index etablissement_idx
on geremi_batch.etablissement (code_etablissement);

-- Création de la table carr_prod_extraction

CREATE TABLE geremi_batch.carr_prod_extraction(
	id INTEGER PRIMARY KEY DEFAULT nextval('geremi.extraction_id_seq'),
	annee TEXT,
	code_etablissement TEXT,
	id_ligne_excel VARCHAR(10),
	quantite_restante_accessible TEXT,
	commentaires_quantite_restante_accessible TEXT,
	quantite_annuelle_steriles TEXT,
	commentaires_quantite_annuelle_steriles TEXT,
	substances_a_recycler TEXT,
	famille_usage_debouches TEXT,
	precision_famille_usage TEXT,
	sous_famille_usage_debouches TEXT,
	precision_sous_famille_usage TEXT,
	sous_famille_usage_debouches_2 TEXT,
	precision_sous_famille_usage_2 TEXT,
	quantite_annuelle TEXT,
	commentaires_quantite_annuelle TEXT,
	total_substance TEXT,
	total_dont_steriles TEXT,
	commentaires_alerte TEXT
	);
alter table geremi_batch.carr_prod_extraction owner to geremi;


CREATE TABLE geremi_batch.carr_prod_destination(
	id INTEGER PRIMARY KEY DEFAULT nextval('geremi.destination_id_seq'),
	annee TEXT,
	code_etablissement TEXT,
	id_ligne_excel VARCHAR(10),
	famille_rattachement TEXT,
	type_produits_expedies TEXT,
	destination TEXT,
	tonnage TEXT,
	commentaires TEXT
);
alter table geremi_batch.carr_prod_destination owner to geremi;


-- Création de la séquence
CREATE SEQUENCE geremi_batch.carr_env_info_gen_id_seq
	START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE geremi_batch.carr_env_info_gen(
	id INTEGER PRIMARY KEY DEFAULT nextval('geremi_batch.carr_env_info_gen_id_seq'),
	annee TEXT,
	code_etablissement TEXT,
	id_ligne_excel VARCHAR(10),
	production_max_autorisee TEXT,
	commentaires_production_max_autorisee TEXT,
	production_moyenne_autorisee TEXT,
	commentaires_production_moyenne_autorisee TEXT,
	date_fin_autor TEXT,
	commentaires_date_fin_autor TEXT,
	type_carriere TEXT,
	commentaires_type_carriere TEXT
);
alter table geremi_batch.carr_env_info_gen owner to geremi;


-- Création de la séquence
CREATE SEQUENCE geremi_batch.anomalie_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Création de la table "ANOMALIE"
CREATE TABLE geremi_batch.anomalie (
    id_anomalie INTEGER DEFAULT nextval('geremi_batch.anomalie_id_seq') PRIMARY KEY,
    date_creation DATE,
    code_ref_anomalie TEXT,
    nom_table TEXT,
    nom_champ TEXT,
    id_ligne VARCHAR(10),
    id_verification INTEGER,
    id_statut_ano INTEGER,
    date_maj DATE,
    bloquante BOOLEAN,
    id_etablissement INTEGER,
    annee INTEGER,
    FOREIGN KEY (code_ref_anomalie) REFERENCES geremi.REF_ANOMALIE (code_anomalie),
    FOREIGN KEY (id_statut_ano) REFERENCES geremi.REF_STATUT (id_ref_statut)
);
alter table geremi_batch.anomalie owner to geremi;


CREATE VIEW  geremi_batch.r17  AS
(SELECT row_number() over (order by code asc)  as id, code from
(
 select DISTINCT code_etablissement  as code 
	FROM 
	(SELECT
        d.code_etablissement 
    FROM
        geremi_batch.declaration d 
    WHERE
        d.carriere = 'Oui' 
	UNION
	SELECT
    	tde.code_etablissement 
	FROM
    	geremi_batch.traitement_dechet tde 
	WHERE tde.code_dechet IN('17 01 01', '17 01 02', '17 01 03', '17 01 07', '17 03 02', '17 05 04', '17 05 06', '17 05 08', '17 08 02', '17 09 04')
) AS listefiltree
) as listefiltreeWithId);

ALTER TABLE geremi_batch.r17 OWNER TO geremi;
