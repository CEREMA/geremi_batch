package com.geremi.bdrepbatch.tasklet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

public class ToFinalDbTasklet implements Tasklet{

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Value("${spring.datasource.username}")
	private String dataSourceUsername;

	@Value("${spring.datasource.password}")
	private String dataSourcePassword;
	
	private static final Logger log = LoggerFactory.getLogger(ToFinalDbTasklet.class);
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		/*Enregistrement/mise à jour des établissements*/
		String sqlEtabUpdate = """
				UPDATE geremi.etablissement e
				SET date_fin = make_date ( ?, 12, 31 ), date_maj = current_timestamp
				WHERE e.code_etab NOT IN (SELECT code_etab FROM (SELECT code_etab,nom_etablissement,siret,region,departement,libelle_adresse,code_insee_commune,libelle_commune,code_postal_site,code_ape,activite_principale,
									long,lat,volume_production,unite,type_produit,nb_employe,site_internet,the_geom,service_inspection,
									if_carriere,if_quota,origin_mat,max_production_annuelle_autorisee,moy_production_annuelle_autorisee,date_fin_autorisation,type_carriere
									FROM geremi.etablissement
				INTERSECT
				SELECT e.code_etablissement AS code_etablissement,e.nom_etablissement AS nom_etablissement,
				CASE WHEN e.numerosiret<>'' THEN e.numerosiret::BIGINT ELSE NULL END AS siret,
				d.region AS region,d.departement AS departement,e.adressesite AS libelle_adresse,d.code_insee AS code_insee_commune,e.commune AS libelle_commune,e.codepostal::INTEGER AS code_postal_site,e.codeape AS code_ape,e.activiteprincipale AS activite_principale,
				CASE WHEN e.abscisselongitudex != '' THEN e.abscisselongitudex::FLOAT ELSE NULL END AS long,
				CASE WHEN e.ordonneelatitudey != '' THEN e.ordonneelatitudey::FLOAT ELSE NULL END AS lat,
				CASE WHEN e.volumeproduction != '' THEN e.volumeproduction::NUMERIC ELSE NULL END AS volume_production,
				e.unite AS unite,e.matiereproduite AS type_produit,
				CASE WHEN e.nbemployes != '' THEN e.nbemployes::INTEGER ELSE NULL END AS nb_employe,e.adressesiteinternet AS site_internet,public.ST_SetSRID(public.ST_MakePoint(e.abscisselongitudex::DOUBLE PRECISION,e.ordonneelatitudey::DOUBLE PRECISION),4326) AS the_geom,d.service_inspection AS service_inspection,
				CASE WHEN d.carriere='Oui' THEN true ELSE false END AS if_carriere,CASE WHEN d.quotas='Oui' THEN true ELSE false END AS if_quota,
				CASE 
				WHEN carriereOui.code_etablissement IS NOT NULL AND inListeCodeDechet.code_etablissement IS NOT NULL THEN 'Matériaux naturels et recyclés' 
				WHEN carriereOui.code_etablissement IS NOT NULL THEN 'Matériaux naturels' 
				WHEN inListeCodeDechet.code_etablissement IS NOT NULL THEN 'Matériaux recyclés' 
				END AS origin_mat,
				CASE WHEN ceig.production_max_autorisee != '' THEN ceig.production_max_autorisee::NUMERIC ELSE NULL END AS max_production_annuelle_autorisee,
				CASE WHEN ceig.production_moyenne_autorisee != '' THEN ceig.production_moyenne_autorisee::NUMERIC ELSE NULL END AS moy_production_annuelle_autorisee,
				TO_DATE(ceig.date_fin_autor,'DD/MM/YYYY') AS date_fin_autorisation,ceig.type_carriere AS type_carriere
				FROM geremi_batch.etablissement e 
				LEFT OUTER JOIN geremi_batch.declaration d ON d.code_etablissement=e.code_etablissement AND d.annee = e.annee 
				LEFT OUTER JOIN (SELECT DISTINCT d.code_etablissement FROM geremi_batch.declaration d WHERE d.carriere='Oui') AS carriereOui ON carriereOui.code_etablissement=e.code_etablissement 
				LEFT OUTER JOIN (SELECT DISTINCT td.code_etablissement FROM geremi_batch.traitement_dechet td WHERE td.code_dechet IN ('17 01 01', '17 01 02', '17 01 03', '17 01 07', '17 03 02', '17 05 04', '17 05 06', '17 05 08', '17 08 02', '17 09 04')) AS inListeCodeDechet ON inListeCodeDechet.code_etablissement = e.code_etablissement
				LEFT OUTER JOIN geremi_batch.carr_env_info_gen ceig ON ceig.code_etablissement = e.code_etablissement
				WHERE carriereOui.code_etablissement IS NOT NULL OR inListeCodeDechet.code_etablissement IS NOT NULL) AS equalRecords)
				AND e.date_fin = '31-DEC-9999'::DATE
				AND EXISTS (SELECT 1 FROM geremi_batch.etablissement gbe WHERE gbe.code_etablissement = e.code_etab
				AND gbe.code_etablissement IN (SELECT r17.code 
				FROM 
				geremi_batch.r17 r17))  
				""";
		
