package br.ufrn.reposytoryMining.metrics.model;

public class ClassMetrics {

	private String classe;

	private Integer CA;
	private Integer NOC;
	private Integer LCOM;
	private Integer WMC;
	private Integer NPM;
	private Integer DIT;
	private Integer RFC;
	private Integer CBO;

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public Integer getCA() {
		return CA;
	}

	public void setCA(Integer cA) {
		CA = cA;
	}

	public Integer getNOC() {
		return NOC;
	}

	public void setNOC(Integer nOC) {
		NOC = nOC;
	}

	public Integer getLCOM() {
		return LCOM;
	}

	public void setLCOM(Integer lCOM) {
		LCOM = lCOM;
	}

	public Integer getWMC() {
		return WMC;
	}

	public void setWMC(Integer wMC) {
		WMC = wMC;
	}

	public Integer getNPM() {
		return NPM;
	}

	public void setNPM(Integer nPM) {
		NPM = nPM;
	}

	public Integer getDIT() {
		return DIT;
	}

	public void setDIT(Integer dIT) {
		DIT = dIT;
	}

	public Integer getRFC() {
		return RFC;
	}

	public void setRFC(Integer rFC) {
		RFC = rFC;
	}

	public Integer getCBO() {
		return CBO;
	}

	public void setCBO(Integer cBO) {
		CBO = cBO;
	}

	public String toString() {
		return "class: " + classe + " CA: " + CA + " NOC: " + NOC + " CBO: "
				+ CBO + " DIT: " + DIT + " LCOM: " + LCOM + " NPM: " + NPM
				+ " RFC: " + RFC + " WCM: " + WMC;

	}

}
