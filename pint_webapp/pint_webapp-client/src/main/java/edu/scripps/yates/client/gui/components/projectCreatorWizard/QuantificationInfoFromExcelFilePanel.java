package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.google.gwt.user.client.ui.SimplePanel;

import edu.scripps.yates.ImportWizardService;
import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.projectCreator.ExcelDataReference;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class QuantificationInfoFromExcelFilePanel extends Composite
		implements ReferencesDataObject, ContainsImportJobID, RepresentsObject<QuantificationExcelTypeBean> {
	protected static final String YES = "yes";
	protected static final String NO = "no";
	protected static final String SEPARATOR = "\n";
	private final Button checkButton;
	private FileNameWithTypeBean excelFileWithFormatBean;
	private final ImportWizardServiceAsync service = GWT.create(ImportWizardService.class);
	private int importJobID;
	private int numTestCases = 100;
	private final FlowPanel flowPanelProteinQuantifications;
	private final FlowPanel flowPanelPeptideQuantifications;

	private final IncrementableTextBox proteinQuantificationValuesIncrementableTextBox;
	private final IncrementableTextBox peptideQuantificationValuesIncrementableTextBox;
	private final IncrementableTextBox psmQuantificationValuesIncrementableTextBox;

	private final FlowPanel flowPanel_2;
	private final Label lblCustomizableOptionsFor;
	private final SimplePanel flowPanelOptions;
	private FileTypeBean excelFileBean;
	private final List<AmountPanel> proteinAmountPanels = new ArrayList<AmountPanel>();
	private final List<AmountPanel> peptideAmountPanels = new ArrayList<AmountPanel>();
	private final List<AmountPanel> psmAmountPanels = new ArrayList<AmountPanel>();

	private QuantificationExcelTypeBean excelQuantificationType = new QuantificationExcelTypeBean();
	private final FlowPanel flowPanel_3;
	private final Label lblMoveTheMouse;
	private final SimplePanel simplePanelTable;
	private final Grid grid_2;
	private final Grid grid_3;
	private final FlowPanel flowPanel_4;
	private final Label lblQuantificationValuesFor;
	private final FlowPanel flowPanel_5;
	private final Label lblQuantificationValuesFor_1;
	private final Label lblMsRun;
	private final ListBox msRunComboBox;
	private ChangeHandler startCheckChangeHandler;
	private ValueChangeHandler<Boolean> startCheckValueChangeHandler;
	private final String sessionID;
	private final FlowPanel flowPanel_7;
	private final Label lblQuantificationValuesAt;
	private final FlowPanel flowPanelPSMQuantifications;
	private final Grid grid_4;
	private final Label lblPsmQuantificationValues;

	public QuantificationInfoFromExcelFilePanel(String sessionID, int importJobID) {
		this.sessionID = sessionID;

		this.importJobID = importJobID;

		FlexTable flexTable = new FlexTable();
		initWidget(flexTable);
		flexTable.setBorderWidth(0);
		flexTable.setSize("900px", "500px");

		FlexTable grid = new FlexTable();
		grid.setCellPadding(5);
		grid.setBorderWidth(0);
		flexTable.setWidget(0, 0, grid);
		grid.setStyleName("verticalComponent");
		grid.setSize("400px", "100%");

		lblMsRun = new Label("MS Run:");
		grid.setWidget(0, 0, lblMsRun);

		msRunComboBox = new ListBox();
		msRunComboBox.setMultipleSelect(true);
		msRunComboBox.setVisibleItemCount(4);
		grid.setWidget(0, 1, msRunComboBox);

		InlineLabel nlnlblCheckRemoteData = new InlineLabel("Check data:");
		grid.setWidget(1, 0, nlnlblCheckRemoteData);

		checkButton = new Button("check");
		grid.setWidget(1, 1, checkButton);
		checkButton.setText("check");
		grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		FlowPanel flowPanel = new FlowPanel();

		flowPanel.setStyleName("IdentificationInfoRemoteFilePanel");
		flexTable.setWidget(2, 0, flowPanel);
		flowPanel.setSize("100%", "100%");

		FlowPanel righFlowPanel = new FlowPanel();
		FlowPanel flowPanel_6 = new FlowPanel();
		flowPanel_6.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		righFlowPanel.add(flowPanel_6);
		Label lblDataTable = new Label("Data table:");
		flowPanel_6.add(lblDataTable);

		simplePanelTable = new SimplePanel();
		simplePanelTable.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		simplePanelTable.setSize("", "450px");
		righFlowPanel.add(simplePanelTable);

		FlowPanel flowPanelRight = new FlowPanel();
		flowPanelRight.setWidth("400px");

		Grid grid_1 = new Grid(1, 3);
		grid_1.setBorderWidth(0);
		grid_1.setCellPadding(10);
		flowPanel.add(grid_1);
		grid_1.setSize("100%", "240px");

		FlowPanel flowPanelLeft = new FlowPanel();
		grid_1.setWidget(0, 0, flowPanelLeft);
		flowPanelLeft.setWidth("400px");

		flowPanel_3 = new FlowPanel();
		flowPanel_3.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		flowPanelLeft.add(flowPanel_3);

		lblMoveTheMouse = new Label("Move the mouse over these options to configure:");
		flowPanel_3.add(lblMoveTheMouse);

		flowPanelOptions = new SimplePanel();
		flowPanelOptions.setStyleName("IdentificationInfoFromExcelEmptyPanel");

		flowPanel_4 = new FlowPanel();
		flowPanel_4.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanel_4);

		lblQuantificationValuesFor = new Label("Quantification values at protein level:");
		flowPanel_4.add(lblQuantificationValuesFor);

		flowPanelProteinQuantifications = new FlowPanel();
		flowPanelProteinQuantifications.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelProteinQuantifications);

		grid_2 = new Grid(1, 2);
		flowPanelProteinQuantifications.add(grid_2);
		Label lblProteinQuantificationValues = new Label("Protein quantification values (amounts):");
		grid_2.setWidget(0, 0, lblProteinQuantificationValues);

		proteinQuantificationValuesIncrementableTextBox = new IncrementableTextBox(0);
		grid_2.setWidget(0, 1, proteinQuantificationValuesIncrementableTextBox);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		proteinQuantificationValuesIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				final AmountTypeBean newAmountTypeBean = new AmountTypeBean();
				excelQuantificationType.getProteinAmounts().add(newAmountTypeBean);
				createNewProteinQuantificationValue(newAmountTypeBean);

			}
		});
		proteinQuantificationValuesIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// create a new MyExcelSectionFlowPanel and add it to
				// the flowPanelproteinAnnotation
				if (!proteinAmountPanels.isEmpty()) {
					AmountPanel proteinAmountPanel = proteinAmountPanels.get(proteinAmountPanels.size() - 1);
					excelQuantificationType.getProteinAmounts().remove(proteinAmountPanel.getRepresentedObject());
					proteinAmountPanel.removeFromParent();
					if (proteinAmountPanel.getRelatedExcelSectionFlowPanel() != null) {
						proteinAmountPanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					proteinAmountPanels.remove(proteinAmountPanel);
				}
			}
		});
		FlowPanel flowPanel_1 = new FlowPanel();
		flowPanel_1.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanel_1);

		flowPanel_5 = new FlowPanel();
		flowPanel_5.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanel_5);

		lblQuantificationValuesFor_1 = new Label("Quantification values at peptide level:");
		flowPanel_5.add(lblQuantificationValuesFor_1);

		flowPanelPeptideQuantifications = new FlowPanel();
		flowPanelPeptideQuantifications.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelPeptideQuantifications);

		grid_3 = new Grid(1, 2);
		flowPanelPeptideQuantifications.add(grid_3);

		Label label2 = new Label("Peptide quantification values (amounts):");
		grid_3.setWidget(0, 0, label2);
		peptideQuantificationValuesIncrementableTextBox = new IncrementableTextBox(0);
		grid_3.setWidget(0, 1, peptideQuantificationValuesIncrementableTextBox);
		grid_3.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);

		peptideQuantificationValuesIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				final AmountTypeBean newAmountTypeBean = new AmountTypeBean();
				excelQuantificationType.getPeptideAmounts().add(newAmountTypeBean);
				createNewPeptideQuantificationValue(newAmountTypeBean);

			}
		});
		peptideQuantificationValuesIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// create a new MyExcelSectionFlowPanel and add it to
				// the flowPanelproteinAnnotation
				if (!peptideAmountPanels.isEmpty()) {
					AmountPanel peptideAmountPanel = peptideAmountPanels.get(peptideAmountPanels.size() - 1);
					excelQuantificationType.getPeptideAmounts().remove(peptideAmountPanel.getRepresentedObject());
					peptideAmountPanel.removeFromParent();
					if (peptideAmountPanel.getRelatedExcelSectionFlowPanel() != null) {
						peptideAmountPanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					peptideAmountPanels.remove(peptideAmountPanel);
				}
			}
		});

		flowPanel_7 = new FlowPanel();
		flowPanel_7.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanel_7);

		lblQuantificationValuesAt = new Label("Quantification values at PSM level:");
		flowPanel_7.add(lblQuantificationValuesAt);

		flowPanelPSMQuantifications = new FlowPanel();
		flowPanelPSMQuantifications.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelPSMQuantifications);

		grid_4 = new Grid(1, 2);
		flowPanelPSMQuantifications.add(grid_4);

		lblPsmQuantificationValues = new Label("PSM quantification values (amounts):");
		grid_4.setWidget(0, 0, lblPsmQuantificationValues);

		psmQuantificationValuesIncrementableTextBox = new IncrementableTextBox(0);
		grid_4.setWidget(0, 1, psmQuantificationValuesIncrementableTextBox);
		grid_4.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		psmQuantificationValuesIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final AmountTypeBean newAmountTypeBean = new AmountTypeBean();
				excelQuantificationType.getPsmAmounts().add(newAmountTypeBean);
				createNewPSMQuantificationValue(newAmountTypeBean);
			}
		});
		psmQuantificationValuesIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!psmAmountPanels.isEmpty()) {
					AmountPanel psmAmountPanel = psmAmountPanels.get(psmAmountPanels.size() - 1);
					excelQuantificationType.getPsmAmounts().remove(psmAmountPanel.getRepresentedObject());
					psmAmountPanel.removeFromParent();
					if (psmAmountPanel.getRelatedExcelSectionFlowPanel() != null) {
						psmAmountPanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					psmAmountPanels.remove(psmAmountPanel);
				}

			}
		});

		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);

		grid_1.setWidget(0, 1, flowPanelRight);

		flowPanel_2 = new FlowPanel();
		flowPanel_2.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		flowPanelRight.add(flowPanel_2);

		lblCustomizableOptionsFor = new Label("Customizable options for the selected information:");
		flowPanel_2.add(lblCustomizableOptionsFor);

		flowPanelRight.add(flowPanelOptions);
		flowPanelOptions.setHeight("");
		grid_1.setWidget(0, 2, righFlowPanel);
		righFlowPanel.setWidth("400px");
		grid_1.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		grid_1.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		grid_1.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);

		addHandlers();
		updateExcelIdentificationTypeObjectFromGUIOptions();

		// register this as a listener of msRuns
		ProjectCreatorRegister.registerAsListenerByObjectClass(MsRunTypeBean.class, this);

		updateGUIReferringToDataObjects();

	}

	private void createNewPSMQuantificationValue(AmountTypeBean amountTypeBean) {

		AmountPanel psmAmountPanel = new AmountPanel(excelFileBean, "PSM amount " + (psmAmountPanels.size() + 1),
				amountTypeBean);
		psmAmountPanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		psmAmountPanel.addCombinationTypeChangeHandler(getStartCheckChangeHandler());
		psmAmountPanel.addIsACombinationCheckBox(getStartCheckValueChangeHandler());
		psmAmountPanel.addAmountTypeChangeHandler(getStartCheckChangeHandler());
		psmAmountPanels.add(psmAmountPanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel("PSM amount " + psmAmountPanels.size(),
				flowPanelOptions, psmAmountPanel, simplePanelTable);
		flowPanelPSMQuantifications.add(panel);

	}

	private void createNewPeptideQuantificationValue(AmountTypeBean amountTypeBean) {

		AmountPanel peptideAmountPanel = new AmountPanel(excelFileBean,
				"Peptide amount " + (peptideAmountPanels.size() + 1), amountTypeBean);
		peptideAmountPanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		peptideAmountPanel.addCombinationTypeChangeHandler(getStartCheckChangeHandler());
		peptideAmountPanel.addIsACombinationCheckBox(getStartCheckValueChangeHandler());
		peptideAmountPanel.addAmountTypeChangeHandler(getStartCheckChangeHandler());
		peptideAmountPanels.add(peptideAmountPanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel("Peptide amount " + peptideAmountPanels.size(),
				flowPanelOptions, peptideAmountPanel, simplePanelTable);
		flowPanelPeptideQuantifications.add(panel);

	}

	private void createNewProteinQuantificationValue(AmountTypeBean amountTypeBean) {

		AmountPanel proteinAmountPanel = new AmountPanel(excelFileBean,
				"Protein amount " + (proteinAmountPanels.size() + 1), amountTypeBean);
		proteinAmountPanel.addAmountTypeChangeHandler(getStartCheckChangeHandler());
		proteinAmountPanel.addCombinationTypeChangeHandler(getStartCheckChangeHandler());
		proteinAmountPanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		proteinAmountPanel.addIsACombinationCheckBox(getStartCheckValueChangeHandler());
		proteinAmountPanels.add(proteinAmountPanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel("Protein amount " + proteinAmountPanels.size(),
				flowPanelOptions, proteinAmountPanel, simplePanelTable);
		flowPanelProteinQuantifications.add(panel);

	}

	private ValueChangeHandler<Boolean> getStartCheckValueChangeHandler() {
		if (startCheckValueChangeHandler == null)
			startCheckValueChangeHandler = new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					startCheck();
				}
			};
		return startCheckValueChangeHandler;
	}

	/**
	 * Calls to getExcelFileBean using the web service
	 */
	private void getExcelFileBean() {
		FileNameWithTypeBean found = null;
		for (FileNameWithTypeBean fileNameWithTypeBean : SharedDataUtil.excelBeansByExcelFileWithFormatBeansMap
				.keySet()) {
			if (fileNameWithTypeBean.equals(excelFileWithFormatBean)) {
				found = fileNameWithTypeBean;
				break;
			}
		}

		if (found != null) {
			setExcelFileBean(SharedDataUtil.excelBeansByExcelFileWithFormatBeansMap.get(found));
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
							SharedDataUtil.excelBeansByExcelFileWithFormatBeansMap.put(excelFileWithFormatBean,
									result);
						}
					});
		}

	}

	private void updateExcelIdentificationTypeObjectFromGUIOptions() {
		for (AmountPanel proteinAmountPanel : proteinAmountPanels) {
			proteinAmountPanel.updateRepresentedObject();
		}
		for (AmountPanel peptideAmountPanel : peptideAmountPanels) {
			peptideAmountPanel.updateRepresentedObject();
		}
		for (AmountPanel psmAmountPanel : psmAmountPanels) {
			psmAmountPanel.updateRepresentedObject();
		}
		if (msRunComboBox.getSelectedIndex() != -1) {
			excelQuantificationType.setMsRunRef(ProjectCreatorWizardUtil.getSelectedItemTextsFromListBox(msRunComboBox,
					SharedConstants.MULTIPLE_ITEM_SEPARATOR));
		}
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

	}

	/**
	 * Sets the object {@link FileTypeBean} and sets that object to all the panels
	 * that depends on it
	 *
	 * @param excelFileBean
	 */
	private void setExcelFileBean(FileTypeBean excelFileBean) {
		this.excelFileBean = excelFileBean;

		for (AmountPanel proteinAmountPanel : proteinAmountPanels) {
			proteinAmountPanel.setExcelFileBean(excelFileBean);
		}
		for (AmountPanel peptideAmountPanel : psmAmountPanels) {
			peptideAmountPanel.setExcelFileBean(excelFileBean);
		}
	}

	protected void startCheck() {
		// disable start check button
		checkButton.setEnabled(false);

		retrieveData();

	}

	/**
	 * Returns a list of String representing the accessions of the proteins of the
	 * data source.<br>
	 * If the list is not loaded, ask for it to the server in a synchonous way
	 *
	 * @return
	 */
	private void retrieveData() {

		for (int index = 0; index < proteinAmountPanels.size(); index++) {
			loadProteinAmountRandomData(index, proteinAmountPanels.get(index));
		}
		for (int index = 0; index < peptideAmountPanels.size(); index++) {
			loadPeptideAmountRandomData(index, peptideAmountPanels.get(index));
		}
		for (int index = 0; index < psmAmountPanels.size(); index++) {
			loadPSMAmountRandomData(index, psmAmountPanels.get(index));
		}

	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link PsmSequenceTypeBean} object created from the GUI
	 *
	 * @param psmAmountPanel
	 *
	 */
	public void loadPSMAmountRandomData(final int index, AmountPanel psmAmountPanel) {
		final MyExcelColumnCellTable<Pair<String, AmountTypeBean>> table = psmAmountPanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = psmAmountPanel.getExcelColumnRefPanel();
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
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								Pair<String, AmountTypeBean> pair = new Pair<String, AmountTypeBean>(value,
										excelQuantificationType.getPsmAmounts().get(index));
								table.addData(pair);
							}
						}
					});
		} else {
			checkButton.setEnabled(true);
			updateExcelIdentificationTypeObjectFromGUIOptions();
			table.redraw();
		}
	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ScoreTypeBean} object created from the GUI
	 *
	 * @param index              : index of the {@link ScoreTypeBean} of protein
	 *                           scores
	 * @param proteinAmountPanel
	 *
	 */
	private void loadProteinAmountRandomData(final int index, AmountPanel proteinAmountPanel) {
		final MyExcelColumnCellTable<Pair<String, AmountTypeBean>> table = proteinAmountPanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = proteinAmountPanel.getExcelColumnRefPanel();
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
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								final AmountTypeBean amountType = excelQuantificationType.getProteinAmounts()
										.get(index);
								Pair<String, AmountTypeBean> pair = new Pair<String, AmountTypeBean>(value, amountType);
								table.addData(pair);
							}
						}
					});
		} else {
			checkButton.setEnabled(true);
			updateExcelIdentificationTypeObjectFromGUIOptions();
			table.redraw();
		}

	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ScoreTypeBean} object created from the GUI
	 *
	 * @param index              : index of the {@link ScoreTypeBean} of protein
	 *                           scores
	 * @param peptideAmountPanel
	 *
	 */
	private void loadPeptideAmountRandomData(final int index, AmountPanel peptideAmountPanel) {
		final MyExcelColumnCellTable<Pair<String, AmountTypeBean>> table = peptideAmountPanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = peptideAmountPanel.getExcelColumnRefPanel();
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
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								final AmountTypeBean amountType = excelQuantificationType.getPeptideAmounts()
										.get(index);
								Pair<String, AmountTypeBean> pair = new Pair<String, AmountTypeBean>(value, amountType);
								table.addData(pair);
							}
						}
					});
		} else {
			checkButton.setEnabled(true);
			updateExcelIdentificationTypeObjectFromGUIOptions();
			table.redraw();
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
	 * @param numTestCases the numTestCases to set
	 */
	public void setNumTestCases(int numTestCases) {
		this.numTestCases = numTestCases;
	}

	/**
	 * @param remoteFileBean the remoteFileWithType to set
	 */
	public boolean setFileNameWithTypeBean(FileNameWithTypeBean remoteFileBean) {
		if ((excelFileWithFormatBean != null && !excelFileWithFormatBean.equals(remoteFileBean))
				|| excelFileWithFormatBean == null && remoteFileBean != null) {
			excelFileWithFormatBean = remoteFileBean;
			getExcelFileBean();
			return true;
		}
		return false;
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		// update msrun combo
		ProjectCreatorWizardUtil.updateCombo(msRunComboBox, MsRunTypeBean.class, false);
	}

	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;

	}

	private ChangeHandler getStartCheckChangeHandler() {
		if (startCheckChangeHandler == null)
			startCheckChangeHandler = new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					startCheck();
				}
			};
		return startCheckChangeHandler;
	}

	public void resetData() {
		for (AmountPanel peptideAmountPanel : psmAmountPanels) {
			peptideAmountPanel.resetData();
		}
		for (AmountPanel proteinAmountPanel : proteinAmountPanels) {
			proteinAmountPanel.resetData();
		}
	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}

	@Override
	public QuantificationExcelTypeBean getObject() {
		return excelQuantificationType;
	}

	@Override
	public void updateRepresentedObject() {
		updateExcelIdentificationTypeObjectFromGUIOptions();

	}

	@Override
	public void updateGUIFromObjectData(QuantificationExcelTypeBean dataObject) {
		excelQuantificationType = dataObject;
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {
		if (excelQuantificationType != null) {
			// msrun
			ProjectCreatorWizardUtil.selectInCombo(msRunComboBox, excelQuantificationType.getMsRunRef(),
					SharedConstants.MULTIPLE_ITEM_SEPARATOR);

			// remove all previous psm amount panels
			for (AmountPanel psmAmountPanel : psmAmountPanels) {
				psmAmountPanel.removeFromParent();
			}
			psmAmountPanels.clear();
			psmQuantificationValuesIncrementableTextBox.setValue(0);
			if (excelQuantificationType.getPsmAmounts() != null) {
				for (AmountTypeBean amountTypeBean : excelQuantificationType.getPsmAmounts()) {
					createNewPSMQuantificationValue(amountTypeBean);
					psmQuantificationValuesIncrementableTextBox
							.setValue(psmQuantificationValuesIncrementableTextBox.getIntegerNumber() + 1);
				}
			}
			// remove all previous peptide amount panels
			for (AmountPanel peptideAmountPanel : peptideAmountPanels) {
				peptideAmountPanel.removeFromParent();
			}
			peptideAmountPanels.clear();
			peptideQuantificationValuesIncrementableTextBox.setValue(0);
			if (excelQuantificationType.getPeptideAmounts() != null) {
				for (AmountTypeBean amountTypeBean : excelQuantificationType.getPeptideAmounts()) {
					createNewPeptideQuantificationValue(amountTypeBean);
					peptideQuantificationValuesIncrementableTextBox
							.setValue(peptideQuantificationValuesIncrementableTextBox.getIntegerNumber() + 1);
				}
			}
			// remove all previous protein amount panels
			for (AmountPanel proteinAmountPanel : proteinAmountPanels) {
				proteinAmountPanel.removeFromParent();
			}
			proteinAmountPanels.clear();
			proteinQuantificationValuesIncrementableTextBox.setValue(0);
			if (excelQuantificationType.getProteinAmounts() != null) {
				for (AmountTypeBean proteinAmount : excelQuantificationType.getProteinAmounts()) {
					createNewProteinQuantificationValue(proteinAmount);
					proteinQuantificationValuesIncrementableTextBox
							.setValue(proteinQuantificationValuesIncrementableTextBox.getIntegerNumber() + 1);

				}
			}
		}
	}

}
