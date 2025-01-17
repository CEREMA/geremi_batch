package com.geremi.bdrepbatch.job.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.config.ExcelSheetReader;
import com.geremi.bdrepbatch.job.dto.CarrProdExtractionDTO;
import com.geremi.bdrepbatch.job.mapper.CarrProdExtractionRowMapper;

public class CarrProdExtractionExcelReader extends ExcelSheetReader<CarrProdExtractionDTO>{
	private static final Logger log = LoggerFactory.getLogger(CarrProdExtractionExcelReader.class);

	public CarrProdExtractionExcelReader(String inputFilePath, CarrProdExtractionRowMapper carrProdExtractionRowMapper,
			String sheetcarrProdExtraction, Integer linesToSkip) {
		log.debug("Creating CarrProdExtractionExcelReader");
		setLinesToSkip(linesToSkip);
		setResource(new FileSystemResource(inputFilePath));
		setRowMapper(carrProdExtractionRowMapper);
		setSheetNumber(Integer.parseInt(sheetcarrProdExtraction));
		log.debug("CarrProdExtractionExcelReader created successfully");
	}
}
