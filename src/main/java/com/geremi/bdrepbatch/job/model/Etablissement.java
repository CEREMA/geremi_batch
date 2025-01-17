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
@Table(schema = "geremi_batch", name = "Etablissement")
public class Etablissement {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "etablissement_id_seq")
	@SequenceGenerator(schema = "geremi", name = "etablissement_id_seq", sequenceName = "geremi.etablissement_id_seq", allocationSize = 1)
	private Integer id;
	
	@Column(name = "id_ligne_excel")
	private String idLigneExcel;

	@Column(name = "annee")
	private String annee;

	@Column(name = "code_etablissement")
	private String codeEtablissement;

	@Column(name = "nom_etablissement")
	private String nomEtablissement;

	@Column(name = "adressesite")
	private String adresseSite;

	@Column(name = "codepostal")
	private String codePostal;

	@Column(name = "commune")
	private String commune;

	@Column(name = "numerosiret")
	private String numeroSiret;

	@Column(name = "codeape")
	private String codeAPE;

	@Column(name = "activiteprincipale")
	private String activitePrincipale;

	@Column(name = "coordonneesgeographiques")
	private String coordonneesGeographiques;

	@Column(name = "abscisselongitudex")
	private String abscisseLongitudeX;

	@Column(name = "ordonneelatitudey")
	private String ordonneeLatitudeY;

	@Column(name = "volumeproduction")
	private String volumeProduction;

	@Column(name = "unite")
	private String unite;

	@Column(name = "matiereproduite")
	private String matiereProduite;

	@Column(name = "nbheuresexploitation")
	private String nbHeuresExploitation;

	@Column(name = "nbemployes")
	private String nbEmployes;

	@Column(name = "adressesiteinternet")
	private String adressesiteInternet;

	@Column(name = "informationscomplementaires")
	private String informationsComplementaires;

	@Column(name = "commentairessection")
	private String commentairesSection;

	public Etablissement(String idLigneExcel, String annee, String codeEtablissement,
			String nomEtablissement, String adresseSite, String codePostal, String commune, String numeroSiret,
			String codeAPE, String activitePrincipale, String coordonneesGeographiques, String abscisseLongitudeX,
			String ordonneeLatitudeY, String volumeProduction, String unite, String matiereProduite,
			String nbHeuresExploitation, String nbEmployes, String adressesiteInternet,
			String informationsComplementaires, String commentairesSection) {

		this.idLigneExcel = idLigneExcel;
		this.annee = annee;
		this.codeEtablissement = codeEtablissement;
		this.nomEtablissement = nomEtablissement;
		this.adresseSite = adresseSite;
		this.codePostal = codePostal;
		this.commune = commune;
		this.numeroSiret = numeroSiret;
		this.codeAPE = codeAPE;
		this.activitePrincipale = activitePrincipale;
		this.coordonneesGeographiques = coordonneesGeographiques;
		this.abscisseLongitudeX = abscisseLongitudeX;
		this.ordonneeLatitudeY = ordonneeLatitudeY;
		this.volumeProduction = volumeProduction;
		this.unite = unite;
		this.matiereProduite = matiereProduite;
		this.nbHeuresExploitation = nbHeuresExploitation;
		this.nbEmployes = nbEmployes;
		this.adressesiteInternet = adressesiteInternet;
		this.informationsComplementaires = informationsComplementaires;
		this.commentairesSection = commentairesSection;
	}

	// Getters and setters
}
