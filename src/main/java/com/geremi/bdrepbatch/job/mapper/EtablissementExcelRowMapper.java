package com.geremi.bdrepbatch.job.mapper;

import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import com.geremi.bdrepbatch.job.dto.EtablissementDTO;

public class EtablissementExcelRowMapper implements RowMapper<EtablissementDTO> {

	private Integer columsToSkip;

	public EtablissementExcelRowMapper(Integer columsToSkip) {
		this.columsToSkip = columsToSkip;
	}
	@Override
	public EtablissementDTO mapRow(RowSet rowSet) throws Exception {
		if (rowSet == null || rowSet.getCurrentRow() == null) {
			return null;
		}

		String[] currentRow = rowSet.getCurrentRow();
		EtablissementDTO etablissementDTO = new EtablissementDTO();
		etablissementDTO.setIdLigneExcel(String.valueOf(rowSet.getCurrentRowIndex()+1));
		etablissementDTO.setAnnee(currentRow[0 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setCodeEtablissement(currentRow[1 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setNomEtablissement(currentRow[2 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setAdresseSite(currentRow[3 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setCodePostal(currentRow[4 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setCommune(currentRow[5 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setNumeroSiret(currentRow[6 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setCodeAPE(currentRow[7 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setActivitePrincipale(currentRow[8 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setAbscisseLongitudeX(currentRow[10 + this.columsToSkip].replace("\n", "").replace(',', '.').trim());
		etablissementDTO.setOrdonneeLatitudeY(currentRow[11 + this.columsToSkip].replace("\n", "").replace(',', '.').trim());
		etablissementDTO.setVolumeProduction(currentRow[12 + this.columsToSkip].replace(',', '.').replace("\n", ""));
		etablissementDTO.setUnite(currentRow[13 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setMatiereProduite(currentRow[14 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setNbHeuresExploitation(currentRow[15 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setNbEmployes(currentRow[16 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setSiteInternet(currentRow[17 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setInformationsComplementaires(currentRow[18 + this.columsToSkip].replace("\n", " "));
		etablissementDTO.setCommentairesSection(currentRow[19 + this.columsToSkip].replace("\n", " "));

		return etablissementDTO;
	}
}
