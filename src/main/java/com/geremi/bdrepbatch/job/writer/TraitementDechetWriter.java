package com.geremi.bdrepbatch.job.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.model.TraitementDechet;
import com.geremi.bdrepbatch.job.repository.TraitementDechetRepository;

import jakarta.transaction.Transactional;

public class TraitementDechetWriter implements ItemWriter<TraitementDechet>{
	
	private static final Logger log = LoggerFactory.getLogger(TraitementDechetWriter.class);
	
	@Autowired
	private TraitementDechetRepository traitementDechetRepository;

	@Transactional
	@Override
	public void write(Chunk<? extends TraitementDechet> chunk) throws Exception {
		traitementDechetRepository.saveAll(chunk.getItems());
		
	}

}
