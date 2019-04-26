package edu.scripps.yates.client.gui.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.PSMColumnManager;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.PSMFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.client.interfaces.ShowHiddePanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class PSMTablePanel extends AbstractDataTable<PSMBeanLight> {
	public static final String SELECT_PROTEIN_TO_LOAD_PSMS_TEXT = "Select one protein to load PSMs";
	public static final String NO_DATA_FROM_THIS_PROTEIN = "There is not any PSM information for that selection";
	public static final String WAIT_TO_BE_LOADED = "Please wait for PSMs to be loaded...";

	private final ShowHiddePanel psmLoaderFromProjects;

	public PSMTablePanel(String sessionID, String emptyLabel, ShowHiddePanel showhiddePSMPanel,
			AbstractAsyncDataProvider<PSMBeanLight> asyncDataListProvider, boolean multipleSelectionModel,
			String tableName, QueryPanel queryPanel) {
		super(sessionID, emptyLabel, asyncDataListProvider, multipleSelectionModel, tableName, queryPanel);
		psmLoaderFromProjects = showhiddePSMPanel;

	}

	@Override
	protected MyDataGrid<PSMBeanLight> makeDataGrid() {
		/**
		 * The key provider that provides the unique ID of a PSMBean.
		 */
		final ProvidesKey<PSMBeanLight> KEY_PROVIDER = new ProvidesKey<PSMBeanLight>() {
			@Override
			public Object getKey(PSMBeanLight item) {
				return item == null ? null : item.getPsmID();
			}
		};
		final MyDataGrid<PSMBeanLight> dataGrid = new MyDataGrid<PSMBeanLight>(KEY_PROVIDER, tableName);

		return dataGrid;
	}

	@Override
	protected SimplePager makePager() {
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		final SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PSM_DEFAULT_PAGE_SIZE * 5, true);
		simplePager.setPageSize(SharedConstants.PSM_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	public void hiddePSMPanel() {
		if (psmLoaderFromProjects != null) {
			psmLoaderFromProjects.hiddePanel();
		}
	}

	public void showPSMPanel() {
		if (psmLoaderFromProjects != null) {
			psmLoaderFromProjects.showPanel();
		}
	}

	@Override
	protected AbstractColumnManager<PSMBeanLight> createColumnManager(FooterManager<PSMBeanLight> footerManager) {
		return new PSMColumnManager(footerManager);
	}

	@Override
	protected FooterManager<PSMBeanLight> createFooterManager(MyDataGrid<PSMBeanLight> dataGrid) {
		return new PSMFooterManager(dataGrid);
	}

	@Override
	public List<ColumnWithVisibility> getItemDefaultView(DefaultView defaultview) {
		return defaultview.getPsmDefaultView();
	}

	@Override
	public ORDER getItemOrder(DefaultView defaultView) {
		return defaultView.getPsmOrder();
	}

	@Override
	public String getSortingScore(DefaultView defaultView) {
		return defaultView.getPsmSortingScore();
	}

	@Override
	public ColumnName getSortedBy(DefaultView defaultView) {
		return defaultView.getPsmsSortedBy();
	}

}