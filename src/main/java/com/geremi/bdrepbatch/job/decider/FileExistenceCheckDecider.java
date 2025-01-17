package com.geremi.bdrepbatch.job.decider;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class FileExistenceCheckDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		if (ExitStatus.FAILED.equals(stepExecution.getExitStatus())) {
			return FlowExecutionStatus.FAILED;
		}
		return FlowExecutionStatus.COMPLETED;
	}
}
