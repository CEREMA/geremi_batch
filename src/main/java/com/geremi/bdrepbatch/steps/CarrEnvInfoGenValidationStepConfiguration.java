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
import com.geremi.bdrepbatch.job.process.CarrEnvInfoGenValidationProcessor;
import com.geremi.bdrepbatch.job.reader.CarrEnvInfoGenReader;
import com.geremi.bdrepbatch.job.writer.AnomalieDatabaseWriter;
import com.geremi.bdrepbatch.job.writer.AnomalieFileWriter;

import jakarta.persistence.Tuple;

@Configuration
public class CarrEnvInfoGenValidationStepConfiguration {
	
	@Value("${chunk.size.carrEnvInfoGen.validation}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(CarrEnvInfoGenValidationStepConfiguration.class);
	
	@Bean
	public Step carrEnvInfoGenValidationStep(ItemReader<Tuple> carrEnvInfoGenValidationReader,
			ItemProcessor<Tuple,List<Anomalie>> carrEnvInfoGenValidationProcessor,
			CompositeItemWriter<List<Anomalie>> compositeCarrEnvInfoGenItemWriter, 
			ItemStreamWriter<List<Anomalie>> anomalieCarrEnvInfoGenDatabaseWriter, 
			FlatFileItemWriter<List<Anomalie>> anomalieCarrEnvInfoGenFileWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		return new StepBuilder("CarrEnvInfoGenValidationStep",jobRepository)
				.<Tuple,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(carrEnvInfoGenValidationReader)
				.processor(carrEnvInfoGenValidationProcessor)
				.writer(compositeCarrEnvInfoGenItemWriter)
				.stream(anomalieCarrEnvInfoGenDatabaseWriter)
				.stream(anomalieCarrEnvInfoGenFileWriter)
				.build();
	}
	
	@Bean
	CarrEnvInfoGenReader carrEnvInfoGenValidationReader() {
		log.info("Création CarrEnvInfoGenReader");
		return new CarrEnvInfoGenReader();
	}
	
	@Bean
	CarrEnvInfoGenValidationProcessor carrEnvInfoGenValidationProcessor() {
		log.info("Création CarrEnvInfoGenValidationProcessor");
		return new CarrEnvInfoGenValidationProcessor();
	}
	
	
	@Bean
	ItemStreamWriter<List<Anomalie>>anomalieCarrEnvInfoGenDatabaseWriter() {
		return new AnomalieDatabaseWriter();
	}
	
    @Bean
    @StepScope
    public AnomalieFileWriter anomalieCarrEnvInfoGenFileWriter() {
    	return new AnomalieFileWriter();
    }
	  
	
	@Bean
	@StepScope
    public CompositeItemWriter<List<Anomalie>> compositeCarrEnvInfoGenItemWriter(ItemWriter<List<Anomalie>> anomalieCarrEnvInfoGenDatabaseWriter, FlatFileItemWriter<List<Anomalie>> anomalieCarrEnvInfoGenFileWriter) throws Exception {
        CompositeItemWriter<List<Anomalie>>  compositeItemWriter = new CompositeItemWriter<>();
        List<org.springframework.batch.item.ItemWriter<? super List<Anomalie>>> delegates = new ArrayList<>(); 
        delegates.add(anomalieCarrEnvInfoGenDatabaseWriter); 
        delegates.add(anomalieCarrEnvInfoGenFileWriter); 
        compositeItemWriter.setDelegates(delegates);
        return compositeItemWriter;
    }
}

