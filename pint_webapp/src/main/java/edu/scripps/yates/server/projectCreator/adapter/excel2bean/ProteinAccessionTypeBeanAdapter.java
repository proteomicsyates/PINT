package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAccessionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean;

public class ProteinAccessionTypeBeanAdapter implements Adapter<ProteinAccessionTypeBean> {

	private final ProteinAccessionType proteinAccessionType;

	public ProteinAccessionTypeBeanAdapter(ProteinAccessionType proteinAccessionType) {
		this.proteinAccessionType = proteinAccessionType;
	}

	@Override
	public ProteinAccessionTypeBean adapt() {
		ProteinAccessionTypeBean ret = new ProteinAccessionTypeBean();
		ret.setColumnRef(proteinAccessionType.getColumnRef());
		ret.setGroups(proteinAccessionType.isGroups());
		ret.setGroupSeparator(proteinAccessionType.getGroupSeparator());
		ret.setRegexp(proteinAccessionType.getRegexp());

		return ret;
	}

}
