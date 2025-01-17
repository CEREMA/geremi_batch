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
@Table(name = "entreprise", schema = "geremi_batch")
public class Entreprise {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entreprise_id_seq")
	@SequenceGenerator(schema = "geremi_batch", name = "entreprise_id_seq", sequenceName = "geremi_batch.entreprise_id_seq", allocationSize = 1)
	private Integer id;

	@Column(name = "annee")
	private String annee;

	@Column(name = "codeetablissement", length = 10)
	private String codeEtablissement;

	@Column(name = "raisonsociale", length = 150)
	private String raisonSociale;

	@Column(name = "societemere", length = 100)
	private String societeMere;

	@Column(name = "formejuridique", length = 150)
	private String formeJuridique;

	@Column(name = "numerosiren", length = 9)
	private String numeroSiren;

	@Column(name = "adresse")
	private String adresse;

	@Column(name = "commune")
	private String commune;

	@Column(name = "pays")
	private String pays;

	@Column(name = "commentairessection", columnDefinition = "TEXT")
	private String commentairesSection;

	// Getters and Setters
}
