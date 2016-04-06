package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;

public class ScoreTypeAdapter implements Adapter<ScoreType> {
	private final ScoreTypeBean scoreTypeBean;

	public ScoreTypeAdapter(ScoreTypeBean scoreTypeBean) {
		this.scoreTypeBean = scoreTypeBean;
	}

	@Override
	public ScoreType adapt() {
		ScoreType ret = new ScoreType();
		ret.setColumnRef(scoreTypeBean.getColumnRef());
		ret.setDescription(scoreTypeBean.getDescription());
		ret.setScoreName(scoreTypeBean.getScoreName());
		ret.setScoreType(scoreTypeBean.getScoreType());
		return ret;
	}

}
