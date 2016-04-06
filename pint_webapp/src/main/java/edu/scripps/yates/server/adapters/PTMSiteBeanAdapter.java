package edu.scripps.yates.server.adapters;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.PTMSiteBean;

public class PTMSiteBeanAdapter implements Adapter<PTMSiteBean> {
	private final PtmSite ptmSite;
	private final static Map<Integer, PTMSiteBean> map = new HashMap<Integer, PTMSiteBean>();

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
			ret.setScore(new ScoreBeanAdapter(
					ptmSite.getConfidenceScoreValue(), ptmSite
							.getConfidenceScoreName(), ptmSite
							.getConfidenceScoreType()).adapt());
		return ret;
	}

}
