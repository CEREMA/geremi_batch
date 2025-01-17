package com.geremi.bdrepbatch.job.process;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.geremi.bdrepbatch.job.dto.EntrepriseDTO;
import com.geremi.bdrepbatch.job.model.Entreprise;

public class EntrepriseCsvProcessor implements ItemProcessor<EntrepriseDTO, Entreprise> {

	private static final Logger log = LoggerFactory.getLogger(EntrepriseCsvProcessor.class);

	@Override
	public Entreprise process(EntrepriseDTO fields) throws Exception {
		Entreprise entreprise = new Entreprise();

		entreprise.setAnnee(StringUtils.isNotBlank(fields.getAnnee()) ? fields.getAnnee(): null);
		entreprise.setCodeEtablissement(fields.getCodeEtablissement());
		entreprise.setRaisonSociale(fields.getRaisonSociale());
		entreprise.setSocieteMere(fields.getSocieteMere());
		entreprise.setFormeJuridique(fields.getFormeJuridique());
		entreprise.setNumeroSiren(fields.getNumeroSiren());
		entreprise.setAdresse(fields.getAdresse());
		entreprise.setCommune(fields.getCommune());
		entreprise.setPays(fields.getPays());
		entreprise.setCommentairesSection(fields.getCommentaires());

		log.debug("Processed EntrepriseExcel: {}", entreprise);

		return entreprise;
	}

}