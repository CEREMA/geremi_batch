
package com.geremi.bdrepbatch.job.process;

import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.DeclarationDTO;
import com.geremi.bdrepbatch.job.model.Declaration;

public class DeclarationExcelProcessor implements ItemProcessor<DeclarationDTO, Declaration>{

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
	
	@Override
	public Declaration process(DeclarationDTO item) throws Exception {
		
		return new Declaration (StringUtils.isNotBlank(item.getAnnee())?item.getAnnee():null, 
				StringUtils.isNotBlank(item.getCodeEtablissement())?item.getCodeEtablissement():null, 
				StringUtils.isNotBlank((item.getIdLigneExcel()))?item.getIdLigneExcel():null,
				StringUtils.isNotBlank(item.getCodeInsee())?item.getCodeInsee():null, 
				StringUtils.isNotBlank(item.getCommune())?item.getCommune():null,
				StringUtils.isNotBlank(item.getNomEtablissement())?item.getNomEtablissement():null, 
				StringUtils.isNotBlank(item.getServiceInspection())?item.getServiceInspection():null,
				StringUtils.isNotBlank(item.getRegion())?item.getRegion():null, 
				StringUtils.isNotBlank(item.getDepartement())?item.getDepartement():null,
				StringUtils.isNotBlank(item.getStatutDeclaration())?item.getStatutDeclaration():null, 
				StringUtils.isNotBlank(item.getStatutQuotaEmission())?item.getStatutQuotaEmission():null, 
				StringUtils.isNotBlank(item.getStatutQuotaNiveauxActivites())?item.getStatutQuotaNiveauxActivites():null,
				StringUtils.isNotBlank(item.getProgression())?item.getProgression():null, 
				StringUtils.isNotBlank(item.getDateDerniereActionDeclarant())?item.getDateDerniereActionDeclarant():null, 
				StringUtils.isNotBlank(item.getDateDerniereActionInspecteur())?item.getDateDerniereActionInspecteur():null,
				StringUtils.isNotBlank(item.getCarriere())?item.getCarriere():null, 
				StringUtils.isNotBlank(item.getQuotas())?item.getQuotas():null, 
				StringUtils.isNotBlank(item.getIsdi())?item.getIsdi():null, 
				StringUtils.isNotBlank(item.getIsdnd())?item.getIsdnd():null,
				StringUtils.isNotBlank(item.getDateInitDeclaration())?item.getDateInitDeclaration():null);
	}


}
