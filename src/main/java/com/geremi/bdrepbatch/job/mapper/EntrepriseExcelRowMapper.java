package com.geremi.bdrepbatch.job.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import com.geremi.bdrepbatch.job.dto.EntrepriseDTO;

public class EntrepriseExcelRowMapper implements RowMapper<EntrepriseDTO> {

	private static final Logger log = LoggerFactory.getLogger(EntrepriseExcelRowMapper.class);

	private Integer columsToSkip;

	public EntrepriseExcelRowMapper(Integer columsToSkip) {
		this.columsToSkip = columsToSkip;
	}
	@Override
	public EntrepriseDTO mapRow(RowSet rowSet) throws Exception {

		if (rowSet == null || rowSet.getCurrentRow() == null) {
			log.debug("RowSet is null or current row is null, returning null");
			return null;
		}

		String[] currentRow = rowSet.getCurrentRow();
		EntrepriseDTO entrepriseDTO = new EntrepriseDTO();
		entrepriseDTO.setAnnee(currentRow[0 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setCodeEtablissement(currentRow[1 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setRaisonSociale(currentRow[2 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setSocieteMere(currentRow[3 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setFormeJuridique(currentRow[4 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setNumeroSiren(currentRow[5 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setAdresse(currentRow[6 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setCommune(currentRow[7 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setPays(currentRow[8 + this.columsToSkip].replace("\n", " "));
		entrepriseDTO.setCommentaires(currentRow[9 + this.columsToSkip].replace("\n", " "));

		return entrepriseDTO;
	}
}
