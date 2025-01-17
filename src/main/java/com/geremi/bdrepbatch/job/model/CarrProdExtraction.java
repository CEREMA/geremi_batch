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
@Table(schema = "geremi_batch", name = "carr_prod_extraction")
public class CarrProdExtraction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carr_prod_extraction_id_seq")
	@SequenceGenerator(schema = "geremi", name = "carr_prod_extraction_id_seq", sequenceName = "geremi.extraction_id_seq", allocationSize = 1)
	private Integer id;

	@Column(name = "annee")
	private String annee;

	@Column(name = "code_etablissement")
	private String codeEtablissement;

	@Column(name = "id_ligne_excel")
	private String idLigneExcel;

	@Column(name = "quantite_restante_accessible")
	private String quantiteRestanteAccessible;

	@Column(name = "commentaires_quantite_restante_accessible")
	private String commentairesQuantiteRestanteAccessible;

	@Column(name = "quantite_annuelle_steriles")
	private String quantiteAnnuelleSteriles;

	@Column(name = "commentaires_quantite_annuelle_steriles")
	private String commentairesQuantiteAnnuelleSteriles;

	@Column(name = "substances_a_recycler")
	private String substancesARecycler;

	@Column(name = "famille_usage_debouches")
	private String familleUsageDebouches;

	@Column(name = "precision_famille_usage")
	private String precisionFamilleUsage;

	@Column(name = "sous_famille_usage_debouches")
	private String sousFamilleUsageDebouches;

	@Column(name = "precision_sous_famille_usage")
	private String precisionSousFamilleUsage;

	@Column(name = "sous_famille_usage_debouches_2")
	private String sousFamilleUsageDebouches2;

	@Column(name = "precision_sous_famille_usage_2")
	private String precisionSousFamilleUsage2;

	@Column(name = "quantite_annuelle")
	private String quantiteAnnuelle;

	@Column(name = "commentaires_quantite_annuelle")
	private String commentairesQuantiteAnnuelle;

	@Column(name = "total_substance")
	private String totalSubstance;

	@Column(name = "total_dont_steriles")
	private String totalDontSteriles;

	@Column(name = "commentaires_alerte")
	private String commentairesAlerte;

	public CarrProdExtraction(String annee, String codeEtablissement, String idLigneExcel,
			String quantiteRestanteAccessible, String commentairesQuantiteRestanteAccessible, String quantiteAnnuelleSteriles,
			String commentairesQuantiteAnnuelleSteriles, String substancesARecycler, String familleUsageDebouches,
			String precisionFamilleUsage, String sousFamilleUsageDebouches, String precisionSousFamilleUsage,
			String sousFamilleUsageDebouches2, String precisionSousFamilleUsage2, String quantiteAnnuelle,
			String commentairesQuantiteAnnuelle, String totalSubstance, String totalDontSteriles, String commentairesAlerte) {
		this.annee=annee;
		this.codeEtablissement = codeEtablissement;
		this.idLigneExcel = idLigneExcel;
		this.quantiteRestanteAccessible = quantiteRestanteAccessible;
		this.commentairesQuantiteRestanteAccessible = commentairesQuantiteRestanteAccessible;
		this.quantiteAnnuelleSteriles = quantiteAnnuelleSteriles;
		this.commentairesQuantiteAnnuelleSteriles = commentairesQuantiteAnnuelleSteriles;
		this.substancesARecycler = substancesARecycler;
		this.familleUsageDebouches = familleUsageDebouches;
		this.precisionFamilleUsage = precisionFamilleUsage;
		this.sousFamilleUsageDebouches = sousFamilleUsageDebouches;
		this.precisionSousFamilleUsage = precisionSousFamilleUsage;
		this.sousFamilleUsageDebouches2 = sousFamilleUsageDebouches2;
		this.precisionSousFamilleUsage2 = precisionSousFamilleUsage2;
		this.quantiteAnnuelle = quantiteAnnuelle;
		this.commentairesQuantiteAnnuelle = commentairesQuantiteAnnuelle;
		this.totalSubstance = totalSubstance;
		this.totalDontSteriles = totalDontSteriles;
		this.commentairesAlerte = commentairesAlerte;
	}
}