		String sqlEtabInsert = """
				INSERT INTO geremi.etablissement
				(code_etab,nom_etablissement,siret,region,departement,libelle_adresse,code_insee_commune,libelle_commune,code_postal_site,code_ape,activite_principale,
				long,lat,volume_production,unite,type_produit,nb_employe,site_internet,the_geom,service_inspection,
				if_carriere,if_quota,origin_mat,
				max_production_annuelle_autorisee,moy_production_annuelle_autorisee,date_fin_autorisation,type_carriere,
				date_debut,date_fin,id_etab,annee) 
				select r.*, 
				CASE WHEN EXISTS(SELECT 1 FROM geremi.etablissement ge WHERE ge.code_etab = r.code_etablissement) THEN  make_date(ge2.annee::INTEGER,1,1) ELSE make_date(1900,1,1) END,
				'9999-12-31',ge2.id::INTEGER, ge2.annee::INTEGER from(
				SELECT e.code_etablissement AS code_etablissement,e.nom_etablissement AS nom_etablissement,
				CASE WHEN e.numerosiret<>'' THEN e.numerosiret::BIGINT ELSE NULL END AS siret,
				d.region AS region,d.departement AS departement,e.adressesite AS libelle_adresse,d.code_insee AS code_insee_commune,e.commune AS libelle_commune,e.codepostal::INTEGER AS code_postal_site,e.codeape AS code_ape,e.activiteprincipale AS activite_principale,
				CASE WHEN e.abscisselongitudex != '' THEN e.abscisselongitudex::FLOAT ELSE NULL END AS long,
				CASE WHEN e.ordonneelatitudey != '' THEN e.ordonneelatitudey::FLOAT ELSE NULL END AS lat,
				CASE WHEN e.volumeproduction != '' THEN e.volumeproduction::NUMERIC ELSE NULL END AS volume_production,
				e.unite AS unite,e.matiereproduite AS type_produit,
				CASE WHEN e.nbemployes != '' THEN e.nbemployes::INTEGER ELSE NULL END AS nb_employe,e.adressesiteinternet AS site_internet,public.ST_SetSRID(public.ST_MakePoint(e.abscisselongitudex::DOUBLE PRECISION,e.ordonneelatitudey::DOUBLE PRECISION),4326) AS the_geom,d.service_inspection AS service_inspection,
				CASE WHEN d.carriere='Oui' THEN true ELSE false END AS if_carriere,CASE WHEN d.quotas='Oui' THEN true ELSE false END AS if_quota,
				CASE 
				WHEN carriereOui.code_etablissement IS NOT NULL AND inListeCodeDechet.code_etablissement IS NOT NULL THEN 'Matériaux naturels et recyclés' 
				WHEN carriereOui.code_etablissement IS NOT NULL THEN 'Matériaux naturels' 
				WHEN inListeCodeDechet.code_etablissement IS NOT NULL THEN 'Matériaux recyclés' 
				END AS origin_mat,
				CASE WHEN ceig.production_max_autorisee != '' THEN ceig.production_max_autorisee::NUMERIC ELSE NULL END AS max_production_annuelle_autorisee,
				CASE WHEN ceig.production_moyenne_autorisee != '' THEN ceig.production_moyenne_autorisee::NUMERIC ELSE NULL END AS moy_production_annuelle_autorisee,
				TO_DATE(ceig.date_fin_autor,'DD/MM/YYYY') AS date_fin_autorisation,ceig.type_carriere AS type_carriere
				FROM geremi_batch.etablissement e 
				LEFT OUTER JOIN geremi_batch.declaration d ON d.code_etablissement=e.code_etablissement AND d.annee = e.annee 
				LEFT OUTER JOIN (SELECT DISTINCT d.code_etablissement FROM geremi_batch.declaration d WHERE d.carriere='Oui') AS carriereOui ON carriereOui.code_etablissement=e.code_etablissement 
				LEFT OUTER JOIN (SELECT DISTINCT td.code_etablissement FROM geremi_batch.traitement_dechet td WHERE td.code_dechet IN ('17 01 01', '17 01 02', '17 01 03', '17 01 07', '17 03 02', '17 05 04', '17 05 06', '17 05 08', '17 08 02', '17 09 04')) AS inListeCodeDechet ON inListeCodeDechet.code_etablissement = e.code_etablissement
				LEFT OUTER JOIN geremi_batch.carr_env_info_gen ceig ON ceig.code_etablissement = e.code_etablissement
				WHERE carriereOui.code_etablissement IS NOT NULL OR inListeCodeDechet.code_etablissement IS NOT NULL
				EXCEPT
				SELECT code_etab, nom_etablissement,siret,region,departement,libelle_adresse,code_insee_commune,libelle_commune,code_postal_site,code_ape,activite_principale,
														long,lat,volume_production,unite,type_produit,nb_employe,site_internet,the_geom,service_inspection,
														if_carriere,if_quota,origin_mat,max_production_annuelle_autorisee,moy_production_annuelle_autorisee,date_fin_autorisation,type_carriere
					FROM geremi.etablissement) as r
				JOIN geremi_batch.etablissement	ge2 ON ge2.code_etablissement = r.code_etablissement
			""";
		
