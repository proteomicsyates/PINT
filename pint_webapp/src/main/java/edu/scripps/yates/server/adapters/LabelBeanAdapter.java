package edu.scripps.yates.server.adapters;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.LabelBean;

public class LabelBeanAdapter implements Adapter<LabelBean> {
	private final edu.scripps.yates.proteindb.persistence.mysql.Label label;
	private final static Map<Integer, LabelBean> map = new HashMap<Integer, LabelBean>();

	public LabelBeanAdapter(
			edu.scripps.yates.proteindb.persistence.mysql.Label label) {
		this.label = label;
	}

	@Override
	public LabelBean adapt() {
		if (map.containsKey(label.getId()))
			return map.get(label.getId());
		LabelBean ret = new LabelBean();
		map.put(label.getId(), ret);
		ret.setMassDiff(label.getMassDiff());
		ret.setName(label.getName());
		return ret;
	}

}
