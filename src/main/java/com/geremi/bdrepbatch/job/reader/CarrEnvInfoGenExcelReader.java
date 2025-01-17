package com.geremi.bdrepbatch.job.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.config.ExcelSheetReader;
import com.geremi.bdrepbatch.job.dto.CarrEnvInfoGenDTO;
import com.geremi.bdrepbatch.job.mapper.CarrEnvInfoGenRowMapper;

public class CarrEnvInfoGenExcelReader extends ExcelSheetReader<CarrEnvInfoGenDTO>{
	private static final Logger log = LoggerFactory.getLogger(CarrEnvInfoGenExcelReader.class);


	public CarrEnvInfoGenExcelReader(String inputFilePath, CarrEnvInfoGenRowMapper carrEnvInfoGenRowMapper,
			String sheetCarrEnvInfoGen, Integer linesToSkip) {
		log.debug("Creating CarrEnvInfoGenExcelReader");
		setLinesToSkip(linesToSkip);
		setResource(new FileSystemResource(inputFilePath));
		setRowMapper(carrEnvInfoGenRowMapper);
		setSheetNumber(Integer.parseInt(sheetCarrEnvInfoGen));
		log.debug("CarrEnvInfoGenExcelReader created successfully");
	}
}
