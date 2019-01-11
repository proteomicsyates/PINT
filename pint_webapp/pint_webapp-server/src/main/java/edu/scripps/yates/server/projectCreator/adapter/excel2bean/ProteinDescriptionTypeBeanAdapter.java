package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinDescriptionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean;

public class ProteinDescriptionTypeBeanAdapter implements
		Adapter<ProteinDescriptionTypeBean> {
	private final ProteinDescriptionType proteinDescriptionType;

	public ProteinDescriptionTypeBeanAdapter(
			ProteinDescriptionType proteinDescriptionType) {
		this.proteinDescriptionType = proteinDescriptionType;
	}

	@Override
	public ProteinDescriptionTypeBean adapt() {
		ProteinDescriptionTypeBean ret = new ProteinDescriptionTypeBean();
		ret.setColumnRef(proteinDescriptionType.getColumnRef());
		ret.setGroups(proteinDescriptionType.isGroups());
		ret.setGroupSeparator(proteinDescriptionType.getGroupSeparator());
		ret.setRegexp(proteinDescriptionType.getRegexp());
		return ret;
	}

}
