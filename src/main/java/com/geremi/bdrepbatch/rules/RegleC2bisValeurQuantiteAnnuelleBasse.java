package com.geremi.bdrepbatch.rules;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("RegleC2bisValeurQuantiteAnnuelleBasse")
public class RegleC2bisValeurQuantiteAnnuelleBasse extends Regle<String> {

	public final String CODE = "C2bis";
	public final String LABEL = "Production anormalement basse";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(String param) {
		return StringUtils.isNotBlank(param) && BigDecimal.valueOf(0.01).compareTo(new BigDecimal(param))== -1;
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
