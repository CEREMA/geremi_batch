package com.geremi.bdrepbatch.job.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.config.ExcelSheetReader;
import com.geremi.bdrepbatch.job.dto.DeclarationDTO;
import com.geremi.bdrepbatch.job.mapper.DeclarationRowMapper;

public class DeclarationExcelReader extends ExcelSheetReader<DeclarationDTO>{
	private static final Logger log = LoggerFactory.getLogger(DeclarationExcelReader.class);

	public DeclarationExcelReader(String inputFilePath, DeclarationRowMapper declarationRowMapper,
			String sheetDeclaration, Integer linesToSkip) {
		log.debug("Creating DeclarationReader");
		setLinesToSkip(linesToSkip);
		setResource(new FileSystemResource(inputFilePath));
		setRowMapper(declarationRowMapper);
		setSheetNumber(Integer.parseInt(sheetDeclaration));
		log.debug("DeclarationReader created successfully");
	}
}
