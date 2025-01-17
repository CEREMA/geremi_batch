package com.geremi.bdrepbatch.job.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.model.CarrEnvInfoGen;
import com.geremi.bdrepbatch.job.repository.CarrEnvInfoGenRepository;

public class CarrEnvInfoGenWriter implements ItemWriter<CarrEnvInfoGen> {

	@Autowired
	private CarrEnvInfoGenRepository carrEnvInfoGenRepository;

	@Override
	public void write(Chunk<? extends CarrEnvInfoGen> chunk) throws Exception {
		carrEnvInfoGenRepository.saveAll(chunk.getItems());
		
	}

}
