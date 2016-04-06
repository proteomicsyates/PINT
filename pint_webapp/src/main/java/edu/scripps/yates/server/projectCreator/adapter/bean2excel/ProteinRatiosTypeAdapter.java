package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinRatiosType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean;

public class ProteinRatiosTypeAdapter implements Adapter<ProteinRatiosType> {
	private final ProteinRatiosTypeBean proteinAmountRatios;

	public ProteinRatiosTypeAdapter(ProteinRatiosTypeBean proteinAmountRatios) {
		this.proteinAmountRatios = proteinAmountRatios;

	}

	@Override
	public ProteinRatiosType adapt() {
		ProteinRatiosType ret = new ProteinRatiosType();
		if (proteinAmountRatios.getExcelRatio() != null) {
			for (ExcelAmountRatioTypeBean excelAmountRatioTypeBean : proteinAmountRatios.getExcelRatio()) {
				ret.getExcelRatio().add(new ExcelAmountRatioTypeAdapter(excelAmountRatioTypeBean).adapt());
			}
		}

		// TODO add remotefilesRatios to proteins
		// if (proteinAmountRatios.getRemoteFilesRatio() != null) {
		// for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean :
		// proteinAmountRatios
		// .getRemoteFilesRatio()) {
		// ret.getRemoteFilesRatio().add(
		// new RemoteFilesRatioTypeAdapter(
		// remoteFilesRatioTypeBean).adapt());
		// }
		// }
		return ret;
	}

}
