package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.pages.AbstractWizardPage;
import edu.scripps.yates.client.ui.wizard.pages.PageIDController;
import edu.scripps.yates.client.ui.wizard.pages.PageTitleController;
import edu.scripps.yates.client.ui.wizard.pages.panels.InputFileSummaryPanel;
import edu.scripps.yates.client.ui.wizard.pages.widgets.AbstractConditionSelectorForFileWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileSummary;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public abstract class AbstractWizardPageFileProcessor extends AbstractWizardPage {

	private FlexTable panel;
	private final FileTypeBean file;

	private boolean isReadyForNextStep = false;
	private int rowForNextPanel;
	private InputFileSummaryPanel inputFileSummaryPanel;
	private final int fileNumber;
	private int rowForNextPanelFixed;

	public AbstractWizardPageFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(fileNumber + "-" + file.getName(), context);
		this.file = file;
		this.fileNumber = fileNumber;
	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		ret.add(panel);
		panel.setStyleName(WizardStyles.wizardRegularPage);
		//
		int row = 0;
		final Label welcomeLabel1 = new Label(getText1());
		welcomeLabel1.setStyleName(getText1StyleName());
		panel.setWidget(row, 0, welcomeLabel1);
		//
		if (getText2() != null) {
			row++;
			final Label welcomeLabel2 = new Label(getText2());
			welcomeLabel2.setStyleName(getText2StyleName());
			panel.setWidget(row, 0, welcomeLabel2);
		}
		//
		if (getText3() != null) {
			row++;
			final Label welcomeLabel3 = new Label(getText3());
			welcomeLabel3.setStyleName(getText3StyleName());
			panel.setWidget(row, 0, welcomeLabel3);
		}
		rowForNextPanel = ++row;
		rowForNextPanelFixed = rowForNextPanel;
		return ret;
	}

	/**
	 * Override if you want different style for text1 than
	 * {@link WizardStyles}.WizardWelcomeLabel2
	 * 
	 * @return
	 */
	protected String getText1StyleName() {
		return WizardStyles.WizardWelcomeLabel2;
	}

	/**
	 * Override if you want different style for text2 than
	 * {@link WizardStyles}.WizardExplanationLabel
	 * 
	 * @return
	 */
	protected String getText2StyleName() {
		return WizardStyles.WizardExplanationLabel;
	}

	/**
	 * Override if you want different style for text3 than
	 * {@link WizardStyles}.WizardExplanationLabel
	 * 
	 * @return
	 */
	private String getText3StyleName() {
		return WizardStyles.WizardExplanationLabel;
	}

	private String getText1() {
		return "Processing input file " + fileNumber + "/"
				+ PintImportCfgUtil.getFiles(context.getPintImportConfiguration()).size() + " '" + file.getName() + "'";
	}

	/**
	 * Override for adding a text below text1
	 * 
	 * @return
	 */
	protected String getText2() {
		return null;
	}

	/**
	 * Override for adding a text below text2
	 * 
	 * @return
	 */
	protected String getText3() {
		return null;
	}

	@Override
	final public PageID getPageID() {
		return PageIDController.getPageIDForInputFileProcessor(getTitle());
	}

	@Override
	final protected void registerPageTitle(String title) {
		PageTitleController.addPageTitle(this.getPageID(), title);
	}

	@Override
	public void beforeShow() {
		clearWidgets(true);
		rowForNextPanel = rowForNextPanelFixed;
		inputFileSummaryPanel = new InputFileSummaryPanel(wizard.getContext(), getFile());
		addNextWidget(inputFileSummaryPanel);
		setOnFileSummaryReceivedTask(inputFileSummaryPanel);

		// disable next button
		wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, false);
		wizard.setButtonOverride(true);
		//
		updateNextButtonState();
		super.beforeShow();
	}

	/**
	 * Override if you want other thing to happen different than adding the
	 * {@link AbstractConditionSelectorForFileWidget} below the input file
	 * summary.<br>
	 * To override, create a {@link DoSomethingTask} and call to
	 * setOnFilesummaryReceivedTask on the {@link InputFileSummaryPanel}
	 * 
	 * @param inputFileSummaryPanel
	 */
	protected void setOnFileSummaryReceivedTask(InputFileSummaryPanel inputFileSummaryPanel) {
		final DoSomethingTask<Void> task = new DoSomethingTask<Void>() {

			@Override
			public Void doSomething() {
				// show the panel to select conditions
				final AbstractConditionSelectorForFileWidget conditionSelector = createConditionSelectorPanel(
						getFile());
				if (conditionSelector != null) {
					addNextWidget(conditionSelector);
				}

				updateNextButtonState();
				return null;
			}
		};
		inputFileSummaryPanel.setOnFileSummaryReceivedTask(task);
	}

	protected void clearWidgets(boolean clearAlsoFirstPanel) {

		int i = 1;
		if (clearAlsoFirstPanel) {
			i = 0;
		}
		while (rowForNextPanelFixed + i < panel.getRowCount()) {
			panel.clearCell(rowForNextPanelFixed + i, 0);
			i++;
		}
	}

	protected void addNextWidget(Widget widget) {
		panel.setWidget(rowForNextPanel++, 0, widget);
	}

	/**
	 * Create a new {@link AbstractConditionSelectorForFileWidget} and then call to
	 * setOnConditionAddedTask and setOnConditionRemovedTask to add the
	 * corresponding tasks.<br>
	 * This method is usually called after receiving the {@link FileSummary}.
	 * 
	 * @param file2
	 * @return
	 */
	protected abstract AbstractConditionSelectorForFileWidget createConditionSelectorPanel(FileTypeBean file2);

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractWizardPageFileProcessor) {
			final AbstractWizardPageFileProcessor page2 = (AbstractWizardPageFileProcessor) obj;
			if (page2.getPageID().equals(getPageID())) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}

	protected void updateNextButtonState() {
		final boolean readyForNextStep = isReadyForNextStep();
		this.wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, readyForNextStep);
	}

	/**
	 * Can be override to check whether it is ready for next step or not
	 * 
	 * @return
	 */
	public boolean isReadyForNextStep() {
		return isReadyForNextStep;
	}

	protected InputFileSummaryPanel getInputFileSummaryPanel() {
		return inputFileSummaryPanel;
	}

	public FileTypeBean getFile() {
		return file;
	}

	public void setReadyForNextStep(boolean isReadyForNextStep) {
		this.isReadyForNextStep = isReadyForNextStep;
	}
}
