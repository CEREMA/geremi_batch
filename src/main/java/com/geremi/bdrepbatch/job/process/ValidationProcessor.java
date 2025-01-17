package com.geremi.bdrepbatch.job.process;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.AnomalieDTO;
import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.model.RefAnomalie;
import com.geremi.bdrepbatch.job.model.RefStatut;

public abstract class ValidationProcessor<T> implements ItemProcessor<T, List<Anomalie>>,StepExecutionListener{
	
	/*WIP - à implémenter pour le lot C*/
	/*private String anneeMaxEtab;
	private List<RefAnomalie> refAnomalies;
	private List<RefStatut> refStatuts;

	
	public abstract List<Anomalie> process(T item)throws Exception;
	
	public List<Anomalie> convertListeAnomalieDTOToListAnomalie(List<Optional<AnomalieDTO>> listeIntermAnomalie) {
		
		return listeIntermAnomalie.stream()
				.filter(Optional::isPresent)
				.map(anoDto->new Anomalie(anoDto.get().getDateCreation(),
				findRefByCodeAnomalie(anoDto.get().getCodeAnomalie()),
				anoDto.get().getNomTable(),
				anoDto.get().getNomChamp(),
				anoDto.get().getIdLigne(),
				null,
				findRefByCodeStatut("A_CORRIGER"),
				null,
				anoDto.get().isBloquante()
				)).collect(Collectors.toList());	

		
	}
	
	protected RefAnomalie findRefByCodeAnomalie(String codeAnomalie) {
		return refAnomalies.stream().filter(ra -> codeAnomalie.equals(ra.getCodeAnomalie())).findFirst().orElse(null);
	}
	
	protected RefStatut findRefByCodeStatut(String codeStatut) {
		return refStatuts.stream().filter(rs -> codeStatut.equals(rs.getStatut())).findFirst().orElse(null);
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		setAnneeMaxEtab((String) stepExecution.getJobExecution().getExecutionContext().get("anneeMax"));
		refAnomalies = (List<RefAnomalie>) stepExecution.getJobExecution().getExecutionContext().get("refAnomalies");
		refStatuts = (List<RefStatut>) stepExecution.getJobExecution().getExecutionContext().get("refStatuts");
	}

	public String getAnneeMaxEtab() {
		return anneeMaxEtab;
	}

	public void setAnneeMaxEtab(String anneeMaxEtab) {
		this.anneeMaxEtab = anneeMaxEtab;
	}*/


}
