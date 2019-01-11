package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAccessionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean;

public class ProteinAccessionTypeAdapter implements Adapter<ProteinAccessionType> {

	private final ProteinAccessionTypeBean proteinAccessionTypeBean;

	public ProteinAccessionTypeAdapter(ProteinAccessionTypeBean proteinAccessionTypeBean) {
		this.proteinAccessionTypeBean = proteinAccessionTypeBean;
	}

	@Override
	public ProteinAccessionType adapt() {
		ProteinAccessionType ret = new ProteinAccessionType();
		ret.setColumnRef(proteinAccessionTypeBean.getColumnRef());
		ret.setGroups(proteinAccessionTypeBean.isGroups());
		ret.setGroupSeparator(proteinAccessionTypeBean.getGroupSeparator());
		ret.setRegexp(proteinAccessionTypeBean.getRegexp());

		return ret;
	}

}
