package com.geremi.bdrepbatch.rules;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleAnnee")
public class RegleR1Annee extends Regle<HashMap<String,String>>{
	
	/*
	 * L'année devra être égale à dernière année en base + 1 et cohérente avec celle présente sur l'onglet Etablissement
	 * */
	
	public final String CODE = "R1";
	public final String LABEL = "L'année doit être unique et suivre immédiatement la dernière année en base";
	public final boolean BLOQUANTE = true;

	@Override
	protected boolean test(HashMap<String,String> param) {
		
		Integer anneeMaxEtab = StringUtils.isNotBlank(param.get("anneeMaxEtab"))?Integer.parseInt(param.get("anneeMaxEtab")):null;
		Integer anneeAVerifier = StringUtils.isNotBlank(param.get("anneeAVerifier"))?Integer.parseInt(param.get("anneeAVerifier")):null;

		if (anneeAVerifier == null) {
			return false;
		}
		if(anneeMaxEtab != null) {
			return anneeAVerifier == anneeMaxEtab + 1;	
		} 
		return true;
	}

	@Override
	protected String getLibelleRegle() {
		return LABEL;
	}

	@Override
	protected String getCodeRegle() {
		return CODE;
	}

	@Override
	protected boolean getBloquante() {
		return BLOQUANTE;
	}
}
