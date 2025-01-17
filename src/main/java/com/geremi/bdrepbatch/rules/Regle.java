package com.geremi.bdrepbatch.rules;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geremi.bdrepbatch.job.dto.AnomalieDTO;

public abstract class Regle<T> {

	
	private static final Logger log = LoggerFactory.getLogger(Regle.class);
	
	public Optional<AnomalieDTO> executerRegle(T param, String nomTable, String nomChamp, String idLigneExcel, Integer idEtablissement, Integer annee) {
		if(!test(param)) {
			log.warn(
					"Règle "+getCodeRegle()+" non respectée: " + getLibelleRegle());
			return Optional.ofNullable(new AnomalieDTO(LocalDate.now(), getCodeRegle(),
					getLibelleRegle(), nomTable, nomChamp, idLigneExcel,
					"A_CORRIGER", null, null,getBloquante(),idEtablissement, annee));
			
		}
		return Optional.empty();
	}
	
	protected abstract boolean test(T param);
	
	protected abstract String getLibelleRegle();
	
	protected abstract String getCodeRegle();
	
	
	protected abstract boolean getBloquante();
}
