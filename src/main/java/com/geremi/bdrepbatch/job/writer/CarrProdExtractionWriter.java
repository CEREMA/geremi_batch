package com.geremi.bdrepbatch.job.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.model.CarrProdExtraction;
import com.geremi.bdrepbatch.job.repository.CarrProdExtractionRepository;

import jakarta.transaction.Transactional;

public class CarrProdExtractionWriter implements ItemWriter<CarrProdExtraction>{

	@Autowired
	private CarrProdExtractionRepository carrProdExtractionRepository;

	@Transactional
	@Override
	public void write(Chunk<? extends CarrProdExtraction> chunk) throws Exception {
		carrProdExtractionRepository.saveAll(chunk.getItems());
		
	}

}
