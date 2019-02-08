package edu.scripps.yates.client.ui.wizard.pages;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.pages.panels.InputDataFilesPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.ReferencedMSRunsPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class WizardPageInputFilesToMSRuns extends AbstractWizardPage {

	private static final String text1 = "Ok, we are in the last step of this wizard.";
	private static final String text2 = "Last thing you have to do is to say from which Experiments or Replicates are the data of each of the input files.";
	private static final String text3 = "Why? Because we want to remove any redundancy of proteins or peptides coming from the same experiment but imported with several input files.";
	private static final String text4 = "For example, you may have:"
			+ " an input file (typically an Excel file) containing quantitative ratios calculated after combining several experiments, and you also have a file with the data from each experiment. In this case, you need to assign all the experiments to the Excel file.";
	private FlexTable panel;
	private InputDataFilesPanel inputDataFilesPanel;

	public WizardPageInputFilesToMSRuns() {
		super("Experiment/Replicate to file");

	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		panel.setStyleName(WizardStyles.wizardRegularPage);
		final Label welcomeLabel1 = new Label(text1);
		welcomeLabel1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(0, 0, welcomeLabel1);
		final InlineHTML labelExplanation1 = new InlineHTML(text2);
		labelExplanation1.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(1, 0, labelExplanation1);
		final InlineHTML labelExplanation2 = new InlineHTML(text3);
		labelExplanation2.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(2, 0, labelExplanation2);
		final InlineHTML labelExplanation3 = new InlineHTML(text4);
		labelExplanation3.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(3, 0, labelExplanation3);
		ret.add(panel);

		// because we have another panel of referenced samples at the right at (3,1)
		panel.getFlexCellFormatter().setColSpan(0, 0, 2);
		panel.getFlexCellFormatter().setColSpan(1, 0, 2);
		panel.getFlexCellFormatter().setColSpan(2, 0, 2);
		panel.getFlexCellFormatter().setColSpan(3, 0, 2);
		return ret;
	}

	@Override
	public void beforeShow() {
		// first column
		inputDataFilesPanel = new InputDataFilesPanel(getWizard());
		panel.setWidget(4, 0, inputDataFilesPanel);
		panel.getFlexCellFormatter().setVerticalAlignment(4, 0, HasVerticalAlignment.ALIGN_TOP);
		inputDataFilesPanel.getElement().getStyle().setMarginTop(10, Unit.PX);
		final DoSomethingTask2<MsRunTypeBean> onContextUpdated = new DoSomethingTask2<MsRunTypeBean>() {

			@Override
			public Void doSomething(MsRunTypeBean t) {
				checkNextButtonState();
				return null;
			}
		};
		inputDataFilesPanel.addOnUpdateContextTask(onContextUpdated);
		// second column
		final FlexTable rightColumnTable = new FlexTable();
		panel.setWidget(4, 1, rightColumnTable);
		panel.getFlexCellFormatter().setVerticalAlignment(4, 1, HasVerticalAlignment.ALIGN_TOP);
		panel.getFlexCellFormatter().setWidth(4, 1, "100%");

		final ReferencedMSRunsPanel referencedMSRunPanel = new ReferencedMSRunsPanel(getWizard());
		registerReferencedPanel(referencedMSRunPanel);
		rightColumnTable.setWidget(0, 0, referencedMSRunPanel);
		rightColumnTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		referencedMSRunPanel.getElement().getStyle().setMarginTop(10, Unit.PX);
		referencedMSRunPanel.getElement().getStyle().setHeight(1, Unit.PX);

		super.beforeShow();
	}

	protected void checkNextButtonState() {
		getWizard().getButton(ButtonType.BUTTON_FINISH).setEnabled(isReadyForNextStep());
		getWizard().setButtonOverride(true);

	}

	private boolean isReadyForNextStep() {
		// check that final all id, quant and ratios have an msrunref not empty
		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil.getConditions(getPintImportConfg());
		for (final ExperimentalConditionTypeBean condition : conditions) {
			if (condition.getIdentificationInfo() != null) {
				for (final IdentificationExcelTypeBean excelID : condition.getIdentificationInfo()
						.getExcelIdentInfo()) {
					if (excelID.getMsRunRef() == null || "".equals(excelID.getMsRunRef())) {
						return false;
					}
				}
				for (final RemoteInfoTypeBean remoteID : condition.getIdentificationInfo().getRemoteFilesIdentInfo()) {
					if (remoteID.getMsRunRef() == null || "".equals(remoteID.getMsRunRef())) {
						return false;
					}
				}
			}
			if (condition.getQuantificationInfo() != null) {
				for (final QuantificationExcelTypeBean excelQuant : condition.getQuantificationInfo()
						.getExcelQuantInfo()) {
					if (excelQuant.getMsRunRef() == null || "".equals(excelQuant.getMsRunRef())) {
						return false;
					}
				}
				for (final RemoteInfoTypeBean remoteQuant : condition.getQuantificationInfo()
						.getRemoteFilesQuantInfo()) {
					if (remoteQuant.getMsRunRef() == null || "".equals(remoteQuant.getMsRunRef())) {
						return false;
					}
				}
			}
		}
		final RatiosTypeBean ratios = getPintImportConfg().getProject().getRatios();
		if (ratios != null) {
			if (ratios.getPeptideAmountRatios() != null) {
				for (final ExcelAmountRatioTypeBean excelRatio : ratios.getPeptideAmountRatios().getExcelRatio()) {
					if (excelRatio.getMsRunRef() == null || "".equals(excelRatio.getMsRunRef())) {
						return false;
					}
				}
				for (final RemoteFilesRatioTypeBean remoteRatio : ratios.getPeptideAmountRatios()
						.getRemoteFilesRatio()) {
					if (remoteRatio.getMsRunRef() == null || "".equals(remoteRatio.getMsRunRef())) {
						return false;
					}
				}
			}
			if (ratios.getProteinAmountRatios() != null) {
				for (final ExcelAmountRatioTypeBean excelRatio : ratios.getProteinAmountRatios().getExcelRatio()) {
					if (excelRatio.getMsRunRef() == null || "".equals(excelRatio.getMsRunRef())) {
						return false;
					}
				}
				for (final RemoteFilesRatioTypeBean remoteRatio : ratios.getProteinAmountRatios()
						.getRemoteFilesRatio()) {
					if (remoteRatio.getMsRunRef() == null || "".equals(remoteRatio.getMsRunRef())) {
						return false;
					}
				}
			}
			if (ratios.getPsmAmountRatios() != null) {
				for (final ExcelAmountRatioTypeBean excelRatio : ratios.getPsmAmountRatios().getExcelRatio()) {
					if (excelRatio.getMsRunRef() == null || "".equals(excelRatio.getMsRunRef())) {
						return false;
					}
				}
				for (final RemoteFilesRatioTypeBean remoteRatio : ratios.getPsmAmountRatios().getRemoteFilesRatio()) {
					if (remoteRatio.getMsRunRef() == null || "".equals(remoteRatio.getMsRunRef())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public PageID getPageID() {
		return PageIDController.getPageIDByPageClass(this.getClass());
	}

	@Override
	protected void registerPageTitle(String title) {
		PageTitleController.addPageTitle(this.getPageID(), title);
	}
}
