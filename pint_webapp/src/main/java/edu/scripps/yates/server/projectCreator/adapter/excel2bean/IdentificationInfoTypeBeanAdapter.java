package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean;

public class IdentificationInfoTypeBeanAdapter implements
		Adapter<IdentificationInfoTypeBean> {
	private final IdentificationInfoType identificationInfo;

	public IdentificationInfoTypeBeanAdapter(
			IdentificationInfoType identificationInfo) {
		this.identificationInfo = identificationInfo;
	}

	@Override
	public IdentificationInfoTypeBean adapt() {
		IdentificationInfoTypeBean ret = new IdentificationInfoTypeBean();
		if (identificationInfo.getExcelIdentInfo() != null) {
			for (IdentificationExcelType identificationExcelType : identificationInfo
					.getExcelIdentInfo()) {
				ret.getExcelIdentInfo().add(
						new IdentificationExcelTypeBeanAdapter(
								identificationExcelType).adapt());
			}
		}
		if (identificationInfo.getRemoteFilesIdentInfo() != null) {
			for (RemoteInfoType remoteInfoType : identificationInfo
					.getRemoteFilesIdentInfo()) {
				ret.getRemoteFilesIdentInfo().add(
						new RemoteInfoTypeBeanAdapter(remoteInfoType).adapt());
			}
		}
		return ret;
	}

}
