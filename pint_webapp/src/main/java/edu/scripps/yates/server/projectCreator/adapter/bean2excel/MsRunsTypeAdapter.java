package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunsType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean;

public class MsRunsTypeAdapter implements Adapter<MsRunsType> {
	private final MsRunsTypeBean msRuns;

	public MsRunsTypeAdapter(MsRunsTypeBean msRuns) {
		this.msRuns = msRuns;
	}

	@Override
	public MsRunsType adapt() {
		MsRunsType ret = new MsRunsType();
		if (msRuns.getMsRun() != null) {
			for (MsRunTypeBean msRunTypeBean : msRuns.getMsRun()) {
				ret.getMsRun().add(new MsRunTypeAdapter(msRunTypeBean).adapt());
			}
		}
		return ret;
	}

}
