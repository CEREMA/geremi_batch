package com.geremi.bdrepbatch.rules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ParseException;
import org.springframework.stereotype.Component;

@Component("RegleC1DateFinAutorisation")
public class RegleC1DateFinAutorisation extends Regle<HashMap<String,String>>{
	/*
	 * Si l'année du millésime versé est supérieure à celle de la date de fin d'autorisation, les données de production 
	 * du millésime sont marquées suspectes avec le motif "date de fin d'autorisation dépassée". 
	 * Lors de l'étape de validation des corrections par la DREAL, celle-ci, en plus de la correction des données 
	 * (production / date de fin d'autorisation)pourra déclarer un établissement comme ayant été fermé.
	 * */
	
	public final String CODE = "C1";
	public final String LABEL = "Date de fin d'autorisation dépassée";
	public final boolean BLOQUANTE = false;

	/*
	 * param 1 : l'année à tester
	 * param 2 :  la date de fin d'autor
	 * */
	@Override
	protected boolean test(HashMap<String,String> params) {
		if(StringUtils.isNotBlank(params.get("dateFinAutor")) && verifDateFinAutor(params.get("dateFinAutor"))) {
			Integer annee = StringUtils.isNotBlank(params.get("anneeAVerifier"))?Integer.parseInt(params.get("anneeAVerifier")):null;
			String [] splitDateFinAutor = params.get("dateFinAutor").split("/");
			Integer dateFinAutor = splitDateFinAutor!=null && splitDateFinAutor.length>0? Integer.parseInt(splitDateFinAutor[2]):null;
			if(dateFinAutor!=null) {
				return annee <= dateFinAutor;
			} else {
				return true;
			}
		}
		return true;
	}
	
	private boolean verifDateFinAutor(String dateFinAutor) {
		if(StringUtils.isNotBlank(dateFinAutor)) {
			DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			sdf.setLenient(false); 
			try { 
				sdf.parse(dateFinAutor); 
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
