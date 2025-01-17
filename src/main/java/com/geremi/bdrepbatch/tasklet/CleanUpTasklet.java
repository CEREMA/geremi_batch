package com.geremi.bdrepbatch.tasklet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

public class CleanUpTasklet implements Tasklet, StepExecutionListener{

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Value("${spring.datasource.username}")
	private String dataSourceUsername;

	@Value("${spring.datasource.password}")
	private String dataSourcePassword;
	
	@Value("${output.file.path.errors.tmp}")
	private String outputFilePathErrorsTemp;
	
	@Value("${output.file.path.errors.final}")
	private String outputFilePathErrorsFinal;
	
	private StepExecution stepExecution;
	
	private static final Logger log = LoggerFactory.getLogger(CleanUpTasklet.class);
	
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		//la DB temporaire n'est vidée qu'en début d'exécution ou en cas de success
		if(chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("hasBloquante")==null) {
			cleanDatabases();	
		}

		if(Files.exists(Path.of(outputFilePathErrorsTemp))){
			moveAndRenameErrorsFile();	
		}
		
		return RepeatStatus.FINISHED;
	}
	
	private void moveAndRenameErrorsFile() {
		//déplacer le fichier et le renommer en indiquant la date
		try {
			Path source = Paths.get(outputFilePathErrorsTemp);
		
			String fileName = FilenameUtils.removeExtension(source.getFileName().toString());
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-dd-M--HH-mm-ss");
			
		    Path target = Paths.get(outputFilePathErrorsFinal + fileName +"_"+ LocalDateTime.now().format(dtf)+".txt");
	
		    Files.move(source, target,StandardCopyOption.REPLACE_EXISTING);
			
		}catch(IOException f){
			f.printStackTrace();
		}
		
	}

	private void cleanDatabases() {
		//vidage de la base
		try(Connection conn = DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword);
				Statement stmt = conn.createStatement();){
			
			log.debug("Nettoyage de la BDD...");
			String truncateGeremiBatch = "TRUNCATE TABLE geremi_batch.etablissement;"
					+ "TRUNCATE TABLE geremi_batch.traitement_dechet;"
					+ "TRUNCATE TABLE geremi_batch.declaration;"
					+ "TRUNCATE TABLE geremi_batch.carr_prod_extraction;"
					+ "TRUNCATE TABLE geremi_batch.carr_prod_destination;"
					+ "TRUNCATE TABLE geremi_batch.carr_env_info_gen;"
					+ "TRUNCATE TABLE geremi_batch.anomalie;";

			stmt.execute(truncateGeremiBatch);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
