package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean;

public class LabelSetTypeBeanAdapter implements Adapter<LabelSetTypeBean> {
	private final LabelSetType labelSet;

	public LabelSetTypeBeanAdapter(LabelSetType labelSet) {
		this.labelSet = labelSet;
	}

	@Override
	public LabelSetTypeBean adapt() {
		LabelSetTypeBean ret = new LabelSetTypeBean();
		if (labelSet.getLabel() != null) {
			for (LabelType labelType : labelSet.getLabel()) {
				ret.getLabel().add(new LabelTypeBeanAdapter(labelType).adapt());
			}
		}
		return ret;
	}

}
