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
@Table(name = "ref_anomalie", schema = "geremi")
public class RefAnomalie {

	@Id
	@Column(name = "code_anomalie")
	private String codeAnomalie;

	@Column(name = "libelle_anomalie")
	private String libelleAnomalie;

	public RefAnomalie(String codeAnomalie2, String libelleAnomalie2) {
		this.codeAnomalie=codeAnomalie2;
		this.libelleAnomalie=libelleAnomalie2;
	}
	
	public RefAnomalie() {
	}
}
