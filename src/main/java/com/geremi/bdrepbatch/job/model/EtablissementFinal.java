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
@Table(schema = "geremi", name = "Etablissement")
public class EtablissementFinal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "etablissement_id_seq")
	@SequenceGenerator(schema = "geremi", name = "etablissement_id_seq", sequenceName = "geremi.etablissement_id_seq", allocationSize = 1)
	private Integer id;

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

}
