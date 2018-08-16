package edu.scripps.yates.client.gui.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.ProteinGroupColumnManager;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.ProteinGroupFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class ProteinGroupTablePanel extends AbstractDataTable<ProteinGroupBean> {

	public ProteinGroupTablePanel(String sessionID, Widget emptyWidget,
			AbstractAsyncDataProvider<ProteinGroupBean> asyncDataProvider, boolean multipleSelectionModel,
			QueryPanel queryPanel) {
		super(sessionID, emptyWidget, asyncDataProvider, multipleSelectionModel, "protein group table", queryPanel);
	}

	/**
	 * Adds the selectionHandler to the protein table.
	 *
	 * @param selectionHandler
	 */
	public void addProteinSelectionHandler(SelectionChangeEvent.Handler selectionHandler) {
		selectionModel.addSelectionChangeHandler(selectionHandler);
	}

	@Override
	public MyDataGrid<ProteinGroupBean> makeDataGrid() {
		final ProvidesKey<ProteinGroupBean> KEY_PROVIDER = new ProvidesKey<ProteinGroupBean>() {
			@Override
			public Object getKey(ProteinGroupBean item) {

				return item == null ? null : item.getProteinDBString();
			}
		};
		final MyDataGrid<ProteinGroupBean> dataGrid = new MyDataGrid<ProteinGroupBean>(KEY_PROVIDER, tableName);

		return dataGrid;
	}

	@Override
	protected SimplePager makePager() {
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		final SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PROTEINGROUP_DEFAULT_PAGE_SIZE * 5, true);

		simplePager.setPageSize(SharedConstants.PROTEINGROUP_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	@Override
	public List<ColumnWithVisibility> getItemDefaultView(DefaultView defaultview) {
		return defaultview.getProteinGroupDefaultView();
	}

	@Override
	public ORDER getItemOrder(DefaultView defaultView) {
		return defaultView.getProteinGroupOrder();
	}

	@Override
	public String getSortingScore(DefaultView defaultView) {
		return defaultView.getProteinGroupSortingScore();
	}

	@Override
	public ColumnName getSortedBy(DefaultView defaultView) {
		return defaultView.getProteinGroupsSortedBy();
	}

	@Override
	protected AbstractColumnManager<ProteinGroupBean> createColumnManager(
			FooterManager<ProteinGroupBean> footerManager) {
		return new ProteinGroupColumnManager(footerManager, sessionID);
	}

	@Override
	protected FooterManager<ProteinGroupBean> createFooterManager(MyDataGrid<ProteinGroupBean> dataGrid) {
		return new ProteinGroupFooterManager(dataGrid);
	}

}
