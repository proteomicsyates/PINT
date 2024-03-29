package edu.scripps.yates.proteindb.persistence.mysql;

// Generated Feb 24, 2015 2:42:07 PM by Hibernate Tools 3.4.0.CR1

/**
 * PeptideAmount generated by hbm2java
 */
public class PeptideAmount implements java.io.Serializable {

	private Integer id;
	private Peptide peptide;
	private String amountType;
	private Condition condition;
	private String combinationType;
	private double value;

	public PeptideAmount() {
	}

	public PeptideAmount(Peptide peptide, String amountType, Condition condition, double value) {
		this.peptide = peptide;
		this.amountType = amountType;
		this.condition = condition;
		this.value = value;
	}

	public PeptideAmount(Peptide peptide, String amountType, Condition condition, String combinationType,
			double value) {
		this.peptide = peptide;
		this.amountType = amountType;
		this.condition = condition;
		this.combinationType = combinationType;
		this.value = value;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Peptide getPeptide() {
		return this.peptide;
	}

	public void setPeptide(Peptide peptide) {
		this.peptide = peptide;
	}

	public String getAmountType() {
		return this.amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public Condition getCondition() {
		return this.condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public String getCombinationType() {
		return this.combinationType;
	}

	public void setCombinationType(String combinationType) {
		this.combinationType = combinationType;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
