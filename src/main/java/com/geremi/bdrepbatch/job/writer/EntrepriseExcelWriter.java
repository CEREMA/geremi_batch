package com.geremi.bdrepbatch.job.writer;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import com.geremi.bdrepbatch.job.model.Entreprise;

public class EntrepriseExcelWriter extends FlatFileItemWriter<Entreprise> {

	public EntrepriseExcelWriter(String outputFilePath) throws Exception {

		setResource(new FileSystemResource(outputFilePath));

		BeanWrapperFieldExtractor<Entreprise> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] { "annee", "codeEtablissement", "raisonSociale", "societeMere",
				"formeJuridique", "numeroSiren", "adresse", "commune", "pays", "commentaires" });

		DelimitedLineAggregator<Entreprise> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(";");
		lineAggregator.setFieldExtractor(fieldExtractor);

		setLineAggregator(lineAggregator);
		setHeaderCallback(writer -> writer.write(
				"annee;codeEtablissement;raisonSociale;societeMere;formeJuridique;numeroSiren;adresse;commune;pays;commentaires"));

		afterPropertiesSet();
	}

}
