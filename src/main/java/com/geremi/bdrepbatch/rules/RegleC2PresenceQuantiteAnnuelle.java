package com.geremi.bdrepbatch.rules;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleC2PresenceQuantiteAnnuelle")
public class RegleC2PresenceQuantiteAnnuelle extends Regle<String> {

	public final String CODE = "C2";
	public final String LABEL = "Pas de production ou production nulle";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(String param) {
		return StringUtils.isNotBlank(param) && BigDecimal.valueOf(0).compareTo(new BigDecimal(param))==-1;	
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
