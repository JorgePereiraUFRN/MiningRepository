package br.ufrn.reposytoryMining.metrics.model;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.egit.github.core.RepositoryCommit;
import org.kohsuke.github.GHCommit;

public class Commit {
	
	private RepositoryCommit commit;
	private Commit previus;
	private Map<Metric, Double> metrics = new HashMap<Metric, Double>();
	private String pathSourceCode;
	
	
	
	public String getPathSourceCode() {
		return pathSourceCode;
	}
	public void setPathSourceCode(String pathSourceCode) {
		this.pathSourceCode = pathSourceCode;
	}
	public Map<Metric, Double> getMetrics() {
		return metrics;
	}
	public void setMetrics(Map<Metric, Double> metrics) {
		this.metrics = metrics;
	}
	public RepositoryCommit getCommit() {
		return commit;
	}
	public void setCommit(RepositoryCommit commit) {
		this.commit = commit;
	}
	public Commit getPrevius() {
		return previus;
	}
	public void setPrevius(Commit previus) {
		this.previus = previus;
	}
	
	
	

}
