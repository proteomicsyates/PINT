package edu.scripps.yates.client.gui.columns.footers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;

public abstract class FooterManager<T> {
	protected final MyDataGrid<T> dataGrid;
	protected final Map<ColumnName, Header<String>> footers = new HashMap<ColumnName, Header<String>>();

	protected FooterManager(MyDataGrid<T> datagrid) {
		this.dataGrid = datagrid;
	}

	public Header<String> getFooter(ColumnName columnName) {
		return footers.get(columnName);
	}

	public abstract Header<String> getAmountFooterByCondition(final String conditionName, final AmountType amountType,
			final String projectName);

	public abstract Header<String> getRatioFooterByConditions(final String condition1Name, final String condition2Name,
			final String projectTag, String ratioName);

	public abstract Header<String> getRatioScoreFooterByConditions(final String condition1Name,
			final String condition2Name, final String projectTag, String ratioName);

	public abstract Header<String> getScoreFooterByScore(String scoreName);
}
