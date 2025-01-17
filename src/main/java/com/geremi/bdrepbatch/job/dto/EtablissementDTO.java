package com.geremi.bdrepbatch.job.dto;

import lombok.Data;

@Data
public class EtablissementDTO {

	private String idLigneExcel;
	private String annee;
	private String codeEtablissement;
	private String nomEtablissement;
	private String adresseSite;
	private String codePostal;
	private String commune;
	private String numeroSiret;
	private String codeAPE;
	private String activitePrincipale;
	private String coordonneesGeographiques;
	private String abscisseLongitudeX;
	private String ordonneeLatitudeY;
	private String volumeProduction;
	private String unite;
	private String matiereProduite;
	private String nbHeuresExploitation;
	private String nbEmployes;
	private String siteInternet;
	private String informationsComplementaires;
	private String commentairesSection;

	// Getters et setters
}
