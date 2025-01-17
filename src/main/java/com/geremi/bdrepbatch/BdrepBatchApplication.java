package com.geremi.bdrepbatch;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication/*(scanBasePackages = {"com.geremi.bdrepbatch.rules"})*/
public class BdrepBatchApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private ImportJobConfig importJobConfig;
	
	

	public static void main(String[] args) {
		SpringApplication.run(BdrepBatchApplication.class, args);
	}

	//@EventListener(ApplicationReadyEvent.class)
	/*public void runBatchJob() throws Exception {
		// Run the importJob
		jobLauncher.run(importJobConfig.importJob(), new JobParameters());

	}*/
}
