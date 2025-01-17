package com.geremi.bdrepbatch.rules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ParseException;
import org.springframework.stereotype.Component;

@Component("RegleVerifDates")
public class RegleVerifDates extends Regle<String> {

	public final String CODE = "R03";
	public final String LABEL = "Date mal formatée";
	public final boolean BLOQUANTE = true;
	
	@Override
	protected boolean test(String param) {

		if(StringUtils.isNotBlank(param)) {
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false); 
			try { 
				sdf.parse(param); 
				return true;
			} catch (ParseException | java.text.ParseException e) {
				return false;	
			}
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
