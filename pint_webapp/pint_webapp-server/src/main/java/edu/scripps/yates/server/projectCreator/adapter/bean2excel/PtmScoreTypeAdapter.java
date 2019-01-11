package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PtmScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean;

public class PtmScoreTypeAdapter implements Adapter<PtmScoreType> {
	private final PtmScoreTypeBean ptmScoreTypeBean;

	public PtmScoreTypeAdapter(PtmScoreTypeBean ptmScoreTypeBean) {
		this.ptmScoreTypeBean = ptmScoreTypeBean;
	}

	@Override
	public PtmScoreType adapt() {
		PtmScoreType ret = new PtmScoreType();
		ret.setColumnRef(ptmScoreTypeBean.getColumnRef());
		ret.setDescription(ptmScoreTypeBean.getDescription());
		ret.setModificationName(ptmScoreTypeBean.getModificationName());
		ret.setScoreName(ptmScoreTypeBean.getScoreName());
		ret.setScoreType(ptmScoreTypeBean.getScoreType());
		return ret;
	}

}
