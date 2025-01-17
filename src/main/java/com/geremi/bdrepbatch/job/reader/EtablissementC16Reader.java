package com.geremi.bdrepbatch.job.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Tuple;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;

import com.geremi.bdrepbatch.job.repository.EtablissementRepository;

public class EtablissementC16Reader extends RepositoryItemReader<Tuple> implements StepExecutionListener{

	@Autowired
	private EtablissementRepository etablissementRepository;
	
	@Value("${page.size.etablissement.c16}")
	private Integer pageSize;
	
	private List<String> args = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		setRepository(etablissementRepository);
		setMethodName("findEtablissementC16");
		setArguments(args);
		setPageSize(pageSize);
		setSort(Collections.singletonMap("id", Direction.ASC));
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		args.add((String) stepExecution.getJobExecution().getExecutionContext().get("anneeMax"));
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
