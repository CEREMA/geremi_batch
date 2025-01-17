package com.geremi.bdrepbatch.job.dto;

import lombok.Data;

@Data
public class CarrEnvInfoGenDTO {
	String annee;
	String codeEtablissement;
	String idLigneExcel;
	String productionMaxAutorisee;
	String commentairesProductionMaxAutorisee;
	String productionMoyenneAutorisee;
	String commentairesProductionMoyenneAutorisee;
	String dateFinAutor;
	String commentairesDateFinAutor;
	String typeCarriere;
	String commentairesTypeCarriere;
	
}
