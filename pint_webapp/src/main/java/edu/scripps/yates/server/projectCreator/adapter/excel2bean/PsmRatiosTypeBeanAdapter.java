package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExcelAmountRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PsmRatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean;

public class PsmRatiosTypeBeanAdapter implements Adapter<PsmRatiosTypeBean> {
	private final PsmRatiosType psmAmountRatios;

	public PsmRatiosTypeBeanAdapter(PsmRatiosType psmAmountRatios) {
		this.psmAmountRatios = psmAmountRatios;
	}

	@Override
	public PsmRatiosTypeBean adapt() {
		PsmRatiosTypeBean ret = new PsmRatiosTypeBean();
		if (psmAmountRatios.getExcelRatio() != null) {
			for (ExcelAmountRatioType excelAmountRatioType : psmAmountRatios.getExcelRatio()) {
				ret.getExcelRatio().add(new ExcelAmountRatioTypeBeanAdapter(excelAmountRatioType).adapt());
			}
		}
		if (psmAmountRatios.getRemoteFilesRatio() != null) {
			for (RemoteFilesRatioType remoteFilesRatioType : psmAmountRatios.getRemoteFilesRatio()) {
				ret.getRemoteFilesRatio().add(new RemoteFilesRatioTypeBeanAdapter(remoteFilesRatioType).adapt());
			}
		}

		return ret;
	}

}
