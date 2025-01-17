package com.geremi.bdrepbatch.rules;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleR6ValideLat")
public class RegleR6ValideLat extends Regle<String>{
	/*
	 *séparateur numerique = point, les x/y sont transformés en point dans la base de données
	 * */
	
	public final String CODE = "R6";
	public final String LABEL = "Impossible de créer la géométrie, coordonnées incorrectes";
	public final boolean BLOQUANTE = true;
	
	@Override
	protected boolean test(String param) {

		if (StringUtils.isNotBlank(param) && param.matches("^-?[0-9]+(\\.[0-9]+)?$")) {
			BigDecimal bd = new BigDecimal(param);
			return BigDecimal.valueOf(-90).compareTo(bd) <= 0 && BigDecimal.valueOf(90).compareTo(bd) >=0;
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
