package com.geremi.bdrepbatch.rules;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
@Component("RegleVerifValeursNumeriques")
public class RegleVerifValeursNumeriques extends Regle<String> {
	
	public final String CODE = "R02";
	public final String LABEL = "La valeur contient des caractères autres que numériques";
	public final boolean BLOQUANTE = true;

	@Override
	protected boolean test(String param) {

		return StringUtils.isNotBlank(param)?param.replace(".","").matches("^-?[0-9]*$"):true;

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
