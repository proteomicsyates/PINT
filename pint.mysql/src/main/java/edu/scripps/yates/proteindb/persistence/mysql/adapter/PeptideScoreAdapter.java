package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
	private final static Map<Integer, PeptideScore> map = new HashMap<Integer, PeptideScore>();

	public PeptideScoreAdapter(Score score, Peptide hibPeptide) {
		this.score = score;
		peptide = hibPeptide;
	}

	@Override
	public PeptideScore adapt() {
		if (map.containsKey(score.hashCode()))
			return map.get(score.hashCode());

		PeptideScore ret = new PeptideScore(new ConfidenceScoreTypeAdapter(
				score).adapt(), peptide, score.getScoreName(),
				Double.valueOf(score.getValue()));
		map.put(score.hashCode(), ret);
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
