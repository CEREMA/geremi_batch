package com.geremi.bdrepbatch.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.Anomalie;

@Repository
public interface AnomalieRepository extends JpaRepository<Anomalie, Integer> {


}
