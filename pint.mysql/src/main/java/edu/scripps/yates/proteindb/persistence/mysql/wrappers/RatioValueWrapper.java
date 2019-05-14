package edu.scripps.yates.proteindb.persistence.mysql.wrappers;

public class RatioValueWrapper {
	private final Integer id;
	private final String confidenceScoreType;
	private final int proteinPeptideOrPSMID;
	private final String combinationType;
	private final double value;
	private final Double confidenceScoreValue;
	private final String confidenceScoreName;
	private final int ratioDescriptorID;

	public RatioValueWrapper(Integer id, String confidenceScoreType, int proteinID, String combinationType,
			double value, Double confidenceScoreValue, String confidenceScoreName, int ratioDescriptorID) {
		super();
		this.id = id;
		this.confidenceScoreType = confidenceScoreType;
		proteinPeptideOrPSMID = proteinID;
		this.combinationType = combinationType;
		this.value = value;
		this.confidenceScoreValue = confidenceScoreValue;
		this.confidenceScoreName = confidenceScoreName;
		this.ratioDescriptorID = ratioDescriptorID;
	}

	public Integer getId() {
		return id;
	}

	public String getConfidenceScoreType() {
		return confidenceScoreType;
	}

	public int getProteinPeptideOrPSMID() {
		return proteinPeptideOrPSMID;
	}

	public String getCombinationType() {
		return combinationType;
	}

	public double getValue() {
		return value;
	}

	public Double getConfidenceScoreValue() {
		return confidenceScoreValue;
	}

	public String getConfidenceScoreName() {
		return confidenceScoreName;
	}

	public int getRatioDescriptorID() {
		return ratioDescriptorID;
	}

}
