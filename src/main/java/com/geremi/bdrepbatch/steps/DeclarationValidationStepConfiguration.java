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
import com.geremi.bdrepbatch.job.process.DeclarationValidationProcessor;
import com.geremi.bdrepbatch.job.reader.DeclarationReader;
import com.geremi.bdrepbatch.job.writer.AnomalieDatabaseWriter;
import com.geremi.bdrepbatch.job.writer.AnomalieFileWriter;

import jakarta.persistence.Tuple;
@Configuration
public class DeclarationValidationStepConfiguration {
	
	@Value("${chunk.size.declaration.validation}")
	private Integer chunkSize;

	
	private static final Logger log = LoggerFactory.getLogger(DeclarationValidationStepConfiguration.class);
	
	@Bean
	public Step declarationValidationStep(ItemReader<Tuple>declarationValidationReader,
			ItemProcessor<Tuple,List<Anomalie>> declarationValidationProcessor,
			CompositeItemWriter<List<Anomalie>> compositeItemDeclarationWriter,
			ItemStreamWriter<List<Anomalie>> anomalieDeclarationDatabaseWriter, 
			FlatFileItemWriter<List<Anomalie>> anomalieDeclarationFileWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		return new StepBuilder("declarationValidationStep",jobRepository)
				.<Tuple,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(declarationValidationReader)
				.processor(declarationValidationProcessor)
				.writer(compositeItemDeclarationWriter)
				.stream(anomalieDeclarationDatabaseWriter)
				.stream(anomalieDeclarationFileWriter)
				.build();
	}
	
	@Bean
	DeclarationReader declarationValidationReader() {
		log.info("Création declarationValidationReader");
		return new DeclarationReader();
	}

	@Bean
	ItemProcessor<Tuple,List<Anomalie>> declarationValidationProcessor() {
		log.info("Création ValidationProcessor");
		return new DeclarationValidationProcessor();
	}
	
	@Bean
	ItemStreamWriter<List<Anomalie>> anomalieDeclarationDatabaseWriter() {
		log.info("Création ValidationWriter");
		return new AnomalieDatabaseWriter();
	}
	
    @Bean
    @StepScope
    public AnomalieFileWriter anomalieDeclarationFileWriter() throws Exception {
    	log.info("Création AnomalieFileWriter");
    	return new AnomalieFileWriter();
    }
	  
	
	@Bean
	@StepScope
    public CompositeItemWriter<List<Anomalie>> compositeItemDeclarationWriter(ItemWriter<List<Anomalie>> anomalieDeclarationDatabaseWriter, 
    		FlatFileItemWriter<List<Anomalie>> anomalieDeclarationFileWriter) throws Exception {
        CompositeItemWriter<List<Anomalie>>  compositeItemWriter = new CompositeItemWriter<>();
        List<org.springframework.batch.item.ItemWriter<? super List<Anomalie>>> delegates = new ArrayList<>(); 
        delegates.add(anomalieDeclarationDatabaseWriter); 
        delegates.add(anomalieDeclarationFileWriter); 
        compositeItemWriter.setDelegates(delegates);
        return compositeItemWriter;
    }

}
