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

import com.geremi.bdrepbatch.job.dto.CarrEnvInfoGenDTO;
import com.geremi.bdrepbatch.job.mapper.CarrEnvInfoGenRowMapper;
import com.geremi.bdrepbatch.job.model.CarrEnvInfoGen;
import com.geremi.bdrepbatch.job.process.CarrEnvInfoGenExcelProcessor;
import com.geremi.bdrepbatch.job.reader.CarrEnvInfoGenExcelReader;
import com.geremi.bdrepbatch.job.writer.CarrEnvInfoGenWriter;

@Configuration
public class CarrEnvInfoGenExcelToDbStepConfiguration {
	
	@Value("${input.file.path}")
	private String inputFilePath;
	
	@Value("${sheet.carrEnvInfoGen.index}")
	private String sheetcarrEnvInfoGen;

	@Value("${sheet.carrEnvInfoGen.lines-to-skip}")
	private Integer linesToSkip;

	@Value("${sheet.carrEnvInfoGen.columns-to-skip}")
	private Integer columnsToSkip;
	
	@Value("${chunk.size.carrEnvInfoGen.excel-to-db}")
	private Integer chunkSize;
	
	private static final Logger log = LoggerFactory.getLogger(CarrEnvInfoGenExcelToDbStepConfiguration.class);
	
	@Bean
	public Step carrEnvInfoGenExcelToDbStep(CarrEnvInfoGenExcelReader carrEnvInfoGenExcelReader, 
			ItemProcessor<CarrEnvInfoGenDTO,CarrEnvInfoGen> carrEnvInfoGenExcelProcessor,
			ItemWriter<CarrEnvInfoGen> carrEnvInfoGenWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step carrEnvInfoGenExcelToDbStep");
		return new StepBuilder("carrEnvInfoGenExcelToDbStep",jobRepository)
				.<CarrEnvInfoGenDTO,CarrEnvInfoGen>chunk(chunkSize,transactionManager)//à gauche : ce qu'on prend en entrée, à droite : ce qui sort du traitement
				.reader(carrEnvInfoGenExcelReader)
				.processor(carrEnvInfoGenExcelProcessor)
				.writer(carrEnvInfoGenWriter)
				.build();
	}
	
	@Bean
	@StepScope
	CarrEnvInfoGenExcelReader carrEnvInfoGenExcelReader() {
		return new CarrEnvInfoGenExcelReader(inputFilePath, carrEnvInfoGenRowMapper(), sheetcarrEnvInfoGen, linesToSkip);
	}
	
	@Bean
	ItemProcessor<CarrEnvInfoGenDTO,CarrEnvInfoGen> carrEnvInfoGenExcelProcessor() {
		return new CarrEnvInfoGenExcelProcessor();
	}
	
	@Bean
	ItemWriter<CarrEnvInfoGen> carrEnvInfoGenWriter() throws Exception {
		return new CarrEnvInfoGenWriter();
	}
	
	@Bean
	CarrEnvInfoGenRowMapper carrEnvInfoGenRowMapper() {
		return new CarrEnvInfoGenRowMapper(columnsToSkip);
	}
}
