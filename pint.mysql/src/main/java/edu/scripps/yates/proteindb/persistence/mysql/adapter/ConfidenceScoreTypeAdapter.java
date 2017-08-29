package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ConfidenceScoreTypeAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6579820518253735434L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Score score;
	private final static TIntObjectHashMap<ConfidenceScoreType> map = new TIntObjectHashMap<ConfidenceScoreType>();

	public ConfidenceScoreTypeAdapter(edu.scripps.yates.utilities.proteomicsmodel.Score score) {
		this.score = score;
	}

	@Override
	public ConfidenceScoreType adapt() {
		ConfidenceScoreType ret = new ConfidenceScoreType(score.getScoreType());
		if (map.containsKey(score.hashCode()))
			return map.get(score.hashCode());
		map.put(score.hashCode(), ret);
		ret.setDescription(score.getScoreDescription());

		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
