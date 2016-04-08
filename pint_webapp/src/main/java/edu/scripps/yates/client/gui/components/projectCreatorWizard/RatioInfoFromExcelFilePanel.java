package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.excel.util.ExcelUtils;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import edu.scripps.yates.shared.model.projectCreator.ExcelDataReference;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatioDescriptorTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class RatioInfoFromExcelFilePanel extends Composite
		implements ReferencesDataObject, ContainsImportJobID, RepresentsObject<ExcelAmountRatioTypeBean> {

	private Button checkButton;

	private FileNameWithTypeBean excelFileWithFormatBean;
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();
	private int importJobID;
	private int numTestCases = 100;
	private FileTypeBean excelFileBean;
	private ScrollPanel scrollPanelTable;
	private ExcelRatioPanel ratioPanel;
	private ListBox msRunComboBox;

	private ScrollPanel scrollPanelTable2;
	private final MyExcelColumnCellTable<Pair<String, ExcelAmountRatioTypeBean>> ratioScoresTable = new MyExcelColumnCellTable<Pair<String, ExcelAmountRatioTypeBean>>();

	private MyHeader ratioScoreHeader;
	private CustomColumn<Pair<String, ExcelAmountRatioTypeBean>> ratioScoresColumn;

	private final String sessionID;

	private ExcelAmountRatioTypeBean excelRatioTypeBean;

	private final RatioDescriptorTypeBean ratioDescriptorTypeBean;

	public RatioInfoFromExcelFilePanel(String sessionID, int importJobID,
			RatioDescriptorTypeBean ratioDescriptorTypeBean) {
		this.sessionID = sessionID;
		this.importJobID = importJobID;
		excelRatioTypeBean = new ExcelAmountRatioTypeBean();
		this.ratioDescriptorTypeBean = ratioDescriptorTypeBean;
		initPanel(excelRatioTypeBean);

		updateExcelRatioTypeObjectFromRatioDescriptor();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public RatioInfoFromExcelFilePanel(String sessionID, int importJobID, ExcelAmountRatioTypeBean excelRatioBean,
			RatioDescriptorTypeBean ratioDescriptorTypeBean) {
		this.sessionID = sessionID;
		this.importJobID = importJobID;
		excelRatioTypeBean = excelRatioBean;
		initPanel(excelRatioBean);
		this.ratioDescriptorTypeBean = ratioDescriptorTypeBean;
		updateExcelRatioTypeObjectFromRatioDescriptor();

	}

	private void initPanel(ExcelAmountRatioTypeBean excelRatioBean) {
		ScrollPanel scroll = new ScrollPanel();
		scroll.setTouchScrollingDisabled(false);

		initWidget(scroll);
		// scroll.setSize("1029px", "700px");
		scroll.setSize("100%", "100%");

		FlexTable flexTable = new FlexTable();
		flexTable.setBorderWidth(0);
		scroll.setWidget(flexTable);
		flexTable.setSize("900px", "500px");

		Grid grid = new Grid(4, 2);
		grid.setCellPadding(5);
		grid.setBorderWidth(0);
		flexTable.setWidget(0, 0, grid);
		grid.setStyleName("verticalComponent");
		grid.setHeight("100%");

		Label lblMsRun = new Label("MS Run:");
		grid.setWidget(2, 0, lblMsRun);

		msRunComboBox = new ListBox();
		msRunComboBox.setVisibleItemCount(4);
		msRunComboBox.setMultipleSelect(true);
		grid.setWidget(2, 1, msRunComboBox);

		InlineLabel nlnlblCheckRemoteData = new InlineLabel("Check data:");
		grid.setWidget(3, 0, nlnlblCheckRemoteData);

		checkButton = new Button("check");
		grid.setWidget(3, 1, checkButton);
		checkButton.setText("check");
		grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);

		FlowPanel flowPanel = new FlowPanel();

		flowPanel.setStyleName("IdentificationInfoRemoteFilePanel");
		flexTable.setWidget(2, 0, flowPanel);
		flowPanel.setSize("100%", "100%");

		scrollPanelTable = new ScrollPanel();
		scrollPanelTable.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		scrollPanelTable.setWidth("400px");

		Grid grid_1 = new Grid(1, 3);
		grid_1.setBorderWidth(0);
		grid_1.setCellPadding(10);
		flowPanel.add(grid_1);
		grid_1.setSize("100%", "240px");
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		grid_1.setWidget(0, 1, scrollPanelTable);
		ratioPanel = new ExcelRatioPanel(sessionID, excelFileBean, excelRatioBean, ratioDescriptorTypeBean);
		ratioPanel.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		grid_1.setWidget(0, 0, ratioPanel);

		final Label ratioValuesLabel = new Label("Ratio values");
		ratioValuesLabel.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		FlowPanel flowPanel2 = new FlowPanel();
		flowPanel2.setStyleName("verticalComponent");
		flowPanel2.add(ratioValuesLabel);
		flowPanel2.add(ratioPanel.getTable());
		scrollPanelTable.setWidget(flowPanel2);

		scrollPanelTable2 = new ScrollPanel();
		grid_1.setWidget(0, 2, scrollPanelTable2);
		scrollPanelTable2.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		scrollPanelTable2.setWidth("400px");
		final Label scoreValuesLabel = new Label("Score values");
		scoreValuesLabel.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		FlowPanel flowPanel3 = new FlowPanel();
		flowPanel3.setStyleName("verticalComponent");
		flowPanel3.add(scoreValuesLabel);
		flowPanel3.add(ratioScoresTable);
		scrollPanelTable2.setWidget(flowPanel3);
		grid_1.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		grid_1.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		grid_1.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);

		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);

		addHandlers();
		ratioScoresTable.addColumn(getRatioScoreColumn(), getRatioScoreHeader());
		// register as a listener of msruns
		ProjectCreatorRegister.registerAsListenerByObjectClass(MsRunTypeBean.class, this);
		// listen to ratioDescriptorTypeBean
		ProjectCreatorRegister.registerAsListenerByObjectClass(RatioDescriptorTypeBean.class, this);
		updateGUIFromObjectData();

	}

	private MyHeader getRatioScoreHeader() {
		if (ratioScoreHeader == null) {
			final SuggestBox scoreNameTextBox = ratioPanel.getScoreNameTextBox();
			scoreNameTextBox.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					ratioScoresTable.getDataProvider().refresh();
				}
			});
			ratioScoreHeader = new MyHeader(scoreNameTextBox);
		}
		return ratioScoreHeader;
	}

	private CustomColumn<Pair<String, ExcelAmountRatioTypeBean>> getRatioScoreColumn() {
		if (ratioScoresColumn == null) {
			ratioScoresColumn = new CustomColumn<Pair<String, ExcelAmountRatioTypeBean>>("Ratio values") {
				@Override
				public String getValue(Pair<String, ExcelAmountRatioTypeBean> pair) {
					final String rawValue = pair.getFirstElement();
					final ExcelAmountRatioTypeBean secondElement = pair.getSecondElement();
					final ScoreTypeBean ratioScore = secondElement.getRatioScore();
					if (ratioScore != null) {
						return rawValue;
					}
					return null;
				}
			};
		}
		return ratioScoresColumn;
	}

	/**
	 * Calls to getExcelFileBean using the web service
	 */
	private void getExcelFileBean() {
		FileNameWithTypeBean found = null;
		for (FileNameWithTypeBean fileNameWithTypeBean : SharedDataUtils.excelBeansByExcelFileWithFormatBeansMap
				.keySet()) {
			if (fileNameWithTypeBean.equals(excelFileWithFormatBean)) {
				found = fileNameWithTypeBean;
				break;
			}
		}
		if (found != null) {
			setExcelFileBean(SharedDataUtils.excelBeansByExcelFileWithFormatBeansMap.get(found));
		} else {
			service.getExcelFileBean(sessionID, importJobID, excelFileWithFormatBean,
					new AsyncCallback<FileTypeBean>() {

						@Override
						public void onFailure(Throwable caught) {
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(FileTypeBean result) {
							setExcelFileBean(result);
						}
					});
		}
	}

	private void updateExcelRatioTypeObjectFromRatioDescriptor() {

		// update the scoreType, and the Column ref for the ratio values:
		ratioPanel.updateRepresentedObject();
		String id = null;
		String condition1ID = null;
		String condition2ID = null;
		if (ratioDescriptorTypeBean != null) {
			id = ratioDescriptorTypeBean.getId();
			if (ratioDescriptorTypeBean.getCondition1() != null)
				condition1ID = ratioDescriptorTypeBean.getCondition1().getId();
			if (ratioDescriptorTypeBean.getCondition2() != null)
				condition2ID = ratioDescriptorTypeBean.getCondition2().getId();
		}
		ratioPanel.getObject().setName(id);
		ratioPanel.getObject().setNumerator(condition1ID);
		ratioPanel.getObject().setDenominator(condition2ID);
		this.updateGUIFromObjectData();
	}

	private void addHandlers() {

		// if check button is clicked, then start the check
		checkButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetData();
				startCheck();
			}
		});
		ratioPanel.addExcelColumnsChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				resetData();
				startCheck();
			}
		});

		// // add panels hover handlers
		// addHoverHandlers();

	}

	/**
	 * Sets the object {@link ExcelFileBean} and sets that object to all the
	 * panels that depends on it
	 *
	 * @param excelFileBean
	 */
	private boolean setExcelFileBean(FileTypeBean excelFileBean2) {
		boolean different = false;
		if (excelFileWithFormatBean != null && !excelFileWithFormatBean.equals(excelFileBean2)
				|| excelFileWithFormatBean == null && excelFileBean2 != null) {
			different = true;
			excelFileBean = excelFileBean2;
			ratioPanel.setExcelFileBean(excelFileBean2);
		}
		return different;
	}

	protected void startCheck() {
		// disable start check button
		checkButton.setEnabled(false);

		retrieveData();

	}

	private void retrieveData() {
		loadRatiosValuesRandomData(ratioPanel.getExcelColumnRefPanel(), ratioPanel.getTable());
		if (ratioPanel.getExcelColumnRefPanelForScoreValues().getColumnRef() != null) {
			loadRatiosValuesRandomData(ratioPanel.getExcelColumnRefPanelForScoreValues(), ratioScoresTable);
		} else {
			ratioScoresTable.clearData();
		}
	}

	public void loadRatiosValuesRandomData(ExcelColumnRefPanel excelColumnRefPanel,
			final MyExcelColumnCellTable<Pair<String, ExcelAmountRatioTypeBean>> tableRatioValues) {

		if (tableRatioValues.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);

			service.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							checkButton.setEnabled(true);
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							checkButton.setEnabled(true);
							tableRatioValues.clearData();

							ratioPanel.updateRepresentedObject();
							for (String value : result) {
								Pair<String, ExcelAmountRatioTypeBean> pair = new Pair<String, ExcelAmountRatioTypeBean>(
										value, ratioPanel.getObject());
								tableRatioValues.addData(pair);
							}
						}
					});
		} else {
			checkButton.setEnabled(true);
		}
	}

	public void loadProteinRatiosRandomData(final ExcelRatioPanel ratioPanel) {
		final MyExcelColumnCellTable<Pair<String, ExcelAmountRatioTypeBean>> table = ratioPanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = ratioPanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);

			service.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							checkButton.setEnabled(true);
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							checkButton.setEnabled(true);
							table.clearData();

							ratioPanel.updateRepresentedObject();
							for (String value : result) {
								Pair<String, ExcelAmountRatioTypeBean> pair = new Pair<String, ExcelAmountRatioTypeBean>(
										value, ratioPanel.getObject());
								table.addData(pair);
							}
						}
					});
		} else {
			checkButton.setEnabled(true);
		}
	}

	/**
	 * Creates an {@link ExcelDataReference} containing information from
	 * {@link FileNameWithTypeBean} and {@link ExcelColumnRefPanel}
	 *
	 * @param excelFileWithFormatBean2
	 * @param excelColumnRefPanel
	 * @return
	 */
	private ExcelDataReference getExcelDataReference(FileNameWithTypeBean excelFileWithFormatBean2,
			ExcelColumnRefPanel excelColumnRefPanel) {
		ExcelDataReference excelDataReference = new ExcelDataReference();
		excelDataReference.setColumnId(excelColumnRefPanel.getColumnRef());
		excelDataReference.setExcelFileNameWithTypeBean(excelFileWithFormatBean);
		excelDataReference.setSheetId(excelColumnRefPanel.getSheetRef());
		return excelDataReference;
	}

	/**
	 * Sets the number of cases that is going to test. In this case, protein
	 * accessions.
	 *
	 * @param numTestCases
	 *            the numTestCases to set
	 */
	public void setNumTestCases(int numTestCases) {
		this.numTestCases = numTestCases;
	}

	/**
	 * @param remoteFileBean
	 *            the remoteFileWithType to set
	 */
	public boolean setRemoteFileBean(FileNameWithTypeBean remoteFileBean) {
		boolean different = false;
		if (excelFileWithFormatBean != null && !excelFileWithFormatBean.equals(remoteFileBean)
				|| excelFileWithFormatBean == null && remoteFileBean != null)
			different = true;
		excelFileWithFormatBean = remoteFileBean;
		if (different)
			getExcelFileBean();
		return different;
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		ProjectCreatorWizardUtil.updateCombo(msRunComboBox, MsRunTypeBean.class);
	}

	public ExcelAmountRatioTypeBean getProteinExcelRatioTypeBean() {
		if (ratioPanel != null)
			ratioPanel.updateRepresentedObject();
		if (ratioPanel.getAggregationLevel() == SharedAggregationLevel.PROTEIN) {
			final ExcelAmountRatioTypeBean object = ratioPanel.getObject();
			if (msRunComboBox.getSelectedIndex() != -1) {
				object.setMsRunRef(ProjectCreatorWizardUtil.getSelectedItemTextsFromListBox(msRunComboBox,
						ExcelUtils.MULTIPLE_ITEM_SEPARATOR));
			}
			return object;
		}
		return null;
	}

	public ExcelAmountRatioTypeBean getPSMExcelRatioTypeBean() {
		if (ratioPanel != null)
			ratioPanel.updateRepresentedObject();
		if (ratioPanel.getAggregationLevel() == SharedAggregationLevel.PSM) {
			final ExcelAmountRatioTypeBean object = ratioPanel.getObject();
			if (msRunComboBox.getSelectedIndex() != -1) {
				object.setMsRunRef(ProjectCreatorWizardUtil.getSelectedItemTextsFromListBox(msRunComboBox, ","));
			}
			return object;
		}
		return null;
	}

	public ExcelAmountRatioTypeBean getPeptideExcelRatioTypeBean() {
		if (ratioPanel != null)
			ratioPanel.updateRepresentedObject();
		if (ratioPanel.getAggregationLevel() == SharedAggregationLevel.PEPTIDE) {
			final ExcelAmountRatioTypeBean object = ratioPanel.getObject();
			if (msRunComboBox.getSelectedIndex() != -1) {
				object.setMsRunRef(ProjectCreatorWizardUtil.getSelectedItemTextsFromListBox(msRunComboBox, ","));
			}
			return object;
		}
		return null;
	}

	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;

	}

	public void resetData() {
		ratioPanel.resetData();
		ratioScoresTable.clearData();
	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}

	@Override
	public ExcelAmountRatioTypeBean getObject() {
		return ratioPanel.getObject();
	}

	@Override
	public void updateRepresentedObject() {
		ratioPanel.updateRepresentedObject();

		if (ratioDescriptorTypeBean != null) {
			ratioPanel.getRepresentedObject().setNumerator(ratioDescriptorTypeBean.getCondition1().getId());
			ratioPanel.getRepresentedObject().setDenominator(ratioDescriptorTypeBean.getCondition2().getId());
		}

	}

	@Override
	public void updateGUIFromObjectData(ExcelAmountRatioTypeBean dataObject) {
		excelRatioTypeBean = dataObject;
		ratioPanel.updateGUIFromObjectData(dataObject);
		updateGUIFromObjectData();
		updateGUIReferringToDataObjects();
	}

	public void setAggregationLevel(SharedAggregationLevel aggregrationLevel) {
		ratioPanel.setAggregationLevel(aggregrationLevel);
	}

	public SharedAggregationLevel getAggregationLevel() {
		return ratioPanel.getAggregationLevel();
	}

	@Override
	public void updateGUIFromObjectData() {
		ProjectCreatorWizardUtil.updateCombo(msRunComboBox, MsRunTypeBean.class, false);
		if (excelRatioTypeBean != null && excelRatioTypeBean.getMsRunRef() != null
				&& !"".equals(excelRatioTypeBean.getMsRunRef())) {
			ProjectCreatorWizardUtil.selectInCombo(msRunComboBox, excelRatioTypeBean.getMsRunRef(),
					ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
		}

		ratioPanel.updateGUIFromObjectData();
	}
}
