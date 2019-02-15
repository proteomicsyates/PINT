package edu.scripps.yates.client.ui.wizard.pages.widgets.excel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;

public class ScoresPanel extends ExcelObjectPanel<List<ScoreTypeBean>> {
	private int rowForScores;
	private List<ScorePanel> scorePanels;

	public ScoresPanel(String itemName, PintContext context, FileTypeBean file, String excelSheet,
			List<ScoreTypeBean> scores) {
		super(itemName, context, file, excelSheet, scores);
	}

	@Override
	protected void init() {

		int row = 0;

		final Label label00 = new Label(getName() + " scores in Excel file:");
		label00.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(row, 0, label00);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		// add new score
		row++;
		final Label addNewScore = new Label("Add new score");
		addNewScore.addStyleName(WizardStyles.WizardButtonSmall);
		setWidget(row, 0, addNewScore);
		addNewScore.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final ScoreTypeBean newscoreObj = new ScoreTypeBean();
				final ScorePanel scorePanel = new ScorePanel(getName().toLowerCase(), getContext(), file,
						getExcelSheet(), newscoreObj, true);
				setWidget(rowForScores + object.size() + 1, 0, scorePanel);
				object.add(newscoreObj);
				scorePanels.add(scorePanel);
			}
		});
		//
		rowForScores = row++;
		for (final ScoreTypeBean score : object) {

			final ScorePanel scorePanel = new ScorePanel(getName().toLowerCase(), getContext(), file, getExcelSheet(),
					score, true);
			scorePanel.addOnDeletePeptideScoreTask(new DoSomethingTask2<ScoreTypeBean>() {

				@Override
				public Void doSomething(ScoreTypeBean score) {
					object.remove(score);
					return null;
				}
			});
			setWidget(row, 0, scorePanel);
			getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
			row++;
			if (scorePanels == null) {
				scorePanels = new ArrayList<ScorePanel>();
			}
			this.scorePanels.add(scorePanel);
		}
	}

	@Override
	public boolean isReady() {
		for (final ScoreTypeBean score : object) {
			if (isEmpty(score.getColumnRef())) {
				return false;
			}
			if (isEmpty(score.getScoreName())) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void updateGUIFromContext() {
		for (final ScorePanel scorePanel : this.scorePanels) {
			scorePanel.updateGUIFromContext();
		}

	}

}
