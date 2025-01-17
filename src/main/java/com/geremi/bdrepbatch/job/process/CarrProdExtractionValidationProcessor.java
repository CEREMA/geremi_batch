package com.geremi.bdrepbatch.job.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.dto.AnomalieDTO;
import com.geremi.bdrepbatch.job.model.Anomalie;
import com.geremi.bdrepbatch.job.repository.RefStatutRepository;
import com.geremi.bdrepbatch.rules.RegleC2PresenceQuantiteAnnuelle;
import com.geremi.bdrepbatch.rules.RegleC2bisValeurQuantiteAnnuelleHaute;
import com.geremi.bdrepbatch.rules.RegleC2bisValeurQuantiteAnnuelleBasse;
import com.geremi.bdrepbatch.rules.RegleC3QuantiteAnnuelleProductionMaximaleAutorisee;
import com.geremi.bdrepbatch.rules.RegleC4ProductionVsMaxMinDernieresAnnees;
import com.geremi.bdrepbatch.rules.RegleC5FuseauProductionDernieresAnnees;
import com.geremi.bdrepbatch.rules.RegleR15Filtrage;
import com.geremi.bdrepbatch.rules.RegleR1Annee;
import com.geremi.bdrepbatch.rules.RegleVerifChampsObligatoires;
import com.geremi.bdrepbatch.rules.RegleVerifLongueurInput;
import com.geremi.bdrepbatch.rules.RegleVerifValeursNumeriques;

import jakarta.persistence.Tuple;

public class CarrProdExtractionValidationProcessor implements ItemProcessor<Tuple, List<Anomalie>>,StepExecutionListener {

	@Autowired
	private RefStatutRepository refStatutRepository;
	
	@Autowired
	private RegleVerifLongueurInput verifLongueurInput;
	
	@Autowired
	RegleVerifValeursNumeriques verifValeurNumerique;
	
	@Autowired
	private RegleVerifChampsObligatoires verifChampsObligatoires;
	
	@Autowired
	private RegleR15Filtrage filtrageR15;
	
	@Autowired
	private RegleR1Annee regleAnnee;
	
	@Autowired
	private RegleC2PresenceQuantiteAnnuelle verifPresenceQuantiteAnnuelle;
	
	@Autowired
	private RegleC2bisValeurQuantiteAnnuelleHaute verifValeurQuantiteAnnuelleHaute;
	
	@Autowired
	private RegleC2bisValeurQuantiteAnnuelleBasse verifValeurQuantiteAnnuelleBasse;
	
	
	@Autowired
	private RegleC3QuantiteAnnuelleProductionMaximaleAutorisee verifQuantiteAnnuelleProductionMax;
	
	@Autowired
	private RegleC4ProductionVsMaxMinDernieresAnnees verifProductionVsMaxMinDernieresAnnees;
	
	@Autowired
	private RegleC5FuseauProductionDernieresAnnees verifFuseauProduction;
	
	private String anneeMaxEtab;

