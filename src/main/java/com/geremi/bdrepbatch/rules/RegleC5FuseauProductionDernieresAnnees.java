package com.geremi.bdrepbatch.rules;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleC5FuseauProductionDernieresAnnees")
public class RegleC5FuseauProductionDernieresAnnees extends Regle<HashMap<String,String>> {
	
	/*
	 * Si le contrôle C4 passe, et que le total de production est supérieur à la moyenne des productions des 3 à 10 années précédentes 
	 * (selon disponibilité) augmenté de 1,25 x l'écart type de ces mêmes productions, ou inférieur à cette moyenne 
	 * diminuée de 1,25 x cet écart type,  les données de production du millésime sont marquées suspectes avec les motifs respectifs 
	 * "production déclarée supérieure au fuseau de production des (n) dernières années" ou  
	 * "production déclarée inférieure au fuseau de production des (n) dernières années"
	 * */
	
	public final String CODE = "C5";
	public final String LABEL = "Production déclarée supérieure/inférieure au fuseau de production des 3 à 10 dernières années";
	public final boolean BLOQUANTE = false;

	@Override
	protected boolean test(HashMap<String, String> param) {
		if(StringUtils.isNotBlank(param.get("avgProd")) && StringUtils.isNotBlank(param.get("ecartType"))) {
			BigDecimal totalProdEtab = new BigDecimal(String.valueOf(param.get("totalQuantiteAnnuelle")));
			BigDecimal moyenneProd = new BigDecimal(String.valueOf(param.get("avgProd")));
			BigDecimal ecartType = new BigDecimal(String.valueOf(param.get("ecartType")));
			return (totalProdEtab.compareTo(moyenneProd.add(ecartType.multiply(new BigDecimal(1.25))))<=0)
					&&(totalProdEtab.compareTo(moyenneProd.subtract(ecartType.multiply(new BigDecimal(1.25))))>=0);
				
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
