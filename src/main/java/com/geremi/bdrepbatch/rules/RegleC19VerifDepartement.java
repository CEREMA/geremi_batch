package com.geremi.bdrepbatch.rules;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleR9VerifDepartement")
public class RegleC19VerifDepartement extends Regle<HashMap<String,String>>{

	public final String CODE = "C19";
	public final String LABEL = "Le département doit correspondre au nom du département lié au numéro insee de l'onglet";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(HashMap<String, String> param) {
		if(StringUtils.isNotBlank(param.get("nomDepartement")) && StringUtils.isNotBlank(param.get("nomDepartementCheck"))) {
			String nomDepartement = StringUtils.stripAccents(param.get("nomDepartement").replaceAll("[\\-]", " "));
			String nomDepartementCheck = StringUtils.stripAccents(param.get("nomDepartementCheck").replaceAll("[\\-]", " ")); 
			return nomDepartement.equalsIgnoreCase(nomDepartementCheck);	
		}
		return false;
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
