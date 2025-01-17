package com.geremi.bdrepbatch.job.process;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.CarrProdExtractionDTO;
import com.geremi.bdrepbatch.job.model.CarrProdExtraction;

public class CarrProdExtractionExcelProcessor implements ItemProcessor<CarrProdExtractionDTO, CarrProdExtraction> {

	@Override
	public CarrProdExtraction process(CarrProdExtractionDTO item) throws Exception {

		return new CarrProdExtraction(StringUtils.isNotBlank(item.getAnnee())?item.getAnnee():null,
				StringUtils.isNotBlank(item.getCodeEtablissement())?item.getCodeEtablissement():null,
				StringUtils.isNotBlank(item.getIdLigneExcel())?item.getIdLigneExcel():null,
				StringUtils.isNotBlank(item.getQuantiteRestanteAccessible())?item.getQuantiteRestanteAccessible():null,
				StringUtils.isNotBlank(item.getCommentairesQuantiteRestanteAccessible())?item.getCommentairesQuantiteRestanteAccessible():null,
				StringUtils.isNotBlank(item.getQuantiteAnnuelleSteriles())?item.getQuantiteAnnuelleSteriles():null,
				StringUtils.isNotBlank(item.getCommentairesQuantiteAnnuelleSteriles())?item.getCommentairesQuantiteAnnuelleSteriles():null, 
				StringUtils.isNotBlank(item.getSubstancesARecycler())?item.getSubstancesARecycler():null, 
				StringUtils.isNotBlank(item.getFamilleUsageDebouches())?item.getFamilleUsageDebouches():null,
				StringUtils.isNotBlank(item.getPrecisionFamilleUsage())?item.getPrecisionFamilleUsage():null,
				StringUtils.isNotBlank(item.getSousFamilleUsageDebouches())?item.getSousFamilleUsageDebouches():null, 
				StringUtils.isNotBlank(item.getPrecisionSousFamilleUsage())?item.getPrecisionSousFamilleUsage():null, 
				StringUtils.isNotBlank(item.getSousFamilleUsageDebouches2())?item.getSousFamilleUsageDebouches2():null, 
				StringUtils.isNotBlank(item.getPrecisionSousFamilleUsage2())?item.getPrecisionSousFamilleUsage2():null,
				StringUtils.isNotBlank(item.getQuantiteAnnuelle())?item.getQuantiteAnnuelle():null,
				StringUtils.isNotBlank(item.getCommentairesQuantiteAnnuelle())?item.getCommentairesQuantiteAnnuelle():null, 
				StringUtils.isNotBlank(item.getTotalSubstance())?item.getTotalSubstance():null, 
				StringUtils.isNotBlank(item.getTotalDontSteriles())?item.getTotalDontSteriles():null,
				StringUtils.isNotBlank(item.getCommentairesAlerte())?item.getCommentairesAlerte():null);
	}

}
