package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinDescriptionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean;

public class ProteinDescriptionTypeAdapter implements
		Adapter<ProteinDescriptionType> {
	private final ProteinDescriptionTypeBean proteinDescriptionTypeBean;

	public ProteinDescriptionTypeAdapter(
			ProteinDescriptionTypeBean proteinDescriptionType) {
		proteinDescriptionTypeBean = proteinDescriptionType;
	}

	@Override
	public ProteinDescriptionType adapt() {
		ProteinDescriptionType ret = new ProteinDescriptionType();
		ret.setColumnRef(proteinDescriptionTypeBean.getColumnRef());
		ret.setGroups(proteinDescriptionTypeBean.isGroups());
		ret.setGroupSeparator(proteinDescriptionTypeBean.getGroupSeparator());
		ret.setRegexp(proteinDescriptionTypeBean.getRegexp());
		return ret;
	}

}
