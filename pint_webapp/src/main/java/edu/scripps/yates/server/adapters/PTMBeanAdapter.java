package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.PTMBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PTMBeanAdapter implements Adapter<PTMBean> {
	private final Ptm ptm;
	private final static TIntObjectHashMap<PTMBean> map = new TIntObjectHashMap<PTMBean>();

	public PTMBeanAdapter(Ptm ptm) {
		this.ptm = ptm;
	}

	@Override
	public PTMBean adapt() {
		if (map.containsKey(ptm.getId())) {
			map.get(ptm.getId());
		}
		PTMBean ret = new PTMBean();
		map.put(ptm.getId(), ret);
		ret.setCvId(ptm.getCvId());
		ret.setMassShift(ptm.getMassShift());
		ret.setName(ptm.getName());
		if (ptm.getPtmSites() != null) {
			for (Object obj : ptm.getPtmSites()) {
				PtmSite ptmSite = (PtmSite) obj;
				ret.addPtmSite(new PTMSiteBeanAdapter(ptmSite).adapt());
			}
		}

		return ret;
	}

}
