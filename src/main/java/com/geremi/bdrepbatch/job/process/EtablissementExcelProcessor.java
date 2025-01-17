package com.geremi.bdrepbatch.job.process;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.EtablissementDTO;
import com.geremi.bdrepbatch.job.model.Etablissement;

public class EtablissementExcelProcessor implements ItemProcessor<EtablissementDTO, Etablissement> {

	private static final Logger log = LoggerFactory.getLogger(EtablissementExcelProcessor.class);

	@Override
	public Etablissement process(EtablissementDTO dto) throws Exception {

		

		// Map attributes to EtablissementExcel object
		return new Etablissement(StringUtils.isNotBlank(dto.getIdLigneExcel())?dto.getIdLigneExcel():null,
				StringUtils.isNotBlank(dto.getAnnee())?dto.getAnnee():null,
				StringUtils.isNotBlank(dto.getCodeEtablissement())?dto.getCodeEtablissement():null,
				StringUtils.isNotBlank(dto.getNomEtablissement())?dto.getNomEtablissement():null,
				StringUtils.isNotBlank(dto.getAdresseSite())?dto.getAdresseSite():null,
				StringUtils.isNotBlank(dto.getCodePostal())?dto.getCodePostal():null,
				StringUtils.isNotBlank(dto.getCommune())?dto.getCommune():null,
				StringUtils.isNotBlank(dto.getNumeroSiret())?dto.getNumeroSiret():null,
				StringUtils.isNotBlank(dto.getCodeAPE())?dto.getCodeAPE():null,
				StringUtils.isNotBlank(dto.getActivitePrincipale())?dto.getActivitePrincipale():null,
				StringUtils.isNotBlank(dto.getCoordonneesGeographiques())?dto.getCoordonneesGeographiques():null,
				StringUtils.isNotBlank(dto.getAbscisseLongitudeX())?dto.getAbscisseLongitudeX():null,
				StringUtils.isNotBlank(dto.getOrdonneeLatitudeY())?dto.getOrdonneeLatitudeY():null,
				StringUtils.isNotBlank(dto.getVolumeProduction())?dto.getVolumeProduction():null,
				StringUtils.isNotBlank(dto.getUnite())?dto.getUnite():null,
				StringUtils.isNotBlank(dto.getMatiereProduite())?dto.getMatiereProduite():null,
				StringUtils.isNotBlank(dto.getNbHeuresExploitation())?dto.getNbHeuresExploitation():null,
				StringUtils.isNotBlank(dto.getNbEmployes())?dto.getNbEmployes():null,
				StringUtils.isNotBlank(dto.getSiteInternet())?dto.getSiteInternet():null,
				StringUtils.isNotBlank(dto.getInformationsComplementaires())?dto.getInformationsComplementaires():null,
				StringUtils.isNotBlank(dto.getCommentairesSection())?dto.getCommentairesSection():null);

	}

}
