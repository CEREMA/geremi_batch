
package com.geremi.bdrepbatch.job.repository;

import java.util.List;

import jakarta.persistence.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.Etablissement;

@Repository
public interface EtablissementRepository extends JpaRepository<Etablissement, Integer> {

	//recherche de l'année maximale pour un établissement dans la base définitive
	@Query(value="SELECT MAX(e.annee) FROM geremi.etablissement e",nativeQuery=true)
	String findMaxAnneeDef();
	
	//Si pas d'enregistrements en base définitive, recherche de l'année min dans la base tempo
	@Query(value="SELECT MIN(e.annee) FROM geremi_batch.etablissement e "
			+ "INNER JOIN geremi_batch.r17 r17 ON r17.code = e.code_etablissement",nativeQuery=true)
	String findMinAnneeTempo();
	
	@Query(value="SELECT e.*,ceig.date_fin_autor "
			+ "FROM geremi_batch.etablissement e "
			+ "LEFT OUTER JOIN geremi_batch.carr_env_info_gen ceig "
			+ "ON ceig.code_etablissement = e.code_etablissement "
			+ "INNER JOIN geremi_batch.r17 r17 ON r17.code = e.code_etablissement",nativeQuery=true)
	Page<Tuple> findAllEtablissements(Pageable pageable);
	
	@Query("SELECT e.codeEtablissement FROM Etablissement e")
	List<String> findAllCodesEtablissement ();
	
	@Query(value="SELECT * FROM geremi_batch.etablissement e WHERE e.code_etablissement = :codeEtab",nativeQuery=true)
	Etablissement findEtablissementByCodeEtab(@Param("codeEtab")String codeEtab);
	
	@Query(value="SELECT r17.code "
			+ "FROM  "
			+ "geremi_batch.r17 r17 "
			+ "WHERE NOT EXISTS ("
			+ "select e.code_etablissement from geremi_batch.etablissement e "
			+ "where e.code_etablissement = r17.code"
			+ ")",
			nativeQuery=true)
	Page<String> findEtablissementR17(Pageable pageable);
		
	@Query(value="""
			SELECT id_etab as id , code_etab as codeEtablissement, annee
			FROM geremi.etablissement ge 
			WHERE ge.date_fin = '9999-12-31'\\:\\:DATE
			AND EXTRACT(YEAR FROM ge.date_fin_autorisation) <= (:annee\\:\\:NUMERIC)
			AND NOT EXISTS 
			(SELECT 1
			FROM geremi_batch.etablissement gbe where gbe.code_etablissement = ge.code_etab)
			""",nativeQuery=true)
	Page<Tuple> findEtablissementC16(String annee,Pageable pageable);
	
}
