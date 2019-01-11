package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAnnotationType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean;

public class ProteinAnnotationTypeAdapter implements
		Adapter<ProteinAnnotationType> {

	private final ProteinAnnotationTypeBean proteinAnnotationTypeBean;

	public ProteinAnnotationTypeAdapter(
			ProteinAnnotationTypeBean proteinAnnotationTypeBean) {
		this.proteinAnnotationTypeBean = proteinAnnotationTypeBean;
	}

	@Override
	public ProteinAnnotationType adapt() {
		ProteinAnnotationType ret = new ProteinAnnotationType();
		ret.setBinary(proteinAnnotationTypeBean.isBinary());
		ret.setColumnRef(proteinAnnotationTypeBean.getColumnRef());
		ret.setName(proteinAnnotationTypeBean.getName());
		ret.setYesValue(proteinAnnotationTypeBean.getYesValue());
		return ret;
	}
}
