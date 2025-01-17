package com.geremi.bdrepbatch.rules;

import org.springframework.stereotype.Component;

import org.apache.commons.lang.StringUtils;

@Component("RegleC13StatutDeclaration")
public class RegleC13StatutDeclaration extends Regle<String> {
	
	public final String CODE = "C13";
	public final String LABEL = "Il existe des statuts de déclaration non valides";
	public final boolean BLOQUANTE = false;
	public final String AUTHORIZED_VALUE = "Validée";

	@Override
	protected boolean test(String param) {
		if(StringUtils.isNotBlank(param)) {
			return AUTHORIZED_VALUE.equals(param);
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
