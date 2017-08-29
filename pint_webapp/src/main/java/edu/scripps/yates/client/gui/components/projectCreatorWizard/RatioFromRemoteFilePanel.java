package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.widgetideas.graphics.client.Color;

import edu.scripps.yates.client.ImportWizardService;
import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.util.TextAndRegexp;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatioDescriptorTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;

public class RatioFromRemoteFilePanel extends Composite
		implements ReferencesDataObject, ContainsImportJobID, RepresentsObject<RemoteFilesRatioTypeBean> {
	private final ListBox comboBoxAggregationLevel;

	protected static final String YES = "yes";
	protected static final String NO = "no";
	private Button checkButton;
	private CellTable<TextAndRegexp> accsCellTable;
	private CustomColumn<TextAndRegexp> accColumn;
	private CustomColumn<TextAndRegexp> decoyTextColumn;
	private SimpleCheckBox discardDecoysCheckBox;
	private RegularExpressionTextBox decoyRegexpSuggestBox;
	private FileNameWithTypeBean fileBean;
	private final ImportWizardServiceAsync service = GWT.create(ImportWizardService.class);
	private final List<String> proteinAccessions = new ArrayList<String>();
	private int importJobID;
	private int numTestCases = 100;
	private final ListDataProvider<TextAndRegexp> dataProvider = new ListDataProvider<TextAndRegexp>();
	private ListBox msRunComboBox;
	private MyDialogBox loadingDialog;
	private RemoteFilesRatioTypeBean remoteFilesRatioTypeBean;
	private String sessionID;
	private final RatioDescriptorTypeBean ratioDescriptorTypeBean;

	public RatioFromRemoteFilePanel(String sessionID, int importJobID,
			RatioDescriptorTypeBean ratioDescriptorTypeBean) {
		this.sessionID = sessionID;
		this.ratioDescriptorTypeBean = ratioDescriptorTypeBean;
		this.importJobID = importJobID;

		ScrollPanel scroll = new ScrollPanel();
		scroll.setTouchScrollingDisabled(false);

		initWidget(scroll);
		scroll.setSize("100%", "400px");

		FlexTable grid = new FlexTable();
		scroll.setWidget(grid);
		grid.setSize("500px", "400px");

		grid.setStyleName("verticalComponent");
		grid.setCellSpacing(4);
		grid.setSize("100%", "100%");
		Label lblType = new Label("Quantitation ratios at level(s):");
		String tooltip = "You can select more than one level.<br>(It will depend on the input file type whether each ratio can be extracted or not)";
		lblType.setTitle(tooltip);
		grid.setWidget(0, 0, lblType);
		comboBoxAggregationLevel = new ListBox();
		comboBoxAggregationLevel.setMultipleSelect(true);
		comboBoxAggregationLevel.setTitle(tooltip);
		for (SharedAggregationLevel aggregationLevel : SharedAggregationLevel.values()) {
			if (aggregationLevel == SharedAggregationLevel.PROTEINGROUP) {
				continue;
			}
			comboBoxAggregationLevel.addItem(aggregationLevel.name(), aggregationLevel.name());
		}
		grid.setWidget(0, 1, comboBoxAggregationLevel);

		Label lblSelectTheRemote = new Label("Discard decoy hits:");
		grid.setWidget(1, 0, lblSelectTheRemote);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		discardDecoysCheckBox = new SimpleCheckBox();
		grid.setWidget(1, 1, discardDecoysCheckBox);

		Label lblRegularExpressionFor = new Label("Regular expression for decoy accessions:");
		grid.setWidget(2, 0, lblRegularExpressionFor);

		decoyRegexpSuggestBox = new RegularExpressionTextBox("Reverse", 1);
		decoyRegexpSuggestBox.setEnabled(false);
		grid.setWidget(2, 1, decoyRegexpSuggestBox);
		grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		Label lblMsRun = new Label("MS Run:");
		grid.setWidget(3, 0, lblMsRun);

		msRunComboBox = new ListBox();
		grid.setWidget(3, 1, msRunComboBox);

		InlineLabel nlnlblCheckRemoteData = new InlineLabel("Check remote data:");
		grid.setWidget(4, 0, nlnlblCheckRemoteData);

		checkButton = new Button("check");
		grid.setWidget(4, 1, checkButton);
		checkButton.setText("check");
		grid.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);

		FlowPanel flowPanel = new FlowPanel();

		flowPanel.setStyleName("IdentificationInfoRemoteFilePanel");
		grid.setWidget(5, 0, flowPanel);
		flowPanel.setHeight("100%");

		ScrollPanel scrollPanel = new ScrollPanel();
		flowPanel.add(scrollPanel);
		scrollPanel.setSize("290px", "240px");

		accsCellTable = new CellTable<TextAndRegexp>();
		dataProvider.addDataDisplay(accsCellTable);
		scrollPanel.setWidget(accsCellTable);
		accsCellTable.setEmptyTableWidget(new Label("(Click on check to load the table)"));
		accsCellTable.setSize("100%", "100%");
		accsCellTable.setPageSize(100);

		accColumn = new CustomColumn<TextAndRegexp>() {
			@Override
			public String getValue(TextAndRegexp object) {
				return object.getAccession();
			}
		};
		accColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		accColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		accsCellTable.addColumn(accColumn, "Protein accession");

		decoyTextColumn = new CustomColumn<TextAndRegexp>() {
			@Override
			public String getValue(TextAndRegexp object) {
				final String parsedAccession = object.getParsedAccession();
				if (parsedAccession != null) {
					if (!parsedAccession.equals(object.getAccession())) {
						return YES;
					}
				}
				return NO;
			}
		};
		decoyTextColumn.addCustomColorCell(YES, Color.RED);
		decoyTextColumn.addCustomColorCell(NO, Color.GREEN);

		decoyTextColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		decoyTextColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		accsCellTable.addColumn(decoyTextColumn, "is decoy?");
		grid.getFlexCellFormatter().setColSpan(5, 0, 2);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		addHandlers();

		// register this as a listener of msruns
		ProjectCreatorRegister.registerAsListenerByObjectClass(MsRunTypeBean.class, this);

		updateGUIReferringToDataObjects();

	}

	private void addHandlers() {
		// if discard decoy hits checkbox is enabled, enable regular exp textbox
		discardDecoysCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				decoyRegexpSuggestBox.setEnabled(event.getValue());
			}
		});
		// if check button is clicked, then start the check
		checkButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetData();
				startCheck();
			}
		});
	}

	protected void startCheck() {
		// disable start check button
		checkButton.setEnabled(false);
		// if regular expression is a valid regular expression
		if (discardDecoysCheckBox.getValue()) {
			if (decoyRegexpSuggestBox.checkRegularExpression()) {
				retrieveData();
			}
		} else {
			retrieveData();
		}

	}

	/**
	 * Returns a list of String representing the accessions of the proteins of
	 * the data source.<br>
	 * If the list is not loaded, ask for it to the server in a synchonous way
	 *
	 * @return
	 */
	private void retrieveData() {
		if (dataProvider.getList().isEmpty()) {
			if (fileBean != null && fileBean instanceof RemoteFileWithTypeBean) {
				final RemoteFileWithTypeBean remoteFileBean = (RemoteFileWithTypeBean) fileBean;
				showDialog("Loading random proteins from remote file '" + fileBean.getId() + " ("
						+ fileBean.getFileName() + ")'...", true, false, true);
				// not use fasta file
				service.getRandomProteinAccessions(sessionID, importJobID, remoteFileBean, null, numTestCases,
						new AsyncCallback<List<String>>() {

							@Override
							public void onSuccess(List<String> result) {
								proteinAccessions.clear();
								proteinAccessions.addAll(result);
								loadDataOnTable();
								checkButton.setEnabled(true);
								hiddeDialog();
							}

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
								checkButton.setEnabled(true);
								hiddeDialog();
							}
						});

				// }
				// });

			} else if (fileBean != null && fileBean instanceof FileNameWithTypeBean) {
				service.addDataFile(sessionID, importJobID, fileBean, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						checkButton.setEnabled(true);
						hiddeDialog();
					}

					@Override
					public void onSuccess(Void result) {
						showDialog("Loading random proteins from uploaded file '" + fileBean.getId() + " ("
								+ fileBean.getFileName() + ")'...", true, false, true);

						// not use fasta file bean
						service.getRandomProteinAccessions(sessionID, importJobID, fileBean, null, numTestCases,
								new AsyncCallback<List<String>>() {

									@Override
									public void onSuccess(List<String> result) {
										proteinAccessions.addAll(result);
										loadDataOnTable();
										checkButton.setEnabled(true);
										hiddeDialog();
									}

									@Override
									public void onFailure(Throwable caught) {
										StatusReportersRegister.getInstance().notifyStatusReporters(caught);
										checkButton.setEnabled(true);
										hiddeDialog();
									}
								});
					}
				});

			} else {
				// if not remote and no uploaded file is present
				checkButton.setEnabled(true);
				StatusReportersRegister.getInstance().notifyStatusReporters(
						"Selected data source is not properly setup. Go to 'Project elements' tab to review it.");
			}

		} else {
			// if data was already loaded
			checkButton.setEnabled(true);
			loadDataOnTable();
		}

	}

	private void loadDataOnTable() {
		if (!dataProvider.getList().isEmpty())
			dataProvider.getList().clear();
		RegExp regexp = decoyRegexpSuggestBox.getCurrentRegularExpression();
		for (String proteinAcc : proteinAccessions) {
			dataProvider.getList().add(new TextAndRegexp(proteinAcc, regexp));
		}

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
		if ((fileBean != null && !fileBean.equals(remoteFileBean)) || fileBean == null && remoteFileBean != null) {
			fileBean = remoteFileBean;
			return true;
		}
		return false;
	}

	public void resetData() {
		dataProvider.getList().clear();
		checkButton.setEnabled(true);
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		ProjectCreatorWizardUtil.updateCombo(msRunComboBox, MsRunTypeBean.class);
	}

	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;

	}

	private void showDialog(String text, boolean showLoaderBar, boolean autoHide, boolean modal) {
		if (loadingDialog == null) {
			loadingDialog = new MyDialogBox(text, autoHide, modal, showLoaderBar);
		} else {
			loadingDialog.setAutoHideEnabled(autoHide);
			loadingDialog.setModal(modal);
			loadingDialog.setText(text);
			loadingDialog.setShowLoadingBar(showLoaderBar);
		}
		loadingDialog.center();
	}

	private void hiddeDialog() {
		loadingDialog.hide();
	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}

	@Override
	public RemoteFilesRatioTypeBean getObject() {
		updateRepresentedObject();
		return remoteFilesRatioTypeBean;
	}

	@Override
	public void updateRepresentedObject() {
		if (remoteFilesRatioTypeBean == null)
			remoteFilesRatioTypeBean = new RemoteFilesRatioTypeBean();

		if (fileBean != null) {
			remoteFilesRatioTypeBean.setFileRef(fileBean.getId());
		}
		if (decoyRegexpSuggestBox.getCurrentRegularExpressionText() != null
				&& !"".equals(decoyRegexpSuggestBox.getCurrentRegularExpressionText()))
			remoteFilesRatioTypeBean.setDiscardDecoys(decoyRegexpSuggestBox.getCurrentRegularExpressionText());
		final int selectedIndex = msRunComboBox.getSelectedIndex();
		if (selectedIndex > 0) {
			final String itemText = msRunComboBox.getItemText(selectedIndex);
			remoteFilesRatioTypeBean.setMsRunRef(itemText);
		} else {
			remoteFilesRatioTypeBean.setMsRunRef(null);
		}
		if (ratioDescriptorTypeBean.getCondition1() != null) {
			remoteFilesRatioTypeBean.setNumerator(ratioDescriptorTypeBean.getCondition1().getId());
		}
		if (ratioDescriptorTypeBean.getCondition2() != null) {
			remoteFilesRatioTypeBean.setDenominator(ratioDescriptorTypeBean.getCondition2().getId());
		}

	}

	public Set<SharedAggregationLevel> getSelectedAggregationLevels() {
		Set<SharedAggregationLevel> ret = new HashSet<SharedAggregationLevel>();
		for (int i = 0; i < comboBoxAggregationLevel.getItemCount(); i++) {
			if (comboBoxAggregationLevel.isItemSelected(i)) {
				ret.add(SharedAggregationLevel.valueOf(comboBoxAggregationLevel.getValue(i)));
			}
		}
		return ret;
	}

	@Override
	public void updateGUIFromObjectData(RemoteFilesRatioTypeBean dataObject) {
		remoteFilesRatioTypeBean = dataObject;
		updateGUIFromObjectData();

	}

	@Override
	public void updateGUIFromObjectData() {
		if (remoteFilesRatioTypeBean != null) {
			if (remoteFilesRatioTypeBean.getMsRunRef() != null)
				ProjectCreatorWizardUtil.selectInCombo(msRunComboBox, remoteFilesRatioTypeBean.getMsRunRef(),
						edu.scripps.yates.excel.util.ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
			if (remoteFilesRatioTypeBean.getDiscardDecoys() != null
					&& !"".equals(remoteFilesRatioTypeBean.getDiscardDecoys())) {
				discardDecoysCheckBox.setValue(true);
				decoyRegexpSuggestBox.setText(remoteFilesRatioTypeBean.getDiscardDecoys());
				decoyRegexpSuggestBox.setEnabled(true);

			} else {
				discardDecoysCheckBox.setValue(false);
				decoyRegexpSuggestBox.setText("");
				decoyRegexpSuggestBox.setEnabled(false);

			}
		} else {
			discardDecoysCheckBox.setValue(false);
			decoyRegexpSuggestBox.setEnabled(false);
			decoyRegexpSuggestBox.setText("");
			ProjectCreatorWizardUtil.selectInCombo(msRunComboBox, "",
					edu.scripps.yates.excel.util.ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
		}

	}

	public void setAggregationLevels(Collection<SharedAggregationLevel> aggregationLevels) {
		for (SharedAggregationLevel sharedAggregationLevel : aggregationLevels) {
			ProjectCreatorWizardUtil.selectInComboByValue(comboBoxAggregationLevel, sharedAggregationLevel.name());

		}
	}
}