		/*Enregistrement des traitement de déchets*/
		String sqlTraitementDechetInsert = """
    				INSERT INTO geremi.traitement_dechet
					(id_traitement_dechet, id_etablissement,annee,code_dechet,libelle_dechet,if_statut_sortie_dechet,departement_origine,pays_origine,quantite_admise,quantite_traitee,code_ope,libelle_ope,numero_notification)
					SELECT td.id, e.id_etab,td.annee::INTEGER, td.code_dechet,td.libelle_dechet,
					CASE WHEN td.statut_sortie_dechet='oui' THEN true ELSE false END, td.departement_origine,td.pays_origine,
					CASE WHEN td.quantite_admise_tpa != '' THEN td.quantite_admise_tpa::NUMERIC ELSE NULL END,
					CASE WHEN td.quantite_traitee_tpa != '' THEN td.quantite_traitee_tpa::NUMERIC ELSE NULL END,
					td.code_ope, td.libelle_ope,td.numero_notification FROM geremi_batch.traitement_dechet td
					LEFT OUTER JOIN geremi.etablissement e on e.code_etab=td.code_etablissement AND e.date_fin = '31-DEC-9999'::DATE
					WHERE td.code_dechet IN ('17 01 01', '17 01 02', '17 01 03', '17 01 07', '17 03 02', '17 05 04', '17 05 06', '17 05 08', '17 08 02', '17 09 04')
					""";
		
