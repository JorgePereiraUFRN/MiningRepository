package br.ufrn.reposytoryMining.metrics.model;

public class PackageMetrics {
	
	private Double IPSC;
	private Double IIPUD;
	private Double IPCI;
	private Double IIPED;
	private Double PF;
	
	private String packageName;
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Double getIPSC() {
		return IPSC;
	}
	public void setIPSC(Double iPSC) {
		IPSC = iPSC;
	}
	public Double getIIPUD() {
		return IIPUD;
	}
	public void setIIPUD(Double iIPUD) {
		IIPUD = iIPUD;
	}
	public Double getIPCI() {
		return IPCI;
	}
	public void setIPCI(Double iPCI) {
		IPCI = iPCI;
	}
	public Double getIIPED() {
		return IIPED;
	}
	public void setIIPED(Double iIPED) {
		IIPED = iIPED;
	}
	public Double getPF() {
		return PF;
	}
	public void setPF(Double pF) {
		PF = pF;
	}
	

	public String toString(){
		return "package: "+packageName+" IIPED: "+IIPED+" IIPUD: "+IIPUD
				+" IIPCI: "+IPCI+" IPSC: "+IPSC+" PF: "+PF;
	}
}
