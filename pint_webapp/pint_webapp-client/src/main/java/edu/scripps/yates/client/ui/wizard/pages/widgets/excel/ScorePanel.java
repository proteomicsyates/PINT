package edu.scripps.yates.client.ui.wizard.pages.widgets.excel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.pages.widgets.NewExcelReferenceWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ExtendedTextBox;
import edu.scripps.yates.client.util.TextChangeEvent;
import edu.scripps.yates.client.util.TextChangeEventHandler;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;

public class ScorePanel extends ExcelObjectPanel<ScoreTypeBean> {
	private DoSomethingTask2<ScoreTypeBean> onDeletePeptideScoreTask;
	private Label label0;
	private NewExcelReferenceWidget peptideSequenceSelector;
	private ExtendedTextBox scoreDescriptionText;
	private ExtendedTextBox scoreTypeText;
	private ExtendedTextBox scoreNameText;
	private final boolean showDeleteButton;

	public ScorePanel(String itemName, PintContext context, FileTypeBean file, String excelSheet, ScoreTypeBean score,
			boolean showDeleteButton) {
		super(context, file, excelSheet, score);
		this.label0.setText("Column for " + itemName + " score:");
		this.showDeleteButton = showDeleteButton;
	}

	@Override
	protected void init() {
		setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		int row = 0;

		//
		if (showDeleteButton) {
			final Label deleteLabel = new Label("delete");
			deleteLabel.setStyleName(WizardStyles.WizardButtonSmall);
			deleteLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (onDeletePeptideScoreTask != null) {
						onDeletePeptideScoreTask.doSomething(object);
					}
					removeFromParent();
				}
			});
			setWidget(row, 1, deleteLabel);
			getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
			//
			row++;
		}
		label0 = new Label("Column for score:");
		label0.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label0);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		peptideSequenceSelector = new NewExcelReferenceWidget(file, getExcelSheet());
		setWidget(row, 1, peptideSequenceSelector);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);

		peptideSequenceSelector.addExcelSheetsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// if sheet is changed, the column ref will be null until selected it
				object.setColumnRef(null);
			}
		});
		scoreNameText = new ExtendedTextBox();
		peptideSequenceSelector.addExcelColumnsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// set the column ref of the ratio score
				final String columnRef = peptideSequenceSelector.getColumnRef();
				object.setColumnRef(columnRef);
				// set score name as the column name
				scoreNameText.setValue(peptideSequenceSelector.getColumnNameWithNoLetter(), true);
				object.setScoreName(scoreNameText.getValue());
			}
		});
		//
		row++;
		final Label label1 = new Label("Score name:");
		label1.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label1);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		setWidget(row, 1, scoreNameText);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		scoreNameText.addTextChangeEventHandler(new TextChangeEventHandler() {

			@Override
			public void onTextChange(TextChangeEvent event) {
				object.setScoreName(scoreNameText.getValue());

			}
		});
		if (object.getScoreName() != null) {
			scoreNameText.setValue(object.getScoreName());
		}
		//
		row++;
		final Label label2 = new Label("Score type:");
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label2);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		scoreTypeText = new ExtendedTextBox();
		if (object.getScoreType() != null) {
			scoreTypeText.setValue(object.getScoreType());
		}
		setWidget(row, 1, scoreTypeText);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		scoreTypeText.addTextChangeEventHandler(new TextChangeEventHandler() {

			@Override
			public void onTextChange(TextChangeEvent event) {
				object.setScoreType(scoreTypeText.getValue());

			}
		});
		//
		row++;
		final Label label3 = new Label("Score description:");
		label3.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label3);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		scoreDescriptionText = new ExtendedTextBox();
		if (object.getDescription() != null) {
			scoreDescriptionText.setValue(object.getDescription());
		}
		setWidget(row, 1, scoreDescriptionText);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		scoreDescriptionText.addTextChangeEventHandler(new TextChangeEventHandler() {

			@Override
			public void onTextChange(TextChangeEvent event) {
				object.setDescription(scoreDescriptionText.getValue());

			}
		});
	}

	public void addOnDeletePeptideScoreTask(DoSomethingTask2<ScoreTypeBean> doSomethingTask2) {
		this.onDeletePeptideScoreTask = doSomethingTask2;
	}

	@Override
	public boolean isReady() {
		if (isEmpty(object.getColumnRef())) {
			return false;
		}
		if (isEmpty(object.getScoreName())) {
			return false;
		}
		return true;
	}

	@Override
	protected void updateGUIFromContext() {
		this.peptideSequenceSelector.selectExcelColumn(object.getColumnRef());
		this.scoreNameText.setValue(object.getScoreName());
		this.scoreDescriptionText.setValue(object.getDescription());
		this.scoreTypeText.setValue(object.getScoreType());
	}

}
