package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PsmType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean;

public class PsmTypeBeanAdapter implements Adapter<PsmTypeBean> {
	private final PsmType psmId;

	public PsmTypeBeanAdapter(PsmType psmId) {
		this.psmId = psmId;
	}

	@Override
	public PsmTypeBean adapt() {
		PsmTypeBean ret = new PsmTypeBean();
		ret.setColumnRef(psmId.getColumnRef());
		return ret;
	}

}
