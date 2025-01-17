package com.geremi.bdrepbatch.job.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import com.geremi.bdrepbatch.job.dto.TraitementDechetDTO;

public class TraitementDechetRowMapper implements RowMapper<TraitementDechetDTO>{
	
	private static final Logger log = LoggerFactory.getLogger(TraitementDechetRowMapper.class);

	private Integer columsToSkip;

	public TraitementDechetRowMapper(Integer columsToSkip) {
		this.columsToSkip = columsToSkip;
	}
	@Override
	public TraitementDechetDTO mapRow(RowSet rs) throws Exception {

		if (rs == null || rs.getCurrentRow() == null) {
			log.debug("RowSet is null or current row is null, returning null");
			return null;
		}
		
		
		String[] currentRow = rs.getCurrentRow();
		TraitementDechetDTO traitementDechetDTO = new TraitementDechetDTO();
		traitementDechetDTO.setIdLigneExcel(String.valueOf(rs.getCurrentRowIndex()+1));
		traitementDechetDTO.setAnnee(currentRow[0 + this.columsToSkip]);
		traitementDechetDTO.setCodeEtablissement(currentRow[1 + this.columsToSkip]);
		traitementDechetDTO.setCodeDechet(currentRow[3 + this.columsToSkip]);
		traitementDechetDTO.setLibelleDechet(currentRow[4 + this.columsToSkip]);
		traitementDechetDTO.setDangereux(currentRow[5 + this.columsToSkip]);
		traitementDechetDTO.setStatutSortieDechet(currentRow[6 + this.columsToSkip]);
		traitementDechetDTO.setDepartementOrigine(currentRow[7 + this.columsToSkip]);
		traitementDechetDTO.setPaysOrigine(currentRow[8 + this.columsToSkip]);
		traitementDechetDTO.setQuantiteAdmiseTPA(currentRow[9 + this.columsToSkip].replace(',', '.'));
		traitementDechetDTO.setQuantiteTraiteeTPA(currentRow[10 + this.columsToSkip].replace(',', '.'));
		traitementDechetDTO.setCodeOpeElimOuValo(currentRow[11 + this.columsToSkip]);
		traitementDechetDTO.setLibelleOpeElimOuValo(currentRow[12 + this.columsToSkip]);
		traitementDechetDTO.setNumeroNotification(currentRow[13 + this.columsToSkip]);
		
		return traitementDechetDTO;
	}

}
