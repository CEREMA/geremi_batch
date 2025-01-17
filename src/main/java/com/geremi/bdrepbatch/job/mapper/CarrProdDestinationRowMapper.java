package com.geremi.bdrepbatch.job.mapper;

import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import com.geremi.bdrepbatch.job.dto.CarrProdDestinationDTO;

public class CarrProdDestinationRowMapper implements RowMapper<CarrProdDestinationDTO>{

	private Integer columsToSkip;

	public CarrProdDestinationRowMapper(Integer columsToSkip) {
		this.columsToSkip = columsToSkip;
	}

	@Override
	public CarrProdDestinationDTO mapRow(RowSet rs) throws Exception {
		if (rs == null || rs.getCurrentRow() == null) {
			return null;
		}

		String[] currentRow = rs.getCurrentRow();
		CarrProdDestinationDTO carrProdDestinationDTO = new CarrProdDestinationDTO();
		carrProdDestinationDTO.setIdLigneExcel(String.valueOf(rs.getCurrentRowIndex()+1));
		carrProdDestinationDTO.setAnnee(currentRow[0 + this.columsToSkip].replace("\n", " "));
		carrProdDestinationDTO.setCodeEtablissement(currentRow[1+ this.columsToSkip].replace("\n", " "));
		carrProdDestinationDTO.setFamilleRattachement(currentRow[3 + this.columsToSkip].replace("\n", " "));
		carrProdDestinationDTO.setTypeProduitsExpedies(currentRow[4 + this.columsToSkip].replace("\n", " "));
		carrProdDestinationDTO.setDestination(currentRow[5 + this.columsToSkip].replace("\n", " "));
		carrProdDestinationDTO.setTonnage(currentRow[6 + this.columsToSkip].replace("\n", " ").replace(',', '.'));
		carrProdDestinationDTO.setCommentaires(currentRow[7 + this.columsToSkip].replace("\n", " "));
		
		return carrProdDestinationDTO;
	}

}
