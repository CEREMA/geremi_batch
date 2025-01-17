package com.geremi.bdrepbatch.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.geremi.bdrepbatch.ImportJobConfig;
import com.geremi.bdrepbatch.job.dto.DeclarationDTO;
import com.geremi.bdrepbatch.job.mapper.DeclarationRowMapper;
import com.geremi.bdrepbatch.job.model.Declaration;
import com.geremi.bdrepbatch.job.process.DeclarationExcelProcessor;
import com.geremi.bdrepbatch.job.reader.DeclarationExcelReader;
import com.geremi.bdrepbatch.job.writer.DeclarationWriter;

@Configuration
public class DeclarationExcelToDbStepConfiguration {
	
	@Value("${input.file.path}")
	private String inputFilePath;
	
	@Value("${sheet.declaration.index}")
	private String sheetDeclaration;

	@Value("${sheet.declaration.lines-to-skip}")
	private Integer linesToSkip;

	@Value("${sheet.declaration.columns-to-skip}")
	private Integer columnsToSkip;
	
	@Value("${chunk.size.declaration.excel-to-db}")
	private Integer chunkSize;

	
	private static final Logger log = LoggerFactory.getLogger(ImportJobConfig.class);
	
	@Bean
	public Step declarationExcelToDbStep(ItemReader<DeclarationDTO> declarationReader, 
			ItemProcessor<DeclarationDTO,Declaration>declarationProcessor,
			ItemWriter<Declaration> declarationWriter,
			JobRepository jobRepository,
			PlatformTransactionManager transactionManager) {
		log.debug("Creating Step declarationExcelToDbStep");
		return new StepBuilder("declarationExcelToDbStep",jobRepository)
				.<DeclarationDTO,Declaration>chunk(chunkSize,transactionManager)
				.reader(declarationReader)
				.processor(declarationProcessor)
				.writer(declarationWriter)
				.build();
	}
	
	@Bean
	@StepScope
	DeclarationExcelReader declarationReader() {
		log.debug("Creating DeclarationReader bean");
		return new DeclarationExcelReader(inputFilePath, declarationRowMapper(), sheetDeclaration, linesToSkip);

	}
	
	@Bean
	ItemProcessor<DeclarationDTO,Declaration> declarationProcessor() {
		log.debug("Creating DeclarationProcessor bean");
		return new DeclarationExcelProcessor();

	}
	
	@Bean
	ItemWriter<Declaration> declarationWriter() throws Exception {
		return new DeclarationWriter();
	}
	
	@Bean
	DeclarationRowMapper declarationRowMapper() {
		return new DeclarationRowMapper(columnsToSkip);
	}

}
