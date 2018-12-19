package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.utilities.pattern.Adapter;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.factories.ScoreEx;

public class ScoreAdapter implements Adapter<Score> {
	private final ScoreType scoreCfg;
	private final ExcelFileReader excelFileReader;
	private final int rowIndex;

	public ScoreAdapter(ScoreType scoreCfg, ExcelFileReader excelFileReader, int rowIndex) {
		this.scoreCfg = scoreCfg;
		this.excelFileReader = excelFileReader;
		this.rowIndex = rowIndex;
	}

	@Override
	public Score adapt() {
		final ExcelColumn excelColumn = excelFileReader.getExcelColumnFromReference(scoreCfg.getColumnRef());
		Object object = null;
		if (excelColumn.getValues().size() > rowIndex)
			object = excelColumn.getValues().get(rowIndex);
		if (object == null)
			return null;
		final String value = object.toString().trim();
		if (value.equals("-")) {
			return null;
		}
		if (value.equalsIgnoreCase("NaN"))
			return null;
		ScoreEx ret = new ScoreEx(value, scoreCfg.getScoreName(), scoreCfg.getScoreType(), scoreCfg.getDescription());

		return ret;
	}
}
