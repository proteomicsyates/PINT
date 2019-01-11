package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExcelAmountRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinRatiosType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean;

public class ProteinRatiosTypeBeanAdapter implements Adapter<ProteinRatiosTypeBean> {
	private final ProteinRatiosType proteinAmountRatios;

	public ProteinRatiosTypeBeanAdapter(ProteinRatiosType proteinAmountRatios) {
		this.proteinAmountRatios = proteinAmountRatios;

	}

	@Override
	public ProteinRatiosTypeBean adapt() {
		ProteinRatiosTypeBean ret = new ProteinRatiosTypeBean();
		if (proteinAmountRatios.getExcelRatio() != null) {
			for (ExcelAmountRatioType excelAmountRatioTypeBean : proteinAmountRatios.getExcelRatio()) {
				if (excelAmountRatioTypeBean.getProteinAccession() == null
						|| "".equals(excelAmountRatioTypeBean.getProteinAccession())) {
					throw new IllegalArgumentException(
							"protein_accession element is missing for a protein amount ratio");
				}

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
