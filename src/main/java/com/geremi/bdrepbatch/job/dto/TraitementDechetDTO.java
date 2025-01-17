package com.geremi.bdrepbatch.job.dto;

import lombok.Data;

@Data
public class TraitementDechetDTO {

	private String annee;
	private String codeEtablissement;
	private String idLigneExcel;
	private String codeDechet;
	private String libelleDechet;
	private String dangereux;
	private String statutSortieDechet;
	private String departementOrigine;
	private String paysOrigine;
	private String quantiteAdmiseTPA;
	private String quantiteTraiteeTPA;
	private String codeOpeElimOuValo;
	private String libelleOpeElimOuValo;
	private String numeroNotification;
	
	public TraitementDechetDTO(String annee, String codeEtablissement, String idLigneExcel,String codeDechet, String libelleDechet, String dangereux, String statutSortieDechet,
			String departementOrigine, String paysOrigine, String quantiteAdmiseTPA, String quantiteTraiteeTPA,
			String codeOpeElimOuValo, String libelleOpeElimOuValo, String numeroNotification) {
		this.annee = annee;
		this.codeEtablissement = codeEtablissement;
		this.idLigneExcel = idLigneExcel;
		this.codeDechet = codeDechet;
		this.libelleDechet = libelleDechet;
		this.dangereux = dangereux;
		this.statutSortieDechet = statutSortieDechet;
		this.departementOrigine = departementOrigine;
		this.paysOrigine = paysOrigine;
		this.quantiteAdmiseTPA = quantiteAdmiseTPA;
		this.quantiteTraiteeTPA = quantiteTraiteeTPA;
		this.codeOpeElimOuValo = codeOpeElimOuValo;
		this.libelleOpeElimOuValo = libelleOpeElimOuValo;
		this.numeroNotification = numeroNotification;
	}
	
	public TraitementDechetDTO() {}

}