		/*Enregistrement des déclarations*/
		String sqlDeclarationInsert = """
				INSERT INTO geremi.declaration 
				(id_declaration, id_etab,annee,statut_declaration,statut_quota_emission,statut_quota_niveau_activite,progression,date_derniere_action_declarant,
				date_derniere_action_inspecteur,date_initialisation,nb_heure_exploitation) 
				SELECT d.id, ge.id_etab,d.annee::INTEGER, d.statut_declaration,d.statut_quota_emission,d.statut_quota_niveaux_activites,d.progression::INTEGER,
				TO_DATE(d.date_derniere_action_declarant,'DD/MM/YYYY'), TO_DATE(d.date_derniere_action_inspecteur,'DD/MM/YYYY'), TO_DATE(d.date_init_declaration,'DD/MM/YYYY'),gbe.nbheuresexploitation::INTEGER 
				FROM geremi_batch.declaration d 
				LEFT OUTER JOIN geremi_batch.etablissement gbe ON gbe.code_etablissement = d.code_etablissement 
				LEFT OUTER JOIN geremi.etablissement ge ON ge.code_etab = d.code_etablissement AND ge.date_fin = '31-DEC-9999'::DATE
				WHERE d.code_etablissement IN 
				(SELECT r17.code 
				FROM 
				geremi_batch.r17 r17 
				WHERE EXISTS (
				select d.code_etablissement from geremi_batch.declaration d 
				where d.code_etablissement = r17.code))
				""";
		
		/*Enregistrement des extractions*/
		String sqlExtractionInsert = """
				INSERT INTO geremi.extraction 
				(id_extraction, id_etablissement, annee,reserve_restante_certaine,substance_a_recycler,famille_usage_debouche,precision_famille,sous_famille_usage_debouche_niv_1,precision_sous_famille_niv_1,sous_famille_usage_debouche_niv_2,precision_sous_famille_niv_2,
				quantite_annuelle_substance,tonnage_total_substance,tonnage_total_destination,quantite_annuelle_sterile,total_avec_quantite_sterile)
				SELECT cpe.id, e.id_etab,cpe.annee::INTEGER,cpe.quantite_restante_accessible,cpe.substances_a_recycler,cpe.famille_usage_debouches,cpe.precision_famille_usage,cpe.sous_famille_usage_debouches,cpe.precision_sous_famille_usage,cpe.sous_famille_usage_debouches_2,cpe.precision_sous_famille_usage_2,
				(CASE WHEN cpe.quantite_annuelle IS NOT NULL AND cpe.quantite_annuelle !='' THEN REPLACE(cpe.quantite_annuelle,',','.')::NUMERIC ELSE NULL END) AS quantite_annuelle_substance,
				(CASE WHEN cpe.total_substance IS NOT NULL AND cpe.total_substance !=''THEN REPLACE(cpe.total_substance,',','.')::NUMERIC ELSE NULL END) AS tonnage_total_substance,
				((SELECT SUM(CASE WHEN tonnage IS NOT NULL AND tonnage != '' THEN REPLACE(tonnage,',','.')::NUMERIC ELSE NULL END) AS total FROM geremi_batch.carr_prod_destination cpd WHERE cpd.code_etablissement = cpe.code_etablissement AND (cpd.tonnage !='0' AND cpd.tonnage!=' '))) AS tonnage_total_destination,
				(CASE WHEN cpe.quantite_annuelle_steriles IS NOT NULL AND cpe.quantite_annuelle_steriles != '' THEN REPLACE(cpe.quantite_annuelle_steriles,',','.')::NUMERIC ELSE NULL END) AS quantite_annuelle_sterile,
				(CASE WHEN cpe.total_dont_steriles IS NOT NULL AND cpe.total_dont_steriles != '' THEN REPLACE(cpe.total_dont_steriles,',','.')::NUMERIC ELSE NULL END) AS total_avec_quantite_sterile
				FROM geremi_batch.carr_prod_extraction cpe 
				LEFT OUTER JOIN geremi.etablissement e ON e.code_etab = cpe.code_etablissement AND e.date_fin = '31-DEC-9999'::DATE 
				WHERE cpe.code_etablissement IN 
				(SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				""";
		
