package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;

public class LabelTypeBeanAdapter implements Adapter<LabelTypeBean> {
	private final LabelType labelType;

	public LabelTypeBeanAdapter(LabelType labelType) {

		this.labelType = labelType;
	}

	@Override
	public LabelTypeBean adapt() {

		return adaptFromlabelTypeBean();

	}

	private LabelTypeBean adaptFromlabelTypeBean() {
		LabelTypeBean ret = new LabelTypeBean();

		ret.setId(labelType.getId());
		ret.setMassDiff(labelType.getMassDiff());
		return ret;
	}

}
