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

import com.geremi.bdrepbatch.job.dto.TraitementDechetDTO;
import com.geremi.bdrepbatch.job.mapper.TraitementDechetRowMapper;
import com.geremi.bdrepbatch.job.model.TraitementDechet;
import com.geremi.bdrepbatch.job.process.TraitementDechetExcelProcessor;
import com.geremi.bdrepbatch.job.reader.TraitementDechetReader;
import com.geremi.bdrepbatch.job.writer.TraitementDechetWriter;

@Configuration
public class TraitementDechetExcelToDbStepConfiguration {

	
	@Value("${input.file.path}")
	private String inputFilePath;
	
	@Value("${sheet.traitementDechet.index}")
	private String sheetTraitementDechet;
	
	@Value("${chunk.size.traitementdechet.excel-to-db}")
	private Integer chunkSize;

	@Value("${sheet.traitementDechet.lines-to-skip}")
	private Integer linesToSkip;

	@Value("${sheet.traitementDechet.columns-to-skip}")
	private Integer columnsToSkip;
	
	private static final Logger log = LoggerFactory.getLogger(TraitementDechetExcelToDbStepConfiguration.class);
	
	@Bean
	public Step traitementDechetExcelToDbStep(ItemReader<TraitementDechetDTO> traitementDechetReader, ItemProcessor<TraitementDechetDTO,TraitementDechet>traitementDechetProcessor,ItemWriter<TraitementDechet> traitementDechetWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step traitementDechetExcelToDbStep");
		return new StepBuilder("traitementDechetExcelToDbStep",jobRepository)
				.<TraitementDechetDTO,TraitementDechet>chunk(chunkSize, transactionManager)//à gauche : ce qu'on prend en entrée, à droite : ce qui sort du traitement
				.reader(traitementDechetReader)
				.processor(traitementDechetProcessor)
				.writer(traitementDechetWriter)
				.build();
	}
	
	@Bean
	@StepScope
	TraitementDechetReader traitementDechetReader() {
		return new TraitementDechetReader(inputFilePath, traitementDechetRowMapper(), sheetTraitementDechet, linesToSkip);
	}
	
	@Bean
	ItemProcessor<TraitementDechetDTO,TraitementDechet> traitementDechetProcessor() {

		log.debug("Creating traitementDechetProcessor bean");
		return new TraitementDechetExcelProcessor();

	}
	
	@Bean
	ItemWriter<TraitementDechet> traitementDechetWriter() throws Exception {
		return new TraitementDechetWriter();
	}
	
	@Bean
	TraitementDechetRowMapper traitementDechetRowMapper() {
		return new TraitementDechetRowMapper(columnsToSkip);
	}

	
}
