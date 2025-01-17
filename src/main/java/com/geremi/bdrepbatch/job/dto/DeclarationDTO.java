package com.geremi.bdrepbatch.job.dto;

import lombok.Data;

@Data
public class DeclarationDTO {
	private String annee;
	private String codeEtablissement;
	private String idLigneExcel;
	private String codeInsee;
	private String commune;
	private String nomEtablissement;
	private String serviceInspection;
	private String region;
	private String departement;
	private String statutDeclaration;
	private String statutQuotaEmission;
	private String statutQuotaNiveauxActivites;
	private String progression;
	private String dateDerniereActionDeclarant;
	private String dateDerniereActionInspecteur;
	private String carriere;
	private String quotas;
	private String isdi;
	private String isdnd;
	private String dateInitDeclaration;
	
	public DeclarationDTO(String annee, String codeEtablissement, String idLigneExcel,String codeInsee, String commune,
			String nomEtablissement, String serviceInspection, String region, String departement,
			String statutDeclaration, String statutQuotaEmission, String statutQuotaNiveauxActivites,
			String progression, String dateDerniereActionDeclarant, String dateDerniereActionInspecteur,
			String carriere, String quotas, String isdi, String isdnd, String dateInitDeclaration) {
		this.annee = annee;
		this.codeEtablissement = codeEtablissement;
		this.idLigneExcel = idLigneExcel;
		this.codeInsee = codeInsee;
		this.commune = commune;
		this.nomEtablissement = nomEtablissement;
		this.serviceInspection = serviceInspection;
		this.region = region;
		this.departement = departement;
		this.statutDeclaration = statutDeclaration;
		this.statutQuotaEmission = statutQuotaEmission;
		this.statutQuotaNiveauxActivites = statutQuotaNiveauxActivites;
		this.progression = progression;
		this.dateDerniereActionDeclarant = dateDerniereActionDeclarant;
		this.dateDerniereActionInspecteur = dateDerniereActionInspecteur;
		this.carriere = carriere;
		this.quotas = quotas;
		this.isdi = isdi;
		this.isdnd = isdnd;
		this.dateInitDeclaration = dateInitDeclaration;
	}
	
	public DeclarationDTO() {}

}
