package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelSetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;

public class LabelSetTypeAdapter implements Adapter<LabelSetType> {
	private final LabelSetTypeBean labelSet;

	public LabelSetTypeAdapter(LabelSetTypeBean labelSet) {
		this.labelSet = labelSet;
	}

	@Override
	public LabelSetType adapt() {
		LabelSetType ret = new LabelSetType();
		if (labelSet.getLabel() != null) {
			for (LabelTypeBean labelTypeBean : labelSet.getLabel()) {
				ret.getLabel().add(new LabelTypeAdapter(labelTypeBean).adapt());
			}
		}
		return ret;
	}

}
