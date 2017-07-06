package edu.scripps.yates.server.adapters;

import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Tissue;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.TissueBean;
import gnu.trove.map.hash.THashMap;

public class TissueBeanAdapter implements Adapter<TissueBean> {
	private final Tissue tissue;
	private final static Map<String, TissueBean> map = new THashMap<String, TissueBean>();

	public TissueBeanAdapter(Tissue tissue) {
		this.tissue = tissue;
	}

	@Override
	public TissueBean adapt() {
		if (map.containsKey(tissue.getTissueId()))
			map.get(tissue.getTissueId());
		TissueBean ret = new TissueBean();
		ret.setDescription(tissue.getName());
		ret.setTissueID(tissue.getTissueId());
		map.put(tissue.getTissueId(), ret);
		return ret;
	}

}
