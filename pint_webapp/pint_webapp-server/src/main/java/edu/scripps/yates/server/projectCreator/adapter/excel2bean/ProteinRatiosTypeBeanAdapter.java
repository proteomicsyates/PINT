package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExcelAmountRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinRatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean;

public class ProteinRatiosTypeBeanAdapter implements Adapter<ProteinRatiosTypeBean> {
	private final ProteinRatiosType proteinAmountRatios;

	public ProteinRatiosTypeBeanAdapter(ProteinRatiosType proteinAmountRatios) {
		this.proteinAmountRatios = proteinAmountRatios;

	}

	@Override
	public ProteinRatiosTypeBean adapt() {
		final ProteinRatiosTypeBean ret = new ProteinRatiosTypeBean();
		if (proteinAmountRatios.getExcelRatio() != null) {
			for (final ExcelAmountRatioType excelAmountRatioTypeBean : proteinAmountRatios.getExcelRatio()) {
//				if (excelAmountRatioTypeBean.getProteinAccession() == null
//						|| "".equals(excelAmountRatioTypeBean.getProteinAccession())) {
//					throw new IllegalArgumentException(
//							"protein_accession element is missing for a protein amount ratio");
//				}

				ret.getExcelRatio().add(new ExcelAmountRatioTypeBeanAdapter(excelAmountRatioTypeBean).adapt());
			}
		}

		if (proteinAmountRatios.getRemoteFilesRatio() != null) {
			for (final RemoteFilesRatioType remoteFilesRatioTypeBean : proteinAmountRatios.getRemoteFilesRatio()) {
				ret.getRemoteFilesRatio().add(new RemoteFilesRatioTypeBeanAdapter(remoteFilesRatioTypeBean).adapt());
			}
		}
		return ret;
	}

}
