package com.geremi.bdrepbatch.rules;

import org.apache.commons.lang.StringUtils;
import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component("RegleC3QuantiteAnnuelleProductionMaximaleAutorisee")
public class RegleC3QuantiteAnnuelleProductionMaximaleAutorisee extends Regle<HashMap<String,String>>{

	public final String CODE = "C3";
	public final String LABEL = "Production déclarée supérieure à la production maximale autorisée";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(HashMap<String, String> param) {

		String totalQuantiteAnnuelleStr = param.get("totalQuantiteAnnuelle");
		String productionMaxAutoriseeStr = param.get("productionMaxAutorisee");

		if (StringUtils.isNotBlank(totalQuantiteAnnuelleStr) && StringUtils.isNotBlank(productionMaxAutoriseeStr)) {
			BigDecimal quantiteAnnuelle = new BigDecimal(totalQuantiteAnnuelleStr);
			BigDecimal productionMaxAutorisee = new BigDecimal(productionMaxAutoriseeStr);

			return productionMaxAutorisee.compareTo(quantiteAnnuelle) >= 0;
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
