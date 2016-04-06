package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SequenceType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean;

public class PeptideSequenceTypeBeanAdapter implements Adapter<SequenceTypeBean> {
	private final SequenceType sequenceType;

	public PeptideSequenceTypeBeanAdapter(SequenceType sequenceType) {
		this.sequenceType = sequenceType;
	}

	@Override
	public SequenceTypeBean adapt() {
		SequenceTypeBean ret = new SequenceTypeBean();
		ret.setColumnRef(sequenceType.getColumnRef());
		return ret;
	}

}
