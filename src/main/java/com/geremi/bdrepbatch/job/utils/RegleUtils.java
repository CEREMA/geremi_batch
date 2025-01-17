package com.geremi.bdrepbatch.job.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.geremi.bdrepbatch.job.dto.AnomalieDTO;
import com.geremi.bdrepbatch.rules.RegleVerifLongueurInput;

public final class RegleUtils {
	
	//TODO : trouver comment faire fonctionner pour que cette méthode ne soit pas répétée
	
	/*@Autowired
	private static RegleVerifLongueurInput verifLongueurInput;

	public static Optional<AnomalieDTO> verifLongueurChamp(String valeur, String longueurLimite, String nomTable, String idLigneExcel){
		Map<String,String> params = new HashMap<>();
		params.put("valeur", valeur);
		params.put("longueurLimite", longueurLimite);
		return verifLongueurInput.executerRegle(params, nomTable, "type_carriere", idLigneExcel, null);
	}*/
}
