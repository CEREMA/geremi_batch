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
import com.geremi.bdrepbatch.job.model.Etablissement;
import com.geremi.bdrepbatch.job.repository.EtablissementRepository;
import com.geremi.bdrepbatch.job.repository.RefAnomalieRepository;
import com.geremi.bdrepbatch.job.repository.RefStatutRepository;
import com.geremi.bdrepbatch.rules.RegleR10OuiOuNon;
import com.geremi.bdrepbatch.rules.RegleR1Annee;
import com.geremi.bdrepbatch.rules.RegleC13StatutDeclaration;
import com.geremi.bdrepbatch.rules.RegleC14ProgressionDeclaration;
import com.geremi.bdrepbatch.rules.RegleC17VerifCommune;
import com.geremi.bdrepbatch.rules.RegleC18VerifRegion;
import com.geremi.bdrepbatch.rules.RegleC19VerifDepartement;
import com.geremi.bdrepbatch.rules.RegleVerifChampsObligatoires;
import com.geremi.bdrepbatch.rules.RegleVerifDates;
import com.geremi.bdrepbatch.rules.RegleVerifLongueurInput;
import com.geremi.bdrepbatch.rules.RegleVerifValeursNumeriques;

public class DeclarationValidationProcessor implements ItemProcessor<Tuple, List<Anomalie>>,StepExecutionListener{
	@Autowired
	private RefStatutRepository refStatutRepository;
	
	@Autowired
	private RegleVerifChampsObligatoires verifChampsObligatoires;
	
	@Autowired
	RegleVerifValeursNumeriques verifValeurNumerique;
	
	@Autowired
	private RegleVerifLongueurInput verifLongueurInput;
	
	@Autowired
	private RegleVerifDates verifDates;
	
	
	@Autowired
	private RegleC13StatutDeclaration verifStatutDeclaration;
	
	@Autowired
	private RegleC14ProgressionDeclaration verifProgressionDeclaration;
	
	@Autowired
	private RegleC17VerifCommune regleVerifCommune;
	
	@Autowired
	private RegleC18VerifRegion regleVerifRegion;
	
	@Autowired
	private RegleC19VerifDepartement regleVerifDepartement;
	
	@Autowired
	private RegleR1Annee regleAnnee;
	
	@Autowired
	private RegleR10OuiOuNon verifRegleR10OuiOuNon;
	
	private String anneeMaxEtab;

