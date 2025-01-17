package com.geremi.bdrepbatch.job.reader;

import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.config.ExcelSheetReader;
import com.geremi.bdrepbatch.job.dto.EntrepriseDTO;
import com.geremi.bdrepbatch.job.mapper.EntrepriseExcelRowMapper;

public class EntrepriseExcelReader extends ExcelSheetReader<EntrepriseDTO> {

	public EntrepriseExcelReader(String inputFilePath, EntrepriseExcelRowMapper entrepriseExcelRowMapper,
			String sheetEntreprise) {
		setLinesToSkip(9);
		setResource(new FileSystemResource(inputFilePath));
		setRowMapper(entrepriseExcelRowMapper);
		setSheetNumber(Integer.parseInt(sheetEntreprise));
	}
}
