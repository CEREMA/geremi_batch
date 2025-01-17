package com.geremi.bdrepbatch.rules;

import org.springframework.stereotype.Component;

@Component("RegleR10OuiOuNon")
public class RegleR10OuiOuNon extends Regle<String>{

	public final String CODE = "R10";
	public final String LABEL = "deux valeurs possibles Oui ou Non";
	public final boolean BLOQUANTE = true;
	

	@Override
	protected boolean test(String param) {
		return param.matches("^Oui|Non$");
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
