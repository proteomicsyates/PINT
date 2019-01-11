package edu.scripps.yates.client.interfaces;

import java.util.Set;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;

public interface HasColumns {

	/**
	 * Show or hide a certain column
	 *
	 * @param columnName
	 * @param show
	 */
	void showOrHideColumn(ColumnName columnName, boolean show);

	/**
	 * Show or hide a certain column that is referring to a some conditions
	 * included in the set of conditionNames passed by parameter
	 *
	 * @param columnName
	 * @param condition1Name
	 * @param condition2Name
	 * @param projectName
	 * @param visible
	 */
	void showOrHideExperimentalConditionColumn(ColumnName columnName, Set<String> conditionNames, String projectName,
			boolean visible);

	/**
	 * Show or hide a certain column identified by a keyName
	 *
	 * @param keyName
	 * @param visible
	 */
	void showOrHideExperimentalConditionColumn(String keyName, boolean visible);

	/**
	 * Removes the columns that are of the {@link ColumnName} specified by the
	 * parameter
	 *
	 * @param columnName
	 */
	void removeColumn(ColumnName columnName);

	/**
	 * Push sorting order to the table and refresh the data accordingly
	 *
	 * @param sortedBy
	 * @param order
	 * @param proteinSortingScore
	 */
	void pushSortingOrder(ColumnName sortedBy, ORDER order, String sortingScore);

	/**
	 * Hidde or show the columns stated by the {@link DefaultView} and sort the
	 * columns according to the sorting configuration stated in
	 * {@link DefaultView}
	 *
	 * @param defaultView
	 */
	public void setDefaultView(DefaultView defaultView);

}
