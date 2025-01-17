package com.geremi.bdrepbatch.job.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.model.Declaration;
import com.geremi.bdrepbatch.job.repository.DeclarationRepository;

public class DeclarationWriter implements ItemWriter<Declaration>{

	@Autowired
	private DeclarationRepository declarationRepository;
	
	@Override
	public void write(Chunk<? extends Declaration> chunk) throws Exception {
		declarationRepository.saveAll(chunk.getItems());
		
	}

}
