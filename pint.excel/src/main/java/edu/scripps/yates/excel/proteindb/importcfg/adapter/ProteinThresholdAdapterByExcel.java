package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinThresholdType;
import edu.scripps.yates.utilities.model.factories.ProteinThresholdEx;
import edu.scripps.yates.utilities.pattern.Adapter;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;
import gnu.trove.set.hash.THashSet;

public class ProteinThresholdAdapterByExcel implements Adapter<Set<Threshold>> {
	private final int rowIndex;
	private final IdentificationExcelType excelCondition;
	private final ExcelFileReader excelFileReader;

	public ProteinThresholdAdapterByExcel(int rowIndex, IdentificationExcelType excelCondition,
			ExcelFileReader excelFileReader) {
		this.rowIndex = rowIndex;
		this.excelCondition = excelCondition;
		this.excelFileReader = excelFileReader;
	}

	@Override
	public Set<Threshold> adapt() {
		Set<Threshold> ret = new THashSet<Threshold>();

		if (excelCondition.getProteinThresholds() != null) {
			final List<ProteinThresholdType> proteinThresholdsCfg = excelCondition.getProteinThresholds()
					.getProteinThreshold();
			if (proteinThresholdsCfg != null) {
				for (ProteinThresholdType proteinThresholdsTypeCfg : proteinThresholdsCfg) {
					final ExcelColumn proteinThresholdColumn = excelFileReader
							.getExcelColumnFromReference(proteinThresholdsTypeCfg.getColumnRef());
					if (proteinThresholdColumn != null) {

						final List<Object> thresholdValues = proteinThresholdColumn.getValues();
						if (thresholdValues.size() > rowIndex) {
							final Object thresholdValue = thresholdValues.get(rowIndex);
							if (thresholdValue != null) {
								String thresholdString = thresholdValue.toString();

								if (thresholdString.equals(proteinThresholdsTypeCfg.getYesValue())) {
									ProteinThresholdEx threshold = new ProteinThresholdEx(
											proteinThresholdsTypeCfg.getName(),
											proteinThresholdsTypeCfg.getDescription(), true);
									ret.add(threshold);
								} else {

								}

							}
						}
					}
				}
			}
		}
		return ret;
	}

}
