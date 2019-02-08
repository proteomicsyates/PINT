package edu.scripps.yates.client.ui.wizard.pages;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.exception.DuplicatePageException;
import edu.scripps.yates.client.ui.wizard.pages.inputfiles.WizardPageCensusOutFileProcessor;
import edu.scripps.yates.client.ui.wizard.pages.inputfiles.WizardPageDTASelectFileProcessor;
import edu.scripps.yates.client.ui.wizard.pages.inputfiles.WizardPageExcelFileProcessor;
import edu.scripps.yates.client.ui.wizard.pages.inputfiles.WizardPageFastaFileProcessor;
import edu.scripps.yates.client.ui.wizard.pages.inputfiles.WizardPageMzIdentMLFileProcessor;
import edu.scripps.yates.client.ui.wizard.pages.panels.Summary1Panel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageSummary1 extends AbstractWizardPage {

	private static final String text1 = "Here is a summary of what we have so far:";

	private static final String welcomeText2 = "Here you can review the information defined up to now and you can jump to each section in order to edit some information.";
	private static final String welcomeText3 = "In any case, you can always come back later to edit all this information.";
	private FlexTable table;

	public WizardPageSummary1() {
		super("Summary of elements");

	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		table = new FlexTable();
		ret.add(table);
		final FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName(WizardStyles.wizardWelcome);
		final Label welcomeLabel1 = new Label(text1);
		welcomeLabel1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		flowPanel.add(welcomeLabel1);

		final Label welcomeLabel2 = new Label(welcomeText2);
		welcomeLabel2.setStyleName(WizardStyles.WizardExplanationLabel);
		flowPanel.add(welcomeLabel2);

		final Label welcomeLabel3 = new Label(welcomeText3);
		welcomeLabel3.setStyleName(WizardStyles.WizardExplanationLabel);
		flowPanel.add(welcomeLabel3);

		table.setWidget(0, 0, flowPanel);

		return ret;
	}

	@Override
	public PageID getPageID() {
		return PageIDController.getPageIDByPageClass(this.getClass());
	}

	@Override
	protected void registerPageTitle(String title) {
		PageTitleController.addPageTitle(this.getPageID(), title);
	}

	@Override
	public void beforeShow() {
		final Summary1Panel summaryPanel = new Summary1Panel(wizard);
		table.setWidget(1, 0, summaryPanel);

		// look for the input files and create a page for each of them
		final List<FileTypeBean> files = PintImportCfgUtil.getFiles(wizard.getContext().getPintImportConfiguration());
		int fileNumber = 1;
		for (final FileTypeBean fileTypeBean : files) {
			try {
				switch (fileTypeBean.getFormat()) {
				case CENSUS_CHRO_XML:
					wizard.addPage(new WizardPageCensusOutFileProcessor(getContext(), fileNumber++, fileTypeBean),
							WizardStyles.activeSmaller, WizardStyles.inactiveSmaller);
					break;
				case CENSUS_OUT_TXT:
					wizard.addPage(new WizardPageCensusOutFileProcessor(getContext(), fileNumber++, fileTypeBean),
							WizardStyles.activeSmaller, WizardStyles.inactiveSmaller);
					break;
				case DTA_SELECT_FILTER_TXT:
					wizard.addPage(new WizardPageDTASelectFileProcessor(getContext(), fileNumber++, fileTypeBean),
							WizardStyles.activeSmaller, WizardStyles.inactiveSmaller);
					break;
				case EXCEL:
					wizard.addPage(new WizardPageExcelFileProcessor(getContext(), fileNumber++, fileTypeBean),
							WizardStyles.activeSmaller, WizardStyles.inactiveSmaller);
					break;
				case FASTA:
					wizard.addPage(new WizardPageFastaFileProcessor(getContext(), fileNumber++, fileTypeBean),
							WizardStyles.activeSmaller, WizardStyles.inactiveSmaller);
					break;
				case MZIDENTML:
					wizard.addPage(new WizardPageMzIdentMLFileProcessor(getContext(), fileNumber++, fileTypeBean),
							WizardStyles.activeSmaller, WizardStyles.inactiveSmaller);
					break;
				default:
					break;
				}

			} catch (final DuplicatePageException e) {
				GWT.log(e.getMessage(), e);
			}
		}
		try {
			wizard.addPage(new WizardPageInputFilesToMSRuns());
		} catch (final DuplicatePageException e) {
			GWT.log(e.getMessage(), e);
		}
		super.beforeShow();
	}

}
