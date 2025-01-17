package com.geremi.bdrepbatch.job.repository;

import jakarta.persistence.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.Declaration;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Integer>{
	
	@Query(value="SELECT r17.code "
			+ "FROM  "
			+ "geremi_batch.r17 r17 "
			+ "WHERE NOT EXISTS ("
			+ "select d.code_etablissement from geremi_batch.declaration d "
			+ "where d.code_etablissement = r17.code"
			+ ")",
			nativeQuery=true)
	Page<String> findFilteredDeclaration(Pageable pageable);
	
	@Query("SELECT d FROM Declaration d")
	Page<Declaration> findAllDeclarations(Pageable pageable);
	
	@Query(value="SELECT d.*, rc.nom_commune AS rcnc, rd.nom_departement AS rdnd, rr.nom_region AS rrnr,e.id AS idEtab "
			+ "FROM geremi_batch.declaration d "
			+ "LEFT OUTER JOIN geremi.ref_commune rc ON rc.insee_commune = d.code_insee "
			+ "LEFT OUTER JOIN geremi.ref_departement rd on (substr(d.code_insee, 1,2) = rd.insee_departement OR substr(d.code_insee, 1,3) = rd.insee_departement) "
			+ "LEFT OUTER JOIN geremi.ref_region rr ON rd.id_region = rr.id "
			+ "INNER JOIN geremi_batch.etablissement e ON e.code_etablissement = d.code_etablissement "
			+ "INNER JOIN geremi_batch.r17 r17 ON r17.code = d.code_etablissement", nativeQuery=true)
	Page<Tuple> findDeclarationsWithInseeInfo(Pageable pageable);
	
	@Query(value="SELECT e.id FROM geremi_batch.declaration e WHERE e.declaration = :codeEtab",nativeQuery=true)
	Integer findDeclarationErrorId(@Param("codeEtab")String codeEtab);
	
	

}
