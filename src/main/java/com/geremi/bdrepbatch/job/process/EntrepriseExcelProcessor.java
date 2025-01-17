package com.geremi.bdrepbatch.job.process;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.EntrepriseDTO;
import com.geremi.bdrepbatch.job.model.Entreprise;

public class EntrepriseExcelProcessor implements ItemProcessor<EntrepriseDTO, Entreprise> {

	private static final Logger log = LoggerFactory.getLogger(EntrepriseExcelProcessor.class);

	@Override
	public Entreprise process(EntrepriseDTO fields) throws Exception {

		log.debug("Processing fields: {}", (Object) fields);
		// Map fields to EntrepriseExcel object
		Entreprise entrepriseExcel = new Entreprise();

		// Set attributes with setters
		if (fields.getAnnee().length() > 0 && StringUtils.isNotBlank(fields.getAnnee())) {
			String anneeStr = StringUtils.substringBefore(fields.getAnnee(), ".");
			
			entrepriseExcel.setAnnee(anneeStr);
		}

		if (StringUtils.isNotBlank(fields.getCodeEtablissement())) {
			String codeEtablissementStr = StringUtils.substringBefore(fields.getCodeEtablissement(), ".");
			
			entrepriseExcel.setCodeEtablissement(codeEtablissementStr);
		}

		if (StringUtils.isNotBlank(fields.getRaisonSociale())) {
			entrepriseExcel.setRaisonSociale(fields.getRaisonSociale());
		}

		if (StringUtils.isNotBlank(fields.getSocieteMere())) {
			entrepriseExcel.setSocieteMere(fields.getSocieteMere());
		}

		if (StringUtils.isNotBlank(fields.getFormeJuridique())) {
			entrepriseExcel.setFormeJuridique(fields.getFormeJuridique());
		}

		if (StringUtils.isNotBlank(fields.getNumeroSiren())) {
			if (StringUtils.isNumeric(fields.getNumeroSiren())) {
				
				entrepriseExcel.setNumeroSiren(fields.getNumeroSiren());
			} else {

			}
		}

		if (StringUtils.isNotBlank(fields.getAdresse())) {
			entrepriseExcel.setAdresse(fields.getAdresse());
		}

		if (StringUtils.isNotBlank(fields.getCommune())) {
			entrepriseExcel.setCommune(fields.getCommune());
		}

		if (StringUtils.isNotBlank(fields.getPays())) {
			entrepriseExcel.setPays(fields.getPays());
		}

		if (StringUtils.isNotBlank(fields.getCommentaires())) {
			entrepriseExcel.setCommentairesSection(fields.getCommentaires());
		}

		log.debug("Processed EntrepriseExcel: {}", entrepriseExcel);

		return entrepriseExcel;

	}

}