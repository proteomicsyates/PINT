package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PsmRatiosType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;

public class PsmRatiosTypeAdapter implements Adapter<PsmRatiosType> {
	private final PsmRatiosTypeBean psmAmountRatios;

	public PsmRatiosTypeAdapter(PsmRatiosTypeBean psmAmountRatios) {
		this.psmAmountRatios = psmAmountRatios;
	}

	@Override
	public PsmRatiosType adapt() {
		PsmRatiosType ret = new PsmRatiosType();
		if (psmAmountRatios.getExcelRatio() != null) {
			for (ExcelAmountRatioTypeBean excelAmountRatioTypeBean : psmAmountRatios.getExcelRatio()) {
				ret.getExcelRatio().add(new ExcelAmountRatioTypeAdapter(excelAmountRatioTypeBean).adapt());
			}
		}
		if (psmAmountRatios.getRemoteFilesRatio() != null) {
			for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean : psmAmountRatios.getRemoteFilesRatio()) {
				ret.getRemoteFilesRatio().add(new RemoteFilesRatioTypeAdapter(remoteFilesRatioTypeBean).adapt());
			}
		}

		return ret;
	}

}
