package com.geremi.bdrepbatch.job.mapper;

import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import com.geremi.bdrepbatch.job.dto.CarrProdExtractionDTO;

public class CarrProdExtractionRowMapper implements RowMapper<CarrProdExtractionDTO>{

	private Integer columsToSkip;

	public CarrProdExtractionRowMapper(Integer columsToSkip) {
		this.columsToSkip = columsToSkip;
	}

	@Override
	public CarrProdExtractionDTO mapRow(RowSet rs) throws Exception {
		if (rs == null || rs.getCurrentRow() == null) {
			return null;
		}

		String[] currentRow = rs.getCurrentRow();
		CarrProdExtractionDTO carrProdExtractionDTO = new CarrProdExtractionDTO();
		carrProdExtractionDTO.setIdLigneExcel(String.valueOf(rs.getCurrentRowIndex()+1));
		carrProdExtractionDTO.setAnnee(currentRow[0 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setCodeEtablissement(currentRow[1 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setQuantiteRestanteAccessible(currentRow[3 + this.columsToSkip].replace("\n", " ").replace(',', '.'));
		carrProdExtractionDTO.setCommentairesQuantiteRestanteAccessible(currentRow[4 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setQuantiteAnnuelleSteriles(currentRow[5 + this.columsToSkip].replace("\n", " ").replace(',', '.'));
		carrProdExtractionDTO.setCommentairesQuantiteAnnuelleSteriles(currentRow[6 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setSubstancesARecycler(currentRow[7 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setFamilleUsageDebouches(currentRow[8 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setPrecisionFamilleUsage(currentRow[9 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setSousFamilleUsageDebouches(currentRow[10 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setPrecisionSousFamilleUsage(currentRow[11 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setSousFamilleUsageDebouches2(currentRow[12 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setPrecisionSousFamilleUsage2(currentRow[13 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setQuantiteAnnuelle(currentRow[14 + this.columsToSkip].replace("\n", " ").replace(',', '.'));
		carrProdExtractionDTO.setCommentairesQuantiteAnnuelle(currentRow[15 + this.columsToSkip].replace("\n", " "));
		carrProdExtractionDTO.setTotalSubstance(currentRow[16 + this.columsToSkip].replace("\n", " ").replace(',', '.'));
		carrProdExtractionDTO.setTotalDontSteriles(currentRow[17 + this.columsToSkip].replace("\n", " ").replace(',', '.'));
		carrProdExtractionDTO.setCommentairesAlerte(currentRow[18 + this.columsToSkip].replace("\n", " "));

		return carrProdExtractionDTO;
	}

}
