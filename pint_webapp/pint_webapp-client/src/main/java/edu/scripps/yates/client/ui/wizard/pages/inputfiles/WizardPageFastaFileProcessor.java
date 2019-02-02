package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.pages.panels.FastaDigestionPanelNew;
import edu.scripps.yates.client.ui.wizard.pages.panels.InputFileSummaryPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.pages.widgets.AbstractConditionSelectorForFileWidget;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageFastaFileProcessor extends AbstractWizardPageFileProcessor {

	public WizardPageFastaFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(context, fileNumber, file);
	}

	@Override
	public String getText2() {
		return "Fasta files are used to map peptides to proteins because sometimes the input files don't contain all the proteins mapping to the identified peptides (depending on how the software generating that file works).";
	}

	@Override
	public void beforeShow() {

		//
		updateNextButtonState();
		super.beforeShow();
	}

	@Override
	protected void setOnFileSummaryReceivedTask(InputFileSummaryPanel inputFileSummaryPanel) {
		if (wizard.getContext().isUseFasta() == null) {
			final String question = "Do you want to use this file?";
			final String explanation = "If so, we will need to ask you about how to in-silico cleave the proteins to get the peptides. Otherwise, it will be ignored.";
			final ClickHandler yesClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					context.setUseFasta(true);
					// show what we have
					addNextWidget(new FastaDigestionPanelNew(getFile()));
					updateNextButtonState();
				}
			};
			final ClickHandler noClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					context.setUseFasta(false);
					updateNextButtonState();
					wizard.showNextPage();
				}
			};
			final WizardQuestionPanel questionPanel = new WizardQuestionPanel(question, explanation);
			questionPanel.addNoClickHandler(noClickHandler);
			questionPanel.addYesClickHandler(yesClickHandler);
			addNextWidget(questionPanel);

			// disable next button
			wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, false);
			wizard.setButtonOverride(true);
		} else {
			if (wizard.getContext().isUseFasta()) {
				// show what we have
				addNextWidget(new FastaDigestionPanelNew(getFile()));
			} else {
				// not using the fasta file

			}
		}

	}

	@Override
	protected AbstractConditionSelectorForFileWidget createConditionSelectorPanel(FileTypeBean file2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WizardPageFastaFileProcessor) {
			final WizardPageFastaFileProcessor page2 = (WizardPageFastaFileProcessor) obj;
			if (page2.getPageID().equals(getPageID())) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public boolean isReadyForNextStep() {
		if (context.isUseFasta() == null) {
			return false;
		}
		if (!context.isUseFasta()) {
			return true;
		} else {
			if (getFile().getFastaDigestion().getCleavageAAs() == null
					&& "".equals(getFile().getFastaDigestion().getCleavageAAs())) {
				return false;
			}
			if (getFile().getFastaDigestion().getMisscleavages() < 0) {
				return false;
			}
			return true;
		}
	}
}
