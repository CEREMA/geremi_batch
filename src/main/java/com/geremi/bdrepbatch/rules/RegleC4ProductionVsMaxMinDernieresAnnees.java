package com.geremi.bdrepbatch.rules;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleC4ProductionVsMaxMinDernieresAnnees")
public class RegleC4ProductionVsMaxMinDernieresAnnees extends Regle<HashMap<String,String>> {

	/*Si le total des productions de l'établissement est supérieur au maximun du total des productions des 3 à 10 années précédentes 
	 * (selon disponibilité) ou inférieur au minimum du total de ces mêmes productions, les données de production du millésime sont 
	 * marquées suspectes avec les motifs respectifs "production déclarée supérieure au maximun des (n) dernières années" 
	 * ou  "production déclarée inférieure au minimum des (n) dernières années"*/
	
	public final String CODE = "C4";
	public final String LABEL = "Production déclarée supérieure/inférieure à la production maximale/minimale des 3 à 10 dernières années précédentes";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(HashMap<String, String> param) {
		if(StringUtils.isNotBlank(param.get("maxProdAnneesPrecedentes")) && StringUtils.isNotBlank(param.get("minProdAnneesPrecedentes"))) {
			BigDecimal totalProdEtab = new BigDecimal(String.valueOf(param.get("totalQuantiteAnnuelle")));
			BigDecimal maxTotauxPourEtab = new BigDecimal(String.valueOf(param.get("maxProdAnneesPrecedentes")));
			BigDecimal minTotauxPourEtab = new BigDecimal(String.valueOf(param.get("minProdAnneesPrecedentes")));
			return maxTotauxPourEtab.compareTo(totalProdEtab)>=0 && minTotauxPourEtab.compareTo(totalProdEtab)<=0;
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
