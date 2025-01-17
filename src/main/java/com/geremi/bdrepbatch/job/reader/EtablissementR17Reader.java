package com.geremi.bdrepbatch.job.reader;

import java.util.Collections;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Tuple;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;

import com.geremi.bdrepbatch.job.repository.EtablissementRepository;

public class EtablissementR17Reader extends RepositoryItemReader<String>{
	
	@Autowired
	private EtablissementRepository etablissementRepository;
	
	@Value("${page.size.etablissement.r17}")
	private Integer pageSize;
	
	@PostConstruct
	public void init() {
		setRepository(etablissementRepository);
		setMethodName("findEtablissementR17");
		setPageSize(pageSize);
		setSort(Collections.singletonMap("id", Direction.ASC));
	}

}
