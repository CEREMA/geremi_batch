package com.geremi.bdrepbatch.job.process;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.CarrProdDestinationDTO;
import com.geremi.bdrepbatch.job.model.CarrProdDestination;

public class CarrProdDestinationExcelProcessor implements ItemProcessor<CarrProdDestinationDTO, CarrProdDestination> {

	@Override
	public CarrProdDestination process(CarrProdDestinationDTO item) throws Exception {
		return new CarrProdDestination(StringUtils.isNotBlank(item.getAnnee())?item.getAnnee():null, 
				StringUtils.isNotBlank(item.getCodeEtablissement())?item.getCodeEtablissement():null, 
				StringUtils.isNotBlank(item.getIdLigneExcel())?item.getIdLigneExcel():null, 
				StringUtils.isNotBlank(item.getFamilleRattachement())?item.getFamilleRattachement():null, 
				StringUtils.isNotBlank(item.getTypeProduitsExpedies())?item.getTypeProduitsExpedies():null, 
				StringUtils.isNotBlank(item.getDestination())?item.getDestination():null, 
				StringUtils.isNotBlank(item.getTonnage())?item.getTonnage():null, 
				StringUtils.isNotBlank(item.getCommentaires())?item.getCommentaires():null);
	}

}
