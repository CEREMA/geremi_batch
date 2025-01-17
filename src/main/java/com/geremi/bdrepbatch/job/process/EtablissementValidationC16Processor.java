package com.geremi.bdrepbatch.job.process;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.dto.AnomalieDTO;
import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.model.RefAnomalie;
import com.geremi.bdrepbatch.job.repository.RefStatutRepository;

public class EtablissementValidationC16Processor implements ItemProcessor<Tuple, List<Anomalie>> {

	public final String CODE = "C16";
	public final String LABEL = " Etablissement %s absent pour le millésime en cours et date de fin d'autorisation dépassée - Vérif. DREAL nécessaire";
	public final boolean BLOQUANTE = false;

	
	@Autowired
	private RefStatutRepository refStatutRepository;
	
	private static final Logger log = LoggerFactory.getLogger(EtablissementValidationC16Processor.class);
	
	@Override
	public List<Anomalie> process(Tuple item) throws Exception {
		List<AnomalieDTO> listeIntermAnomalie = new ArrayList<>();
		
		listeIntermAnomalie.add(new AnomalieDTO(LocalDate.now(), CODE,
				String.format(LABEL, (String) item.get("codeEtablissement")), "etablissement", "code_etablissement", "Absent",
				"A_CORRIGER", null, null,BLOQUANTE, (Integer)item.get("id"), (Integer)item.get("annee")));
		log.warn(
				"Règle "+CODE+" non respectée: " + String.format(LABEL, (String) item.get("codeEtablissement")));
		return convertListeAnomalieDTOToListAnomalie(listeIntermAnomalie);
	}
	
	private List<Anomalie> convertListeAnomalieDTOToListAnomalie(List<AnomalieDTO> listeIntermAnomalie) {
		
		return listeIntermAnomalie.stream()
				.map(anoDto->new Anomalie(anoDto.getDateCreation(),
						anoDto.getCodeAnomalie(),
						anoDto.getNomTable(),
						anoDto.getNomChamp(),
						anoDto.getIdLigne(),
						null,
						refStatutRepository.findByCodestatut("A_CORRIGER"),
						null,
						anoDto.isBloquante(),
						anoDto.getIdEtablissement(),
						anoDto.getAnnee()
						)).collect(Collectors.toList());	
		
	}

}


