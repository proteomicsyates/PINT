package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Label;

public class LabelAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.Label>,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2244272853353899357L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Label label;
	private final static Map<Integer, Label> map = new HashMap<Integer, Label>();

	public LabelAdapter(edu.scripps.yates.utilities.proteomicsmodel.Label label) {
		this.label = label;
	}

	@Override
	public Label adapt() {
		if (map.containsKey(label.hashCode())) {
			return map.get(label.hashCode());
		}
		Label ret = new Label(label.getName());
		map.put(label.hashCode(), ret);

		ret.setMassDiff(label.getMassDiff());
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
