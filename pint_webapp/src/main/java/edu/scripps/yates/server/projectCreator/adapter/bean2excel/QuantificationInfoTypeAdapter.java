package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationInfoType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class QuantificationInfoTypeAdapter implements
		Adapter<QuantificationInfoType> {
	private final QuantificationInfoTypeBean quantificationInfo;

	public QuantificationInfoTypeAdapter(
			QuantificationInfoTypeBean quantificationInfo) {
		this.quantificationInfo = quantificationInfo;
	}

	@Override
	public QuantificationInfoType adapt() {
		QuantificationInfoType ret = new QuantificationInfoType();
		if (quantificationInfo.getExcelQuantInfo() != null) {
			for (QuantificationExcelTypeBean quantificationExcelTypeBean : quantificationInfo
					.getExcelQuantInfo()) {
				ret.getExcelQuantInfo().add(
						new QuantificationExcelTypeAdapter(
								quantificationExcelTypeBean).adapt());
			}
		}
		if (quantificationInfo.getRemoteFilesQuantInfo() != null) {
			for (RemoteInfoTypeBean remoteInfoTypeBean : quantificationInfo
					.getRemoteFilesQuantInfo()) {
				ret.getRemoteFilesQuantInfo().add(
						new RemoteInfoTypeAdapter(remoteInfoTypeBean).adapt());
			}
		}
		return ret;
	}
}
