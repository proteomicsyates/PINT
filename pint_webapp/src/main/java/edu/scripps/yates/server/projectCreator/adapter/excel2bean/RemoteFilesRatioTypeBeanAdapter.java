package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;

public class RemoteFilesRatioTypeBeanAdapter implements
		Adapter<RemoteFilesRatioTypeBean> {
	private final RemoteFilesRatioType remoteFilesRatio;

	public RemoteFilesRatioTypeBeanAdapter(RemoteFilesRatioType ratio) {
		remoteFilesRatio = ratio;
	}

	@Override
	public RemoteFilesRatioTypeBean adapt() {
		RemoteFilesRatioTypeBean ret = new RemoteFilesRatioTypeBean();
		ret.setDenominator(remoteFilesRatio.getDenominator().getConditionRef());
		ret.setDiscardDecoys(remoteFilesRatio.getDiscardDecoys());
		ret.setFileRef(remoteFilesRatio.getFileRef());
		ret.setMsRunRef(remoteFilesRatio.getMsRunRef());
		ret.setNumerator(remoteFilesRatio.getNumerator().getConditionRef());
		ret.setId(remoteFilesRatio.getName());
		return ret;
	}

}
