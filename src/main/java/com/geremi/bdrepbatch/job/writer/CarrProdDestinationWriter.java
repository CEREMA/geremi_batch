package com.geremi.bdrepbatch.job.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.model.CarrProdDestination;
import com.geremi.bdrepbatch.job.repository.CarrProdDestinationRepository;

import jakarta.transaction.Transactional;

public class CarrProdDestinationWriter implements ItemWriter<CarrProdDestination>{
	
	@Autowired
	private CarrProdDestinationRepository carrProdDestinationRepository;

	@Transactional
	@Override
	public void write(Chunk<? extends CarrProdDestination> chunk) throws Exception {
		carrProdDestinationRepository.saveAll(chunk.getItems());
		
	}
}
