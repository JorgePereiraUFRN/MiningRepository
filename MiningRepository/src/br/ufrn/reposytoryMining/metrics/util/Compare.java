package br.ufrn.reposytoryMining.metrics.util;


import java.util.Map;
import java.util.Set;

import br.ufrn.reposytoryMining.metrics.model.ClassMetrics;
import br.ufrn.reposytoryMining.metrics.model.DifferenceMetrics;


public class Compare {
	
	


	public DifferenceMetrics calcDiferenceClassMetrics( Map<String, ClassMetrics> oldMetrics, Map<String, ClassMetrics> currentMetrics) {
		
		DifferenceMetrics difference = new DifferenceMetrics();
		
		Set<String> classes = oldMetrics.keySet();
		
		
		return difference;
	
	}

	

}
