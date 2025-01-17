package com.geremi.bdrepbatch.job.utils;

import java.util.HashMap;
import java.util.Map;

public final class RefUtils {
	
private  RefUtils() {
	// private constructor hiding the default public
}	


private static Map<String, String> refAnomalies = new HashMap<>();



public static Map<String, String> getRefAnomalies() {
	return refAnomalies;
}

public static void setRefAnomalies(Map<String, String> refAnomalies) {
	RefUtils.refAnomalies = refAnomalies;
}





}