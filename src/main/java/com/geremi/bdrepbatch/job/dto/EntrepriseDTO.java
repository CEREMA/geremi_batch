package com.geremi.bdrepbatch.job.dto;

import lombok.Data;

@Data
public class EntrepriseDTO {
	private String annee;
	private String codeEtablissement;
	private String raisonSociale;
	private String societeMere;
	private String formeJuridique;
	private String numeroSiren;
	private String adresse;
	private String commune;
	private String pays;
	private String commentaires;

}
