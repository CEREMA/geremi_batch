package com.geremi.bdrepbatch.job.process;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.CarrEnvInfoGenDTO;
import com.geremi.bdrepbatch.job.model.CarrEnvInfoGen;

public class CarrEnvInfoGenExcelProcessor implements ItemProcessor<CarrEnvInfoGenDTO, CarrEnvInfoGen> {

	@Override
	public CarrEnvInfoGen process(CarrEnvInfoGenDTO item) throws Exception {
		
		return new CarrEnvInfoGen(StringUtils.isNotBlank(item.getAnnee()) ? item.getAnnee() : null,
				StringUtils.isNotBlank(item.getCodeEtablissement()) ? item.getCodeEtablissement() : null,
						StringUtils.isNotBlank(item.getIdLigneExcel()) ? item.getIdLigneExcel() :null,
						StringUtils.isNotBlank(item.getProductionMaxAutorisee()) ? item.getProductionMaxAutorisee() :null,
						StringUtils.isNotBlank(item.getCommentairesProductionMaxAutorisee()) ? item.getCommentairesProductionMaxAutorisee() :null,
						StringUtils.isNotBlank(item.getProductionMoyenneAutorisee()) ? item.getProductionMoyenneAutorisee() :null,
						StringUtils.isNotBlank(item.getCommentairesProductionMoyenneAutorisee()) ? item.getCommentairesProductionMoyenneAutorisee() :null,
						StringUtils.isNotBlank(item.getDateFinAutor()) ? item.getDateFinAutor() :null,
						StringUtils.isNotBlank(item.getCommentairesDateFinAutor()) ? item.getCommentairesDateFinAutor() :null,
						StringUtils.isNotBlank(item.getTypeCarriere()) ? item.getTypeCarriere() :null,
						StringUtils.isNotBlank(item.getCommentairesTypeCarriere()) ? item.getCommentairesTypeCarriere() :null);
				
	}
}
