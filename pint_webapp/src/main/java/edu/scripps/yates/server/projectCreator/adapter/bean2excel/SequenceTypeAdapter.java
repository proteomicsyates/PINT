package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SequenceType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean;

public class SequenceTypeAdapter implements Adapter<SequenceType> {
	private final SequenceTypeBean peptideSequenceTypeBean;

	public SequenceTypeAdapter(SequenceTypeBean sequenceTypeBean) {
		peptideSequenceTypeBean = sequenceTypeBean;
	}

	@Override
	public SequenceType adapt() {
		SequenceType ret = new SequenceType();
		ret.setColumnRef(peptideSequenceTypeBean.getColumnRef());
		return ret;
	}

}
