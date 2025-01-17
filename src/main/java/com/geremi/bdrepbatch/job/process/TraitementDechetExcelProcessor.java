package com.geremi.bdrepbatch.job.process;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.TraitementDechetDTO;
import com.geremi.bdrepbatch.job.model.TraitementDechet;

public class TraitementDechetExcelProcessor implements ItemProcessor<TraitementDechetDTO, TraitementDechet>{

	@Override
	public TraitementDechet process(TraitementDechetDTO item) throws Exception {
		return new TraitementDechet(
				StringUtils.isNotBlank(item.getAnnee())?item.getAnnee():null,
				StringUtils.isNotBlank(item.getCodeEtablissement())?item.getCodeEtablissement():null,
				StringUtils.isNotBlank(item.getIdLigneExcel())?item.getIdLigneExcel():null,
				StringUtils.isNotBlank(item.getCodeDechet())?item.getCodeDechet():null,
				StringUtils.isNotBlank(item.getLibelleDechet())?item.getLibelleDechet():null,
				StringUtils.isNotBlank(item.getDangereux())?item.getDangereux():null,
				StringUtils.isNotBlank(item.getStatutSortieDechet())?item.getStatutSortieDechet():null,
				StringUtils.isNotBlank(item.getDepartementOrigine())?item.getDepartementOrigine():null,
				StringUtils.isNotBlank(item.getPaysOrigine())?item.getPaysOrigine():null,
				StringUtils.isNotBlank(item.getQuantiteAdmiseTPA())?item.getQuantiteAdmiseTPA():null,
				StringUtils.isNotBlank(item.getQuantiteTraiteeTPA())?item.getQuantiteTraiteeTPA():null,
				StringUtils.isNotBlank(item.getCodeOpeElimOuValo())?item.getCodeOpeElimOuValo():null,
				StringUtils.isNotBlank(item.getLibelleOpeElimOuValo())?item.getLibelleOpeElimOuValo():null,
				StringUtils.isNotBlank(item.getNumeroNotification())?item.getNumeroNotification():null);

	}

	

}
