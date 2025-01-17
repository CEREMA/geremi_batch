package com.geremi.bdrepbatch.job.repository;

import jakarta.persistence.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.TraitementDechet;

@Repository
public interface TraitementDechetRepository extends JpaRepository<TraitementDechet, Integer>{
	
	/*
	 * Filtrage préalable des données correspondant à la R17
	 * */
	/*@Query(value="SELECT td.*, r17.code "
			+ "FROM geremi_batch.traitement_dechet td "
			+ "LEFT OUTER JOIN "
			+ "geremi_batch.r17 r17 " 
			+ "ON td.code_etablissement = r17.code",
			nativeQuery=true)
	Page<Tuple> findFilteredTraitementDechet(Pageable pageable);*/
	
	@Query(value="SELECT td.*, e.annee as anneeEtab, e.id as idEtab "
			+ "FROM geremi_batch.traitement_dechet td "
			+ "LEFT OUTER JOIN "
			+ "geremi_batch.etablissement e " 
			+ "ON td.code_etablissement = e.code_etablissement AND td.annee = e.annee "
			+ "INNER JOIN geremi_batch.r17 r17 ON r17.code = td.code_etablissement",
			nativeQuery=true)
	Page<Tuple> findFilteredTraitementDechet(Pageable pageable);
	
	
	@Query(value="SELECT * FROM geremi_batch.traitement_dechet t WHERE t.code_etablissement = :codeEtab",nativeQuery=true)
	TraitementDechet findByIdEtablissement(@Param("codeEtab")String codeEtab);

}
