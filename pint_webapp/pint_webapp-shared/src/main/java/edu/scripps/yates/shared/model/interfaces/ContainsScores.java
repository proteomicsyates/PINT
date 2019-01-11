package edu.scripps.yates.shared.model.interfaces;

import java.util.Map;

import edu.scripps.yates.shared.model.ScoreBean;

public interface ContainsScores {

	public Map<String, ScoreBean> getScores();

	public void setScores(Map<String, ScoreBean> scores);

	public void addScore(ScoreBean score);

	public ScoreBean getScoreByName(String scoreName);
}
