package br.ufrn.reposytoryMining.metrics.util;

import java.util.HashMap;
import java.util.Map;

import br.ufrn.reposytoryMining.metrics.model.ClassMetrics;
import br.ufrn.reposytoryMining.metrics.model.Metric;

public class Calc {

	public static Map<Metric, Double> getAverageClassMetrics(
			Map<String, ClassMetrics> metrics) {

		Map<Metric, Double> average = new HashMap<Metric, Double>();

		double CA = 0, NOC = 0, LCOM = 0, WMC = 0, NPM = 0, DIT = 0, RFC = 0, CBO = 0;
		
		if (metrics != null) {

			for (ClassMetrics m : metrics.values()) {

				if (m.getCA() != null)
					CA += m.getCA();
				if (m.getCBO() != null)
					CBO += m.getCBO();
				if (m.getDIT() != null)
					DIT += m.getDIT();
				if (m.getLCOM() != null)
					LCOM += m.getLCOM();
				if (m.getNOC() != null)
					NOC += m.getNOC();
				if (m.getNPM() != null)
					NPM += m.getNPM();
				if (m.getRFC() != null)
					RFC += m.getRFC();
				if (m.getWMC() != null)
					WMC += m.getWMC();

			}
			
			
			int cont = metrics.size();
			
			if(cont > 0){
				
				CA = CA / cont;
				NOC = NOC / cont;
				LCOM = LCOM / cont;
				WMC = WMC / cont;
				NPM = NPM / cont;
				DIT = DIT / cont;
				RFC = RFC / cont;
				CBO = CBO / cont;
			}
			
			average.put(Metric.CA, CA);
			average.put(Metric.CBO, CBO);
			average.put(Metric.DIT, DIT);
			average.put(Metric.LCOM, LCOM);
			average.put(Metric.NOC, NOC);
			average.put(Metric.NPM, NPM);
			average.put(Metric.RFC, RFC);
			average.put(Metric.WMC, WMC);
			
		}

		return average;

	}

}
