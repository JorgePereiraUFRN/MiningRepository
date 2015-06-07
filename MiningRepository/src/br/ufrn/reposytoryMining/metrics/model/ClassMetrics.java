package br.ufrn.reposytoryMining.metrics.model;

public class ClassMetrics {

	private String classe;

	private Double CA;
	private Double NOC;
	private Double LCOM;
	private Double WMC;
	private Double NPM;
	private Double DIT;
	private Double RFC;
	private Double CBO;

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public Double getCA() {
		return CA;
	}

	public void setCA(Double cA) {
		CA = cA;
	}

	public Double getNOC() {
		return NOC;
	}

	public void setNOC(Double nOC) {
		NOC = nOC;
	}

	public Double getLCOM() {
		return LCOM;
	}

	public void setLCOM(Double lCOM) {
		LCOM = lCOM;
	}

	public Double getWMC() {
		return WMC;
	}

	public void setWMC(Double wMC) {
		WMC = wMC;
	}

	public Double getNPM() {
		return NPM;
	}

	public void setNPM(Double nPM) {
		NPM = nPM;
	}

	public Double getDIT() {
		return DIT;
	}

	public void setDIT(Double dIT) {
		DIT = dIT;
	}

	public Double getRFC() {
		return RFC;
	}

	public void setRFC(Double rFC) {
		RFC = rFC;
	}

	public Double getCBO() {
		return CBO;
	}

	public void setCBO(Double cBO) {
		CBO = cBO;
	}

	public String toString() {
		return "class: " + classe + " CA: " + CA + " NOC: " + NOC + " CBO: "
				+ CBO + " DIT: " + DIT + " LCOM: " + LCOM + " NPM: " + NPM
				+ " RFC: " + RFC + " WCM: " + WMC;

	}

}
