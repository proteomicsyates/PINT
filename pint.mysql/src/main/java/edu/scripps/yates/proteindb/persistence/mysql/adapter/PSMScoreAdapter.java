package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
	private final static Map<Integer, PsmScore> map = new HashMap<Integer, PsmScore>();

	public PSMScoreAdapter(Score score, Psm hibPSM) {
		this.score = score;
		psm = hibPSM;
	}

	@Override
	public PsmScore adapt() {
		if (map.containsKey(score.hashCode()))
			return map.get(score.hashCode());

		PsmScore ret = new PsmScore(
				new ConfidenceScoreTypeAdapter(score).adapt(), psm,
				score.getScoreName(), Double.valueOf(score.getValue()));
		map.put(score.hashCode(), ret);
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
