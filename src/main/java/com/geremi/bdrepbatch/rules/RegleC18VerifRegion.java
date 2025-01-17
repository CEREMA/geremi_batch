package com.geremi.bdrepbatch.rules;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleR8VerifRegion")
public class RegleC18VerifRegion extends Regle<HashMap<String,String>>{

	public final String CODE = "C18";
	public final String LABEL = "La région doit correspondre au nom de la région lié au numéro insee de l'onglet";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(HashMap<String, String> param) {
		if(StringUtils.isNotBlank(param.get("nomRegion")) && StringUtils.isNotBlank(param.get("nomRegionCheck"))) {
			String nomRegion = StringUtils.stripAccents(param.get("nomRegion").replaceAll("[\\-]", " "));
			String nomRegionCheck = StringUtils.stripAccents(param.get("nomRegionCheck").replaceAll("[\\-]", " ")); 
			return nomRegion.equalsIgnoreCase(nomRegionCheck);	
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
