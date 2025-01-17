package com.geremi.bdrepbatch.job.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.config.ExcelSheetReader;
import com.geremi.bdrepbatch.job.dto.EtablissementDTO;
import com.geremi.bdrepbatch.job.mapper.EtablissementExcelRowMapper;

public class EtablissementExcelReader extends ExcelSheetReader<EtablissementDTO> {

	private static final Logger log = LoggerFactory.getLogger(EtablissementExcelReader.class);

	public EtablissementExcelReader(String inputFilePath, EtablissementExcelRowMapper etablissementExcelRowMapper,
			String sheetetablissement, Integer linesToSkip) {
		setLinesToSkip(linesToSkip);
		setResource(new FileSystemResource(inputFilePath));
		setRowMapper(etablissementExcelRowMapper);
		setSheetNumber(Integer.parseInt(sheetetablissement));
	}
}
