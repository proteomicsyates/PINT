package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PsmType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SequenceType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean;

public class PsmIdTypeAdapter implements Adapter<PsmType>{
private final PsmTypeBean psmId;
	public PsmIdTypeAdapter(PsmTypeBean psmId) {
this.psmId=psmId;	}

	@Override
	public PsmType adapt() {
		PsmType ret = new PsmType();
		ret.setColumnRef(psmId.getColumnRef());
		return ret;
		}

}
