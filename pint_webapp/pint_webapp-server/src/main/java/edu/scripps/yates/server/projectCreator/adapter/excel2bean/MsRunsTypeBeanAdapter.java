package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunsType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean;

public class MsRunsTypeBeanAdapter implements Adapter<MsRunsTypeBean> {
	private final MsRunsType msRuns;

	public MsRunsTypeBeanAdapter(MsRunsType msRuns) {
		this.msRuns = msRuns;
	}

	@Override
	public MsRunsTypeBean adapt() {
		MsRunsTypeBean ret = new MsRunsTypeBean();
		if (msRuns.getMsRun() != null) {
			for (MsRunType msRunTypeBean : msRuns.getMsRun()) {
				ret.getMsRun().add(new MsRunTypeBeanAdapter(msRunTypeBean).adapt());
			}
		}
		return ret;
	}

}
