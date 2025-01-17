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
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.process.EtablissementValidationR14Processor;
import com.geremi.bdrepbatch.job.reader.EtablissementR17Reader;
import com.geremi.bdrepbatch.job.writer.AnomalieFileWriter;

import jakarta.persistence.Tuple;

@Configuration
public class EtablissementValidationR14StepConfiguration {
	
	@Value("${chunk.size.etablissement.r14.validation}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(EtablissementValidationR14StepConfiguration.class);
	
	@Bean
	public Step etablissementValidationR14Step(ItemReader<String> etablissementR17Reader,
			ItemProcessor<String,List<Anomalie>> etablissementValidationR14Processor,
			FlatFileItemWriter<List<Anomalie>> etablissementAnomalieR14FileWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step etablissementValidationR14Step");
		return new StepBuilder("etablissementValidationR14Step",jobRepository)
				.<String,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(etablissementR17Reader)
				.processor(etablissementValidationR14Processor)
				.writer(etablissementAnomalieR14FileWriter)
				.build();
	}
	
	@Bean
	EtablissementR17Reader etablissementR17Reader() {
		return new EtablissementR17Reader();
	}
	
	@Bean
	ItemProcessor<String,List<Anomalie>> etablissementValidationR14Processor() {
		return new EtablissementValidationR14Processor();
	}
	
	@Bean
	@StepScope
    public AnomalieFileWriter etablissementAnomalieR14FileWriter() throws Exception {
    	return new AnomalieFileWriter();
    }
	
}
