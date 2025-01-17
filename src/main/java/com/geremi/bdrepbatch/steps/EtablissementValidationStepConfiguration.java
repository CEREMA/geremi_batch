package com.geremi.bdrepbatch.steps;

import java.util.ArrayList;
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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.process.EtablissementValidationProcessor;
import com.geremi.bdrepbatch.job.reader.EtablissementReader;
import com.geremi.bdrepbatch.job.writer.AnomalieDatabaseWriter;
import com.geremi.bdrepbatch.job.writer.AnomalieFileWriter;

import jakarta.persistence.Tuple;

@Configuration
public class EtablissementValidationStepConfiguration {

	
	@Value("${chunk.size.etablissement.validation}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(EtablissementValidationStepConfiguration.class);
	
	@Bean
	public Step etablissementValidationStep(ItemReader<Tuple> etablissementValidationReader, 
			ItemProcessor<Tuple,List<Anomalie>> etablissementValidationProcessor, CompositeItemWriter<List<Anomalie>> compositeItemEtabWriter, 
			ItemStreamWriter<List<Anomalie>> anomalieEtabDatabaseWriter, FlatFileItemWriter<List<Anomalie>> anomalieEtabFileWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step etablissementValidationStep");
		return new StepBuilder("etablissementValidationStep",jobRepository)
				.<Tuple,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(etablissementValidationReader)
				.processor((ItemProcessor<? super Tuple, ? extends List<Anomalie>>) etablissementValidationProcessor)
				.writer(compositeItemEtabWriter)
				.stream(anomalieEtabDatabaseWriter)
				.stream(anomalieEtabFileWriter)
				.build();
	}
	
	@Bean
	EtablissementReader	 etablissementValidationReader() {
		return new EtablissementReader();
	}

	@Bean
	@StepScope
	EtablissementValidationProcessor etablissementValidationProcessor() {
		log.info("Création ValidationProcessor");
		return new EtablissementValidationProcessor();

	}
	
	@Bean
	ItemStreamWriter<List<Anomalie>>anomalieEtabDatabaseWriter() {
		return new AnomalieDatabaseWriter();
	}
	
    @Bean
    @StepScope
    public AnomalieFileWriter anomalieEtabFileWriter() throws Exception {
    	return new AnomalieFileWriter();
    }
	  
	
	@Bean
	@StepScope
    public CompositeItemWriter<List<Anomalie>> compositeItemEtabWriter(ItemWriter<List<Anomalie>> anomalieEtabDatabaseWriter, FlatFileItemWriter<List<Anomalie>> anomalieEtabFileWriter) throws Exception {
        CompositeItemWriter<List<Anomalie>>  compositeItemWriter = new CompositeItemWriter<>();
        List<org.springframework.batch.item.ItemWriter<? super List<Anomalie>>> delegates = new ArrayList<>(); 
        delegates.add(anomalieEtabDatabaseWriter); 
        delegates.add(anomalieEtabFileWriter); 
        compositeItemWriter.setDelegates(delegates);
        return compositeItemWriter;
    }
}
