package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean;

public class QuantificationInfoTypeBeanAdapter implements
		Adapter<QuantificationInfoTypeBean> {
	private final QuantificationInfoType quantificationInfo;

	public QuantificationInfoTypeBeanAdapter(
			QuantificationInfoType quantificationInfo) {
		this.quantificationInfo = quantificationInfo;
	}

	@Override
	public QuantificationInfoTypeBean adapt() {
		QuantificationInfoTypeBean ret = new QuantificationInfoTypeBean();
		if (quantificationInfo.getExcelQuantInfo() != null) {
			for (QuantificationExcelType quantificationExcelType : quantificationInfo
					.getExcelQuantInfo()) {
				ret.getExcelQuantInfo().add(
						new QuantificationExcelTypeBeanAdapter(
								quantificationExcelType).adapt());
			}
		}
		if (quantificationInfo.getRemoteFilesQuantInfo() != null) {
			for (RemoteInfoType remoteInfoType : quantificationInfo
					.getRemoteFilesQuantInfo()) {
				ret.getRemoteFilesQuantInfo().add(
						new RemoteInfoTypeBeanAdapter(remoteInfoType).adapt());
			}
		}
		return ret;
	}
}
