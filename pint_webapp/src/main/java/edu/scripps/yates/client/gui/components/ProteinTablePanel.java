package edu.scripps.yates.client.gui.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.ProteinColumnManager;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.ProteinFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class ProteinTablePanel extends AbstractDataTable<ProteinBean> {

	private static final String cwDataGridEmpty = "No proteins shown";

	public ProteinTablePanel(String sessionID, String emptyLabel,
			AbstractAsyncDataProvider<ProteinBean> asyncProteinBeanListDataProvider, boolean multipleSelectionModel) {
		super(sessionID, emptyLabel, asyncProteinBeanListDataProvider, multipleSelectionModel, "protein table");

		dataGrid.redraw();
	}

	@Override
	protected MyDataGrid<ProteinBean> makeDataGrid() {
		final ProvidesKey<ProteinBean> KEY_PROVIDER = new ProvidesKey<ProteinBean>() {
			@Override
			public Object getKey(ProteinBean item) {

				return item == null ? null : item.getProteinDBString();
			}
		};
		MyDataGrid<ProteinBean> dataGrid = new MyDataGrid<ProteinBean>(KEY_PROVIDER, tableName);
		dataGrid.setEmptyTableWidget(new Label(cwDataGridEmpty));

		return dataGrid;
	}

	@Override
	protected SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PROTEIN_DEFAULT_PAGE_SIZE * 5, true);
		simplePager.setPageSize(SharedConstants.PROTEIN_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	/**
	 * @return the dataGrid
	 */
	@Override
	public MyDataGrid<ProteinBean> getDataGrid() {
		return dataGrid;
	}

	@Override
	protected AbstractColumnManager<ProteinBean> createColumnManager(FooterManager<ProteinBean> footerManager) {
		return new ProteinColumnManager(footerManager, sessionID);
	}

	@Override
	protected FooterManager<ProteinBean> createFooterManager(MyDataGrid<ProteinBean> dataGrid) {
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
