package com.geremi.bdrepbatch.job.repository;

import jakarta.persistence.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.geremi.bdrepbatch.job.model.CarrProdDestination;

public interface CarrProdDestinationRepository  extends JpaRepository<CarrProdDestination, Integer>{

	@Query(value="""
			SELECT cpd.*, r17.code,gbe.id AS idEtab, total_quantite_annuelle.total_qa
			FROM geremi_batch.carr_prod_destination cpd
			LEFT OUTER JOIN geremi_batch.r17 r17
			    ON cpd.code_etablissement = r17.code
			LEFT OUTER JOIN geremi_batch.etablissement gbe ON gbe.code_etablissement = cpd.code_etablissement
			LEFT OUTER JOIN (
			    SELECT
			        gbcpd.code_etablissement,
			        SUM (
			            CASE WHEN gbcpd.tonnage ~E'^\\\\d+(\\\\.\\\\d+)?$'
			            THEN CAST (gbcpd.tonnage AS NUMERIC)
			            ELSE 0 END
			        ) AS total_qa
			    FROM geremi_batch.carr_prod_destination gbcpd
			    group by gbcpd.code_etablissement
			) AS total_quantite_annuelle on total_quantite_annuelle.code_etablissement = cpd.code_etablissement
			WHERE cpd.tonnage IS NOT NULL AND cpd.tonnage != '' AND cpd.tonnage != '0'
			""",
			nativeQuery=true)
	Page<Tuple> findFilteredCarrProdDestination(Pageable pageable);
}
