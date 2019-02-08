package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.panels.ExcelProcessorPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.InputFileSummaryPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel.WizardQuestionPanelButtons;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ConditionSelectorForFileWithNORatiosWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageExcelFileProcessor extends AbstractWizardPageFileProcessor {

	private WizardQuestionPanel questionPanel;

	public WizardPageExcelFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(context, fileNumber, file);

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WizardPageExcelFileProcessor) {
			final WizardPageExcelFileProcessor page2 = (WizardPageExcelFileProcessor) obj;
			if (page2.getPageID().equals(getPageID())) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}

	@Override
	protected void setOnFileSummaryReceivedTask(InputFileSummaryPanel inputFileSummaryPanel) {
		inputFileSummaryPanel.setOnFileSummaryReceivedTask(new DoSomethingTask<Void>() {

			@Override
			public Void doSomething() {
				final List<ExperimentalConditionTypeBean> conditionsAssociatedWithFile = PintImportCfgUtil
						.getConditionsAssociatedWithFile(context.getPintImportConfiguration(), getFile().getId());
				if (conditionsAssociatedWithFile != null) {
					for (final ExperimentalConditionTypeBean condition : conditionsAssociatedWithFile) {
						inputFileSummaryPanel.addAssociatedCondition(condition);
					}
				}
				return null;
			}
		});

	}

	@Override
	public boolean isReadyForNextStep() {
		final List<ExperimentalConditionTypeBean> conditionsAssociatedWithFile = PintImportCfgUtil
				.getConditionsAssociatedWithFile(getPintImportConfg(), getFile().getId());
		final boolean empty = conditionsAssociatedWithFile.isEmpty();
		return !empty;
	}

	@Override
	protected ConditionSelectorForFileWithNORatiosWidget createConditionSelectorPanel(FileTypeBean file) {
		return null;
	}

	@Override
	public void beforeShow() {
		super.beforeShow();// this sets the widget index to add nextWidgets

		// create the question
		questionPanel = new WizardQuestionPanel(getExplanation(), WizardStyles.WizardExplanationLabel, getQuestion(),
				WizardStyles.WizardQuestionLabel, WizardQuestionPanelButtons.NONE);

		questionPanel.getElement().getStyle().setPadding(20, Unit.PX);
		questionPanel.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		questionPanel.getElement().getStyle().setWidth(690, Unit.PX);
		// show questionPanel
		addNextWidget(questionPanel);
		addNextWidget(new ExcelProcessorPanel(getContext(), getFile()));

		super.updateNextButtonState();
	}

	public String getQuestion() {
		return "Excel files can contain Proteins, peptides, PSMs, scores, ratios, etc.";

	}

	public String getExplanation() {
		return "But you need to define from which columns can we retrieve them.";
	}
}
