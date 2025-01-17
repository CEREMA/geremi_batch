package com.geremi.bdrepbatch.tasklet;

import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class FileExistenceCheckTasklet implements Tasklet, StepExecutionListener {

	private String inputFilePath;
	
	
	private static final Logger log = LoggerFactory.getLogger(FileExistenceCheckTasklet.class);

	public FileExistenceCheckTasklet(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

		if (!Files.exists(Path.of(inputFilePath))) {
			log.error("Le fichier source n'existe pas à l'emplacement indiqué : "+inputFilePath);
			chunkContext.getStepContext().getStepExecution().getExecutionContext().put("taskletResult", "Failed");
			return RepeatStatus.FINISHED;
		}
		chunkContext.getStepContext().getStepExecution().getExecutionContext().put("taskletResult", "FINISHED");
		return RepeatStatus.FINISHED;
	}

	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		String taskletResult = stepExecution.getExecutionContext().getString("taskletResult");
		stepExecution.getExecutionContext().remove("taskletResult");
		if (taskletResult.equals("Failed")) {
			return ExitStatus.FAILED;
		} else {
			return stepExecution.getExitStatus();
		}
	}

}
