package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinThresholdType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean;

public class ProteinThresholdTypeBeanAdapter implements Adapter<ProteinThresholdTypeBean> {

	private final ProteinThresholdType proteinThresholdType;

	public ProteinThresholdTypeBeanAdapter(ProteinThresholdType proteinThresholdType) {
		this.proteinThresholdType = proteinThresholdType;
	}

	@Override
	public ProteinThresholdTypeBean adapt() {
		ProteinThresholdTypeBean ret = new ProteinThresholdTypeBean();
		ret.setColumnRef(proteinThresholdType.getColumnRef());
		ret.setName(proteinThresholdType.getName());
		ret.setDescription(proteinThresholdType.getDescription());
		ret.setYesValue(proteinThresholdType.getYesValue());
		return ret;
	}
}