		/*Enregistrement des destinations*/
		String sqlDestinationInsert = """
				INSERT INTO geremi.destination 
				(id_destination, id_etablissement, annee,famille_rattachement_destination,type_produit_destination,libelle_destination,tonnage_destination)
				SELECT cpd.id, e.id_etab,cpd.annee::INTEGER,cpd.famille_rattachement,cpd.type_produits_expedies,cpd.destination,
				REPLACE(cpd.tonnage,',','.')::NUMERIC
				FROM geremi_batch.carr_prod_destination cpd 
				LEFT OUTER JOIN geremi.etablissement e ON e.code_etab = cpd.code_etablissement AND e.date_fin = '31-DEC-9999'::DATE  
				WHERE cpd.code_etablissement IN 
				(SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				AND cpd.tonnage IS NOT NULL AND cpd.tonnage != ''
				""";
		
		/*Enregistrement des commentaires*/
		String sqlCommentInsert = """
				INSERT INTO geremi.commentaire 
				(annee,code_etab,nom_table,libelle,commentaire)
				SELECT e.annee::INTEGER, e.code_etablissement,'etablissement','Informations complémentaires/remarques', e.informationscomplementaires
				FROM geremi_batch.etablissement e
				WHERE e.informationscomplementaires !=''
				AND e.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT e.annee::INTEGER, e.code_etablissement,'etablissement','Commentaires de section', e.commentairessection
				FROM geremi_batch.etablissement e
				WHERE e.commentairessection !=''
				AND e.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT ceig.annee::INTEGER, ceig.code_etablissement,'etablissement','Commentaires production max autorisée', ceig.commentaires_production_max_autorisee
				FROM geremi_batch.carr_env_info_gen ceig
				WHERE ceig.commentaires_production_max_autorisee !=''
				AND ceig.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT ceig.annee::INTEGER, ceig.code_etablissement,'etablissement','Commentaires production moyenne autorisée', ceig.commentaires_production_moyenne_autorisee
				FROM geremi_batch.carr_env_info_gen ceig
				WHERE ceig.commentaires_production_moyenne_autorisee !=''
				AND ceig.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT ceig.annee::INTEGER, ceig.code_etablissement,'etablissement','Commentaires date fin autorisation', ceig.commentaires_date_fin_autor
				FROM geremi_batch.carr_env_info_gen ceig
				WHERE ceig.commentaires_date_fin_autor !=''
				AND ceig.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT ceig.annee::INTEGER, ceig.code_etablissement,'etablissement','Commentaires type carrière', ceig.commentaires_type_carriere
				FROM geremi_batch.carr_env_info_gen ceig
				WHERE ceig.commentaires_type_carriere !=''
				AND ceig.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT cpe.annee::INTEGER,cpe.code_etablissement,'extraction', 'commentaires_reserve_restante_certaine',cpe.commentaires_quantite_restante_accessible
				FROM geremi_batch.carr_prod_extraction cpe 
				WHERE cpe.commentaires_quantite_restante_accessible != ''
				AND cpe.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT cpe.annee::INTEGER,cpe.code_etablissement,'extraction', 'commentaires_quantite_annuelle_steriles',cpe.commentaires_quantite_annuelle_steriles
				FROM geremi_batch.carr_prod_extraction cpe 
				WHERE cpe.commentaires_quantite_annuelle_steriles != ''
				AND cpe.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT cpe.annee::INTEGER,cpe.code_etablissement,'extraction', 'commentaires_quantite_annuelle',cpe.commentaires_quantite_annuelle
				FROM geremi_batch.carr_prod_extraction cpe 
				WHERE cpe.commentaires_quantite_annuelle != ''
				AND cpe.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT cpe.annee::INTEGER,cpe.code_etablissement,'extraction', 'commentaires_alerte',cpe.commentaires_alerte
				FROM geremi_batch.carr_prod_extraction cpe 
				WHERE cpe.commentaires_alerte != ''
				AND cpe.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				UNION
				SELECT cpd.annee::INTEGER,cpd.code_etablissement,'destination', 'commentaires_destination',cpd.commentaires
				FROM geremi_batch.carr_prod_destination cpd 
				WHERE cpd.commentaires != ''
				AND cpd.code_etablissement IN (SELECT r17.code FROM geremi_batch.r17 WHERE r17.code IS NOT NULL)
				""";
		
