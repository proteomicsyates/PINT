package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmScore;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PSMScoreAdapter implements Adapter<PsmScore>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 954101426967213722L;
	private final Score score;
	private final Psm psm;

	public PSMScoreAdapter(Score score, Psm hibPSM) {
		this.score = score;
		psm = hibPSM;
	}

	@Override
	public PsmScore adapt() {

		PsmScore ret = new PsmScore(new ConfidenceScoreTypeAdapter(score).adapt(), psm, score.getScoreName(),
				Double.valueOf(score.getValue()));
		return ret;
	}

}
