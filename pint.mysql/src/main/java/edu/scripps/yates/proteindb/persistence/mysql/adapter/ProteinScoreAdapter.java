package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class ProteinScoreAdapter implements Adapter<ProteinScore>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 954101426967213722L;
	private final Score score;
	private final Protein protein;

	public ProteinScoreAdapter(Score score, Protein protein) {
		this.score = score;
		this.protein = protein;
	}

	@Override
	public ProteinScore adapt() {

		ProteinScore ret = new ProteinScore(new ConfidenceScoreTypeAdapter(score).adapt(), protein,
				score.getScoreName(), Double.valueOf(score.getValue()));
		return ret;
	}

}
