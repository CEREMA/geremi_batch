package com.geremi.bdrepbatch.job.dto;

import lombok.Data;

@Data
public class CarrProdExtractionDTO {
	private String annee;
	private String codeEtablissement;
	private String idLigneExcel;
	private String quantiteRestanteAccessible;
	private String commentairesQuantiteRestanteAccessible;
	private String quantiteAnnuelleSteriles;
	private String commentairesQuantiteAnnuelleSteriles;
	private String substancesARecycler;
	private String familleUsageDebouches;
	private String precisionFamilleUsage;
	private String sousFamilleUsageDebouches;
	private String precisionSousFamilleUsage;
	private String sousFamilleUsageDebouches2;
	private String precisionSousFamilleUsage2;
	private String quantiteAnnuelle;
	private String commentairesQuantiteAnnuelle;
	private String totalSubstance;
	private String totalDontSteriles;
	private String commentairesAlerte;
}
