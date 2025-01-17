package com.geremi.bdrepbatch.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.RefAnomalie;

@Repository
public interface RefAnomalieRepository extends JpaRepository<RefAnomalie, Integer> {

	@Query("SELECT a FROM RefAnomalie a WHERE a.codeAnomalie = :codeAnomalie")
	RefAnomalie findByCodeAnomalie(@Param("codeAnomalie") String codeAnomalie);
}
