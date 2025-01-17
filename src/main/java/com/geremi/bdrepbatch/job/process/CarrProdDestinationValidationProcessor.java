package com.geremi.bdrepbatch.job.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.geremi.bdrepbatch.rules.RegleC2PresenceQuantiteAnnuelle;
import com.geremi.bdrepbatch.rules.RegleC2bisValeurQuantiteAnnuelleBasse;
import com.geremi.bdrepbatch.rules.RegleC2bisValeurQuantiteAnnuelleHaute;

import jakarta.persistence.Tuple;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.dto.AnomalieDTO;
import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.repository.RefAnomalieRepository;
import com.geremi.bdrepbatch.job.repository.RefStatutRepository;
import com.geremi.bdrepbatch.rules.RegleR15Filtrage;
import com.geremi.bdrepbatch.rules.RegleR1Annee;
import com.geremi.bdrepbatch.rules.RegleVerifChampsObligatoires;
import com.geremi.bdrepbatch.rules.RegleVerifValeursNumeriques;

public class CarrProdDestinationValidationProcessor implements ItemProcessor<Tuple, List<Anomalie>>,StepExecutionListener {

	@Autowired
	private RefAnomalieRepository refAnomalieRepository;

	@Autowired
	private RefStatutRepository refStatutRepository;
	
	@Autowired
	RegleVerifValeursNumeriques verifValeurNumerique;
	
	@Autowired
	private RegleVerifChampsObligatoires verifChampsObligatoires;
	
	@Autowired
	private RegleR15Filtrage filtrageR15;
	
	@Autowired
	private RegleR1Annee regleAnnee;

	@Autowired
	private RegleC2bisValeurQuantiteAnnuelleHaute verifValeurQuantiteAnnuelleHaute;
	
	@Autowired
	private RegleC2bisValeurQuantiteAnnuelleBasse verifValeurQuantiteAnnuelleBasse;
	
	private String anneeMaxEtab;

	@Override
	public List<Anomalie> process(Tuple item) throws Exception {
		List<Optional<AnomalieDTO>> listeIntermAnomalie = new ArrayList<>();

		String totalQa = item.get("total_qa") != null ?String.valueOf(item.get("total_qa")) : null;
		String idLigneExcel = (String)item.get("id_ligne_excel");
		String annee = item.get("annee") != null ? String.valueOf(item.get("annee")) : null;
		Integer anneeInt = StringUtils.isNotBlank(annee)&& annee.replace(".","").matches("^-?[0-9]*$")?Integer.valueOf(annee):Integer.valueOf(anneeMaxEtab) + 1;
		String nomTable = "carr_prod_destination";
		
		//R00
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle(annee, nomTable, "annee", idLigneExcel,null,anneeInt));
		
		//R02 -vérification valeurs numériques
		verifValeurNumerique.executerRegle((String)item.get("tonnage"), nomTable, "tonnage", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("annee"), nomTable, "annee", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));

		//R15
		filtrageR15.executerRegle((String)item.get("code"), nomTable, "code_etablissement", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//R1
		HashMap<String,String> params = new HashMap<>();
		params.put("anneeAVerifier",(String)item.get("annee"));
		params.put("anneeMaxEtab", anneeMaxEtab);
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R02".equals(a.get().getCodeAnomalie())) 
				&& "annee".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			listeIntermAnomalie.add(regleAnnee.executerRegle(params,nomTable, "annee",idLigneExcel,null,anneeInt));	
		}
		
		//C2bis
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && ("R02".equals(a.get().getCodeAnomalie())
				&& "tonnage".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			verifValeurQuantiteAnnuelleHaute.executerRegle(totalQa, nomTable,"tonnage" , String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
			verifValeurQuantiteAnnuelleBasse.executerRegle(totalQa, nomTable,"tonnage" , String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		}
		
		return convertListeAnomalieDTOToListAnomalie(listeIntermAnomalie);
	}
	
	private List<Anomalie> convertListeAnomalieDTOToListAnomalie(List<Optional<AnomalieDTO>> listeIntermAnomalie) {
		
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
				)).collect(Collectors.collectingAndThen(Collectors.toList(), anos -> anos.isEmpty() ? null : anos));	

		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		anneeMaxEtab = (String) stepExecution.getJobExecution().getExecutionContext().get("anneeMax");
		
	}

}
