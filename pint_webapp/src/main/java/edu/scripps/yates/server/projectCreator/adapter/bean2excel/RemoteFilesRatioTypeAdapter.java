package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;

public class RemoteFilesRatioTypeAdapter implements
		Adapter<RemoteFilesRatioType> {
	private final RemoteFilesRatioTypeBean remoteFilesRatioBean;

	public RemoteFilesRatioTypeAdapter(RemoteFilesRatioTypeBean ratio) {
		remoteFilesRatioBean = ratio;
	}

	@Override
	public RemoteFilesRatioType adapt() {
		RemoteFilesRatioType ret = new RemoteFilesRatioType();
		if (remoteFilesRatioBean.getDenominator() != null)
			ret.setDenominator(new ConditionRefTypeAdapter(remoteFilesRatioBean
					.getDenominator()).adapt());
		ret.setDiscardDecoys(remoteFilesRatioBean.getDiscardDecoys());
		ret.setFileRef(remoteFilesRatioBean.getFileRef());
		ret.setMsRunRef(remoteFilesRatioBean.getMsRunRef());
		ret.setName(remoteFilesRatioBean.getId());
		if (remoteFilesRatioBean.getNumerator() != null)
			ret.setNumerator(new ConditionRefTypeAdapter(remoteFilesRatioBean
					.getNumerator()).adapt());
		return ret;
	}

}
