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
import com.geremi.bdrepbatch.job.process.CarrProdExtractionValidationProcessor;
import com.geremi.bdrepbatch.job.reader.CarrProdExtractionReader;
import com.geremi.bdrepbatch.job.writer.AnomalieDatabaseWriter;
import com.geremi.bdrepbatch.job.writer.AnomalieFileWriter;

import jakarta.persistence.Tuple;

@Configuration
public class CarrProdExtractionValidationStepConfiguration {
	
	@Value("${chunk.size.carrProdExtraction.validation}")
	private Integer chunkSize;

	
	private static final Logger log = LoggerFactory.getLogger(CarrProdExtractionValidationStepConfiguration.class);
	
	@Bean
	public Step carrProdExtractionValidationStep(ItemReader<Tuple>carrProdExtractionReader,
			ItemProcessor<Tuple,List<Anomalie>> carrProdExtractionValidationProcessor,
			CompositeItemWriter<List<Anomalie>> compositeItemCarrProdExtractionWriter,
			ItemStreamWriter<List<Anomalie>> anomalieCarrProdExtractionDatabaseWriter, 
			FlatFileItemWriter<List<Anomalie>> anomalieCarrProdExtractionFileWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		return new StepBuilder("carrProdExtractionValidationStep",jobRepository)
				.<Tuple,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(carrProdExtractionReader)
				.processor(carrProdExtractionValidationProcessor)
				.writer(compositeItemCarrProdExtractionWriter)
				.stream(anomalieCarrProdExtractionDatabaseWriter)
				.stream(anomalieCarrProdExtractionFileWriter)
				.build();
	}
	
	@Bean
	CarrProdExtractionReader carrProdExtractionReader() {
		log.info("Création CarrProdExtractionReader");
		return new CarrProdExtractionReader();
	}

	@Bean
	ItemProcessor<Tuple,List<Anomalie>> carrProdExtractionValidationProcessor() {
		log.info("Création carrProdExtractionValidationProcessor");
		return new CarrProdExtractionValidationProcessor();
	}
	
	@Bean
	ItemStreamWriter<List<Anomalie>> anomalieCarrProdExtractionDatabaseWriter() {
		log.info("Création ValidationWriter");
		return new AnomalieDatabaseWriter();
	}
	
    @Bean
    @StepScope
    public AnomalieFileWriter anomalieCarrProdExtractionFileWriter() throws Exception {
    	log.info("Création AnomalieFileWriter");
    	return new AnomalieFileWriter();
    }
	  
	
	@Bean
	@StepScope
    public CompositeItemWriter<List<Anomalie>> compositeItemCarrProdExtractionWriter(ItemWriter<List<Anomalie>> anomalieCarrProdExtractionDatabaseWriter, 
    		FlatFileItemWriter<List<Anomalie>> anomalieCarrProdExtractionFileWriter) throws Exception {
        CompositeItemWriter<List<Anomalie>>  compositeItemWriter = new CompositeItemWriter<>();
        List<org.springframework.batch.item.ItemWriter<? super List<Anomalie>>> delegates = new ArrayList<>(); 
        delegates.add(anomalieCarrProdExtractionDatabaseWriter); 
        delegates.add(anomalieCarrProdExtractionFileWriter); 
        compositeItemWriter.setDelegates(delegates);
        return compositeItemWriter;
    }
}
