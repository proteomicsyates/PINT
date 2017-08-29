package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.PTMSiteBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PTMSiteBeanAdapter implements Adapter<PTMSiteBean> {
	private final PtmSite ptmSite;
	private final static ThreadLocal<TIntObjectHashMap<PTMSiteBean>> map = new ThreadLocal<TIntObjectHashMap<PTMSiteBean>>();

	public PTMSiteBeanAdapter(PtmSite ptmSite) {
		this.ptmSite = ptmSite;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<PTMSiteBean>());
		}
	}

	@Override
	public PTMSiteBean adapt() {
		if (map.get().containsKey(ptmSite.getId()))
			return map.get().get(ptmSite.getId());
		PTMSiteBean ret = new PTMSiteBean();
		map.get().put(ptmSite.getId(), ret);
		ret.setAa(ptmSite.getAa());
		ret.setPosition(ptmSite.getPosition());
		if (ptmSite.getConfidenceScoreValue() != null)
			ret.setScore(new ScoreBeanAdapter(ptmSite.getConfidenceScoreValue(), ptmSite.getConfidenceScoreName(),
					ptmSite.getConfidenceScoreType()).adapt());
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
