package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.PTMSiteBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PTMSiteBeanAdapter implements Adapter<PTMSiteBean> {
	private final PtmSite ptmSite;
	private final static TIntObjectHashMap<PTMSiteBean> map = new TIntObjectHashMap<PTMSiteBean>();

	public PTMSiteBeanAdapter(PtmSite ptmSite) {
		this.ptmSite = ptmSite;
	}

	@Override
	public PTMSiteBean adapt() {
		if (map.containsKey(ptmSite.getId()))
			return map.get(ptmSite.getId());
		PTMSiteBean ret = new PTMSiteBean();
		map.put(ptmSite.getId(), ret);
		ret.setAa(ptmSite.getAa());
		ret.setPosition(ptmSite.getPosition());
		if (ptmSite.getConfidenceScoreValue() != null)
			ret.setScore(new ScoreBeanAdapter(ptmSite.getConfidenceScoreValue(), ptmSite.getConfidenceScoreName(),
					ptmSite.getConfidenceScoreType()).adapt());
		return ret;
	}

}
