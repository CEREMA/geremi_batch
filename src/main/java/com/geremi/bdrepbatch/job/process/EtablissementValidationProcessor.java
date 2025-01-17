
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
import com.geremi.bdrepbatch.job.repository.RefAnomalieRepository;
import com.geremi.bdrepbatch.job.repository.RefStatutRepository;
import com.geremi.bdrepbatch.rules.RegleC1DateFinAutorisation;
import com.geremi.bdrepbatch.rules.RegleR1Annee;
import com.geremi.bdrepbatch.rules.RegleR6ValideLat;
import com.geremi.bdrepbatch.rules.RegleR6ValideLong;
import com.geremi.bdrepbatch.rules.RegleVerifChampsObligatoires;
import com.geremi.bdrepbatch.rules.RegleVerifDates;
import com.geremi.bdrepbatch.rules.RegleVerifLongueurInput;
import com.geremi.bdrepbatch.rules.RegleVerifValeursNumeriques;

public class EtablissementValidationProcessor /*extends ValidationProcessor<Etablissement>*/implements ItemProcessor<Tuple, List<Anomalie>>, StepExecutionListener {

	@Autowired
	private RefStatutRepository refStatutRepository;
	
	@Autowired
	private RegleVerifChampsObligatoires verifChampsObligatoires;
	
	@Autowired
	private RegleVerifLongueurInput verifLongueurInput;
	
	@Autowired
	RegleVerifValeursNumeriques verifValeurNumerique;
	
	@Autowired
	private RegleVerifDates verifDates;
	
	@Autowired
	private RegleR6ValideLat valideLat;
	
	@Autowired
	private RegleR6ValideLong valideLong;
	
	@Autowired
	private RegleR1Annee regleAnnee;
	
	@Autowired
	RegleC1DateFinAutorisation verifDateFinAutor;


	private String anneeMaxEtab;
	
	@Override
	public List<Anomalie> process(Tuple item) throws Exception {
		
		List<Optional<AnomalieDTO>> listeIntermAnomalie = new ArrayList<>();

		/**
		 * Règles à vérifier : 
		 * R1 - R6
		 * 
		 */
		
		String idLigneExcel = (String)item.get("id_ligne_excel");
		String etab = "etablissement";
		String annee = item.get("annee") != null ? String.valueOf(item.get("annee")) : null;
		Integer anneeInt = StringUtils.isNotBlank(annee)&& annee.replace(".","").matches("^-?[0-9]*$")?Integer.valueOf(annee):Integer.valueOf(anneeMaxEtab) + 1;
		
		HashMap<String,String> params = new HashMap<>();
		
		params.put("idLigneExcel", idLigneExcel);
		params.put("anneeMaxEtab", anneeMaxEtab);
		params.put("anneeAVerifier", (String)item.get("annee"));
		params.put("dateFinAutor", (String) item.get("date_fin_autor"));
		
		//R00
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle(annee, etab, "annee", idLigneExcel,null,anneeInt));
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle((String)item.get("code_etablissement"), etab, "code_etablissement", idLigneExcel,null,anneeInt));
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle((String)item.get("nom_etablissement"), etab, "nom_etablissement", idLigneExcel,null,anneeInt));
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle((String)item.get("adressesite"), etab, "adresse_site", idLigneExcel,null,anneeInt));
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle((String)item.get("commune"), etab, "commune", idLigneExcel,null,anneeInt));
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle((String)item.get("abscisselongitudex"),etab,"abscisselongitudex",idLigneExcel,null,anneeInt));
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle((String)item.get("ordonneelatitudey"),etab,"ordonneelatitudey",idLigneExcel,null,anneeInt));
		
		//vérification de la longueur des champs
		verifLongueurChamp((String)item.get("code_etablissement"), "10", etab, "code_etablissement",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("nom_etablissement"), "255", etab, "nom_etablissement",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("adressesite"), "255", etab, "adressesite",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("codepostal"), "5", etab, "codepostal",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("commune"), "50", etab, "commune",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("numerosiret"), "14", etab, "numerosiret",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("codeape"), "6", etab, "codeape",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("activiteprincipale"), "200", etab, "activiteprincipale",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("unite"), "10", etab, "unite",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("matiereproduite"), "200", etab, "matiereproduite",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("adressesiteinternet"), "700", etab, "adressesiteinternet",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//vérification des valeurs numériques
		verifValeurNumerique.executerRegle((String)item.get("annee"), etab, "annee", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("codepostal"), etab, "codepostal", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("volumeproduction"), etab, "volumeproduction", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("nbheuresexploitation"), etab, "nbheuresexploitation", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("nbemployes"), etab, "nbemployes", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//R1 et C1
		//Si la règle R00 n'est pas validée pour le champ année, pas de vérification de la R1
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R02".equals(a.get().getCodeAnomalie())) 
				&& "annee".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			listeIntermAnomalie.add(regleAnnee.executerRegle(params,etab, "annee",idLigneExcel,null,anneeInt));
			verifDateFinAutor.executerRegle(params, etab, "annee",String.valueOf(item.get("id")),(Integer)item.get("id"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		}
		
		//Si la règle R00 n'est pas validée pour le champ abscisse ou ordonnée, pas de vérification de la R6
		//R6
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && ("R00".equals(a.get().getCodeAnomalie()) && "abscisselongitudex".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			listeIntermAnomalie.add(valideLong.executerRegle((String)item.get("abscisselongitudex"),etab,"abscisselongitudex",idLigneExcel,null,anneeInt));
		}
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && ("R00".equals(a.get().getCodeAnomalie()) && "ordonneelatitudey".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			listeIntermAnomalie.add(valideLat.executerRegle((String)item.get("ordonneelatitudey"),etab,"ordonneelatitudey",idLigneExcel,null,anneeInt));
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
		return verifLongueurInput.executerRegle(params, nomTable, nomChamp, idLigneExcel, null, annee);
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
