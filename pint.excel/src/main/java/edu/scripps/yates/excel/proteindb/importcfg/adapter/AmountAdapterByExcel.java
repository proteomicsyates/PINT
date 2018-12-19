package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountCombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType;
import edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.factories.AmountEx;

public class AmountAdapterByExcel implements edu.scripps.yates.utilities.pattern.Adapter<Amount> {
	private static final Logger log = Logger.getLogger(AmountAdapterByExcel.class);
	private final edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountType amountCfg;
	private final ExcelFileReader excelFileReader;
	private final Condition expCondition;
	private final int rowIndex;

	public AmountAdapterByExcel(int rowIndex, edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountType amountCfg,
			ExcelFileReader excelFileReader, Condition expCondition) {

		this.amountCfg = amountCfg;
		this.excelFileReader = excelFileReader;
		this.expCondition = expCondition;
		this.rowIndex = rowIndex;
	}

	@Override
	public Amount adapt() {
		try {
			final AmountType amountType = AmountType.valueOf(amountCfg.getType().name());

			final ExcelColumn amountValuesColumn = excelFileReader
					.getExcelColumnFromReference(amountCfg.getColumnRef());

			final List<Object> amountValues = amountValuesColumn.getValues();
			final AmountCombinationType combinationType = amountCfg.getCombinationType();
			final CombinationType combinationType2 = CombinationType.getCombinationType(combinationType.name());
			final Double value = Double.valueOf(amountValues.get(rowIndex).toString());
			if (value != null) {
				final AmountEx amount = new AmountEx((double) amountValues.get(rowIndex), amountType, expCondition);
				amount.setCombinationType(combinationType2);
				return amount;
			}
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			log.warn("Error getting amount value from row: " + rowIndex);
		}
		return null;
	}
}