	@Override
	public List<Anomalie> process(Tuple item) throws Exception {
		List<Optional<AnomalieDTO>> listeIntermAnomalie = new ArrayList<>();
		
		String idLigneExcel = (String)item.get("id_ligne_excel");
		String declaration = "declaration";
		String annee = item.get("annee") != null ? String.valueOf(item.get("annee")) : null;
		Integer anneeInt = StringUtils.isNotBlank(annee)&& annee.replace(".","").matches("^-?[0-9]*$")?Integer.valueOf(annee):Integer.valueOf(anneeMaxEtab) + 1;
		
		//vérification de l'alimentation des champs obligatoires
		verifChampsObligatoires.executerRegle(annee, declaration, "annee", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifChampsObligatoires.executerRegle((String)item.get("code_insee"),declaration, "code_insee",idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifChampsObligatoires.executerRegle((String)item.get("service_inspection"), declaration, "service_inspection", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifChampsObligatoires.executerRegle((String)item.get("region"), declaration, "region", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifChampsObligatoires.executerRegle((String)item.get("departement"), declaration, "departement", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifChampsObligatoires.executerRegle((String)item.get("statut_declaration"), declaration, "statut_declaration", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifChampsObligatoires.executerRegle((String)item.get("carriere"), declaration, "carriere", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifChampsObligatoires.executerRegle((String)item.get("quotas"), declaration, "quotas", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//vérification champs numériques
		verifValeurNumerique.executerRegle((String)item.get("annee"), declaration, "annee", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String)item.get("progression"), declaration, "progression", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//vérification de la longueur des champs
		verifLongueurChamp((String)item.get("code_insee"), "5", declaration, "code_insee",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("commune"), "50", declaration, "commune",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("service_inspection"), "10", declaration, "service_inspection",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("region"), "30", declaration, "region",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("departement"), "30", declaration, "departement",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("statut_declaration"), "30", declaration, "statut_declaration",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("statut_quota_emission"), "30", declaration, "statut_quota_emission",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("statut_quota_niveaux_activites"), "30", declaration, "statut_quota_niveaux_activites",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//vérification des champs date
		verifDates.executerRegle((String)item.get("date_derniere_action_declarant"), declaration, "date_derniere_action_declarant", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifDates.executerRegle((String)item.get("date_derniere_action_inspecteur"), declaration, "date_derniere_action_inspecteur", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifDates.executerRegle((String)item.get("date_init_declaration"), declaration, "date_init_declaration", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//Si la règle R00 n'est pas validée pour le code Insee, pas de vérification pour C17/18/19
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R01".equals(a.get().getCodeAnomalie())) 
				&& "code_insee".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			
			if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R01".equals(a.get().getCodeAnomalie())) 
					&& "commune".equals(a.get().getNomChamp()))).findFirst().isEmpty()){
							//C17
							HashMap<String,String> verifCommune = new HashMap<>();
									
							verifCommune.put("nomCommune", (String)item.get("commune"));
							verifCommune.put("nomCommuneCheck", (String)item.get("rcnc"));
							
							regleVerifCommune.executerRegle(verifCommune, declaration, "commune", String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
						}
			if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R01".equals(a.get().getCodeAnomalie())) 
					&& "region".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
							//C18
							HashMap<String,String> verifRegion = new HashMap<>();
							
							verifRegion.put("nomRegion", (String)item.get("region"));
							verifRegion.put("nomRegionCheck", (String)item.get("rrnr"));
							
							regleVerifRegion.executerRegle(verifRegion, declaration, "region", String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
						}
			
			if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R01".equals(a.get().getCodeAnomalie())) 
					&& "departement".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
							//C19
							HashMap<String,String> verifDepartement = new HashMap<>();
							
							verifDepartement.put("nomDepartement",(String)item.get("departement"));
							verifDepartement.put("nomDepartementCheck",(String)item.get("rdnd"));
							
							regleVerifDepartement.executerRegle(verifDepartement, declaration, "departement", String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
						}
			
		}
		
		
		//Si la règle R00 n'est pas validée pour les champs carrière et quotas, pas de vérification de la R10
		//R10
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie())) && "carriere".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			verifRegleR10OuiOuNon.executerRegle((String)item.get("carriere"), declaration, "carriere", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));	
		}
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie())) && "quotas".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			verifRegleR10OuiOuNon.executerRegle((String)item.get("quotas"), declaration, "quotas", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));	
		}
		
		//R1
		HashMap<String,String> params = new HashMap<>();
		params.put("anneeAVerifier",(String)item.get("annee"));
		params.put("anneeMaxEtab", anneeMaxEtab);
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R00".equals(a.get().getCodeAnomalie()) || "R02".equals(a.get().getCodeAnomalie())) 
				&& "annee".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			listeIntermAnomalie.add(regleAnnee.executerRegle(params,declaration, "annee",idLigneExcel,null,anneeInt));	
		}
		
		//C13-C14
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R01".equals(a.get().getCodeAnomalie())) && "statut_declaration".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			verifStatutDeclaration.executerRegle((String) item.get("statut_declaration"), declaration, "statut_declaration",String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));	
		}
		
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R01".equals(a.get().getCodeAnomalie())) && "statut_quota_emission".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			verifStatutDeclaration.executerRegle((String) item.get("statut_quota_emission"), declaration, "statut_quota_emission", String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));			
		}

		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R01".equals(a.get().getCodeAnomalie())) && "statut_quota_niveaux_activites".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			verifStatutDeclaration.executerRegle((String) item.get("statut_quota_niveaux_activites") , declaration, "statut_quota_niveaux_activites", String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));	
		}
		
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && (("R02".equals(a.get().getCodeAnomalie())) && "progression".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			verifProgressionDeclaration.executerRegle((String)item.get("progression"), declaration, "progression", String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
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
	private Optional<AnomalieDTO> verifLongueurChamp(String valeur, String longueurLimite, String nomTable, String nomChamp,String idLigneExcel,Integer annee){
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
