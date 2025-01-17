package com.geremi.bdrepbatch.job.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import com.geremi.bdrepbatch.job.dto.CarrEnvInfoGenDTO;

public class CarrEnvInfoGenRowMapper implements RowMapper<CarrEnvInfoGenDTO>{

	private Integer columsToSkip;

	public CarrEnvInfoGenRowMapper(Integer columsToSkip) {
		this.columsToSkip = columsToSkip;
	}

	@Override
	public CarrEnvInfoGenDTO mapRow(RowSet rs) throws Exception {
		if (rs == null || rs.getCurrentRow() == null) {
			return null;
		}

		String[] currentRow = rs.getCurrentRow();
		CarrEnvInfoGenDTO carrEnvInfoGenDTO = new CarrEnvInfoGenDTO();
		carrEnvInfoGenDTO.setIdLigneExcel(String.valueOf(rs.getCurrentRowIndex()+1));
		carrEnvInfoGenDTO.setAnnee(currentRow[0 + this.columsToSkip]);
		carrEnvInfoGenDTO.setCodeEtablissement(currentRow[1 + this.columsToSkip]);
		carrEnvInfoGenDTO.setProductionMaxAutorisee(currentRow[3 + this.columsToSkip].replace(',', '.'));
		carrEnvInfoGenDTO.setCommentairesProductionMaxAutorisee(currentRow[4 + this.columsToSkip]);
		carrEnvInfoGenDTO.setProductionMoyenneAutorisee(currentRow[5 + this.columsToSkip].replace(',', '.'));
		carrEnvInfoGenDTO.setCommentairesProductionMoyenneAutorisee(currentRow[6 + this.columsToSkip]);
		carrEnvInfoGenDTO.setDateFinAutor(currentRow[7 + this.columsToSkip]);
		carrEnvInfoGenDTO.setCommentairesDateFinAutor(currentRow[8 + this.columsToSkip]);
		carrEnvInfoGenDTO.setTypeCarriere(currentRow[9 + this.columsToSkip]);
		carrEnvInfoGenDTO.setCommentairesTypeCarriere(currentRow[10 + this.columsToSkip]);
		
		return carrEnvInfoGenDTO;
	}
}