		/*Enregistrement des anomalies*/
		String sqlAnomalieInsert = """
				INSERT INTO geremi.anomalie (date_creation, nom_table,nom_champ,id_ligne,id_verification,id_statut_ano,date_maj,bloquante,code_ref_anomalie,id_etablissement,annee)
			    SELECT gba.date_creation,gba.nom_table,gba.nom_champ,CASE WHEN gba.id_ligne='Absent' THEN null ELSE gba.id_ligne::INTEGER END,gba.id_verification,gba.id_statut_ano,gba.date_maj,gba.bloquante,gba.code_ref_anomalie,coalesce(ge.id_etab, gba.id_etablissement),gba.annee 
			    FROM geremi_batch.anomalie gba 
			    LEFT OUTER JOIN geremi_batch.etablissement be ON be.id = gba.id_etablissement
			    LEFT OUTER JOIN geremi.etablissement ge ON ge.code_etab = be.code_etablissement AND ge.date_fin = make_date(9999,12,31)     
				""";
	
		try(Connection conn = DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword);
				PreparedStatement stmtUpdate = conn.prepareStatement(sqlEtabUpdate);
				Statement stmtInsert = conn.createStatement()
			){
			conn.setAutoCommit(false);
			
			String anneeMax = (String)chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("anneeMax"); 

			log.debug("geremi.etablissement : début de l'enregistrement/mise à jour...");
			if(StringUtils.isNotBlank(anneeMax)) {
				stmtUpdate.setInt(1, Integer.parseInt(anneeMax));
				stmtUpdate.executeUpdate();
			}
			
			stmtInsert.execute(sqlEtabInsert);
			log.debug("Enregistrement dans geremi.etablissement réalisé avec succès");
			
			log.debug("geremi.traitement_dechet : début de l'enregistrement...");
			stmtInsert.execute(sqlTraitementDechetInsert);
			log.debug("Enregistrement dans geremi.traitement_dechet réalisé avec succès");
			
			log.debug("geremi.declaration : début de l'enregistrement...");
			stmtInsert.execute(sqlDeclarationInsert);
			log.debug("Enregistrement dans geremi.declaration réalisé avec succès");
			
			log.debug("geremi.extraction : début de l'enregistrement...");
			stmtInsert.execute(sqlExtractionInsert);
			log.debug("Enregistrement dans geremi.extraction réalisé avec succès");
			
			log.debug("geremi.destination : début de l'enregistrement...");
			stmtInsert.execute(sqlDestinationInsert);
			log.debug("Enregistrement dans geremi.destination réalisé avec succès");
			
			log.debug("geremi.commentaire : début de l'enregistrement...");
			stmtInsert.execute(sqlCommentInsert);
			log.debug("Enregistrement dans geremi.commentaire réalisé avec succès");
			
			log.debug("geremi.anomalie : début de l'enregistrement/mise à jour...");
			stmtInsert.execute(sqlAnomalieInsert);
			log.debug("Enregistrement dans geremi.anomalie réalisé avec succès");
			
			conn.commit();
		}catch(SQLException e) {
			e.printStackTrace();
		}
			
		return RepeatStatus.FINISHED;
	}

}
