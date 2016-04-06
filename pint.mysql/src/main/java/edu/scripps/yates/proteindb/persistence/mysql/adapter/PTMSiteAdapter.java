package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PTMSiteAdapter implements Adapter<PtmSite>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5318332398202528901L;
	private final PTMSite ptmSite;
	private final Ptm ptm;
	private final static Map<Integer, PtmSite> map = new HashMap<Integer, PtmSite>();

	public PTMSiteAdapter(PTMSite ptmSite, Ptm ptm) {
		this.ptm = ptm;
		this.ptmSite = ptmSite;
	}

	@Override
	public PtmSite adapt() {
		PtmSite ret = new PtmSite(ptm, ptmSite.getAA(), ptmSite.getPosition());
		if (map.containsKey(ptmSite.hashCode()))
			return map.get(ptmSite.hashCode());
		map.put(ptmSite.hashCode(), ret);
		final Score score = ptmSite.getScore();
		if (score != null) {
			ret.setConfidenceScoreValue(score.getValue());
			ret.setConfidenceScoreType(new ConfidenceScoreTypeAdapter(score)
					.adapt());
			ret.setConfidenceScoreName(score.getScoreName());
		}

		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
