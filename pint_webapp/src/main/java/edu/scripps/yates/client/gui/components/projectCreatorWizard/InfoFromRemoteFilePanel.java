package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.widgetideas.graphics.client.Color;

import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.ImportWizardService;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.client.util.TextAndRegexp;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class InfoFromRemoteFilePanel extends Composite
		implements ReferencesDataObject, ContainsImportJobID, RepresentsObject<RemoteInfoTypeBean> {
	protected static final String YES = "yes";
	protected static final String NO = "no";
	private Button checkButton;
	private CellTable<TextAndRegexp> accsCellTable;
	private CustomColumn<TextAndRegexp> accColumn;
	private CustomColumn<TextAndRegexp> decoyTextColumn;
	private SimpleCheckBox discardDecoysCheckBox;
	private RegularExpressionTextBox decoyRegexpSuggestBox;
	private FileNameWithTypeBean fileBean;
	// private FileNameWithTypeBean fastaFileBean;
	private final ImportWizardServiceAsync service = GWT.create(ImportWizardService.class);
	private final List<String> proteinAccessions = new ArrayList<String>();
	private int importJobID;
	private int numTestCases = 100;
	private final ListDataProvider<TextAndRegexp> dataProvider = new ListDataProvider<TextAndRegexp>();
	private ListBox msRunComboBox;
	private MyDialogBox loadingDialog;
	private FileNameWithTypeProvider fileNameWithTypeProvider;
	private RemoteInfoTypeBean remoteInfoTypeBean;
	private String sessionID;

	public InfoFromRemoteFilePanel(String sessionID, int importJobID) {
		this.sessionID = sessionID;
		this.importJobID = importJobID;
		ScrollPanel scroll = new ScrollPanel();
		scroll.setTouchScrollingDisabled(false);

		initWidget(scroll);
		scroll.setSize("100%", "100%");

		FlexTable flexTable = new FlexTable();
		flexTable.setBorderWidth(0);
		scroll.setWidget(flexTable);
		flexTable.setSize("500px", "400px");

		Grid grid = new Grid(4, 2);
		flexTable.setWidget(0, 0, grid);
		grid.setStyleName("verticalComponent");
		grid.setCellSpacing(4);
		grid.setSize("100%", "100%");

		Label lblSelectTheRemote = new Label("Discard decoy hits:");
		grid.setWidget(0, 0, lblSelectTheRemote);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		discardDecoysCheckBox = new SimpleCheckBox();
		grid.setWidget(0, 1, discardDecoysCheckBox);

		Label lblRegularExpressionFor = new Label("Regular expression for decoy accessions:");
		grid.setWidget(1, 0, lblRegularExpressionFor);

		decoyRegexpSuggestBox = new RegularExpressionTextBox("Reverse", 1);
		decoyRegexpSuggestBox.setEnabled(false);
		grid.setWidget(1, 1, decoyRegexpSuggestBox);
		grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		Label lblMsRun = new Label("MS Run:");
		grid.setWidget(2, 0, lblMsRun);

		msRunComboBox = new ListBox();
		grid.setWidget(2, 1, msRunComboBox);

		InlineLabel nlnlblCheckRemoteData = new InlineLabel("Check remote data:");
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

		ScrollPanel scrollPanel = new ScrollPanel();
		flowPanel.add(scrollPanel);
		scrollPanel.setSize("100%", "100%");

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

					return YES;

				}
				return NO;
			}
		};
		decoyTextColumn.addCustomColorCell(YES, Color.RED);
		decoyTextColumn.addCustomColorCell(NO, Color.GREEN);

		decoyTextColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		decoyTextColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		accsCellTable.addColumn(decoyTextColumn, "is decoy?");
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);

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
			if (fileNameWithTypeProvider != null) {
				setFileNameWithTypeBean(fileNameWithTypeProvider.getFileNameWithTypeBean());
			}
			if (fileBean != null && fileBean instanceof FileNameWithTypeBean) {
				final FileNameWithTypeBean remoteFileBean = fileBean;
				// service.addRemoteFile(importJobID, remoteFileBean,
				// new AsyncCallback<Void>() {
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// StatusReportersRegister.getInstance()
				// .notifyStatusReporters(caught);
				// checkButton.setEnabled(true);
				// }
				//
				// @Override
				// public void onSuccess(Void result) {
				showDialog("Loading random proteins from remote file '" + remoteFileBean.getId() + " ("
						+ remoteFileBean.getFileName() + ")'...", true, false, true);
				// not use fasta file bean
				service.getRandomProteinAccessions(sessionID, importJobID, remoteFileBean, null, numTestCases,
						new AsyncCallback<List<String>>() {

							@Override
							public void onSuccess(List<String> result) {

								proteinAccessions.clear();
								if (result != null) {
									proteinAccessions.addAll(result);
									loadDataOnTable();
								}
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
	 * @param fileBean
	 *            the remoteFileWithType to set
	 */
	private boolean setFileNameWithTypeBean(FileNameWithTypeBean fileBean) {
		if ((this.fileBean != null && !this.fileBean.equals(fileBean)) || this.fileBean == null && fileBean != null) {
			this.fileBean = fileBean;
			resetData();
			return true;
		}
		return false;
	}

	// /**
	// * @return the fastaFileBean
	// */
	// public FileNameWithTypeBean getFastaFileBean() {
	// return fastaFileBean;
	// }

	// /**
	// * @param fastaFileBean
	// * the fastaFileBean to set
	// */
	// public boolean setFastaFileBean(FileNameWithTypeBean fastaFileBean2) {
	// if ((fastaFileBean != null && !fastaFileBean.equals(fastaFileBean2))
	// || fastaFileBean == null && fastaFileBean2 != null) {
	// fastaFileBean = fastaFileBean2;
	// return true;
	// }
	// return false;
	// }

	public void resetData() {
		dataProvider.getList().clear();
		checkButton.setEnabled(true);
	}

	public RemoteInfoTypeBean getRemoteInfoTypeBean() {
		updateGUIReferringToDataObjects();
		RemoteInfoTypeBean remoteInfoTypeBean = new RemoteInfoTypeBean();

		// clear file refs. It will be filled with the combo boxes in parent
		// panel
		remoteInfoTypeBean.getFileRefs().clear();

		if (decoyRegexpSuggestBox.getCurrentRegularExpression() != null
				&& !"".equals(decoyRegexpSuggestBox.getCurrentRegularExpressionText())) {
			remoteInfoTypeBean.setDiscardDecoys(decoyRegexpSuggestBox.getCurrentRegularExpressionText());
		} else {
			remoteInfoTypeBean.setDiscardDecoys(null);
		}
		// ms run ref
		if (msRunComboBox.getSelectedIndex() > 0) {
			final String itemText = msRunComboBox.getItemText(msRunComboBox.getSelectedIndex());
			remoteInfoTypeBean.setMsRunRef(itemText);
		} else {
			remoteInfoTypeBean.setMsRunRef(null);
		}

		return remoteInfoTypeBean;
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
			loadingDialog = MyDialogBox.getInstance(text, autoHide, modal, showLoaderBar);
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

	public interface FileNameWithTypeProvider {
		public FileNameWithTypeBean getFileNameWithTypeBean();
	}

	public void setFileNameWithTypeProvider(FileNameWithTypeProvider fileNameWithTypeProvider) {

		this.fileNameWithTypeProvider = fileNameWithTypeProvider;

	}

	@Override
	public RemoteInfoTypeBean getObject() {
		return remoteInfoTypeBean;
	}

	@Override
	public void updateRepresentedObject() {

		if (remoteInfoTypeBean == null)
			remoteInfoTypeBean = new RemoteInfoTypeBean();

		// clear file refs.
		// It will be filled with the combo boxes in parent panel
		remoteInfoTypeBean.getFileRefs().clear();

		if (decoyRegexpSuggestBox.getCurrentRegularExpression() != null
				&& !"".equals(decoyRegexpSuggestBox.getCurrentRegularExpressionText())) {
			remoteInfoTypeBean.setDiscardDecoys(decoyRegexpSuggestBox.getCurrentRegularExpressionText());
		} else {
			remoteInfoTypeBean.setDiscardDecoys(null);
		}
		// ms run ref
		if (msRunComboBox.getSelectedIndex() > 0) {
			final String itemText = msRunComboBox.getItemText(msRunComboBox.getSelectedIndex());
			remoteInfoTypeBean.setMsRunRef(itemText);
		} else {
			remoteInfoTypeBean.setMsRunRef(null);
		}

	}

	@Override
	public void updateGUIFromObjectData(RemoteInfoTypeBean dataObject) {
		remoteInfoTypeBean = dataObject;
		updateGUIFromObjectData();

	}

	@Override
	public void updateGUIFromObjectData() {
		if (remoteInfoTypeBean != null) {
			if (remoteInfoTypeBean.getDiscardDecoys() != null && !"".equals(remoteInfoTypeBean.getDiscardDecoys())) {
				discardDecoysCheckBox.setValue(true);
				decoyRegexpSuggestBox.setEnabled(true);
				decoyRegexpSuggestBox.setText(remoteInfoTypeBean.getDiscardDecoys());
			} else {
				discardDecoysCheckBox.setValue(false);
				decoyRegexpSuggestBox.setEnabled(false);
				decoyRegexpSuggestBox.setText("");
			}
			ProjectCreatorWizardUtil.selectInCombo(msRunComboBox, remoteInfoTypeBean.getMsRunRef(),
					edu.scripps.yates.excel.util.ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
		} else {
			discardDecoysCheckBox.setValue(false);
			decoyRegexpSuggestBox.setEnabled(false);
			decoyRegexpSuggestBox.setText("");
			ProjectCreatorWizardUtil.selectInCombo(msRunComboBox, "",
					edu.scripps.yates.excel.util.ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
		}

	}
}
