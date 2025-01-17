package com.geremi.bdrepbatch.tasklet;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.geremi.bdrepbatch.job.model.RefAnomalie;
import com.geremi.bdrepbatch.job.repository.EtablissementRepository;
import com.geremi.bdrepbatch.job.repository.RefAnomalieRepository;
import com.geremi.bdrepbatch.job.utils.RefUtils;

public class SetGlobalParametersTasklet implements Tasklet, StepExecutionListener{
	
	
	private EtablissementRepository etablissementRepository;
	private RefAnomalieRepository refAnomalieRepository;

	
	public SetGlobalParametersTasklet(EtablissementRepository etablissementRepository, RefAnomalieRepository refAnomalieRepository) {
		this.etablissementRepository = etablissementRepository;
		this.refAnomalieRepository = refAnomalieRepository;

	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String anneeMax = etablissementRepository.findMaxAnneeDef();
		String anneeMin = etablissementRepository.findMinAnneeTempo();
		if(StringUtils.isNotBlank(anneeMax)) {
			chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("anneeMax", anneeMax);
		} else if (StringUtils.isBlank(anneeMax) && StringUtils.isNotBlank(anneeMin)) {
			chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("anneeMax", String.valueOf((Integer.parseInt(anneeMin)-1)));
		}
		
		List<RefAnomalie> refAno = refAnomalieRepository.findAll();
		RefUtils.setRefAnomalies(refAno.stream().collect(Collectors.toMap(RefAnomalie::getCodeAnomalie, RefAnomalie::getLibelleAnomalie)));
		 
		return RepeatStatus.FINISHED;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
	}
	

	
	
}
