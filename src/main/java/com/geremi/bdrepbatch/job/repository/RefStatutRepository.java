package com.geremi.bdrepbatch.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.RefStatut;

@Repository
public interface RefStatutRepository extends JpaRepository<RefStatut, Integer> {

	@Query("SELECT a FROM RefStatut a WHERE a.statut = :statut")
	RefStatut findByCodestatut(@Param("statut") String statut);
}
