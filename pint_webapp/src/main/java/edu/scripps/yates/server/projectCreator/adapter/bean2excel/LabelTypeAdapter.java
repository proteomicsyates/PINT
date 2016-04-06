package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.LabelBean;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;

public class LabelTypeAdapter implements Adapter<LabelType> {
	private final LabelBean label;
	private final LabelTypeBean labelTypeBean;

	public LabelTypeAdapter(LabelBean label) {
		this.label = label;
		labelTypeBean = null;
	}

	public LabelTypeAdapter(LabelTypeBean labelTypeBean) {
		label = null;
		this.labelTypeBean = labelTypeBean;
	}

	@Override
	public LabelType adapt() {
		if (label != null) {
			return adaptFromLabelBean();
		} else if (labelTypeBean != null) {
			return adaptFromlabelTypeBean();
		}
		return null;

	}

	private LabelType adaptFromlabelTypeBean() {
		LabelType ret = new LabelType();

		ret.setId(labelTypeBean.getId());
		ret.setMassDiff(labelTypeBean.getMassDiff());
		return ret;
	}

	private LabelType adaptFromLabelBean() {
		LabelType ret = new LabelType();

		ret.setId(label.getId());
		ret.setMassDiff(label.getMassDiff());
		return ret;
	}

}
