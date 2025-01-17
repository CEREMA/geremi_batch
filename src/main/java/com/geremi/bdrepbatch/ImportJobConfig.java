package com.geremi.bdrepbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.geremi.bdrepbatch.job.decider.FileExistenceCheckDecider;
import com.geremi.bdrepbatch.job.decider.ValidationDecider;
import com.geremi.bdrepbatch.job.model.Entreprise;
import com.geremi.bdrepbatch.job.repository.EtablissementRepository;
import com.geremi.bdrepbatch.job.repository.RefAnomalieRepository;
import com.geremi.bdrepbatch.job.writer.EntrepriseJdbcWriter;
import com.geremi.bdrepbatch.tasklet.CleanUpTasklet;
import com.geremi.bdrepbatch.tasklet.FileExistenceCheckTasklet;
import com.geremi.bdrepbatch.tasklet.SetGlobalParametersTasklet;
import com.geremi.bdrepbatch.tasklet.ToFinalDbTasklet;

@Configuration
public class ImportJobConfig {

	private static final Logger log = LoggerFactory.getLogger(ImportJobConfig.class);

	@Value("${input.file.path}")
	private String inputFilePath;

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Value("${spring.datasource.driverClassName}")
	private String dataSourceDriverClassName;

	@Value("${spring.datasource.username}")
	private String dataSourceUsername;

	@Value("${spring.datasource.password}")
	private String dataSourcePassword;
	
	@Autowired
	private EtablissementRepository etablissementRepository;
	
	@Autowired
	private RefAnomalieRepository refAnomalieRepository;

	@Bean
	FileExistenceCheckTasklet fileExistenceCheckTasklet() {
		return new FileExistenceCheckTasklet(inputFilePath);
	}
	
	@Bean
	SetGlobalParametersTasklet setGlobalParametersTasklet() {
		return new SetGlobalParametersTasklet(etablissementRepository,refAnomalieRepository);
	}
	
	@Bean
	CleanUpTasklet cleanUpTasklet() {
		return new CleanUpTasklet();
	}
	
	@Bean
	ToFinalDbTasklet toFinalDbTasklet() {
		return new ToFinalDbTasklet();
	}

	@Bean
	FileExistenceCheckDecider fileExistenceCheckDecider() {
		return new FileExistenceCheckDecider();
	}
	
	@Bean
	ValidationDecider validationDecider() {
		return new ValidationDecider();
	}

	//////////////////////// CSV Entreprise //////////////////////////////////

	@Bean
	ItemWriter<Entreprise> entrepriseJdbcWriter() {
		return new EntrepriseJdbcWriter();
	}

	//////////////////////////////////////////////////////////////////////////

	

	//////////////////////////////////////////////////////////////////////////

	@Bean
	@JobScope
	Step cleanUpStep(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
		log.debug("Creating Step cleanUpStep");
		return new StepBuilder("cleanUpStep",jobRepository).tasklet(cleanUpTasklet(),transactionManager)
				.listener(cleanUpTasklet()).build();
	}
	
	@Bean
	Step checkFileExistenceStep(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
		log.debug("Creating Step CheckFileExistenceStep");
		return new StepBuilder("checkFileExistenceStep",jobRepository).tasklet(fileExistenceCheckTasklet(),transactionManager)
				.listener(fileExistenceCheckTasklet()).build();
	}
	
	@Bean
	@JobScope
	Step setGlobalParametersStep(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
		log.debug("Creating Step setGlobalParametersStep");
		return new StepBuilder("setGlobalParametersStep",jobRepository).tasklet(setGlobalParametersTasklet(),transactionManager)
				.listener(setGlobalParametersTasklet()).build();
	}
	
	
	@Bean
	Step toFinalDbStep(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
		log.debug("Creating Step toFinalDbStep");
		return new StepBuilder("toFinalDbStep",jobRepository).tasklet(toFinalDbTasklet(),transactionManager)
				.listener(toFinalDbTasklet()).build();
	}
	
	@Bean
	public Flow finOK(Step toFinalDbStep) {
		return new FlowBuilder<Flow>("finOK")
				.start(toFinalDbStep).end();
	}

	@Bean
	public Flow finKO(Step cleanUpStep) {
		return new FlowBuilder<Flow>("finKO")
				.start(cleanUpStep).end();
	}
	
	@Bean
	public Job importJob(Step cleanUpStep,Step checkFileExistenceStep,
			Step etablissementExcelToDbStep,Step traitementDechetExcelToDbStep, Step declarationExcelToDbStep,Step carrProdExtractionExcelToDbStep,
			Step carrProdDestinationExcelToDbStep,Step carrEnvInfoGenExcelToDbStep,
			Step setGlobalParametersStep, 
			Step traitementDechetValidationStep, Step declarationValidationR14Step, Step declarationValidationStep, 
			Step etablissementValidationR14Step, 
			Step carrProdExtractionValidationStep,Step carrProdDestinationValidationStep,
			Step carrEnvInfoGenValidationStep,
			Step etablissementValidationStep, 
			Step etablissementValidationC16Step,
			Flow finOK, Flow finKO,JobRepository jobRepository ) throws Exception {
		log.debug("Creating Job ImportJob");
		return new JobBuilder("importJob",jobRepository).incrementer(new RunIdIncrementer()).preventRestart()
				.start(cleanUpStep)
				.next(checkFileExistenceStep).next(fileExistenceCheckDecider())
				.on(FlowExecutionStatus.FAILED.getName()).end().from(fileExistenceCheckDecider())
				.on(FlowExecutionStatus.COMPLETED.getName())
				.to(traitementDechetExcelToDbStep)
				.next(declarationExcelToDbStep)
				.next(etablissementExcelToDbStep)
				.next(carrProdExtractionExcelToDbStep)
				.next(carrProdDestinationExcelToDbStep)
				.next(carrEnvInfoGenExcelToDbStep)
				.next(setGlobalParametersStep)
				.next(traitementDechetValidationStep)
				.next(declarationValidationR14Step)
				.next(declarationValidationStep)
				.next(carrEnvInfoGenValidationStep)
				.next(etablissementValidationR14Step)
				.next(etablissementValidationStep)
				.next(etablissementValidationC16Step)
				.next(carrProdExtractionValidationStep)
				.next(carrProdDestinationValidationStep)
				.next(validationDecider())
					.on("ERREUR_BLOQUANTE").to(finKO)
				.from(validationDecider())
					.on(FlowExecutionStatus.COMPLETED.getName()).to(finOK)
				.end().build();
	}

}
