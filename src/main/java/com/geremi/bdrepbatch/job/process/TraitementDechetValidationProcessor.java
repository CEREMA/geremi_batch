package com.geremi.bdrepbatch.job.process;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.Tuple;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.dto.AnomalieDTO;
import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.repository.RefStatutRepository;
import com.geremi.bdrepbatch.rules.RegleR10OuiOuNon;
import com.geremi.bdrepbatch.rules.RegleR1Annee;
import com.geremi.bdrepbatch.rules.RegleVerifChampsObligatoires;
import com.geremi.bdrepbatch.rules.RegleVerifLongueurInput;
import com.geremi.bdrepbatch.rules.RegleVerifValeursNumeriques;


public class TraitementDechetValidationProcessor implements ItemProcessor<Tuple	, List<Anomalie>>,StepExecutionListener {

	@Autowired
	private RefStatutRepository refStatutRepository;
	
	@Autowired
	private RegleVerifLongueurInput verifLongueurInput;
	
	@Autowired
	RegleVerifValeursNumeriques verifValeurNumerique;
	
	@Autowired
	private RegleVerifChampsObligatoires verifChampsObligatoires;
	
	@Autowired
	private RegleR1Annee regleAnnee;
	
	@Autowired
	private RegleR10OuiOuNon regleR10OuiOuNon;
	
	private String anneeMaxEtab;
	
	@Override
	public List<Anomalie> process(Tuple item) throws Exception {
	
		HashMap<String,String> params = new HashMap<>();
		params.put("anneeAVerifier",(String) item.get("annee"));
		params.put("anneeMaxEtab", anneeMaxEtab);
		String nomTable = "traitement_dechet";
		String idLigneExcel = (String)item.get("id_ligne_excel");
		String annee = item.get("annee") != null ? String.valueOf(item.get("annee")) : null;
		Integer anneeInt = StringUtils.isNotBlank(annee)&& annee.replace(".","").matches("^-?[0-9]*$")?Integer.valueOf(annee):Integer.valueOf(anneeMaxEtab) + 1;

		List<Optional<AnomalieDTO>> listeIntermAnomalie = new ArrayList<>();
		
		//R00
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle(annee, nomTable, "annee", idLigneExcel,null,anneeInt));
		
		//vérification de la longueur des champs
		verifLongueurChamp((String)item.get("libelle_dechet"), "200", nomTable, "libelle_dechet",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("departement_origine"), "50", nomTable, "departement_origine",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("pays_origine"), "50", nomTable, "pays_origine",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("code_ope"), "10", nomTable, "code_ope",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("libelle_ope"), "255", nomTable, "libelle_ope",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("numero_notification"), "512", nomTable, "numero_notification",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//vérification valeurs numériques
		verifValeurNumerique.executerRegle((String)item.get("annee"), nomTable, "annee", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("quantite_admise_tpa"), nomTable, "quantite_admise_tpa", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("quantite_traitee_tpa"), nomTable, "quantite_traitee_tpa", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//R10
		listeIntermAnomalie.add(regleR10OuiOuNon.executerRegle((String)item.get("statut_sortie_dechet"), nomTable, "statut_sortie_dechet",idLigneExcel,null,anneeInt));
		
		//R1
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R02".equals(a.get().getCodeAnomalie())) 
				&& "annee".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			listeIntermAnomalie.add(regleAnnee.executerRegle(params,nomTable, "annee",idLigneExcel,null,anneeInt));	
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
	
	//TODO faire en sorte que cette méthode puisse être déportée dans RegleUtils pour ne pas être répétée dans tous les Processors
	private Optional<AnomalieDTO> verifLongueurChamp(String valeur, String longueurLimite, String nomTable, String nomChamp,String idLigneExcel, Integer annee){
		Map<String,String> params = new HashMap<>();
		params.put("valeur", valeur);
		params.put("longueurLimite", longueurLimite);
		return verifLongueurInput.executerRegle(params, nomTable, nomChamp, idLigneExcel, null,annee);
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
