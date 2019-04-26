package edu.scripps.yates.client.gui.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.ProteinColumnManager;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.ProteinFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.light.ProteinBeanLight;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class ProteinTablePanel extends AbstractDataTable<ProteinBeanLight> {

	private static final String cwDataGridEmpty = "No proteins shown";

	public ProteinTablePanel(String sessionID, String emptyLabel,
			AbstractAsyncDataProvider<ProteinBeanLight> asyncProteinBeanListDataProvider,
			boolean multipleSelectionModel, QueryPanel queryPanel) {
		super(sessionID, emptyLabel, asyncProteinBeanListDataProvider, multipleSelectionModel, "protein table",
				queryPanel);

		dataGrid.redraw();
	}

	@Override
	protected MyDataGrid<ProteinBeanLight> makeDataGrid() {
		final ProvidesKey<ProteinBeanLight> KEY_PROVIDER = new ProvidesKey<ProteinBeanLight>() {
			@Override
			public Object getKey(ProteinBeanLight item) {

				return item == null ? null : item.getProteinDBString();
			}
		};
		final MyDataGrid<ProteinBeanLight> dataGrid = new MyDataGrid<ProteinBeanLight>(KEY_PROVIDER, tableName);
		dataGrid.setEmptyTableWidget(new Label(cwDataGridEmpty));

		return dataGrid;
	}

	@Override
	protected SimplePager makePager() {
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		final SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PROTEIN_DEFAULT_PAGE_SIZE * 5, true);
		simplePager.setPageSize(SharedConstants.PROTEIN_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	/**
	 * @return the dataGrid
	 */
	@Override
	public MyDataGrid<ProteinBeanLight> getDataGrid() {
		return dataGrid;
	}

	@Override
	protected AbstractColumnManager<ProteinBeanLight> createColumnManager(
			FooterManager<ProteinBeanLight> footerManager) {
		return new ProteinColumnManager(footerManager, sessionID);
	}

	@Override
	protected FooterManager<ProteinBeanLight> createFooterManager(MyDataGrid<ProteinBeanLight> dataGrid) {
		return new ProteinFooterManager(dataGrid);
	}

	@Override
	public List<ColumnWithVisibility> getItemDefaultView(DefaultView defaultview) {
		return defaultview.getProteinDefaultView();
	}

	@Override
	public ORDER getItemOrder(DefaultView defaultView) {
		return defaultView.getProteinOrder();
	}

	@Override
	public String getSortingScore(DefaultView defaultView) {
		return defaultView.getProteinSortingScore();
	}

	@Override
	public ColumnName getSortedBy(DefaultView defaultView) {
		return defaultView.getProteinsSortedBy();
	}
}
