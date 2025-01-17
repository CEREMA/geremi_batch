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
import com.geremi.bdrepbatch.job.process.DeclarationValidationR14Processor;
import com.geremi.bdrepbatch.job.reader.DeclarationR17Reader;
import com.geremi.bdrepbatch.job.writer.AnomalieFileWriter;


@Configuration
public class DeclarationValidationR14StepConfiguration {
	
	@Value("${chunk.size.declaration.r14.validation}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(DeclarationValidationR14StepConfiguration.class);
	
	@Bean
	public Step declarationValidationR14Step(ItemReader<String> declarationR17Reader,
			ItemProcessor<String,List<Anomalie>> declarationValidationR14Processor,
			FlatFileItemWriter<List<Anomalie>> declarationAnomalieR14FileWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		return new StepBuilder("declarationValidationR14Step",jobRepository)
				.<String,List<Anomalie>>chunk(chunkSize,transactionManager)
				.reader(declarationR17Reader)
				.processor(declarationValidationR14Processor)
				.writer(declarationAnomalieR14FileWriter)
				.build();
	}
	
	@Bean
	DeclarationR17Reader declarationR17Reader() {
		log.info("Création DeclarationR17Reader");
		return new DeclarationR17Reader();
	}
	
	@Bean
	ItemProcessor<String,List<Anomalie>> declarationValidationR14Processor() {
		log.info("Création DeclarationValidationR14Processor");
		return new DeclarationValidationR14Processor();
	}
	
	@Bean
	@StepScope
    public AnomalieFileWriter declarationAnomalieR14FileWriter() throws Exception {
		log.info("Création AnomalieFileWriter");
    	return new AnomalieFileWriter();
    }
}
