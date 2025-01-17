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
import com.geremi.bdrepbatch.job.process.CarrProdDestinationValidationProcessor;
import com.geremi.bdrepbatch.job.reader.CarrProdDestinationReader;
import com.geremi.bdrepbatch.job.writer.AnomalieDatabaseWriter;
import com.geremi.bdrepbatch.job.writer.AnomalieFileWriter;

import jakarta.persistence.Tuple;

@Configuration
public class CarrProdDestinationValidationStepConfiguration {
	
	@Value("${chunk.size.carrProdDestination.validation}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(CarrProdDestinationValidationStepConfiguration.class);
	
	@Bean
	public Step carrProdDestinationValidationStep(ItemReader<Tuple>carrProdDestinationReader,
			ItemProcessor<Tuple,List<Anomalie>> carrProdDestinationValidationProcessor,
			CompositeItemWriter<List<Anomalie>> compositeItemCarrProdDestinationWriter,
			ItemStreamWriter<List<Anomalie>> anomalieCarrProdDestinationDatabaseWriter, 
			FlatFileItemWriter<List<Anomalie>> anomalieCarrProdDestinationFileWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		return new StepBuilder("carrProdDestinationValidationStep",jobRepository)
				.<Tuple,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(carrProdDestinationReader)
				.processor(carrProdDestinationValidationProcessor)
				.writer(compositeItemCarrProdDestinationWriter)
				.stream(anomalieCarrProdDestinationDatabaseWriter)
				.stream(anomalieCarrProdDestinationFileWriter)
				.build();
	}
	
	@Bean
	CarrProdDestinationReader carrProdDestinationReader() {
		log.info("Création CarrProdDestinationReader");
		return new CarrProdDestinationReader();
	}

	@Bean
	ItemProcessor<Tuple,List<Anomalie>> carrProdDestinationValidationProcessor() {
		log.info("Création carrProdDestinationValidationProcessor");
		return new CarrProdDestinationValidationProcessor();
	}
	
	@Bean
	ItemStreamWriter<List<Anomalie>> anomalieCarrProdDestinationDatabaseWriter() {
		log.info("Création ValidationWriter");
		return new AnomalieDatabaseWriter();
	}
	
    @Bean
    @StepScope
    public AnomalieFileWriter anomalieCarrProdDestinationFileWriter() throws Exception {
    	log.info("Création AnomalieFileWriter");
    	return new AnomalieFileWriter();
    }
	  
	
	@Bean
	@StepScope
    public CompositeItemWriter<List<Anomalie>> compositeItemCarrProdDestinationWriter(ItemWriter<List<Anomalie>> anomalieCarrProdDestinationDatabaseWriter, 
    		FlatFileItemWriter<List<Anomalie>> anomalieCarrProdDestinationFileWriter) throws Exception {
        CompositeItemWriter<List<Anomalie>>  compositeItemWriter = new CompositeItemWriter<>();
        List<org.springframework.batch.item.ItemWriter<? super List<Anomalie>>> delegates = new ArrayList<>(); 
        delegates.add(anomalieCarrProdDestinationDatabaseWriter); 
        delegates.add(anomalieCarrProdDestinationFileWriter); 
        compositeItemWriter.setDelegates(delegates);
        return compositeItemWriter;
    }
}
