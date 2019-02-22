package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.event.NavigationEvent;
import edu.scripps.yates.client.ui.wizard.pages.panels.ExcelProcessorPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.InputFileSummaryPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel.WizardQuestionPanelButtons;
import edu.scripps.yates.client.ui.wizard.pages.widgets.AbstractConditionSelectorForFileWidget;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ConditionSelectorForFileWithNORatiosWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean;

public class WizardPageExcelFileProcessor extends AbstractWizardPageFileProcessor {

	private WizardQuestionPanel questionPanel;
	private final String sheetName;

	public WizardPageExcelFileProcessor(PintContext context, int fileNumber, FileTypeBean file, String excelSheet) {
		super(context, fileNumber, fileNumber + "-" + file.getName() + "-" + excelSheet, file, excelSheet);
		this.sheetName = excelSheet;
	}

	@Override
	protected String getText1() {
		return "Processing Sheet '" + sheetName + "' from Excel file " + getFileNumber() + "/"
				+ PintImportCfgUtil.getFiles(context.getPintImportConfiguration()).size() + " '" + getFile().getName()
				+ "'";
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
						.getConditionsAssociatedWithExcelFile(context.getPintImportConfiguration(), getFile().getId(),
								sheetName);
				if (conditionsAssociatedWithFile != null) {
					for (final ExperimentalConditionTypeBean condition : conditionsAssociatedWithFile) {
						inputFileSummaryPanel.addAssociatedCondition(condition);
					}
				}
				// show the panel to select conditions
				final AbstractConditionSelectorForFileWidget conditionSelector = createConditionSelectorPanel(
						getFile());
				if (conditionSelector != null) {
					addNextWidget(conditionSelector);
				}

				// show excel processorpanel
				final ExcelProcessorPanel excelProcessor = new ExcelProcessorPanel(getContext(), getFile(), sheetName);
				// add the conditions already setup in the conditionSelector
				final List<ExperimentalConditionTypeBean> conditions = conditionSelector.getConditions();
				for (final ExperimentalConditionTypeBean condition : conditions) {
					excelProcessor.addCondition(condition);
				}

				conditionSelector.addOnConditionAddedTask(new DoSomethingTask2<ExperimentalConditionTypeBean>() {

					@Override
					public Void doSomething(ExperimentalConditionTypeBean condition) {
						excelProcessor.addCondition(condition);
						return null;
					}
				});
				conditionSelector.addOnConditionRemovedTask(new DoSomethingTask2<ExperimentalConditionTypeBean>() {

					@Override
					public Void doSomething(ExperimentalConditionTypeBean condition) {
						excelProcessor.removeCondition(condition);
						return null;
					}
				});
				addNextWidget(excelProcessor);
//
				updateNextButtonState();

				return null;
			}
		});

	}

	@Override
	public boolean isReadyForNextStep() {
		return true;
	}

	@Override
	public void beforeNext(NavigationEvent event) {
		try {
			checkValidity();
		} catch (final PintException e) {
			event.cancel();
			StatusReportersRegister.getInstance().notifyStatusReporters(e);
		}
		super.beforeNext(event);
	}

	public void checkValidity() throws PintException {
		final List<String> errors = new ArrayList<String>();
		try {
			final List<ExperimentalConditionTypeBean> conditionsAssociatedWithFile = PintImportCfgUtil
					.getConditionsAssociatedWithExcelFile(getPintImportConfg(), getFile().getId(), sheetName);
			final boolean empty = conditionsAssociatedWithFile.isEmpty();
			if (empty) {
				errors.add("You need to associate this file with at least one Experimental Condition");
			}
			for (final ExperimentalConditionTypeBean condition : conditionsAssociatedWithFile) {
				if (!isValid(condition.getIdentificationInfo()).isEmpty()) {
					errors.addAll(isValid(condition.getIdentificationInfo()));
				}
			}
			final Set<IdentificationExcelTypeBean> excelIDs = PintImportCfgUtil
					.getExcelIDAssociatedWithThisFile(getPintImportConfg(), getFile().getId(), sheetName);
			for (final IdentificationExcelTypeBean excelID : excelIDs) {
				if (excelID.getSequence() != null) {
					if (!isValid(excelID.getProteinAccession()).isEmpty()) {
						errors.addAll(isValid(excelID.getProteinAccession()));
						errors.add(
								"If you want to import Peptides from an excel file, they have to be associated with a column for a protein accession");

					}
				}
				if (excelID.getPsmId() != null) {
					if (!isValid(excelID.getProteinAccession()).isEmpty()) {
						errors.addAll(isValid(excelID.getProteinAccession()));
						errors.add(
								"If you want to import PSMs from an excel file, they have to be associated with a column for a protein accession");
					}
				}
			}
		} finally {
			if (!errors.isEmpty()) {
				final StringBuilder sb = new StringBuilder();
				sb.append("Fix the following errors before continuing:\n");
				for (final String error : errors) {
					sb.append(error + "\n");
				}

				throw new PintException(sb.toString(), PINT_ERROR_TYPE.IMPORT_XML_SCHEMA_ERROR);
			}
		}
	}

	private List<String> isValid(IdentificationInfoTypeBean identificationInfo) {
		final List<String> ret = new ArrayList<String>();
		if (identificationInfo == null) {
			ret.add("You have to define either proteins, peptides or PSMs in the Excel file");
		}
		return ret;
	}

	private List<String> isValid(ProteinAccessionTypeBean proteinAccession) {
		final List<String> ret = new ArrayList<String>();
		if (proteinAccession == null || proteinAccession.getColumnRef() == null) {
			ret.add("Protein accession column not defined");
			return ret;
		}

		if (proteinAccession.isGroups() && proteinAccession.getGroupSeparator() == null) {
			ret.add("Protein accession separator is missing");
		}
		return ret;
	}

	@Override
	protected ConditionSelectorForFileWithNORatiosWidget createConditionSelectorPanel(FileTypeBean file) {
		// show the panel to select conditions

		final DoSomethingTask2<ExperimentalConditionTypeBean> onConditionAddedTask = new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {

				getInputFileSummaryPanel().addAssociatedCondition(condition);
				final IdentificationExcelTypeBean excelID = new IdentificationExcelTypeBean();

				PintImportCfgUtil.addExcelIdentificationToCondition(getPintImportConfg(), condition.getId(), excelID);
				updateNextButtonState();
				return null;
			}
		};
		final DoSomethingTask2<ExperimentalConditionTypeBean> onConditionRemovedTask = new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {

				getInputFileSummaryPanel().removeAssociatedCondition(condition);
				PintImportCfgUtil.removeFileFromCondition(file.getId(), sheetName, condition);
				updateNextButtonState();
				return null;
			}
		};
		final ConditionSelectorForFileWithNORatiosWidget conditionSelector = new ConditionSelectorForFileWithNORatiosWidget(
				getContext(), file, onConditionAddedTask, onConditionRemovedTask, sheetName);
		conditionSelector.setWidth("");
		return conditionSelector;
	}

	@Override
	public void beforeShow() {
		super.beforeShow();// this sets the widget index to add nextWidgets

		// create the question
		questionPanel = new WizardQuestionPanel(getQuestion(), WizardStyles.WizardExplanationLabel, getExplanation(),
				WizardStyles.WizardQuestionLabel, WizardQuestionPanelButtons.NONE);

		questionPanel.getElement().getStyle().setPadding(20, Unit.PX);
		questionPanel.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		questionPanel.setHorizontalAlignmentForExplanation(HasHorizontalAlignment.ALIGN_LEFT);
		questionPanel.setHorizontalAlignmentForQuestion(HasHorizontalAlignment.ALIGN_LEFT);
//		questionPanel.getElement().getStyle().setWidth(690, Unit.PX);
		// show questionPanel
		addNextWidget(questionPanel);

		super.updateNextButtonState();
	}

	public String getQuestion() {
		return "Processing Excel files requires a little bit more information than other well-known formatted files. However, don't be scared.\n"
				+ "Excel files can contain Proteins, peptides, PSMs, scores, ratios, etc., so you will have to tell PINT which columns you want to extract the information from.";

	}

	public String getExplanation() {
		return "First of all, you have to define under which Experimental Conditions the data in this file was analyzed.\n"
				+ "Then, you select the items (protein, proteins scores, peptides, etc...) you want to extract and fill the corresponding right panels that will appear.";
	}

}
