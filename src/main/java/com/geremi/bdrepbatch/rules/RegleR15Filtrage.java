package com.geremi.bdrepbatch.rules;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleR15Filtrage")
public class RegleR15Filtrage extends Regle<String>{

	public final String CODE = "R15";
	public final String LABEL = "Tous les établissements de l'onglet doivent se trouver dans la liste filtrée selon la règle R17";
	public final boolean BLOQUANTE = true;
	
	@Override
	protected boolean test(String data) {
		return StringUtils.isNotBlank(data);
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
