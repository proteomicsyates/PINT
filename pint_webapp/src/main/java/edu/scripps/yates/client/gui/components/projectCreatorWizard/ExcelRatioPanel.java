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
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimpleRadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ImportWizardService;
import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatioDescriptorTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class ExcelRatioPanel
		extends ContainsExcelColumnRefPanelAndTable<Pair<String, ExcelAmountRatioTypeBean>, ExcelAmountRatioTypeBean>
		implements RepresentsObject<ExcelAmountRatioTypeBean> {
	private final ListBox comboBoxAggregationLevel;
	private final ExcelColumnRefPanel excelColumnRefPanelForScoreValues;
	private final Label labelNothing;
	private final SimpleCheckBox addAssociatedScoreCheckBox;
	private final FlexTable flexTable;
	private final Label scoreNameLabel;
	private final Label labelNothing2;
	private final Label scoreTypeLabel;
	private final Label labelNothing3;
	private final SuggestBox scoreNameSuggestBox = new SuggestBox();
	private final SuggestBox scoreTypeSuggestoBox = new SuggestBox();
	private final ImportWizardServiceAsync service = GWT.create(ImportWizardService.class);
	private final String sessionID;
	private final Label labelProteinAccession;
	private final Label lblSelectExcelColumn;
	private final SimpleRadioButton radioButtonFromSameRow;
	private final SimpleRadioButton radioButtonFromExternalSource;
	private final static String TAG = "###";
	private final String ratio1Text = "Assign ratio values to " + TAG + " in the same row";
	private final String ratio2Text = "Assign ratio values to " + TAG + " not from Excel file";
	private final FlowPanel proteinAccessionContainer;
	private final Label labelPSMAccession;
	private final Label labelPeptideAccession;
	private final ProteinAccessionPanel proteinAccessionPanel;
	private final ExcelColumnRefPanel peptideColumnRefPanel;
	private final ExcelColumnRefPanel psmIdColumnRefPanel;
	private final Label labelRatio1;
	private final Label labelRatio2;
	private Label label;
	private Widget widget;
	private final RatioDescriptorTypeBean ratioDescriptor;

	private String getRatio1Text(SharedAggregationLevel level) {
		return getRatioText(level, ratio1Text);
	}

	private String getRatio2Text(SharedAggregationLevel level) {
		return getRatioText(level, ratio2Text);
	}

	private String getRatioText(SharedAggregationLevel level, String text) {
		switch (level) {
		case PROTEIN:
			return text.replace(TAG, "proteins");
		case PEPTIDE:
			return text.replace(TAG, "peptides");
		case PSM:
			return text.replace(TAG, "PSMs");
		default:
			break;
		}
		return null;
	}

	public ExcelRatioPanel(String sessionID, FileTypeBean excelFileBean,
			ExcelAmountRatioTypeBean excelAmountRatioTypeBean, RatioDescriptorTypeBean ratioDescriptor) {
		super(excelFileBean, excelAmountRatioTypeBean);
		mainPanel.setSize("380px", "799px");
		this.sessionID = sessionID;
		this.ratioDescriptor = ratioDescriptor;
		scoreNameSuggestBox.setText("Score name");
		excelColumnRefPanelForScoreValues = new ExcelColumnRefPanel(excelFileBean);

		lblSelectExcelColumn = new Label("Select Excel column for ratio values:");
		mainPanel.add(lblSelectExcelColumn);
		lblSelectExcelColumn.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		lblSelectExcelColumn.setWidth("");
		flexTable = new FlexTable();
		flexTable.setCellPadding(5);
		mainPanel.add(flexTable);
		flexTable.setWidth("100%");
		flexTable.setWidget(4, 0, getExcelColumnRefPanel());

		Label lblType = new Label("Quantitation ratios at level:");
		flexTable.setWidget(1, 0, lblType);

		comboBoxAggregationLevel = new ListBox();
		for (SharedAggregationLevel aggregationLevel : SharedAggregationLevel.values()) {
			comboBoxAggregationLevel.addItem(aggregationLevel.name(), aggregationLevel.name());
		}
		reloadTableIfChange(comboBoxAggregationLevel);
		flexTable.setWidget(1, 1, comboBoxAggregationLevel);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setColSpan(4, 0, 2);

		labelRatio1 = new Label(getRatio1Text(SharedAggregationLevel.PSM));
		labelRatio1.setWidth("270px");
		flexTable.setWidget(2, 0, labelRatio1);

		radioButtonFromSameRow = new SimpleRadioButton("new name" + hashCode());
		radioButtonFromSameRow.setValue(true);
		flexTable.setWidget(2, 1, radioButtonFromSameRow);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);

		labelRatio2 = new Label(getRatio2Text(SharedAggregationLevel.PSM));
		labelRatio2.setWidth("270px");
		flexTable.setWidget(3, 0, labelRatio2);

		radioButtonFromExternalSource = new SimpleRadioButton("new name" + hashCode());
		flexTable.setWidget(3, 1, radioButtonFromExternalSource);
		Label lblAddAssociatedScore = new Label("Add associated score:");
		flexTable.setWidget(5, 0, lblAddAssociatedScore);

		addAssociatedScoreCheckBox = new SimpleCheckBox();
		flexTable.setWidget(5, 1, addAssociatedScoreCheckBox);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		labelNothing = new Label("-");
		flexTable.setWidget(6, 0, labelNothing);
		flexTable.getCellFormatter().setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getFlexCellFormatter().setColSpan(6, 0, 2);

		scoreNameLabel = new Label("-");
		flexTable.setWidget(7, 0, scoreNameLabel);
		flexTable.getCellFormatter().setHorizontalAlignment(7, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		labelNothing2 = new Label("-");
		flexTable.setWidget(7, 1, labelNothing2);
		flexTable.getCellFormatter().setHorizontalAlignment(7, 1, HasHorizontalAlignment.ALIGN_LEFT);

		scoreTypeLabel = new Label("-");
		flexTable.setWidget(8, 0, scoreTypeLabel);
		flexTable.getCellFormatter().setHorizontalAlignment(8, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		labelNothing3 = new Label("-");
		flexTable.setWidget(8, 1, labelNothing3);
		flexTable.getCellFormatter().setHorizontalAlignment(8, 1, HasHorizontalAlignment.ALIGN_LEFT);

		labelProteinAccession = new Label("Select Excel column for protein accession:");
		labelProteinAccession.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		labelPSMAccession = new Label("Select Excel column for PSM identifier:");
		labelPSMAccession.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		labelPeptideAccession = new Label("Select Excel column for peptide sequence:");
		labelPeptideAccession.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		flexTable.getFlexCellFormatter().setColSpan(10, 0, 2);
		flexTable.getCellFormatter().setVerticalAlignment(10, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		proteinAccessionPanel = new ProteinAccessionPanel(excelFileBean, new ProteinAccessionTypeBean());
		proteinAccessionContainer = new FlowPanel();
		proteinAccessionContainer.add(labelProteinAccession);
		proteinAccessionContainer.add(proteinAccessionPanel);

		peptideColumnRefPanel = new ExcelColumnRefPanel(excelFileBean);
		psmIdColumnRefPanel = new ExcelColumnRefPanel(excelFileBean);

		// add handlers
		addHandlers();

		// load score types
		loadScoreTypes();

		updateGUIFromObjectData();
	}

	protected void loadInformationForLevel(SharedAggregationLevel level) {
		if (mainPanel.getWidgetIndex(label) != -1) {
			mainPanel.remove(label);
		}
		if (mainPanel.getWidgetIndex(widget) != -1) {
			mainPanel.remove(widget);
		}
		if (level != null) {
			switch (level) {
			case PSM:
				labelRatio1.setText(getRatio1Text(SharedAggregationLevel.PSM));
				labelRatio2.setText(getRatio2Text(SharedAggregationLevel.PSM));
				label = labelPSMAccession;
				widget = psmIdColumnRefPanel;
				break;
			case PEPTIDE:
				labelRatio1.setText(getRatio1Text(SharedAggregationLevel.PEPTIDE));
				labelRatio2.setText(getRatio2Text(SharedAggregationLevel.PEPTIDE));
				label = labelPeptideAccession;
				widget = peptideColumnRefPanel;
				break;
			case PROTEIN:
				labelRatio1.setText(getRatio1Text(SharedAggregationLevel.PROTEIN));
				labelRatio2.setText(getRatio2Text(SharedAggregationLevel.PROTEIN));
				label = labelProteinAccession;
				widget = proteinAccessionContainer;
				break;
			default:
				break;
			}
			if (radioButtonFromExternalSource.getValue()) {
				mainPanel.add(label);
				mainPanel.add(widget);
			}
		}

	}

	private void loadScoreTypes() {
		service.getScoreTypes(sessionID, new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				if (result != null) {
					MultiWordSuggestOracle suggestedWords = (MultiWordSuggestOracle) scoreTypeSuggestoBox
							.getSuggestOracle();
					suggestedWords.addAll(result);
					MultiWordSuggestOracle suggestedWords2 = (MultiWordSuggestOracle) scoreNameSuggestBox
							.getSuggestOracle();
					suggestedWords2.addAll(result);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
			}
		});
	}

	private void addHandlers() {
		// if checkbox is selected, show the excel column ref selector. if is
		// not selected show empty label
		addAssociatedScoreCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				final Boolean addAssociatedScore = event.getValue();
				updateAssociatedScoreGUI(addAssociatedScore);
			}
		});
		comboBoxAggregationLevel.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				loadInformationForLevel(getSelectedAggregationLevel());

			}
		});
		radioButtonFromExternalSource.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				loadInformationForLevel(getSelectedAggregationLevel());

			}
		});
		radioButtonFromSameRow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (radioButtonFromSameRow.getValue()) {
					loadInformationForLevel(null);
				}
			}
		});
	}

	private SharedAggregationLevel getSelectedAggregationLevel() {
		if (comboBoxAggregationLevel.getSelectedIndex() >= 0) {
			return SharedAggregationLevel
					.valueOf(comboBoxAggregationLevel.getValue(comboBoxAggregationLevel.getSelectedIndex()));
		}
		return null;
	}

	private void updateAssociatedScoreGUI(boolean addAssociatedScore) {
		if (addAssociatedScore) {
			flexTable.setWidget(6, 0, excelColumnRefPanelForScoreValues);
			scoreNameLabel.setText("Score name:");
			scoreTypeLabel.setText("Score type:");
			flexTable.setWidget(7, 1, scoreNameSuggestBox);
			flexTable.setWidget(8, 1, scoreTypeSuggestoBox);

		} else {
			flexTable.setWidget(6, 0, labelNothing);
			scoreNameLabel.setText("-");
			scoreTypeLabel.setText("-");
			flexTable.setWidget(7, 1, labelNothing2);
			flexTable.setWidget(8, 1, labelNothing3);

		}

	}

	@Override
	public void updateRepresentedObject() {
		final String columnRef = getExcelColumnRefPanel().getColumnRef();
		representedObject.setColumnRef(columnRef);
		if (ratioDescriptor != null && ratioDescriptor.getCondition1() != null) {
			representedObject.setNumerator(ratioDescriptor.getCondition1().getId());
		}
		if (ratioDescriptor != null && ratioDescriptor.getCondition2() != null) {
			representedObject.setDenominator(ratioDescriptor.getCondition2().getId());
		}
		ProteinAccessionTypeBean proteinAccession = null;
		SequenceTypeBean peptideSequence = null;
		PsmTypeBean psmId = null;

		if (!radioButtonFromSameRow.getValue()) {
			final SharedAggregationLevel level = getAggregationLevel();
			switch (level) {
			case PROTEIN:
				if (proteinAccessionPanel.getExcelColumnRefPanel().getColumnRef() != null) {
					proteinAccession = proteinAccessionPanel.getRepresentedObject();
				}
				break;
			case PEPTIDE:
				if (peptideColumnRefPanel.getColumnRef() != null) {
					peptideSequence = new SequenceTypeBean();
					peptideSequence.setColumnRef(peptideColumnRefPanel.getColumnRef());
				}
				break;
			case PSM:
				if (psmIdColumnRefPanel.getColumnRef() != null) {
					psmId = new PsmTypeBean();
					psmId.setColumnRef(psmIdColumnRefPanel.getColumnRef());
				}
				break;
			default:
				break;
			}
		}
		representedObject.setProteinAccession(proteinAccession);
		representedObject.setPeptideSequence(peptideSequence);
		representedObject.setPsmId(psmId);
		if (addAssociatedScoreCheckBox.getValue() && excelColumnRefPanelForScoreValues.getColumnRef() != null) {
			final ScoreTypeBean ratioScore = new ScoreTypeBean();
			ratioScore.setColumnRef(excelColumnRefPanelForScoreValues.getColumnRef());
			ratioScore.setScoreName(scoreNameSuggestBox.getText());

			if (!"".equals(scoreTypeSuggestoBox.getValue()))
				ratioScore.setScoreType(scoreTypeSuggestoBox.getValue());

			representedObject.setRatioScore(ratioScore);
		} else {
			representedObject.setRatioScore(null);
		}
	}

	@Override
	public List<CustomColumn<Pair<String, ExcelAmountRatioTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, ExcelAmountRatioTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, ExcelAmountRatioTypeBean>>>();

		CustomColumn<Pair<String, ExcelAmountRatioTypeBean>> ratioValuesColumn = new CustomColumn<Pair<String, ExcelAmountRatioTypeBean>>(
				"Ratio values") {
			@Override
			public String getValue(Pair<String, ExcelAmountRatioTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				return rawValue;
			}
		};

		ret.add(ratioValuesColumn);
		return ret;
	}

	@Override
	public List<Header<String>> getInitHeaders() {
		return null;
	}

	@Override
	public boolean isValid() {
		updateRepresentedObject();
		if (isNullOrEmpty(representedObject.getColumnRef()))
			return false;
		final ScoreTypeBean ratioScore = representedObject.getRatioScore();
		if (ratioScore != null) {
			if (ratioScore.getScoreName() == null || "".equals(ratioScore.getScoreName()))
				return false;
			if (ratioScore.getScoreType() == null || "".equals(ratioScore.getScoreType()))
				return false;
		}
		return true;
	}

	private boolean isNullOrEmpty(String string) {
		if (string == null || "".equals(string))
			return true;
		return false;
	}

	public SharedAggregationLevel getAggregationLevel() {
		final String value = comboBoxAggregationLevel.getValue(comboBoxAggregationLevel.getSelectedIndex());
		final SharedAggregationLevel[] values = SharedAggregationLevel.values();
		for (SharedAggregationLevel aggregation_LEVEL : values) {
			if (aggregation_LEVEL.name().equals(value)) {
				return aggregation_LEVEL;
			}
		}
		return null;
	}

	public void setAggregationLevel(SharedAggregationLevel aggregationLevel) {
		ProjectCreatorWizardUtil.selectInComboByValue(comboBoxAggregationLevel, aggregationLevel.name());
		loadInformationForLevel(aggregationLevel);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.client.gui.components.projectCreatorWizard.
	 * ContainsExcelColumnRefPanelAndTable
	 * #setExcelFileBean(edu.scripps.yates.shared
	 * .model.projectCreator.excel.FileTypeBean)
	 */
	@Override
	public void setExcelFileBean(FileTypeBean excelFileBean) {

		excelColumnRefPanelForScoreValues.setExcelFileBean(excelFileBean);
		proteinAccessionPanel.setExcelFileBean(excelFileBean);
		peptideColumnRefPanel.setExcelFileBean(excelFileBean);
		psmIdColumnRefPanel.setExcelFileBean(excelFileBean);
		super.setExcelFileBean(excelFileBean);
	}

	public SuggestBox getScoreNameTextBox() {
		return scoreNameSuggestBox;
	}

	/**
	 * @return the excelColumnRefPanelForScoreValues
	 */
	public ExcelColumnRefPanel getExcelColumnRefPanelForScoreValues() {
		return excelColumnRefPanelForScoreValues;
	}

	@Override
	public ExcelAmountRatioTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(ExcelAmountRatioTypeBean dataObject) {
		representedObject = dataObject;
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {
		if (representedObject != null) {
			getExcelColumnRefPanel().selectExcelColumn(representedObject.getColumnRef());
			final ScoreTypeBean ratioScore = representedObject.getRatioScore();
			if (ratioScore != null) {
				addAssociatedScoreCheckBox.setValue(true);
				updateAssociatedScoreGUI(true);
				getExcelColumnRefPanelForScoreValues().selectExcelColumn(ratioScore.getColumnRef());
				scoreNameSuggestBox.setValue(ratioScore.getScoreName());
				if (ratioScore.getScoreType() != null) {
					scoreTypeSuggestoBox.setValue(ratioScore.getScoreType());
				} else {
					scoreTypeSuggestoBox.setValue("");
				}
			} else {
				addAssociatedScoreCheckBox.setValue(false);
				updateAssociatedScoreGUI(false);
				getExcelColumnRefPanelForScoreValues().selectExcelColumn(null);
			}
			// external source or same row
			if (representedObject.getPeptideSequence() == null && representedObject.getPsmId() == null
					&& representedObject.getProteinAccession() == null) {
				radioButtonFromSameRow.setValue(true);
				radioButtonFromExternalSource.setValue(false);
			} else {
				radioButtonFromExternalSource.setValue(true);
				radioButtonFromSameRow.setValue(false);
			}

			if (representedObject.getProteinAccession() != null) {
				proteinAccessionPanel.updateGUIFromObjectData(representedObject.getProteinAccession());
			} else {
				proteinAccessionPanel.updateGUIFromObjectData(null);
			}
			if (representedObject.getPeptideSequence() != null) {
				peptideColumnRefPanel.selectExcelColumn(representedObject.getPeptideSequence().getColumnRef());
			} else {
				peptideColumnRefPanel.selectExcelColumn(null);
			}
			if (representedObject.getPsmId() != null) {
				psmIdColumnRefPanel.selectExcelColumn(representedObject.getPsmId().getColumnRef());
			} else {
				psmIdColumnRefPanel.selectExcelColumn(null);
			}
			if (representedObject.getPeptideSequence() == null && representedObject.getPsmId() == null
					&& representedObject.getProteinAccession() == null) {
				loadInformationForLevel(null);
			} else {
				loadInformationForLevel(getSelectedAggregationLevel());
			}
		} else {
			addAssociatedScoreCheckBox.setValue(false);
			updateAssociatedScoreGUI(false);
			getExcelColumnRefPanel().selectExcelColumn(null);
			getExcelColumnRefPanelForScoreValues().selectExcelColumn(null);
		}

	}

	@Override
	public void addExcelColumnsChangeHandler(ChangeHandler changeHandler) {
		super.addExcelColumnsChangeHandler(changeHandler);
		excelColumnRefPanelForScoreValues.addExcelColumnsChangeHandler(changeHandler);
	}
}