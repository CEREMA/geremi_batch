package com.geremi.bdrepbatch.job.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.config.ExcelSheetReader;
import com.geremi.bdrepbatch.job.dto.TraitementDechetDTO;
import com.geremi.bdrepbatch.job.mapper.TraitementDechetRowMapper;

public class TraitementDechetReader extends ExcelSheetReader<TraitementDechetDTO> {
	private static final Logger log = LoggerFactory.getLogger(TraitementDechetReader.class);

	public TraitementDechetReader(String inputFilePath, TraitementDechetRowMapper traitementDechetRowMapper,
			String sheetTRaitementDechet, Integer linesToSkip) {
		setLinesToSkip(linesToSkip);
		setResource(new FileSystemResource(inputFilePath));
		setRowMapper(traitementDechetRowMapper);
		setSheetNumber(Integer.parseInt(sheetTRaitementDechet));
	}
}
