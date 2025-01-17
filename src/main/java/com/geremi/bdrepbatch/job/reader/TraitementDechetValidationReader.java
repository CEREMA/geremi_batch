package com.geremi.bdrepbatch.job.reader;

import java.util.Collections;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Tuple;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;

import com.geremi.bdrepbatch.job.repository.TraitementDechetRepository;

public class TraitementDechetValidationReader extends RepositoryItemReader<Tuple>{
	
	@Autowired
	private TraitementDechetRepository traitementDechetRepository;
	
	@Value("${page.size.traitementdechet}")
	private Integer pageSize;
	
	@PostConstruct
	public void init() {
		setRepository(traitementDechetRepository);
		setMethodName("findFilteredTraitementDechet");
		setPageSize(pageSize);
		setSort(Collections.singletonMap("id", Direction.ASC));
	}

}
