package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExcelAmountRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PeptideRatiosType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean;

public class PeptideRatiosTypeBeanAdapter implements Adapter<PeptideRatiosTypeBean> {
	private final PeptideRatiosType peptideAmountRatios;

	public PeptideRatiosTypeBeanAdapter(PeptideRatiosType peptideAmountRatios) {
		this.peptideAmountRatios = peptideAmountRatios;

	}

	@Override
	public PeptideRatiosTypeBean adapt() {
		PeptideRatiosTypeBean ret = new PeptideRatiosTypeBean();
		if (peptideAmountRatios.getExcelRatio() != null) {
			for (ExcelAmountRatioType excelAmountRatioTypeBean : peptideAmountRatios.getExcelRatio()) {
				ret.getExcelRatio().add(new ExcelAmountRatioTypeBeanAdapter(excelAmountRatioTypeBean).adapt());
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
