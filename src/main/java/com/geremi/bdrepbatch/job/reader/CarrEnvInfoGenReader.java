package com.geremi.bdrepbatch.job.reader;

import java.util.Collections;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Tuple;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;

import com.geremi.bdrepbatch.job.repository.CarrEnvInfoGenRepository;

public class CarrEnvInfoGenReader extends RepositoryItemReader<Tuple> {

	@Autowired
	private CarrEnvInfoGenRepository carrEnvInfoGenRepository;
	
	@Value("${page.size.carrEnvInfoGen}")
	private Integer pageSize;
	
	@PostConstruct
	public void init() {
		setRepository(carrEnvInfoGenRepository);
		setMethodName("findFilteredCarrEnvInfoGen");
		setPageSize(pageSize);
		setSort(Collections.singletonMap("id", Direction.ASC));
	}

}
