package com.geremi.bdrepbatch.job.writer;

import java.util.List;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.model.Etablissement;
import com.geremi.bdrepbatch.job.repository.EtablissementRepository;

public class EtablissementJdbcWriter implements ItemWriter<Etablissement> {

	private static final Logger log = LoggerFactory.getLogger(EtablissementJdbcWriter.class);

	@Autowired
	private EtablissementRepository etablissementRepository;

	@Transactional
	@Override
	public void write(Chunk<? extends Etablissement> chunk) throws Exception {
		etablissementRepository.saveAll(chunk.getItems());
		
	}
}
