package com.geremi.bdrepbatch.job.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.config.ExcelSheetReader;
import com.geremi.bdrepbatch.job.dto.CarrProdDestinationDTO;
import com.geremi.bdrepbatch.job.mapper.CarrProdDestinationRowMapper;

public class CarrProdDestinationExcelReader extends ExcelSheetReader<CarrProdDestinationDTO>{
	private static final Logger log = LoggerFactory.getLogger(CarrProdExtractionExcelReader.class);

	public CarrProdDestinationExcelReader(String inputFilePath, CarrProdDestinationRowMapper carrProdDestinationRowMapper,
			String sheetcarrProdDestination, Integer linesToSkip) {
		log.debug("Creating CarrProdDestinationExcelReader");
		setLinesToSkip(linesToSkip);
		setResource(new FileSystemResource(inputFilePath));
		setRowMapper(carrProdDestinationRowMapper);
		setSheetNumber(Integer.parseInt(sheetcarrProdDestination));
		log.debug("CarrProdDestinationExcelReader created successfully");
	}
}
