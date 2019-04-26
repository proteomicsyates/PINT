package edu.scripps.yates.client.gui.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.PeptideColumnManager;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.PeptideFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncPeptideBeanListDataProvider;
import edu.scripps.yates.client.interfaces.ShowHiddePanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class PeptideTablePanel extends AbstractDataTable<PeptideBeanLight> {
	public static final String SELECT_PEPTIDE_TO_LOAD_PSMS_TEXT = "Select one peptide to load PSMs";
	public static final String NO_DATA_FROM_THIS_PEPTIDE = "There is not any PSM information for that selection";
	public static final String WAIT_TO_BE_LOADED = "Please wait for Peptides to be loaded...";
	private final ShowHiddePanel peptideLoaderFromProjects;

	public PeptideTablePanel(String sessionID, String emptyLabel, ShowHiddePanel showhiddePSMPanel,
			AbstractAsyncDataProvider<PeptideBeanLight> asyncDataListProvider, boolean multipleSelectionModel,
			QueryPanel queryPanel) {
		this(sessionID, emptyLabel, showhiddePSMPanel, asyncDataListProvider, multipleSelectionModel, "peptide table",
				queryPanel);
	}

	public PeptideTablePanel(String sessionID, String emptyLabel, ShowHiddePanel showhiddePSMPanel,
			AbstractAsyncDataProvider<PeptideBeanLight> asyncDataListProvider, boolean multipleSelectionModel,
			String tableTitle, QueryPanel queryPanel) {
		super(sessionID, emptyLabel, asyncDataListProvider, multipleSelectionModel, tableTitle, queryPanel);
		peptideLoaderFromProjects = showhiddePSMPanel;

	}

	public void setButtonToLoadWholeSetOfPeptides() {
		final FlexTable panel = new FlexTable();
		// TODO

		setEmptyTableWidget(panel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.scripps.yates.client.gui.components.AbstractDataTable#
	 * createColumnManager()
	 */
	@Override
	protected AbstractColumnManager<PeptideBeanLight> createColumnManager(
			FooterManager<PeptideBeanLight> footerManager) {
		return new PeptideColumnManager(footerManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.scripps.yates.client.gui.components.AbstractDataTable#
	 * createFooterManager()
	 */
	@Override
	protected FooterManager<PeptideBeanLight> createFooterManager(MyDataGrid<PeptideBeanLight> dataGrid) {
		return new PeptideFooterManager(dataGrid);
	}

	@Override
	public MyDataGrid<PeptideBeanLight> makeDataGrid() {
		/**
		 * The key provider that provides the unique ID of a PeptideBean.
		 */
		final ProvidesKey<PeptideBeanLight> KEY_PROVIDER = new ProvidesKey<PeptideBeanLight>() {
			@Override
			public Object getKey(PeptideBeanLight item) {
				return item == null ? null : item.getSequence();
			}
		};
		final MyDataGrid<PeptideBeanLight> dataGrid = new MyDataGrid<PeptideBeanLight>(KEY_PROVIDER, tableName);

		return dataGrid;
	}

	@Override
	protected SimplePager makePager() {
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		final SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PEPTIDE_DEFAULT_PAGE_SIZE * 5, true);
		simplePager.setPageSize(SharedConstants.PEPTIDE_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	public void hiddePeptidePanel() {
		if (peptideLoaderFromProjects != null) {
			peptideLoaderFromProjects.hiddePanel();
		}
	}

	public void showPeptidePanel() {
		if (peptideLoaderFromProjects != null) {
			peptideLoaderFromProjects.showPanel();
		}
	}

	@Override
	public List<ColumnWithVisibility> getItemDefaultView(DefaultView defaultview) {
		return defaultview.getPeptideDefaultView();
	}

	@Override
	public ORDER getItemOrder(DefaultView defaultView) {
		return defaultView.getPeptideOrder();
	}

	@Override
	public String getSortingScore(DefaultView defaultView) {
		return defaultView.getPeptideSortingScore();
	}

	@Override
	public ColumnName getSortedBy(DefaultView defaultView) {
		return defaultView.getPeptidesSortedBy();
	}

	/**
	 * Set the asynchronous data provider to be ready or not to ask for data every
	 * time refresh is called.
	 * 
	 * @param isReadyToShowData
	 */
	public void setReadyToShowData(boolean isReadyToShowData) {
		final AsyncDataProvider<PeptideBeanLight> asyncDataProvider = getAsyncDataProvider();
		if (asyncDataProvider instanceof AsyncPeptideBeanListDataProvider) {
			final AsyncPeptideBeanListDataProvider asyncPeptideBeanListDataProvider = (AsyncPeptideBeanListDataProvider) asyncDataProvider;
			asyncPeptideBeanListDataProvider.setReadyForProvidingData(isReadyToShowData);
		}
	}
}