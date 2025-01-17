package com.geremi.bdrepbatch.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.geremi.bdrepbatch.job.dto.CarrProdDestinationDTO;
import com.geremi.bdrepbatch.job.mapper.CarrProdDestinationRowMapper;
import com.geremi.bdrepbatch.job.model.CarrProdDestination;
import com.geremi.bdrepbatch.job.process.CarrProdDestinationExcelProcessor;
import com.geremi.bdrepbatch.job.reader.CarrProdDestinationExcelReader;
import com.geremi.bdrepbatch.job.writer.CarrProdDestinationWriter;

@Configuration
public class CarrProdDestinationExcelToDbStepConfiguration {
	
	@Value("${input.file.path}")
	private String inputFilePath;
	
	@Value("${sheet.carrProdDestination.index}")
	private String sheetCarrProdDestination;

	@Value("${sheet.carrProdDestination.lines-to-skip}")
	private Integer linesToSkip;

	@Value("${sheet.carrProdDestination.columns-to-skip}")
	private Integer columnsToSkip;

	@Value("${chunk.size.carrProdDestination.excel-to-db}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(CarrProdDestinationExcelToDbStepConfiguration.class);
	
	@Bean
	public Step carrProdDestinationExcelToDbStep(CarrProdDestinationExcelReader carrProdDestinationExcelReader, 
			ItemProcessor<CarrProdDestinationDTO,CarrProdDestination> carrProdDestinationExcelProcessor,
			ItemWriter<CarrProdDestination> carrProdDestinationWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step carrProdDestinationExcelToDbStep");
		return new StepBuilder("carrProdDestinationExcelToDbStep",jobRepository)
				.<CarrProdDestinationDTO,CarrProdDestination>chunk(chunkSize,transactionManager)//à gauche : ce qu'on prend en entrée, à droite : ce qui sort du traitement
				.reader(carrProdDestinationExcelReader)
				.processor(carrProdDestinationExcelProcessor)
				.writer(carrProdDestinationWriter)
				.build();
	}
	
	@Bean
	@StepScope
	CarrProdDestinationExcelReader carrProdDestinationExcelReader() {
		return new CarrProdDestinationExcelReader(inputFilePath, carrProdDestinationRowMapper(), sheetCarrProdDestination, linesToSkip);
	}
	
	@Bean
	ItemProcessor<CarrProdDestinationDTO,CarrProdDestination> carrProdDestinationExcelProcessor() {
		return new CarrProdDestinationExcelProcessor();
	}
	
	@Bean
	ItemWriter<CarrProdDestination> carrProdDestinationWriter() throws Exception {
		return new CarrProdDestinationWriter();
	}
	
	@Bean
	CarrProdDestinationRowMapper carrProdDestinationRowMapper() {
		return new CarrProdDestinationRowMapper(columnsToSkip);
	}
}
