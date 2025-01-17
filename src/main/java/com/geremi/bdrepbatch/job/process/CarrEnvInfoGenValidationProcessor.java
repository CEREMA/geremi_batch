package com.geremi.bdrepbatch.job.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.geremi.bdrepbatch.rules.RegleVerifChampsObligatoires;
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
import com.geremi.bdrepbatch.rules.RegleC8ProductionMoyenneAutorisee;
import com.geremi.bdrepbatch.rules.RegleR15Filtrage;
import com.geremi.bdrepbatch.rules.RegleR1Annee;
import com.geremi.bdrepbatch.rules.RegleVerifDates;
import com.geremi.bdrepbatch.rules.RegleVerifLongueurInput;
import com.geremi.bdrepbatch.rules.RegleVerifValeursNumeriques;

public class CarrEnvInfoGenValidationProcessor implements ItemProcessor<Tuple, List<Anomalie>>,StepExecutionListener {

	@Autowired
	private RefAnomalieRepository refAnomalieRepository;

	@Autowired
	private RefStatutRepository refStatutRepository;
	
	private String anneeMaxEtab;
	
	@Autowired 
	RegleVerifLongueurInput verifLongueurInput;
	
	@Autowired
	RegleVerifValeursNumeriques verifValeurNumerique;

	@Autowired
	private RegleVerifChampsObligatoires verifChampsObligatoires;
	
	@Autowired
	private RegleVerifDates verifDates;
	
	@Autowired
	private RegleR1Annee regleR1Annee;
	
	@Autowired
	private RegleR15Filtrage filtrageR15;
	
	@Autowired
	private RegleC8ProductionMoyenneAutorisee regleC8ProductionMoyenneAutorisee;
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		anneeMaxEtab = (String) stepExecution.getJobExecution().getExecutionContext().get("anneeMax");
		
	}

	@Override
	public List<Anomalie> process(Tuple item) throws Exception {
		List<Optional<AnomalieDTO>> listeIntermAnomalie = new ArrayList<>();
		String idLigneExcel = (String)item.get("id_ligne_excel");
		String nomTable = "carr_env_info_gen";
		String annee = item.get("annee") != null ? String.valueOf(item.get("annee")) : null;
		Integer anneeInt = StringUtils.isNotBlank(annee)&& annee.replace(".","").matches("^-?[0-9]*$")?Integer.valueOf(annee):Integer.valueOf(anneeMaxEtab) + 1;
		HashMap<String,String> params = new HashMap<>();

		//R00
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle(annee, nomTable, "annee", idLigneExcel,null,anneeInt));

		//vérification longueur des champs
		verifLongueurChamp((String)item.get("type_carriere"), "50", nomTable, "type_carriere",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//R02 - verification format des valeurs numériques
		verifValeurNumerique.executerRegle((String)item.get("production_max_autorisee"), nomTable, "production_max_autorisee", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("production_moyenne_autorisee"), nomTable, "production_moyenne_autorisee", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("annee"), nomTable, "annee", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//vérification champs dates
		verifDates.executerRegle((String)item.get("date_fin_autor"), nomTable, "date_fin_autor", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		/*
		 * R1 - R15
		 * */
		filtrageR15.executerRegle((String)item.get("code"), nomTable, "code_etablissement", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R02".equals(a.get().getCodeAnomalie())) 
				&& "annee".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			params.put("anneeAVerifier",(String)item.get("annee"));
			params.put("anneeMaxEtab", anneeMaxEtab);
			regleR1Annee.executerRegle(params, nomTable, "annee", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));	
		}
		
		/*
		 * C8 - vérification conditionnée à la validation de la R02
		 * */
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && "R02".equals(a.get().getCodeAnomalie()) 
				&& ("production_max_autorisee".equals(a.get().getNomChamp())
				|| "production_moyenne_autorisee".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			params.put("productionMaximaleAutorisee", (String)item.get("production_max_autorisee"));
			params.put("productionMoyenneAutorisee", (String)item.get("production_moyenne_autorisee"));
			//on indique directement la table etablissement puisque les infos de carr.env.info.gen sont au final enregistrées dans cette table
			regleC8ProductionMoyenneAutorisee.executerRegle(params, "etablissement", "production_moyenne_autorisee",String.valueOf(item.get("idEtab")),(Integer)item.get("idEtab"), anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		}
		
		
		return convertListeAnomalieDTOToListAnomalie(listeIntermAnomalie);
	}
	
	//TODO faire en sorte que cette méthode puisse être déportée dans RegleUtils pour ne pas être répétée dans tous les Processors
	private Optional<AnomalieDTO> verifLongueurChamp(String valeur, String longueurLimite, String nomTable, String nomChamp,String idLigneExcel,Integer annee){
		Map<String,String> params = new HashMap<>();
		params.put("valeur", valeur);
		params.put("longueurLimite", longueurLimite);
		return verifLongueurInput.executerRegle(params, nomTable, nomChamp, idLigneExcel, null,annee);
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

}
