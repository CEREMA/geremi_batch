package com.geremi.bdrepbatch.steps;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.process.EtablissementValidationC16Processor;
import com.geremi.bdrepbatch.job.reader.EtablissementC16Reader;
import com.geremi.bdrepbatch.job.writer.AnomalieDatabaseWriter;

import jakarta.persistence.Tuple;

@Configuration
public class EtablissementValidationC16StepConfiguration {
	
	@Value("${chunk.size.etablissement.c16.validation}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(EtablissementValidationC16StepConfiguration.class);
	
	@Bean
	public Step etablissementValidationC16Step(ItemReader<Tuple> etablissementC16Reader,
			ItemProcessor<Tuple,List<Anomalie>> etablissementValidationC16Processor,
			ItemStreamWriter<List<Anomalie>> anomalieEtabC16DatabaseWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step etablissementValidationC16Step");
		return new StepBuilder("etablissementValidationC16Step",jobRepository)
				.<Tuple,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(etablissementC16Reader)
				.processor(etablissementValidationC16Processor)
				.writer(anomalieEtabC16DatabaseWriter)
				.build();
	}
	
	@Bean
	ItemReader<Tuple> etablissementC16Reader() {
		return new EtablissementC16Reader();
	}
	
	@Bean
	ItemProcessor<Tuple,List<Anomalie>> etablissementValidationC16Processor() {
		return new EtablissementValidationC16Processor();
	}
	
	
	@StepScope
	@Bean
	ItemStreamWriter<List<Anomalie>>anomalieEtabC16DatabaseWriter() {
		return new AnomalieDatabaseWriter();
	}
}
