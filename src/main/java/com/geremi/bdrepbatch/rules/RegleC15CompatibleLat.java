package com.geremi.bdrepbatch.rules;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component("RegleC15CompatibleLat")
public class RegleC15CompatibleLat extends Regle<String>{

	/**
	 * Les coordonnées doivent être compatibles avec des longitudes et lattitudes (-180 < lon < 180, -90 < lat < 90)
	 */
	
	public final String CODE = "C15";
	public final String LABEL = "Les coordonnées doivent être compatibles avec des latitudes (-90 < lat < 90)";
	public final boolean BLOQUANTE = false;
	
	@Override
	protected boolean test(String param) {
		
		BigDecimal bd = new BigDecimal(param);
		return BigDecimal.valueOf(-90).compareTo(bd) <= 0 && BigDecimal.valueOf(90).compareTo(bd) >=0;

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

