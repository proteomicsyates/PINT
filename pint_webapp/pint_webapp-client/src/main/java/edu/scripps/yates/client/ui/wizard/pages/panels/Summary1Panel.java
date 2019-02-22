package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.style.Color;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.WizardPage.PageID;
import edu.scripps.yates.client.ui.wizard.pages.AbstractWizardPage;
import edu.scripps.yates.client.ui.wizard.pages.PageIDController;
import edu.scripps.yates.client.ui.wizard.pages.PageTitleController;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageConditions;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageInputFiles;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageMSRuns;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageSamples;
import edu.scripps.yates.client.ui.wizard.pages.panels.summary.ConditionSummaryTable;
import edu.scripps.yates.client.ui.wizard.pages.panels.summary.InputFileSummaryTable;
import edu.scripps.yates.client.ui.wizard.pages.panels.summary.MSRunSummaryTable;
import edu.scripps.yates.client.ui.wizard.pages.panels.summary.SampleSummaryTable;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class Summary1Panel extends FlexTable {
	private final Wizard<PintContext> wizard;

	public Summary1Panel(Wizard<PintContext> wizard) {
		this.wizard = wizard;

		init();
	}

	private HorizontalPanel getHorizontalLine() {
		final HorizontalPanel horizontalLine1 = new HorizontalPanel();
		horizontalLine1.getElement().getStyle().setBackgroundColor(Color.LIGHT_GRAY.getHexValue());
		horizontalLine1.setWidth("100%");
		horizontalLine1.getElement().getStyle().setHeight(2, Unit.PX);
		horizontalLine1.getElement().getStyle().setMarginTop(5, Unit.PX);
		horizontalLine1.getElement().getStyle().setMarginBottom(5, Unit.PX);
		return horizontalLine1;
	}

	private void init() {
		int row = 0;
		// line 0
		// intput files
		final List<FileTypeBean> files = PintImportCfgUtil.getFiles(wizard.getContext().getPintImportConfiguration());

		setWidget(row, 0, getPanel(files.size(), "Input file", WizardPageInputFiles.class));
		getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final VerticalPanel verticalPanelInputFiles = new VerticalPanel();
		verticalPanelInputFiles.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		setWidget(row, 1, verticalPanelInputFiles);
		getFlexCellFormatter().setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
		int numInputFile = 1;
		for (final FileTypeBean file : files) {
			final FlexTable inputFileSummaryTable = new InputFileSummaryTable(wizard.getContext(), file, numInputFile++,
					false);
			verticalPanelInputFiles.add(inputFileSummaryTable);
		}

		// line 1
		row++;
		setWidget(row, 0, getHorizontalLine());
		getFlexCellFormatter().setColSpan(row, 0, 2);
		// conditions
		row++;
		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
				.getConditions(wizard.getContext().getPintImportConfiguration());

		setWidget(row, 0, getPanel(conditions.size(), "Experimental condition", WizardPageConditions.class));
		getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final VerticalPanel verticalPanelConditions = new VerticalPanel();
		verticalPanelConditions.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		setWidget(row, 1, verticalPanelConditions);
		getFlexCellFormatter().setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
		int numCondition = 1;
		for (final ExperimentalConditionTypeBean condition : conditions) {
			final FlexTable conditionTable = new ConditionSummaryTable(wizard.getContext(), condition, numCondition++);
			verticalPanelConditions.add(conditionTable);
		}
		// line 2
		row++;
		setWidget(row, 0, getHorizontalLine());
		getFlexCellFormatter().setColSpan(row, 0, 2);
		// samples
		row++;
		final List<SampleTypeBean> samples = PintImportCfgUtil
				.getSamples(wizard.getContext().getPintImportConfiguration());

		setWidget(row, 0, getPanel(samples.size(), "Sample", WizardPageSamples.class));
		getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final VerticalPanel verticalPanelSamples = new VerticalPanel();
		verticalPanelSamples.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		setWidget(row, 1, verticalPanelSamples);
		getFlexCellFormatter().setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
		int numSample = 1;
		for (final SampleTypeBean sample : samples) {
			final FlexTable sampleTable = new SampleSummaryTable(wizard.getContext(), sample, numSample++);
			verticalPanelSamples.add(sampleTable);
		}
		// line 3
		row++;
		setWidget(row, 0, getHorizontalLine());
		getFlexCellFormatter().setColSpan(row, 0, 2);
		// ms runs
		row++;
		final List<MsRunTypeBean> msRuns = PintImportCfgUtil
				.getMSRuns(wizard.getContext().getPintImportConfiguration());

		setWidget(row, 0, getPanel(msRuns.size(), "Experiment/Replicate", WizardPageMSRuns.class));
		getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final VerticalPanel verticalPanelMSRuns = new VerticalPanel();
		verticalPanelMSRuns.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		setWidget(row, 1, verticalPanelMSRuns);
		getFlexCellFormatter().setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
		int numMSRun = 1;
		for (final MsRunTypeBean msRun : msRuns) {
			final FlexTable sampleTable = new MSRunSummaryTable(wizard.getContext(), msRun, numMSRun++);
			verticalPanelMSRuns.add(sampleTable);
		}
	}

	private FlexTable getPanel(int number, String name, Class<? extends AbstractWizardPage> clazz) {
		final FlexTable table = new FlexTable();
		table.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		table.getElement().getStyle().setWidth(100, Unit.PCT);

		final Label label1 = new Label(String.valueOf(number));
		label1.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		final HorizontalPanel flowp = new HorizontalPanel();

		flowp.setCellHorizontalAlignment(label1, HasHorizontalAlignment.ALIGN_RIGHT);
		String nameString = name;
		if (number > 1) {
			nameString += "s";// make plural
		}
		nameString += ":";
		final Label label2 = new Label(nameString);
		label2.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		label2.getElement().getStyle().setPaddingLeft(3, Unit.PX);
		flowp.add(label2);
		flowp.add(label1);
		flowp.setCellHorizontalAlignment(label2, HasHorizontalAlignment.ALIGN_LEFT);
		flowp.getElement().getStyle().setPaddingTop(10, Unit.PX);
		table.setWidget(0, 0, flowp);
		// row 2
		// button to go to the page to create these items
		final PageID pageIDByPageClass = PageIDController.getPageIDByPageClass(clazz);
		final String pageTitle = PageTitleController.getPageTitleByPageID(pageIDByPageClass);
		final Label jumpToPageButton = new Label("Go to " + pageTitle);
		jumpToPageButton.setTitle("Click here to go to " + pageTitle + " wizard page");
		jumpToPageButton.setStyleName(WizardStyles.WizardJumpToPage);
		jumpToPageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// because we don't want to validate this page now, we skip the validation
				// occurring in BeforeNext
				final boolean fireBeforeNext = false;
				wizard.showPage(pageIDByPageClass, fireBeforeNext, true, true);
			}
		});
		table.setWidget(1, 0, jumpToPageButton);
		return table;
	}
}
