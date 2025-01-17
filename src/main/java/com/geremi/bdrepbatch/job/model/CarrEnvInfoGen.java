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
@Table(schema = "geremi_batch", name = "carr_env_info_gen")
public class CarrEnvInfoGen {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carr_env_info_gen_id_seq")
	@SequenceGenerator(schema = "geremi_batch", name = "carr_env_info_gen_id_seq", sequenceName = "geremi_batch.carr_env_info_gen_id_seq", allocationSize = 1)
	private Integer id;
	
	@Column(name="annee")
	String annee;
	
	@Column(name="code_etablissement")
	String codeEtablissement;
	
	@Column(name="id_ligne_excel")
	String idLigneExcel;
	
	@Column(name="production_max_autorisee")
	String productionMaxAutorisee;
	
	@Column(name="commentaires_production_max_autorisee")
	String commentairesProductionMaxAutorisee;
	
	@Column(name="production_moyenne_autorisee")
	String productionMoyenneAutorisee;
	
	@Column(name="commentaires_production_moyenne_autorisee")
	String commentairesProductionMoyenneAutorisee;
	
	@Column(name="date_fin_autor")
	String dateFinAutor;
	
	@Column(name="commentaires_date_fin_autor")
	String commentairesDateFinAutor;
	
	@Column(name="type_carriere")
	String typeCarriere;
	
	@Column(name="commentaires_type_carriere")
	String commentairesTypeCarriere;

	public CarrEnvInfoGen(String annee, String codeEtablissement, String idLigneExcel,
			String productionMaxAutorisee, String commentairesProductionMaxAutorisee, String productionMoyenneAutorisee,
			String commentairesProductionMoyenneAutorisee, String dateFinAutor, String commentairesDateFinAutor,
			String typeCarriere, String commentairesTypeCarriere) {
		
		this.annee = annee;
		this.codeEtablissement = codeEtablissement;
		this.idLigneExcel = idLigneExcel;
		this.productionMaxAutorisee = productionMaxAutorisee;
		this.commentairesProductionMaxAutorisee = commentairesProductionMaxAutorisee;
		this.productionMoyenneAutorisee = productionMoyenneAutorisee;
		this.commentairesProductionMoyenneAutorisee = commentairesProductionMoyenneAutorisee;
		this.dateFinAutor = dateFinAutor;
		this.commentairesDateFinAutor = commentairesDateFinAutor;
		this.typeCarriere = typeCarriere;
		this.commentairesTypeCarriere = commentairesTypeCarriere;
	}
}
