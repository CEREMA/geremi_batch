
package com.geremi.bdrepbatch.job.repository;

import jakarta.persistence.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.CarrProdExtraction;

@Repository
public interface CarrProdExtractionRepository extends JpaRepository<CarrProdExtraction, Integer>{

	/*Si on n'ajoute pas explicitement la countQuery Hibernate en génère une mal formattée et ça plante.
	 * https://www.baeldung.com/spring-data-jpa-query#2-native-1
	 *
	 * Note : Les left outer join sont omis volontairement de la count query pour les performances et la maintenance
	 *        car ils ne changent pas le nombre de lignes (0 ou 1 ligne retournée)
	 * */ 
	@Query(value="""
			SELECT
			  cpe.*,
			  r17.code,
			  total_quantite_annuelle.total_qa,
			  ceig.production_max_autorisee,
			  gbe.id AS idEtab,
			  minMaxExtractionEtablissement.maxSomme AS maxSomme,
			  minMaxExtractionEtablissement.minSomme AS minSomme,
			  minMaxExtractionEtablissement.avgSomme AS avgSomme,
			  minMaxExtractionEtablissement.varSomme AS varSomme
			FROM geremi_batch.carr_prod_extraction cpe
			LEFT OUTER JOIN geremi_batch.r17 r17 ON cpe.code_etablissement = r17.code
			LEFT OUTER JOIN geremi_batch.carr_env_info_gen ceig ON ceig.code_etablissement = cpe.code_etablissement
			LEFT OUTER JOIN geremi_batch.etablissement gbe ON gbe.code_etablissement = cpe.code_etablissement
			LEFT OUTER JOIN (
			  SELECT
				  gbcpe.code_etablissement,
				  SUM(
						CASE WHEN gbcpe.quantite_annuelle ~E'^\\\\d+(\\\\.\\\\d+)?$'
							 THEN CAST (gbcpe.quantite_annuelle AS NUMERIC)
							 ELSE 0 END
				  ) AS total_qa
			  FROM geremi_batch.carr_prod_extraction gbcpe
			  group by gbcpe.code_etablissement
			) AS total_quantite_annuelle on total_quantite_annuelle.code_etablissement = cpe.code_etablissement
			LEFT OUTER JOIN (
			  SELECT
				  MAX(somme) AS maxSomme,
				  MIN(somme) AS minSomme,
				  AVG(somme) AS avgSomme,
				  stddev_pop(somme) AS varSomme,
				  sommeParAnnee.code_etab
			  FROM (
				  SELECT
					  SUM(e.quantite_annuelle_substance) AS somme,
					  ge.code_etab,
					  e.annee,
					  row_number() over(partition by ge.code_etab order by e.annee desc) AS rnum
				  FROM geremi.extraction e
				  JOIN geremi.etablissement ge ON ge.id_etab = e.id_etablissement
				  GROUP BY ge.code_etab, e.annee
			  ) AS sommeParAnnee
			  WHERE sommeParAnnee.rnum <=10
			  GROUP BY sommeParAnnee.code_etab
			  HAVING COUNT(sommeParAnnee.annee)>=3
			) AS minMaxExtractionEtablissement ON cpe.code_etablissement = minMaxExtractionEtablissement.code_etab
			""",
			countQuery="""
			select count(*) FROM geremi_batch.carr_prod_extraction cpe
			LEFT OUTER JOIN geremi_batch.carr_env_info_gen ceig ON ceig.code_etablissement = cpe.code_etablissement
			LEFT OUTER JOIN geremi_batch.etablissement gbe ON gbe.code_etablissement = cpe.code_etablissement
					""",
			nativeQuery=true)
	Page<Tuple> findFilteredCarrProdExtraction(Pageable pageable);

}
