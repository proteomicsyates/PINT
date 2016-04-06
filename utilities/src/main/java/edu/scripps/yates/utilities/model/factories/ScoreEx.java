package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class ScoreEx implements Score, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4774612233108800677L;
	private final String value;
	private final String scoreType;
	private final String description;
	private final String scoreName;

	public ScoreEx(String value, String scoreName, String scoreType,
			String description) {
		this.value = value;
		this.scoreType = scoreType;
		this.description = description;
		this.scoreName = scoreName;

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
		return description;
	}

	@Override
	public String getScoreType() {
		return scoreType;
	}

}
