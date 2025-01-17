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

import com.geremi.bdrepbatch.job.dto.CarrProdExtractionDTO;
import com.geremi.bdrepbatch.job.mapper.CarrProdExtractionRowMapper;
import com.geremi.bdrepbatch.job.model.CarrProdExtraction;
import com.geremi.bdrepbatch.job.process.CarrProdExtractionExcelProcessor;
import com.geremi.bdrepbatch.job.reader.CarrProdExtractionExcelReader;
import com.geremi.bdrepbatch.job.writer.CarrProdExtractionWriter;

@Configuration
public class CarrProdExtractionExcelToDbStepConfiguration {
	
	@Value("${input.file.path}")
	private String inputFilePath;
	
	@Value("${sheet.carrProdExtraction.index}")
	private String sheetCarrProdExtraction;

	@Value("${sheet.carrProdExtraction.lines-to-skip}")
	private Integer linesToSkip;

	@Value("${sheet.carrProdExtraction.columns-to-skip}")
	private Integer columnsToSkip;
	
	@Value("${chunk.size.carrProdExtraction.excel-to-db}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(CarrProdExtractionExcelToDbStepConfiguration.class);
	
	@Bean
	public Step carrProdExtractionExcelToDbStep(CarrProdExtractionExcelReader carrProdExtractionExcelReader, 
			ItemProcessor<CarrProdExtractionDTO,CarrProdExtraction> carrProdExtractionExcelProcessor,
			ItemWriter<CarrProdExtraction> carrProdExtractionWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step carrProdExtractionExcelToDbStep");
		return new StepBuilder("carrProdExtractionExcelToDbStep",jobRepository)
				.<CarrProdExtractionDTO,CarrProdExtraction>chunk(chunkSize,transactionManager)//à gauche : ce qu'on prend en entrée, à droite : ce qui sort du traitement
				.reader(carrProdExtractionExcelReader)
				.processor(carrProdExtractionExcelProcessor)
				.writer(carrProdExtractionWriter)
				.build();
	}
	
	@Bean
	@StepScope
	CarrProdExtractionExcelReader carrProdExtractionExcelReader() {
		return new CarrProdExtractionExcelReader(inputFilePath, carrProdExtractionRowMapper(), sheetCarrProdExtraction, linesToSkip);
	}
	
	@Bean
	ItemProcessor<CarrProdExtractionDTO,CarrProdExtraction> carrProdExtractionExcelProcessor() {
		return new CarrProdExtractionExcelProcessor();
	}
	
	@Bean
	ItemWriter<CarrProdExtraction> carrProdExtractionWriter() throws Exception {
		return new CarrProdExtractionWriter();
	}
	
	@Bean
	CarrProdExtractionRowMapper carrProdExtractionRowMapper() {
		return new CarrProdExtractionRowMapper(columnsToSkip);
	}
}
