package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAnnotationType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean;

public class ProteinAnnotationTypeBeanAdapter implements
		Adapter<ProteinAnnotationTypeBean> {

	private final ProteinAnnotationType proteinAnnotationType;

	public ProteinAnnotationTypeBeanAdapter(
			ProteinAnnotationType proteinAnnotationType) {
		this.proteinAnnotationType = proteinAnnotationType;
	}

	@Override
	public ProteinAnnotationTypeBean adapt() {
		ProteinAnnotationTypeBean ret = new ProteinAnnotationTypeBean();
		ret.setBinary(proteinAnnotationType.isBinary());
		ret.setColumnRef(proteinAnnotationType.getColumnRef());
		ret.setName(proteinAnnotationType.getName());
		ret.setYesValue(proteinAnnotationType.getYesValue());
		return ret;
	}
}
