package com.geremi.bdrepbatch.job.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.geremi.bdrepbatch.job.utils.RefUtils;

import lombok.Data;

@Data
@Entity
@Table(name = "anomalie", schema = "geremi_batch")
public class Anomalie {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anomalie_id_seq")
	@SequenceGenerator(schema = "geremi_batch", name = "anomalie_id_seq", sequenceName = "geremi_batch.anomalie_id_seq", allocationSize = 1)
	private Integer idAnomalie;

	@Column(name = "date_creation")
	private LocalDate dateCreation;

	@Column(name = "code_ref_anomalie")
	private String codeRefAnomalie;

	@Column(name = "nom_table")
	private String nomTable;

	@Column(name = "nom_champ")
	private String nomChamp;

	@Column(name = "id_ligne")
	private String idLigne;

	@Column(name = "id_verification")
	private Integer idVerification;

	@ManyToOne
	@JoinColumn(name = "id_statut_ano", referencedColumnName = "id_ref_statut")
	private RefStatut refStatut;

	@Column(name = "date_maj")
	private LocalDate dateMaj;
	
	@Column(name="bloquante")
	private boolean bloquante;
	
	@Column(name="id_etablissement")
	private Integer idEtablissement;
	
	@Column(name="annee")
	private Integer annee;


	public Anomalie(LocalDate dateCreation, String codeRefAnomalie,String nomTable,
			String nomChamp, String idLigne, Integer idVerification, RefStatut refStatut, LocalDate dateMaj,boolean bloquante,Integer idEtablissement, Integer annee) {
		
		this.dateCreation = dateCreation;
		this.codeRefAnomalie = codeRefAnomalie;
		this.nomTable = nomTable;
		this.nomChamp = nomChamp;
		this.idLigne = idLigne;
		this.idVerification = idVerification;
		this.refStatut = refStatut;
		this.dateMaj = dateMaj;
		this.bloquante = bloquante;
		this.idEtablissement = idEtablissement;
		this.annee = annee;
	}
	
	public String toString() {
		if (codeRefAnomalie == "R14") {
			return "Onglet : " + nomTable + "; Colonne : " +nomChamp+ "; Code établissement : "+idLigne+"  ; codeRefAnomalie : " +codeRefAnomalie+"  ; libelleAnomalie : " +RefUtils.getRefAnomalies().get(codeRefAnomalie)+".\n";
		}
		return "Onglet : " + nomTable + "; Colonne : " +nomChamp+ "; Id ligne fichier: "+idLigne+"  ; codeRefAnomalie : " +codeRefAnomalie+"  ; libelleAnomalie : " +RefUtils.getRefAnomalies().get(codeRefAnomalie)+".\n";
	}
}
