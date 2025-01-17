package com.geremi.bdrepbatch.rules;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleC14ProgressionDeclaration")
public class RegleC14ProgressionDeclaration extends Regle<String> {

	public final String CODE = "C14";
	public final String LABEL = "La déclaration n'est pas terminée";
	public final boolean BLOQUANTE = false;
	public final Integer AUTHORIZED_VALUE = 100;
	
	@Override
	protected boolean test(String param) {
		Integer intParam = StringUtils.isNotBlank(param)?Integer.parseInt(param):null;
		return AUTHORIZED_VALUE.equals(intParam);
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
