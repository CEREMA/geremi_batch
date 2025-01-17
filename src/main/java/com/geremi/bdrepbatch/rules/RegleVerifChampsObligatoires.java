package com.geremi.bdrepbatch.rules;

import org.springframework.stereotype.Component;

import org.apache.commons.lang.StringUtils;

@Component("RegleVerifChampsObligatoires")
public class RegleVerifChampsObligatoires extends Regle<String>{

	public final String CODE = "R00";
	public final String LABEL = "Ce champ ne peut pas contenir de valeur null";
	public final boolean BLOQUANTE = true;
	
	@Override
	protected boolean test(String param) {
		
		return StringUtils.isNotEmpty(param);
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
