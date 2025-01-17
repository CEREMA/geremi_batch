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
import com.geremi.bdrepbatch.job.process.TraitementDechetValidationProcessor;
import com.geremi.bdrepbatch.job.reader.TraitementDechetValidationReader;
import com.geremi.bdrepbatch.job.writer.AnomalieDatabaseWriter;
import com.geremi.bdrepbatch.job.writer.AnomalieFileWriter;

import jakarta.persistence.Tuple;

@Configuration
public class TraitementDechetValidationStepConfiguration {
	
	@Value("${chunk.size.traitementdechet.validation}")
	private Integer chunkSize;

	
	private static final Logger log = LoggerFactory.getLogger(TraitementDechetValidationStepConfiguration.class);
	
	@Bean
	public Step traitementDechetValidationStep(ItemReader<Tuple> traitementDechetValidationReader, 
			ItemProcessor<Tuple,List<Anomalie>> validationProcessor, 
			CompositeItemWriter<List<Anomalie>> compositeItemWriter, 
			ItemStreamWriter<List<Anomalie>> anomalieDatabaseWriter, 
			FlatFileItemWriter<List<Anomalie>> anomalieFileWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step traitementDechetValidationStep");
		return new StepBuilder("TraitementDechetValidationStep",jobRepository)
				.<Tuple,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(traitementDechetValidationReader)
				.processor((ItemProcessor<? super Tuple, ? extends List<Anomalie>>) validationProcessor)
				.writer(compositeItemWriter)
				.stream(anomalieDatabaseWriter)
				.stream(anomalieFileWriter)
				.build();
	}
	
	@Bean
	TraitementDechetValidationReader traitementDechetValidationReader() {
		return new TraitementDechetValidationReader();
	}

	@Bean
	ItemProcessor<Tuple,List<Anomalie>>  validationProcessor() {

		return new TraitementDechetValidationProcessor();
	}
	
	@Bean
	ItemStreamWriter<List<Anomalie>>anomalieDatabaseWriter() {
		return new AnomalieDatabaseWriter();
	}
	
    @Bean
    @StepScope
    public AnomalieFileWriter anomalieFileWriter() {
    	return new AnomalieFileWriter();
    }
	  
	
	@Bean
	@StepScope
    public CompositeItemWriter<List<Anomalie>> compositeItemWriter(ItemWriter<List<Anomalie>> anomalieDatabaseWriter, FlatFileItemWriter<List<Anomalie>> anomalieFileWriter) throws Exception {
        CompositeItemWriter<List<Anomalie>>  compositeItemWriter = new CompositeItemWriter<>();
        List<org.springframework.batch.item.ItemWriter<? super List<Anomalie>>> delegates = new ArrayList<>(); 
        delegates.add(anomalieDatabaseWriter); 
        delegates.add(anomalieFileWriter); 
        compositeItemWriter.setDelegates(delegates);
        return compositeItemWriter;
    }
	
	
}
