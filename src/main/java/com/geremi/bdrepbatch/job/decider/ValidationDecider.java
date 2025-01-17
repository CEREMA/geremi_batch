package com.geremi.bdrepbatch.job.decider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class ValidationDecider  implements JobExecutionDecider {

	private static final Logger log = LoggerFactory.getLogger(ValidationDecider.class);
	
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		if(stepExecution.getJobExecution().getExecutionContext().get("hasBloquante") != null) {
			log.warn("Des erreurs bloquantes ont été trouvées. L'exécution du batch ne peut pas se poursuivre.");
			return new FlowExecutionStatus("ERREUR_BLOQUANTE");
		}	
		log.info("Aucune erreur trouvée, poursuite du traitement.");
		return FlowExecutionStatus.COMPLETED;
	}

}
