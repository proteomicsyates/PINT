package edu.scripps.yates.client.ui.wizard.pages.widgets.excel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.pages.widgets.NewExcelReferencePanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean;

public class PeptideRatiosPanel extends ExcelObjectPanel<ExcelAmountRatioTypeBean> {
	private ScoreTypeBean previousRatioScore;

	public PeptideRatiosPanel(PintContext context, FileTypeBean file, ExcelAmountRatioTypeBean peptideRatio) {
		super(context, file, peptideRatio);
	}

	@Override
	protected void init() {
		int row = 0;
		final Label label0 = new Label("Peptide sequence:");
		label0.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label0);

		final NewExcelReferencePanel peptideSequenceSelector = new NewExcelReferencePanel(file);
		setWidget(row, 1, peptideSequenceSelector);
		peptideSequenceSelector.addExcelSheetsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// if sheet is changed, the column ref will be null until selected it
				if (object.getPeptideSequence() != null) {
					object.getPeptideSequence().setColumnRef(null);
				}
			}
		});
		peptideSequenceSelector.addExcelColumnsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// set the column ref of the ratio score
				final String columnRef = peptideSequenceSelector.getColumnRef();
				if (columnRef != null) {
					if (object.getPeptideSequence() == null) {
						object.setPeptideSequence(new SequenceTypeBean());
					}
					object.getPeptideSequence().setColumnRef(columnRef);
				} else {
					object.setPeptideSequence(null);
				}
			}
		});
		//
		row++;
		final Label label1 = new Label("Ratio value:");
		label1.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label1);

		final NewExcelReferencePanel columnSelector = new NewExcelReferencePanel(file);
		setWidget(row, 1, columnSelector);
		//
		row++;
		final Label label2 = new Label("Condition numerator:");
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label2);

		final ListBox condition1ListBox = createConditionListBox();
		setWidget(row, 1, condition1ListBox);
		//
		row++;
		final Label label3 = new Label("Condition denominator:");
		label3.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label3);

		final ListBox condition2ListBox = createConditionListBox();
		setWidget(row, 1, condition2ListBox);
		//
		row++;
		final Label label4 = new Label("Associated score/p-value:");
		label4.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label4);

		final SimpleCheckBox useScore = new SimpleCheckBox();
		setWidget(row, 1, useScore);

		//
		row++;
		final Label label5 = new Label("Ratio score/p-value:");
		label5.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label5);

		final NewExcelReferencePanel columnSelectorRatioScore = new NewExcelReferencePanel(file);
		columnSelectorRatioScore.setEnabled(false);
		setWidget(row, 1, columnSelectorRatioScore);
		useScore.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// enable or disable the selector of the ratio score
				final boolean selected = event.getValue();
				columnSelectorRatioScore.setEnabled(selected);
				// if selected, create the object if not created before
				if (selected && object.getRatioScore() == null) {
					if (previousRatioScore == null) {
						previousRatioScore = new ScoreTypeBean();
					}
					object.setRatioScore(previousRatioScore);
					columnSelectorRatioScore.selectExcelColumn(previousRatioScore.getColumnRef());
				} else {
					previousRatioScore = object.getRatioScore();
					object.setRatioScore(null);
				}
			}
		});
		columnSelectorRatioScore.addExcelColumnsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// set the column ref of the ratio score
				final String columnRef = columnSelectorRatioScore.getColumnRef();
				object.getRatioScore().setColumnRef(columnRef);
			}
		});
		columnSelectorRatioScore.addExcelSheetsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// if sheet is changed, the column ref will be null until selected it
				object.getRatioScore().setColumnRef(null);
			}
		});
	}

}
