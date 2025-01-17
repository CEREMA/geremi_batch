package com.geremi.bdrepbatch.rules;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleVerifLongueurInput")
public class RegleVerifLongueurInput extends Regle<Map<String,String>>{

	public final String CODE = "R01";
	public final String LABEL = "Valeur trop longue";
	public final boolean BLOQUANTE = true;
	
	@Override
	protected boolean test(Map<String, String> param) {
		Integer longueurLimite = Integer.valueOf(param.get("longueurLimite")) ;
		if(StringUtils.isNotBlank((String)param.get("valeur"))) {
			return ((String)param.get("valeur")).length() <= longueurLimite;	
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
