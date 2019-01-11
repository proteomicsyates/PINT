package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PtmScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean;

public class PtmScoreTypeBeanAdapter implements Adapter<PtmScoreTypeBean> {
	private final PtmScoreType ptmScoreType;

	public PtmScoreTypeBeanAdapter(PtmScoreType ptmScoreType) {
		this.ptmScoreType = ptmScoreType;
	}

	@Override
	public PtmScoreTypeBean adapt() {
		PtmScoreTypeBean ret = new PtmScoreTypeBean();
		ret.setColumnRef(ptmScoreType.getColumnRef());
		ret.setDescription(ptmScoreType.getDescription());
		ret.setModificationName(ptmScoreType.getModificationName());
		ret.setScoreName(ptmScoreType.getScoreName());
		ret.setScoreType(ptmScoreType.getScoreType());
		return ret;
	}

}
