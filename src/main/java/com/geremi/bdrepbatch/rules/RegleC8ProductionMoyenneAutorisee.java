package com.geremi.bdrepbatch.rules;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleC8ProductionMoyenneAutorisee")
public class RegleC8ProductionMoyenneAutorisee extends Regle<HashMap<String,String>> {

	public final String CODE = "C8";
	public final String LABEL = "Production moyenne autorisée supérieure à la production maximale autorisée";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(HashMap<String, String> param) {
		if(StringUtils.isNotBlank(param.get("productionMoyenneAutorisee")) && StringUtils.isNotBlank(param.get("productionMaximaleAutorisee"))) {
			BigDecimal productionMoyenneAutorisee = new BigDecimal(param.get("productionMoyenneAutorisee"));
			BigDecimal productionMaximaleAutorisee = new BigDecimal(param.get("productionMaximaleAutorisee"));
			return (productionMaximaleAutorisee).compareTo(productionMoyenneAutorisee)>=0;	
			
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
