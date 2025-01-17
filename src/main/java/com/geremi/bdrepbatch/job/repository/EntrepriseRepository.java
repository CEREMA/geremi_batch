package com.geremi.bdrepbatch.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.geremi.bdrepbatch.job.model.Entreprise;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {

}
