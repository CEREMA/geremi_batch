package com.geremi.bdrepbatch.job.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "traitement_dechet", schema = "geremi_batch")
public class TraitementDechet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "traitement_dechet_id_seq")
	@SequenceGenerator(schema = "geremi", name = "traitement_dechet_id_seq", sequenceName = "geremi.traitement_dechet_id_seq", allocationSize = 1)
	private Integer id;
	
	@Column(name = "annee")
	private String annee;
	
	@Column(name = "code_etablissement")
	private String codeEtablissement;
	
	@Column(name = "id_ligne_excel")
	private String idLigneExcel;
	
	@Column(name = "code_dechet")
	private String codeDechet;
	
	@Column(name = "libelle_dechet")
	private String libelleDechet;
	
	@Column(name = "dangereux")
	private String dangereux;
	
	@Column(name = "statut_sortie_dechet")
	private String statutSortieDechet;
	
	@Column(name = "departement_origine")
	private String departementOrigine;
	
	@Column(name = "pays_origine")
	private String paysOrigine;
	
	@Column(name = "quantite_admise_tpa")
	private String quantiteAdmiseTPA;
	
	@Column(name = "quantite_traitee_tpa")
	private String quantiteTraiteeTPA;
	
	@Column(name = "code_ope")
	private String codeOpeElimOuValo;
	
	@Column(name = "libelle_ope")
	private String libelleOpeElimOuValo;
	
	@Column(name = "numero_notification")
	private String numeroNotification;
	
	public TraitementDechet() {}

	public TraitementDechet(String annee, String codeEtablissement, String idLigneExcel,String codeDechet, String libelleDechet, String dangereux,
			String statutSortieDechet, String departementOrigine, String paysOrigine, String quantiteAdmiseTPA,
			String quantiteTraiteeTPA, String codeOpeElimOuValo, String libelleOpeElimOuValo,
			String numeroNotification) {
		
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

}
