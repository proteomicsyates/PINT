package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PeptideRatiosType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;

public class PeptideRatiosTypeAdapter implements Adapter<PeptideRatiosType> {
	private final PeptideRatiosTypeBean peptideAmountRatios;

	public PeptideRatiosTypeAdapter(PeptideRatiosTypeBean PeptideAmountRatios) {
		peptideAmountRatios = PeptideAmountRatios;

	}

	@Override
	public PeptideRatiosType adapt() {
		PeptideRatiosType ret = new PeptideRatiosType();
		if (peptideAmountRatios.getExcelRatio() != null) {
			for (ExcelAmountRatioTypeBean excelAmountRatioTypeBean : peptideAmountRatios.getExcelRatio()) {
				ret.getExcelRatio().add(new ExcelAmountRatioTypeAdapter(excelAmountRatioTypeBean).adapt());
			}
		}

		if (peptideAmountRatios.getRemoteFilesRatio() != null) {
			for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean : peptideAmountRatios.getRemoteFilesRatio()) {
				ret.getRemoteFilesRatio().add(new RemoteFilesRatioTypeAdapter(remoteFilesRatioTypeBean).adapt());
			}
		}
		return ret;
	}

}
