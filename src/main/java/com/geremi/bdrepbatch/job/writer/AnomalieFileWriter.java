
package com.geremi.bdrepbatch.job.writer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.job.model.Anomalie;

import jakarta.annotation.PostConstruct;

public class AnomalieFileWriter extends FlatFileItemWriter<List<Anomalie>> implements StepExecutionListener{

	@Value("#{stepExecution}") StepExecution stepExecution;
	
	@Value("${output.file.path.errors.tmp}")
	private String filePath;
	
	public AnomalieFileWriter() {
		
		
	}
	
	@PostConstruct
	public void save() {
		
		try {
					
			this.setShouldDeleteIfExists(false);
			this.setAppendAllowed(true);
			
		    this.setLineAggregator(new LineAggregator <List<Anomalie>>() {
			
				@Override
				public String aggregate(List<Anomalie> item) {
					  return item.stream()
					  .filter(i -> i.getCodeRefAnomalie() != null && i.isBloquante())
					  .map(Anomalie::toString)
					  .collect(Collectors.joining(lineSeparator));
				}});
		   
		    this.setResource(new FileSystemResource(filePath));
		    this.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(Chunk<? extends List<Anomalie>> items) throws Exception {
		List<Anomalie>filteredItems = (List<Anomalie>) items.getItems().stream()
	    .flatMap(e -> e.stream().filter(i -> i.getCodeRefAnomalie() != null && i.isBloquante())).toList();
		Chunk<List<Anomalie>> toWrite = new Chunk<>();
		
		/*correction du pb : erreur bloquante même si au cune erreur car on ajoute quand même une liste vide dans le chunk
		 * + SMART 343 - évite l'insertion de lignes vides dans le fichier
		 * */
		if(filteredItems.size()>0) {
			toWrite.add(filteredItems);
		}
		
		if(!toWrite.isEmpty()) {
			this.stepExecution.getJobExecution().getExecutionContext().put("hasBloquante", true);
			super.write(toWrite);
		}
	}
	

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		
	}
	
	
	
	
}
