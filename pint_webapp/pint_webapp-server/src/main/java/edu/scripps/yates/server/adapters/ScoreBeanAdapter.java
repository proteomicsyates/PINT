package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ScoreBean;

public class ScoreBeanAdapter implements Adapter<ScoreBean> {
	private final String scoreValue;
	private final String scoreName;
	private final String scoreType;

	public ScoreBeanAdapter(String scoreValue, String scoreName, String scoreType) {
		this.scoreName = scoreName;
		this.scoreType = scoreType;
		this.scoreValue = scoreValue;
	}

	@Override
	public ScoreBean adapt() {
		final ScoreBean ret = new ScoreBean();
		if (scoreType != null) {
//			ret.setDescription(scoreType.getDescription());
			ret.setScoreType(scoreType);
		}
		ret.setScoreName(scoreName);
		if (scoreValue != null)
			ret.setValue(scoreValue.toString());
		return ret;
	}

}
