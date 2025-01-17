package com.geremi.bdrepbatch.job.dto;

import lombok.Data;

@Data
public class CarrProdDestinationDTO {
	private String annee;
	private String codeEtablissement;
	private String idLigneExcel;
	private String familleRattachement;
	private String typeProduitsExpedies;
	private String destination;
	private String tonnage;
	private String commentaires;

}
