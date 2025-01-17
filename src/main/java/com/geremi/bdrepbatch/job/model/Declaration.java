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
@Table(name = "declaration", schema = "geremi_batch")
public class Declaration {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "declaration_id_seq")
	@SequenceGenerator(schema = "geremi", name = "declaration_id_seq", sequenceName = "geremi.declaration_id_seq", allocationSize = 1)
	private Integer id;
	
	@Column(name = "annee")
	private String annee;
	
	@Column(name = "code_etablissement")
	private String codeEtablissement;	
	
	@Column(name = "id_ligne_excel")
	private String idLigneExcel;	
	
	@Column(name = "code_insee")
	private  String codeInsee;
	
	@Column(name = "commune")
	private  String commune;
	
	@Column(name = "nom_etablissement")
	private  String nomEtablissement;
	
	@Column(name = "service_inspection")
	private  String serviceInspection;
	
	@Column(name = "region")
	private  String region;
	
	@Column(name = "departement")
	private String departement;
	
	@Column(name = "statut_declaration")
	private  String statutDeclaration;
	
	@Column(name = "statut_quota_emission")
	private  String statutQuotaEmission;
	
	@Column(name = "statut_quota_niveaux_activites")
	private  String statutQuotaNiveauxActivites;
	
	@Column(name = "progression")
	private String progression;
	
	@Column(name = "date_derniere_action_declarant")
	private String dateDerniereActionDeclarant;
	
	@Column(name = "date_derniere_action_inspecteur")
	private String dateDerniereActionInspecteur;
	
	@Column(name = "carriere")
	private String carriere;
	
	@Column(name = "quotas")
	private String quotas;
	
	@Column(name = "isdi")
	private String isdi;
	
	@Column(name = "isdnd")
	private String isdnd;
	
	@Column(name = "date_init_declaration")
	private String dateInitDeclaration;

	public Declaration(String annee, String codeEtablissement, String idLigneExcel, String codeInsee, String commune,
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
	
	public Declaration() {}
}
