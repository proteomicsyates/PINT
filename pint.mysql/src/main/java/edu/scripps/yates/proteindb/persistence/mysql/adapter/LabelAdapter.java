package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.Label;
import gnu.trove.map.hash.TIntObjectHashMap;

public class LabelAdapter implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.Label>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2244272853353899357L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Label label;
	private final static TIntObjectHashMap<Label> map = new TIntObjectHashMap<Label>();

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
