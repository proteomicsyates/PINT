package edu.scripps.yates.shared.columns;

import java.util.List;

public interface ColumnProvider<T> {

	public List<ColumnWithVisibility> getColumns();

	public ColumnWithVisibility getColumn(ColumnName columnName);

//	public String getValue(ColumnName columnName, T p, String conditionName, String condition2Name, String projectName,
//			AmountType amountType, String scoreName, String ratioName, boolean skipRatioInfinities);
}
