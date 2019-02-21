package edu.scripps.yates.client.ui.wizard.pages.widgets.excel;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.widgets.NewExcelReferenceWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.client.util.ExtendedTextBox;
import edu.scripps.yates.client.util.TextChangeEvent;
import edu.scripps.yates.client.util.TextChangeEventHandler;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;

public class RatioPanel extends ExcelObjectPanel<ExcelAmountRatioTypeBean> {
	private NewExcelReferenceWidget columnSelectorForRatioValue;
	private ListBox condition1ListBox;
	private ListBox condition2ListBox;
	private ExtendedTextBox ratioName;

	private SimpleCheckBox useScoreCheckBox;
	private int rowForScore;
	private ScorePanel scorePanel;

	public RatioPanel(String name, PintContext context, FileTypeBean file, String excelSheet,
			ExcelAmountRatioTypeBean peptideRatio) {
		super(name, context, file, excelSheet, peptideRatio);
	}

	@Override
	protected void init() {
		int row = 0;

		final Label label00 = new Label(getName() + " ratios in Excel file:");
		label00.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(row, 0, label00);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		//
		row++;
		final Label label1 = new Label("Ratio value:");
		label1.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label1);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		columnSelectorForRatioValue = new NewExcelReferenceWidget(file, getExcelSheet());
		setWidget(row, 1, columnSelectorForRatioValue);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		columnSelectorForRatioValue.addExcelSheetsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// if sheet is changed, the column ref will be null until selected it
				object.setColumnRef(null);
			}
		});
		columnSelectorForRatioValue.addExcelColumnsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// set the column ref of the ratio score
				final String columnRef = columnSelectorForRatioValue.getColumnRef();
				object.setColumnRef(columnRef);
				ratioName.setValue(columnSelectorForRatioValue.getColumnNameWithNoLetter(), true);
				object.setName(ratioName.getValue());
			}
		});
		//
		row++;
		final Label label8 = new Label("Ratio name:");
		label8.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label8);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		ratioName = new ExtendedTextBox();
		setWidget(row, 1, ratioName);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		ratioName.addTextChangeEventHandler(new TextChangeEventHandler() {

			@Override
			public void onTextChange(TextChangeEvent event) {
				object.setName(ratioName.getValue());
			}
		});
		//
		row++;
		final Label label2 = new Label("Condition numerator:");
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label2);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		condition1ListBox = createConditionListBox();
		setWidget(row, 1, condition1ListBox);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		condition1ListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getObject().setNumerator(condition1ListBox.getSelectedValue());
			}
		});
		//
		row++;
		final Label label3 = new Label("Condition denominator:");
		label3.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label3);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		condition2ListBox = createConditionListBox();
		setWidget(row, 1, condition2ListBox);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		condition2ListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getObject().setDenominator(condition2ListBox.getSelectedValue());
			}
		});
		//
		row++;
		final Label label4 = new Label("Add an associated score/p-value:");
		label4.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label4);

		useScoreCheckBox = new SimpleCheckBox();
		setWidget(row, 1, useScoreCheckBox);
		useScoreCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				showRatioScore(event.getValue());
			}
		});
		//
		row++;
		rowForScore = row;

		//
		updateGUIFromContext();
	}

	protected void showRatioScore(Boolean value) {
		final int row = rowForScore;
		getFlexCellFormatter().setVisible(row, 0, value);
		if (value) {
			if (object.getRatioScore() == null) {
				object.setRatioScore(new ScoreTypeBean());
			}
			if (scorePanel == null) {
				scorePanel = new ScorePanel("score/p-value", getContext(), file, getExcelSheet(),
						object.getRatioScore(), false);
			}
			//
			setWidget(row, 0, scorePanel);

			getFlexCellFormatter().setColSpan(row, 0, 2);
		} else {
			object.setRatioScore(null);
		}
	}

	@Override
	public boolean isReady() {
		if (isEmpty(object.getColumnRef())) {
			return false;
		}
		if (isEmpty(object.getDenominator())) {
			return false;
		}
		if (isEmpty(object.getNumerator())) {
			return false;
		}
		if (object.getRatioScore() != null) {
			if (isEmpty(object.getRatioScore().getColumnRef())) {
				return false;
			}
			if (isEmpty(object.getRatioScore().getScoreName())) {
				return false;
			}
		}
		// ms run can be null, because is not set yet
		return true;
	}

	@Override
	protected void updateGUIFromContext() {
		columnSelectorForRatioValue.selectExcelColumn(object.getColumnRef());
		// if there is 2 conditions created, select one in each listbox
		if (object.getNumerator() != null && object.getDenominator() != null) {
			selectInListBox(condition1ListBox, object.getNumerator());
			selectInListBox(condition2ListBox, object.getDenominator());
		} else {
			final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
					.getConditions(getContext().getPintImportConfiguration());
			if (conditions != null && conditions.size() == 2) {
				ClientGUIUtil.setSelectedValueInListBox(condition1ListBox, conditions.get(0).getId(), true);
				ClientGUIUtil.setSelectedValueInListBox(condition2ListBox, conditions.get(1).getId(), true);
			}
		}

		this.ratioName.setValue(object.getName());
		this.useScoreCheckBox.setValue(object.getRatioScore() != null, true);
		showRatioScore(object.getRatioScore() != null);
	}

}
