package edu.scripps.yates.client.gui.reactome;

import java.util.List;

import org.reactome.web.analysis.client.model.PathwaySummary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.PathwaysColumnManager;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.components.AbstractDataTable;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class ReactomePathwaysTablePanel extends AbstractDataTable<PathwaySummary> {

	public ReactomePathwaysTablePanel(String sessionID,
			AbstractAsyncDataProvider<PathwaySummary> asyncDataListProvider) {
		super(sessionID, "Submit the analysis in order to populate the table", asyncDataListProvider, false,
				"Reactome table", null);

		// flowPanelTable.setSize("100%", "80%");
		// ResizeLayoutPanel resizePanel = new ResizeLayoutPanel();
		// resizePanel.setSize("100%", "90%");
		// flowPanelTable.add(resizePanel);
		super.pushSortingOrder(ColumnName.PATHWAY_FDR, ORDER.ASCENDING, null);
		addStyleName("ReactomePathwaysTablePanel");
	}

	@Override
	protected AbstractColumnManager<PathwaySummary> createColumnManager(FooterManager<PathwaySummary> footerManager) {
		return new PathwaysColumnManager(sessionID);
	}

	@Override
	protected FooterManager<PathwaySummary> createFooterManager(MyDataGrid<PathwaySummary> dataGrid) {
		return null;
	}

	@Override
	protected SimplePager makePager() {
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		final SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.REACTOME_PATHWAYS_DEFAULT_PAGE_SIZE * 5, true);
		simplePager.setPageSize(SharedConstants.REACTOME_PATHWAYS_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	@Override
	public List<ColumnWithVisibility> getItemDefaultView(DefaultView defaultview) {
		return null;
	}

	@Override
	public ORDER getItemOrder(DefaultView defaultView) {
		return null;
	}

	@Override
	public String getSortingScore(DefaultView defaultView) {
		return null;
	}

	@Override
	public ColumnName getSortedBy(DefaultView defaultView) {
		return null;
	}

	@Override
	protected MyDataGrid<PathwaySummary> makeDataGrid() {
		final ProvidesKey<PathwaySummary> KEY_PROVIDER = new ProvidesKey<PathwaySummary>() {
			@Override
			public Object getKey(PathwaySummary item) {
				return item == null ? null : item.getDbId();
			}
		};
		final MyDataGrid<PathwaySummary> dataGrid = new MyDataGrid<PathwaySummary>(KEY_PROVIDER, tableName);
		return dataGrid;
	}
}
