package com.geremi.bdrepbatch.job.repository;

import jakarta.persistence.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.CarrEnvInfoGen;

@Repository
public interface CarrEnvInfoGenRepository extends JpaRepository<CarrEnvInfoGen, Integer>{
	
	/*@Query(value="SELECT ceig.*,r17.code,ceig.annee as anneeEtab, e.id as idEtab "
			+ "FROM geremi_batch.carr_env_info_gen ceig "
			+ "LEFT OUTER JOIN geremi_batch.r17 r17 ON ceig.code_etablissement = r17.code "
			+ "INNER JOIN geremi_batch.etablissement e ON e.code_etablissement = ceig.code_etablissement",
			nativeQuery=true)
	Page<Tuple> findFilteredCarrEnvInfoGen(Pageable pageable);*/
	
	@Query(value="SELECT ceig.*,r17.code,ceig.annee as anneeEtab, e.id as idEtab "
			+ "FROM geremi_batch.carr_env_info_gen ceig "
			+ "LEFT OUTER JOIN geremi_batch.r17 r17 ON ceig.code_etablissement = r17.code "
			+ "LEFT OUTER JOIN geremi_batch.etablissement e ON e.code_etablissement = ceig.code_etablissement",
			nativeQuery=true)
	Page<Tuple> findFilteredCarrEnvInfoGen(Pageable pageable);

}
