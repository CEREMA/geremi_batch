package com.geremi.bdrepbatch.job.reader;

import java.util.Collections;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Tuple;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;

import com.geremi.bdrepbatch.job.repository.DeclarationRepository;

public class DeclarationReader extends RepositoryItemReader<Tuple>{
	@Autowired
	private DeclarationRepository declarationRepository;
	
	@Value("${page.size.declaration}")
	private Integer pageSize;

	@PostConstruct
	public void init() {
		setRepository(declarationRepository);
		setMethodName("findDeclarationsWithInseeInfo");
		setPageSize(pageSize);
		setSort(Collections.singletonMap("id", Direction.ASC));

	}
}
