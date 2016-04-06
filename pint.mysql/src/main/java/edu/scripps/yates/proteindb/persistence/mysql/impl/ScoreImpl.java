package edu.scripps.yates.proteindb.persistence.mysql.impl;

import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.PsmScore;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class ScoreImpl implements Score {
	private final String value;
	private final String scoreType;
	private final String scoreDescription;
	private final String scoreName;

	public ScoreImpl(PsmScore hibScore) {
		this(hibScore.getName(), String.valueOf(hibScore.getValue()), hibScore
				.getConfidenceScoreType());
	}

	public ScoreImpl(PeptideScore hibScore) {
		this(hibScore.getName(), String.valueOf(hibScore.getValue()), hibScore
				.getConfidenceScoreType());
	}

	public ScoreImpl(ProteinScore hibScore) {
		this(hibScore.getName(), String.valueOf(hibScore.getValue()), hibScore
				.getConfidenceScoreType());
	}

	public ScoreImpl(String customScoreName, String confidenceScoreValue,
			ConfidenceScoreType confidenceScoreType) {
		value = confidenceScoreValue;
		scoreType = confidenceScoreType.getName();
		scoreDescription = confidenceScoreType.getDescription();
		scoreName = customScoreName;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getScoreName() {
		return scoreName;
	}

	@Override
	public String getScoreDescription() {
		return scoreDescription;
	}

	@Override
	public String getScoreType() {
		return scoreType;
	}

}
