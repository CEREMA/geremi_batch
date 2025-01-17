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
@Table(schema = "geremi_batch", name = "carr_prod_destination")
public class CarrProdDestination {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carr_prod_destination_id_seq")
	@SequenceGenerator(schema = "geremi", name = "carr_prod_destination_id_seq", sequenceName = "geremi.destination_id_seq", allocationSize = 1)
	private Integer id;
	
	@Column(name = "annee")
	private String annee;
	
	@Column(name = "code_etablissement")
	private String codeEtablissement;
	
	@Column(name = "id_ligne_excel")
	private String idLigneExcel;
	
	@Column(name = "famille_rattachement")
	private String familleRattachement;
	
	@Column(name = "type_produits_expedies")
	private String typeProduitsExpedies;
	
	@Column(name = "destination")
	private String destination;
	
	@Column(name = "tonnage")
	private String tonnage;
	
	@Column(name = "commentaires")
	private String commentaires;

	public CarrProdDestination(String annee, String codeEtablissement, String idLigneExcel,
			String familleRattachement, String typeProduitsExpedies, String destination, String tonnage,
			String commentaires) {
		this.annee = annee;
		this.codeEtablissement = codeEtablissement;
		this.idLigneExcel = idLigneExcel;
		this.familleRattachement = familleRattachement;
		this.typeProduitsExpedies = typeProduitsExpedies;
		this.destination = destination;
		this.tonnage = tonnage;
		this.commentaires = commentaires;
	}

}
