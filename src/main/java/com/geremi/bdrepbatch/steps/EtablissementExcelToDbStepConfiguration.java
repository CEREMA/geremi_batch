package com.geremi.bdrepbatch.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.geremi.bdrepbatch.job.dto.EtablissementDTO;
import com.geremi.bdrepbatch.job.mapper.EtablissementExcelRowMapper;
import com.geremi.bdrepbatch.job.model.Etablissement;
import com.geremi.bdrepbatch.job.process.EtablissementExcelProcessor;
import com.geremi.bdrepbatch.job.reader.EtablissementExcelReader;
import com.geremi.bdrepbatch.job.writer.EtablissementJdbcWriter;

@Configuration
public class EtablissementExcelToDbStepConfiguration {
	
	@Value("${input.file.path}")
	private String inputFilePath;
	
	@Value("${sheet.etablissement.index}")
	private String sheetEtablissement;

	@Value("${sheet.etablissement.lines-to-skip}")
	private Integer linesToSkip;

	@Value("${sheet.etablissement.columns-to-skip}")
	private Integer columnsToSkip;
	
	@Value("${chunk.size.etablissement.excel-to-db}")
	private Integer chunkSize;

	
	private static final Logger log = LoggerFactory.getLogger(EtablissementExcelToDbStepConfiguration.class);
	
	
	@Bean
	public Step etablissementExcelToDbStep(EtablissementExcelReader etablissementExcelReader, 
			ItemProcessor<EtablissementDTO,Etablissement> etablissementExcelProcessor,
			ItemWriter<Etablissement> etablissementJdbcWriter,JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("Creating Step etablissementExcelToDbStep");
		return new StepBuilder("etablissementExcelToDbStep",jobRepository)
				.<EtablissementDTO,Etablissement>chunk(chunkSize,transactionManager)//à gauche : ce qu'on prend en entrée, à droite : ce qui sort du traitement
				.reader(etablissementExcelReader)
				.processor(etablissementExcelProcessor)
				.writer(etablissementJdbcWriter)
				.build();
	}
	
	@Bean
	@StepScope
	EtablissementExcelReader etablissementExcelReader() {
		return new EtablissementExcelReader(inputFilePath, etablissementExcelRowMapper(), sheetEtablissement, linesToSkip);
	}
	
	@Bean
	ItemProcessor<EtablissementDTO,Etablissement> etablissementExcelProcessor() {
		return new EtablissementExcelProcessor();
	}
	
	@Bean
	ItemWriter<Etablissement> etablissementJdbcWriter() throws Exception {
		return new EtablissementJdbcWriter();
	}
	
	@Bean
	EtablissementExcelRowMapper etablissementExcelRowMapper() {
		return new EtablissementExcelRowMapper(columnsToSkip);
	}
	
}

