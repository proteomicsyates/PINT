package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

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

	public PTMSiteAdapter(PTMSite ptmSite, Ptm ptm) {
		this.ptm = ptm;
		this.ptmSite = ptmSite;
	}

	@Override
	public PtmSite adapt() {
		final PtmSite ret = new PtmSite(ptm, ptmSite.getAA(), ptmSite.getPosition());

		final Score score = ptmSite.getScore();
		if (score != null) {
			ret.setConfidenceScoreValue(score.getValue());
			ret.setConfidenceScoreType(score.getScoreType());
			ret.setConfidenceScoreName(score.getScoreName());
		}

		return ret;
	}

}
