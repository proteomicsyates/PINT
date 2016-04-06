package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;

public class ScoreTypeBeanAdapter implements Adapter<ScoreTypeBean> {
	private final ScoreType scoreType;

	public ScoreTypeBeanAdapter(ScoreType scoreTypeBean) {
		scoreType = scoreTypeBean;
	}

	@Override
	public ScoreTypeBean adapt() {
		ScoreTypeBean ret = new ScoreTypeBean();
		ret.setColumnRef(scoreType.getColumnRef());
		ret.setDescription(scoreType.getDescription());
		ret.setScoreName(scoreType.getScoreName());
		ret.setScoreType(scoreType.getScoreType());
		return ret;
	}

}
