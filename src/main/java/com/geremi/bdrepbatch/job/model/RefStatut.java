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
@Table(schema = "geremi", name = "ref_statut")
public class RefStatut {

	@Id
	@Column(name = "id_ref_statut")
	private Integer idRefStatut;

	@Column(name = "statut")
	private String statut;

}
