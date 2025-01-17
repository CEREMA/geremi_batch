package com.geremi.bdrepbatch.job.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import com.geremi.bdrepbatch.job.dto.DeclarationDTO;

public class DeclarationRowMapper implements RowMapper<DeclarationDTO>{
	
	private static final Logger log = LoggerFactory.getLogger(DeclarationRowMapper.class);

	private Integer columsToSkip;

	public DeclarationRowMapper(Integer columsToSkip) {
		this.columsToSkip = columsToSkip;
	}

	@Override
	public DeclarationDTO mapRow(RowSet rs) throws Exception {
		if (rs == null || rs.getCurrentRow() == null) {
			log.debug("RowSet is null or current row is null, returning null");
			return null;
		}
		
		String[] currentRow = rs.getCurrentRow();
		
		DeclarationDTO declarationDTO = new DeclarationDTO();
		declarationDTO.setAnnee(currentRow[0 + this.columsToSkip]);
		declarationDTO.setCodeEtablissement(currentRow[1 + this.columsToSkip]);
		declarationDTO.setIdLigneExcel(String.valueOf(rs.getCurrentRowIndex()+1));
		declarationDTO.setCodeInsee(currentRow[3 + this.columsToSkip]);
		declarationDTO.setCommune(currentRow[4 + this.columsToSkip]);
		declarationDTO.setNomEtablissement(currentRow[5 + this.columsToSkip]);
		declarationDTO.setServiceInspection(currentRow[6 + this.columsToSkip]);
		declarationDTO.setRegion(currentRow[7 + this.columsToSkip]);
		declarationDTO.setDepartement(currentRow[8 + this.columsToSkip]);
		declarationDTO.setStatutDeclaration(currentRow[9 + this.columsToSkip]);
		declarationDTO.setStatutQuotaEmission(currentRow[10 + this.columsToSkip]);
		declarationDTO.setStatutQuotaNiveauxActivites(currentRow[11 + this.columsToSkip]);
		declarationDTO.setProgression(currentRow[12 + this.columsToSkip]);
		declarationDTO.setDateDerniereActionDeclarant(currentRow[13 + this.columsToSkip]);
		declarationDTO.setDateDerniereActionInspecteur(currentRow[14 + this.columsToSkip]);
		declarationDTO.setCarriere(currentRow[15 + this.columsToSkip]);
		declarationDTO.setQuotas(currentRow[17 + this.columsToSkip]);
		declarationDTO.setIsdi(currentRow[19 + this.columsToSkip]);
		declarationDTO.setIsdnd(currentRow[21 + this.columsToSkip]);
		declarationDTO.setDateInitDeclaration(currentRow[22 + this.columsToSkip]);
		
		return declarationDTO;
	}
}
