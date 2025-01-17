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
@Table(name = "commentaire", schema = "geremi")
public class CommentaireFinal {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentaire_id_seq")
	@SequenceGenerator(schema = "geremi", name = "commentaire_id_seq", sequenceName = "geremi.commentaire_id_seq", allocationSize = 1)
	private Integer id;
	
	@Column(name = "code_etablissement")
	private String codeEtablissement;
	
	@Column(name = "annee")
	private Integer annee;
	
	@Column(name = "nom_table")
	private String nomTable;
	
	@Column(name = "libelle")
	private String libelle;
	
	@Column(name="commentaire")
	private String commentaire;

	public CommentaireFinal(String codeEtablissement, Integer annee, String nomTable, String libelle,
			String commentaire) {
		this.codeEtablissement = codeEtablissement;
		this.annee = annee;
		this.nomTable = nomTable;
		this.libelle = libelle;
		this.commentaire = commentaire;
	}
	
	

}
