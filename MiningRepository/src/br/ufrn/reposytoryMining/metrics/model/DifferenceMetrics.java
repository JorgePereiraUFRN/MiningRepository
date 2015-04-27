package br.ufrn.reposytoryMining.metrics.model;

import java.util.HashMap;
import java.util.Map;

import br.ufrn.reposytoryMining.metrics.Diference;

public class DifferenceMetrics {

	private  Map<String, Map<Metric, Diference>> differenceMetrics = new HashMap<String, Map<Metric, Diference>>();

	
	public Map<String, Map<Metric, Diference>> getDifferenceMetrics() {
		return differenceMetrics;
	}

	public void setDifferenceMetrics(Map<String, Map<Metric, Diference>> differenceMetrics) {
		this.differenceMetrics = differenceMetrics;
	}
	
	

}
