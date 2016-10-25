package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideScore;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PeptideScoreAdapter implements Adapter<PeptideScore>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 954101426967213722L;
	private final Score score;
	private final Peptide peptide;

	public PeptideScoreAdapter(Score score, Peptide hibPeptide) {
		this.score = score;
		peptide = hibPeptide;
	}

	@Override
	public PeptideScore adapt() {

		PeptideScore ret = new PeptideScore(new ConfidenceScoreTypeAdapter(score).adapt(), peptide,
				score.getScoreName(), Double.valueOf(score.getValue()));
		return ret;
	}

}
