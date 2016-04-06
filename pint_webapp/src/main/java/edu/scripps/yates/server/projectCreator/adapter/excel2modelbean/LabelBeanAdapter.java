package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.LabelBean;

public class LabelBeanAdapter implements Adapter<LabelBean> {

	private final LabelType labelType;

	public LabelBeanAdapter(LabelType labelType) {
		this.labelType = labelType;
	}

	@Override
	public LabelBean adapt() {
		LabelBean labelBean = new LabelBean();
		labelBean.setName(labelType.getId());
		labelBean.setMassDiff(labelBean.getMassDiff());
		return labelBean;
	}

}
