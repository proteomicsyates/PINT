package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinThresholdType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean;

public class ProteinThresholdTypeAdapter implements Adapter<ProteinThresholdType> {

	private final ProteinThresholdTypeBean proteinDescriptionTypeBean;

	public ProteinThresholdTypeAdapter(ProteinThresholdTypeBean proteinAnnotationTypeBean) {
		proteinDescriptionTypeBean = proteinAnnotationTypeBean;
	}

	@Override
	public ProteinThresholdType adapt() {
		ProteinThresholdType ret = new ProteinThresholdType();
		ret.setColumnRef(proteinDescriptionTypeBean.getColumnRef());
		ret.setName(proteinDescriptionTypeBean.getName());
		ret.setYesValue(proteinDescriptionTypeBean.getYesValue());
		ret.setDescription(proteinDescriptionTypeBean.getDescription());
		return ret;
	}
}
