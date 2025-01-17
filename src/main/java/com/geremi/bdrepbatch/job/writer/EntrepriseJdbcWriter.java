package com.geremi.bdrepbatch.job.writer;

import java.util.List;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.model.Entreprise;
import com.geremi.bdrepbatch.job.repository.EntrepriseRepository;

public class EntrepriseJdbcWriter implements ItemWriter<Entreprise> {

	private static final Logger log = LoggerFactory.getLogger(EntrepriseJdbcWriter.class);

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Transactional
	@Override
	public void write(Chunk<? extends Entreprise> chunk) throws Exception {
		log.debug("Writing {} Entreprises to database", chunk.getItems().size());
		entrepriseRepository.saveAll(chunk.getItems());
		log.debug("{} Entreprises written to database", chunk.getItems().size());
		
	}
}
