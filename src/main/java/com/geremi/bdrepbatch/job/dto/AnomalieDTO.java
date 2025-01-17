package com.geremi.bdrepbatch.job.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Data
public class AnomalieDTO {

	private LocalDate dateCreation;
	private String codeAnomalie;
	private String libelleAnomalie;
	private String nomTable;
	private String nomChamp;
	private String idLigne;
	private String statusAno;
	private Date dateMaj;
	private String user;
	private boolean bloquante;
	private Integer idEtablissement;
	private Integer annee;
	

	public AnomalieDTO(LocalDate dateCreation, String codeAnomalie, String libelleAnomalie,String nomTable, String nomChamp,
			String idLigne, String statusAno, Date dateMaj, String user, boolean bloquante,Integer idEtablissement, Integer annee) {
		this.dateCreation = dateCreation;
		this.codeAnomalie = codeAnomalie;
		this.libelleAnomalie = libelleAnomalie;
		this.nomTable = nomTable;
		this.nomChamp = nomChamp;
		this.idLigne = idLigne;
		this.statusAno = statusAno;
		this.dateMaj = dateMaj;
		this.user = user;
		this.bloquante = bloquante;
		this.idEtablissement = idEtablissement;
		this.annee = annee;
	}

	public AnomalieDTO() {
	}

	public String toString() {
		return codeAnomalie + " - " + libelleAnomalie + " détecté le " + dateCreation + " pour l'enregistrement "
				+ idLigne + " dans la table " + nomTable + " pour le champ " + nomChamp + ".";
	}

}