	@Override
	public List<Anomalie> process(Tuple item) throws Exception {
		List<Optional<AnomalieDTO>> listeIntermAnomalie = new ArrayList<>();
		
		String idLigneExcel = (String)item.get("id_ligne_excel");
		String nomTable = "extraction";
		String totalQa = item.get("total_qa") != null ?String.valueOf(item.get("total_qa")) : null;
		String annee = item.get("annee") != null ? String.valueOf(item.get("annee")) : null;
		Integer anneeInt = StringUtils.isNotBlank(annee)&& annee.replace(".","").matches("^-?[0-9]*$")?Integer.valueOf(annee):Integer.valueOf(anneeMaxEtab) + 1;
		HashMap<String,String> params = new HashMap<>();
		
		//R00
		listeIntermAnomalie.add(verifChampsObligatoires.executerRegle(annee, nomTable, "annee", idLigneExcel,null,anneeInt));
		
		//vérification longueur des champs
		verifLongueurChamp((String)item.get("substances_a_recycler"), "255", nomTable,"substances_a_recycler", idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("famille_usage_debouches"), "255", nomTable, "famille_usage_debouches",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("precision_famille_usage"), "255", nomTable, "precision_famille_usage",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("sous_famille_usage_debouches"), "255", nomTable, "sous_famille_usage_debouches",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("precision_sous_famille_usage"), "255", nomTable, "precision_sous_famille_usage",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("sous_famille_usage_debouches_2"), "255", nomTable, "sous_famille_usage_debouches_2",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifLongueurChamp((String)item.get("precision_sous_famille_usage_2"), "255", nomTable, "precision_sous_famille_usage_2",idLigneExcel,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//vérification des valeurs numériques
		verifValeurNumerique.executerRegle((String)item.get("annee"), nomTable, "annee", idLigneExcel, null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String) item.get("quantite_restante_accessible"),nomTable,"quantite_restante_accessible",idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String) item.get("quantite_annuelle_steriles"),nomTable,"quantite_annuelle_steriles",idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String) item.get("quantite_annuelle"),nomTable,"quantite_annuelle",idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String) item.get("total_substance"),nomTable,"total_substance",idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		verifValeurNumerique.executerRegle((String) item.get("total_dont_steriles"),nomTable,"total_dont_steriles",idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//R15
		filtrageR15.executerRegle((String)item.get("code"), nomTable, "code_etablissement", idLigneExcel,null,anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		
		//R1
		params.put("anneeAVerifier",(String)item.get("annee"));
		params.put("anneeMaxEtab", anneeMaxEtab);
		
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && ("R00".equals(a.get().getCodeAnomalie()) || "R02".equals(a.get().getCodeAnomalie()) 
				&& "annee".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			listeIntermAnomalie.add(regleAnnee.executerRegle(params,nomTable, "annee",idLigneExcel,null,anneeInt));	
		}
		
		//C2 - C2bis - C3
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && ("R02".equals(a.get().getCodeAnomalie())
				&& "quantite_annuelle".equals(a.get().getNomChamp()))).findFirst().isEmpty()) {
			//C2
			verifPresenceQuantiteAnnuelle.executerRegle(totalQa, nomTable,"quantite_annuelle" , String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
			//C2Bis
			if(listeIntermAnomalie.stream().filter(a->a.isPresent() && "C2".equals(a.get().getCodeAnomalie()) && "quantite_annuelle".equals(a.get().getNomChamp())).findFirst().isEmpty()) {
				verifValeurQuantiteAnnuelleHaute.executerRegle(totalQa, nomTable,"quantite_annuelle" , String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
				verifValeurQuantiteAnnuelleBasse.executerRegle(totalQa, nomTable,"quantite_annuelle" , String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
			}
			//C3
			if(verifValeurNumerique.executerRegle((String) item.get("production_max_autorisee"),nomTable,"production_max_autorisee",idLigneExcel,null,anneeInt).isEmpty()) {
				params.put("productionMaxAutorisee", (String) item.get("production_max_autorisee"));
				params.put("totalQuantiteAnnuelle", totalQa);
				verifQuantiteAnnuelleProductionMax.executerRegle(params, nomTable, "quantite_annuelle" , String.valueOf(item.get("id")),(Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
			}
		}
		
		 //C4 - C5
		params.put("maxProdAnneesPrecedentes",item.get("maxSomme") != null?String.valueOf(item.get("maxSomme")):null);
		params.put("minProdAnneesPrecedentes",item.get("minSomme") != null?String.valueOf(item.get("minSomme")):null);
		verifProductionVsMaxMinDernieresAnnees.executerRegle(params, nomTable, "quantite_annuelle", String.valueOf(item.get("id")), (Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
		if(listeIntermAnomalie.stream().filter(a->a.isPresent() && ("C4".equals(a.get().getCodeAnomalie()))).findFirst().isEmpty()) {
			params.put("avgProd", item.get("avgSomme") != null?String.valueOf(item.get("avgSomme")):null);
			params.put("ecartType", item.get("varSomme") != null?String.valueOf(item.get("varSomme")):null);
			verifFuseauProduction.executerRegle(params, nomTable, "quantite_annuelle", String.valueOf(item.get("id")), (Integer)item.get("idEtab"),anneeInt).ifPresent(a->listeIntermAnomalie.add(Optional.of(a)));
			
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
