package com.geremi.bdrepbatch.rules;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleR7VerifCommune")
public class RegleC17VerifCommune extends Regle<HashMap<String,String>>{

	public final String CODE = "C17";
	public final String LABEL = "La commune doit correspondre au numero insee de la commune  spécifiée de l'onglet";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(HashMap<String, String> param) {
		if(StringUtils.isNotBlank(param.get("nomCommune")) && StringUtils.isNotBlank(param.get("nomCommuneCheck"))) {
			String nomCommune = StringUtils.lowerCase(StringUtils.stripAccents(param.get("nomCommune").replaceAll("[\\-]", " ").replaceAll("œ","oe")));
			String nomCommuneCheck = StringUtils.lowerCase(StringUtils.stripAccents(param.get("nomCommuneCheck").replaceAll("[\\-]", " ").replaceAll("œ","oe")));
			String nomCommuneSansAbbr = nomCommune.replaceAll("\\bst\\b", "saint").replaceAll("\\bste\\b", "sainte").replaceAll("'"," ").toLowerCase();//ne fonctionne pas, à corriger (prendre en compte les "ste"/"sainte" également) 
			String nomCommuneCheckSansAbbr = nomCommuneCheck.replaceAll("\\bst\\b", "saint").replaceAll("\\bste\\b", "sainte").replaceAll("'"," ").toLowerCase();
			return nomCommuneSansAbbr.equalsIgnoreCase(nomCommuneCheckSansAbbr);		

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
