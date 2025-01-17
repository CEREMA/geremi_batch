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
import com.geremi.bdrepbatch.job.repository.RefStatutRepository;

public class EtablissementValidationR14Processor implements ItemProcessor<String, List<Anomalie>>{
	
	public final String CODE = "R14";
	public final String LABEL = "Les établissements sont filtrés selon la règle R17, et tous les établissements de cette liste filtrée doivent se trouver dans l'onglet. Code établissement en erreur : %s";
	public final boolean BLOQUANTE = true;

	
	@Autowired
	private RefStatutRepository refStatutRepository;
	
	private static final Logger log = LoggerFactory.getLogger(EtablissementValidationR14Processor.class);

	@Override
	public List<Anomalie> process(String item) throws Exception {
		List<AnomalieDTO> listeIntermAnomalie = new ArrayList<>();
		
		listeIntermAnomalie.add(new AnomalieDTO(LocalDate.now(), CODE,
				String.format(LABEL, item), "etablissement", "code_etablissement", item,
				"A_CORRIGER", null, null,BLOQUANTE,null, null));
		log.warn(
				"Règle "+CODE+" non respectée: " + String.format(LABEL, item));
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
