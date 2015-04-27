package br.ufrn.reposytoryMining.metrics;

public class Diference {

	private double oldValue;
	private double currentValue;
	private double diference;

	public Diference(double oldValue, double currentValue) {
		super();
		this.oldValue = oldValue;
		this.currentValue = currentValue;

		this.diference = this.currentValue - this.oldValue;
	}

	public Diference(int oldValue, int currentValue) {
		this.oldValue = oldValue;
		this.currentValue = currentValue;

		this.diference = this.currentValue - this.oldValue;
	}

	public Diference() {

	}

	public double getOldValue() {
		return oldValue;
	}

	public void setOldValue(double oldValue) {
		this.oldValue = oldValue;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public double getDiference() {
		return diference;
	}

	public void setDiference(double diference) {
		this.diference = diference;
	}

}
