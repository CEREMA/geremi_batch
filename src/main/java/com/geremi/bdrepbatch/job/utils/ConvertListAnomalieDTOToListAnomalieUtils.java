package com.geremi.bdrepbatch.job.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.dto.AnomalieDTO;
import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.repository.RefAnomalieRepository;
import com.geremi.bdrepbatch.job.repository.RefStatutRepository;

public class ConvertListAnomalieDTOToListAnomalieUtils {

	@Autowired
	private static RefAnomalieRepository refAnomalieRepository;

	@Autowired
	private static RefStatutRepository refStatutRepository;
	
	public static List<Anomalie> convertListeAnomalieDTOToListAnomalie(List<Optional<AnomalieDTO>> listeIntermAnomalie) {
		
		return listeIntermAnomalie.stream()
				.filter(Optional::isPresent)
				.map(anoDto->new Anomalie(anoDto.get().getDateCreation(),
				anoDto.get().getCodeAnomalie(),
				anoDto.get().getNomTable(),
				anoDto.get().getNomChamp(),
				anoDto.get().getIdLigne(),
				null,
				refStatutRepository.findByCodestatut("A_CORRIGER"),
				null,
				anoDto.get().isBloquante(),
				anoDto.get().getIdEtablissement(),
				anoDto.get().getAnnee()
				)).collect(Collectors.toList());	

		
	}
	
}

