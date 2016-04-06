package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.Set;

public interface HasScores {
	public Set<Score> getScores();

	public void addScore(Score score);

}
