package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.utilities.model.factories.LabelEx;
import edu.scripps.yates.utilities.proteomicsmodel.Label;

public class LabelAdapter implements edu.scripps.yates.utilities.pattern.Adapter<Label> {
	private static final Map<String, Label> map = new HashMap<String, Label>();
	private final LabelType labelCfg;

	public LabelAdapter(LabelType labelCfg) {
		this.labelCfg = labelCfg;
	}

	@Override
	public Label adapt() {
		if (map.containsKey(labelCfg.getId())) {
			return map.get(labelCfg.getId());
		}
		LabelEx ret = new LabelEx(labelCfg.getId());
		ret.setMassDiff(labelCfg.getMassDiff());
		map.put(labelCfg.getId(), ret);
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
