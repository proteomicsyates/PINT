package edu.scripps.yates.proteindb.persistence.mysql.impl;

import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PTMSiteImpl implements PTMSite {
	protected final static TIntObjectHashMap<PTMSite> ptmSiteMap = new TIntObjectHashMap<PTMSite>();

	private final PtmSite hibPtmSite;

	public PTMSiteImpl(PtmSite hibPtmSite) {
		this.hibPtmSite = hibPtmSite;
		ptmSiteMap.put(hibPtmSite.getId(), this);
	}

	@Override
	public String getAA() {
		return hibPtmSite.getAa();
	}

	@Override
	public int getPosition() {
		return hibPtmSite.getPosition();
	}

	@Override
	public Score getScore() {
		if (hibPtmSite.getConfidenceScoreType() != null && hibPtmSite.getConfidenceScoreValue() != null) {
			return new ScoreImpl(hibPtmSite.getConfidenceScoreName(), hibPtmSite.getConfidenceScoreValue(),
					hibPtmSite.getConfidenceScoreType());
		}
		return null;
	}
}
