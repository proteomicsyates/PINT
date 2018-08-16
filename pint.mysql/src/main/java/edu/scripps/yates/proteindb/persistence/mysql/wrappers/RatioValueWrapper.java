package edu.scripps.yates.proteindb.persistence.mysql.wrappers;

import edu.scripps.yates.proteindb.persistence.mysql.CombinationType;
import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;

public class RatioValueWrapper {
	private final Integer id;
	private final ConfidenceScoreType confidenceScoreType;
	private final int proteinPeptideOrPSMID;
	private final CombinationType combinationType;
	private final double value;
	private final Double confidenceScoreValue;
	private final String confidenceScoreName;
	private final int ratioDescriptorID;

	public RatioValueWrapper(Integer id, ConfidenceScoreType confidenceScoreType, int proteinID,
			CombinationType combinationType, double value, Double confidenceScoreValue, String confidenceScoreName,
			int ratioDescriptorID) {
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

	public ConfidenceScoreType getConfidenceScoreType() {
		return confidenceScoreType;
	}

	public int getProteinPeptideOrPSMID() {
		return proteinPeptideOrPSMID;
	}

	public CombinationType getCombinationType() {
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
