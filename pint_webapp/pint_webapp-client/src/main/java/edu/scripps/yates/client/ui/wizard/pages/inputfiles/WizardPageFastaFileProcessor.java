package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.pages.panels.FastaDigestionPanelNew;
import edu.scripps.yates.client.ui.wizard.pages.panels.InputFileSummaryPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel.WizardQuestionPanelButtons;
import edu.scripps.yates.client.ui.wizard.pages.widgets.AbstractConditionSelectorForFileWidget;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageFastaFileProcessor extends AbstractWizardPageFileProcessor {

	private WizardQuestionPanel questionPanel;

	public WizardPageFastaFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(context, fileNumber, file);
	}

	@Override
	public String getText2() {
		return "Fasta files are used to map peptides to proteins because sometimes the input files don't "
				+ "contain all the proteins mapping to the identified peptides (depending on how the software generating that file works). "
				+ "You could even submit a peptide-only Excel table and then, using a FASTA file, the peptides would be automatically mapped to the corresponding proteins in the FASTA file.";
	}

	@Override
	public void beforeShow() {
		super.beforeShow(); // this sets the widget index to add nextWidgets
		if (wizard.getContext().isUseFasta() == null) {
			final ClickHandler yesClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					context.setUseFasta(true);
					// hide question panel
					questionPanel.setVisible(false);
					// show what we have
					final FastaDigestionPanelNew fastaDigestionPanel = new FastaDigestionPanelNew(getFile());
					fastaDigestionPanel.setOnDataUpdatedTask(new DoSomethingTask<Void>() {

						@Override
						public Void doSomething() {
							updateNextButtonState();
							return null;
						}
					});
					addNextWidget(fastaDigestionPanel);
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
			final String question = "Do you want to use this FASTA file to map the peptides to proteins?";
			final String explanation = "If so, we will need to ask you about how to in-silico cleave the proteins to get the peptides. Otherwise, it will be ignored.";

			questionPanel = new WizardQuestionPanel(question, explanation);
			questionPanel.addNoClickHandler(noClickHandler);
			questionPanel.addYesClickHandler(yesClickHandler);
			questionPanel.getElement().getStyle().setPadding(20, Unit.PX);
			questionPanel.getElement().getStyle().setWidth(690, Unit.PX);
			questionPanel.getQuestionLabel().getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
			questionPanel.getExplanationLabel().getElement().getStyle().setPaddingTop(0, Unit.PX);
			questionPanel.getExplanationLabel().getElement().getStyle().setPaddingBottom(10, Unit.PX);

			addNextWidget(questionPanel);

			// disable next button
			wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, false);
			wizard.setButtonOverride(true);
		} else {
			if (wizard.getContext().isUseFasta()) {
				// not using the fasta file
				final ClickHandler iChangedMyMindClickHandler = new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						context.setUseFasta(false);
						// hide question panel and hide digestion panel
						clearWidgets(false);
						//
						updateNextButtonState();
					}
				};
				final String question = "You chose to use this FASTA file";
				final String explanation = "If you changed your mind, click on the button and we will ignore the file.";

				final WizardQuestionPanelButtons buttons = WizardQuestionPanelButtons.OK;
				buttons.setButton1Text("I changed my mind");
				questionPanel = new WizardQuestionPanel(question, explanation, buttons);
				questionPanel.addOKClickHandler(iChangedMyMindClickHandler);
				questionPanel.getElement().getStyle().setPadding(20, Unit.PX);
				questionPanel.getElement().getStyle().setWidth(690, Unit.PX);
				questionPanel.getQuestionLabel().getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
				questionPanel.getQuestionLabel().getElement().getStyle().setPaddingTop(0, Unit.PX);
				questionPanel.getExplanationLabel().getElement().getStyle().setPaddingTop(0, Unit.PX);
				questionPanel.getExplanationLabel().getElement().getStyle().setPaddingBottom(10, Unit.PX);
				questionPanel.getOKButton().setWidth("auto");
				addNextWidget(questionPanel);
				// show digestion panel
				final FastaDigestionPanelNew fastaDigestionPanel = new FastaDigestionPanelNew(getFile());
				fastaDigestionPanel.setOnDataUpdatedTask(new DoSomethingTask<Void>() {

					@Override
					public Void doSomething() {
						updateNextButtonState();
						return null;
					}
				});
				addNextWidget(fastaDigestionPanel);
				// disable next button until checking that all information is there
				wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, false);
				wizard.setButtonOverride(true);
			} else {
				// not using the fasta file
				final ClickHandler iChangedMyMindClickHandler = new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						context.setUseFasta(true);
						// hide question panel
						questionPanel.setVisible(false);
						// show what we have
						final FastaDigestionPanelNew fastaDigestionPanel = new FastaDigestionPanelNew(getFile());
						fastaDigestionPanel.setOnDataUpdatedTask(new DoSomethingTask<Void>() {

							@Override
							public Void doSomething() {
								updateNextButtonState();
								return null;
							}
						});
						addNextWidget(fastaDigestionPanel);
						updateNextButtonState();
					}
				};
				final String question = "You chose to not to use this FASTA file";
				final String explanation = "If you changed your mind, click on the button and then we will need to ask you about how to in-silico cleave the proteins to get the peptides. Otherwise, it will be ignored.";

				final WizardQuestionPanelButtons buttons = WizardQuestionPanelButtons.OK;
				buttons.setButton1Text("I changed my mind");
				questionPanel = new WizardQuestionPanel(question, explanation, buttons);
				questionPanel.addOKClickHandler(iChangedMyMindClickHandler);
				questionPanel.getElement().getStyle().setPadding(20, Unit.PX);
				questionPanel.getElement().getStyle().setWidth(690, Unit.PX);
				questionPanel.getQuestionLabel().getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
				questionPanel.getExplanationLabel().getElement().getStyle().setPaddingTop(0, Unit.PX);
				questionPanel.getExplanationLabel().getElement().getStyle().setPaddingBottom(10, Unit.PX);
				questionPanel.getOKButton().setWidth("auto");
				addNextWidget(questionPanel);
				// disable next button
				wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, true);
				wizard.setButtonOverride(true);
			}
		}
		//
		updateNextButtonState();

	}

	@Override
	protected void setOnFileSummaryReceivedTask(InputFileSummaryPanel inputFileSummaryPanel) {
		inputFileSummaryPanel.setOnFileSummaryReceivedTask(new DoSomethingTask<Void>() {

			@Override
			public Void doSomething() {
				inputFileSummaryPanel.showAssociatedConditionsRow(false);
				return null;
			}
		});

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
			if (getFile().getFastaDigestion() == null) {
				return false;
			}
			if (getFile().getFastaDigestion().getCleavageAAs() == null
					|| "".equals(getFile().getFastaDigestion().getCleavageAAs())) {
				return false;
			}
			if (getFile().getFastaDigestion().getMisscleavages() < 0) {
				return false;
			}
			return true;
		}
	}
}
