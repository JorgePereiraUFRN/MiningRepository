package br.ufrn.reposytoryMining.metrics;

import java.util.HashMap;
import java.util.Map;

import br.ufrn.reposytoryMining.metrics.model.ClassMetrics;
import br.ufrn.reposytoryMining.metrics.model.Metric;

public class MetricContribuition {

	private double ideal_CA = 7, ideal_NOC = 17, ideal_LCOM = 2,
			ideal_WMC = 14, ideal_NPM = 0, ideal_DIT = 2, ideal_RFC = 27,
			ideal_CBO = 2;

	// retorna um valor que indica a porcentagem de contribuição
	// para melhoria de uma metrica
	private double calcContribuition(Double currentValue, Double oldValue,
			double ideal_value) {

		double contribuition = 0;

		
		if (currentValue != null && oldValue != null) {
			double oldDiference = Math.abs(ideal_value - oldValue);
			double currentDiference = Math.abs(ideal_value - currentValue);
			
			//System.out.println("old: "+oldDiference + " current: "+currentDiference+
				//	" \n diference: "+ (oldDiference - currentDiference ));

			if (oldDiference - currentDiference != 0.0) {
				contribuition = (oldDiference - currentDiference)
						/ oldDiference;
			}
		}

		return contribuition;
	}

	// retorno o quanto melhoraram ou prioram cada uma das metricas de uma
	// classe
	public Map<Metric, Double> calcMetricContibuition(
			ClassMetrics CurrentMetrics, ClassMetrics oldMetrics) {

		Map<Metric, Double> metricsContribuition = new HashMap<Metric, Double>();

		if (CurrentMetrics != null && oldMetrics != null) {

			metricsContribuition.put(
					Metric.CA,
					calcContribuition(CurrentMetrics.getCA(),
							oldMetrics.getCA(), ideal_CA));
			metricsContribuition.put(
					Metric.CBO,
					calcContribuition(CurrentMetrics.getCBO(),
							oldMetrics.getCBO(), ideal_CBO));
			metricsContribuition.put(
					Metric.DIT,
					calcContribuition(CurrentMetrics.getDIT(),
							oldMetrics.getDIT(), ideal_DIT));
			metricsContribuition.put(
					Metric.LCOM,
					calcContribuition(CurrentMetrics.getLCOM(),
							oldMetrics.getLCOM(), ideal_LCOM));
			metricsContribuition.put(
					Metric.NOC,
					calcContribuition(CurrentMetrics.getNOC(),
							oldMetrics.getNOC(), ideal_NOC));
			metricsContribuition.put(
					Metric.NPM,
					calcContribuition(CurrentMetrics.getNPM(),
							oldMetrics.getNPM(), ideal_NPM));
			metricsContribuition.put(
					Metric.RFC,
					calcContribuition(CurrentMetrics.getRFC(),
							oldMetrics.getRFC(), ideal_RFC));
			metricsContribuition.put(
					Metric.WMC,
					calcContribuition(CurrentMetrics.getWMC(),
							oldMetrics.getWMC(), ideal_WMC));

		} else {
			metricsContribuition.put(Metric.CA, 0.0);
			metricsContribuition.put(Metric.CBO, 0.0);
			metricsContribuition.put(Metric.DIT, 0.0);
			metricsContribuition.put(Metric.LCOM, 0.0);
			metricsContribuition.put(Metric.NOC, 0.0);
			metricsContribuition.put(Metric.NPM, 0.0);
			metricsContribuition.put(Metric.RFC, 0.0);
			metricsContribuition.put(Metric.WMC, 0.0);
		}

		return metricsContribuition;

	}

	public double calcMetricsContibuition(ClassMetrics currentMetrics,
			ClassMetrics oldMetrics) {

		double contribuition = 0;

		if (currentMetrics != null && oldMetrics != null) {

			Map<Metric, Double> metricsContribuition = calcMetricContibuition(
					currentMetrics, oldMetrics);

			for (double value : metricsContribuition.values()) {
				contribuition += value;
			}

		}

		return contribuition;
	}

}
