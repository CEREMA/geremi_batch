package com.geremi.bdrepbatch.job.reader;

import java.util.Collections;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Tuple;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.geremi.bdrepbatch.job.repository.EtablissementRepository;

@Component
public class EtablissementReader extends RepositoryItemReader<Tuple> {

	@Autowired
	private EtablissementRepository etablissementRepository;
	
	@Value("${page.size.etablissement}")
	private Integer pageSize;

	@PostConstruct
	public void init() {
		setRepository(etablissementRepository);
		setMethodName("findAllEtablissements");
		setPageSize(pageSize);
		setSort(Collections.singletonMap("id", Direction.ASC));
	}
}
