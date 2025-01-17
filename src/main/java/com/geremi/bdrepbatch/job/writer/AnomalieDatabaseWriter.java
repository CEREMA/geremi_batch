package com.geremi.bdrepbatch.job.writer;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.repository.AnomalieRepository;

public class AnomalieDatabaseWriter implements ItemStreamWriter<List<Anomalie>>{

	private static final Logger log = LoggerFactory.getLogger(AnomalieDatabaseWriter.class);
	
	@Autowired
	private AnomalieRepository anomalieRepository;


	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		
	}

	@Override
	public void close() throws ItemStreamException {
		
	}

	@Override
	public void write(Chunk<? extends List<Anomalie>> chunk) throws Exception {
		anomalieRepository.saveAll(chunk.getItems().stream()
			    .flatMap(e -> e.stream().filter(i -> i.getCodeRefAnomalie() != null && !i.isBloquante()))
			    .collect(Collectors.toList()));
		
	}

}
