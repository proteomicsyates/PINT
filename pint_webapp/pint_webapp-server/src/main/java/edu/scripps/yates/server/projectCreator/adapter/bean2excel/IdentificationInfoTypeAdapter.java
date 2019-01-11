package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationInfoType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class IdentificationInfoTypeAdapter implements
		Adapter<IdentificationInfoType> {
	private final IdentificationInfoTypeBean identificationInfo;

	public IdentificationInfoTypeAdapter(
			IdentificationInfoTypeBean identificationInfo) {
		this.identificationInfo = identificationInfo;
	}

	@Override
	public IdentificationInfoType adapt() {
		IdentificationInfoType ret = new IdentificationInfoType();
		if (identificationInfo.getExcelIdentInfo() != null) {
			for (IdentificationExcelTypeBean identificationExcelTypeBean : identificationInfo
					.getExcelIdentInfo()) {
				ret.getExcelIdentInfo().add(
						new IdentificationExcelTypeAdapter(
								identificationExcelTypeBean).adapt());
			}
		}
		if (identificationInfo.getRemoteFilesIdentInfo() != null) {
			for (RemoteInfoTypeBean remoteInfoTypeBean : identificationInfo
					.getRemoteFilesIdentInfo()) {
				ret.getRemoteFilesIdentInfo().add(
						new RemoteInfoTypeAdapter(remoteInfoTypeBean).adapt());
			}
		}
		return ret;
	}

}
