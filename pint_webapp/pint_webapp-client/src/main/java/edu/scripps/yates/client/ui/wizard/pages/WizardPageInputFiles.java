package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.WizardPageHelper;
import edu.scripps.yates.client.ui.wizard.pages.panels.NewSelectInputFilesPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileFormat;

public class WizardPageInputFiles extends AbstractWizardPage {

	private FlexTable panel;
	private NewSelectInputFilesPanel selectInputFiles;

	public WizardPageInputFiles() {
		super("Input files");
	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		ret.setWidget(panel);
		panel.setStyleName(WizardStyles.wizardRegularPage);

		String text1 = "Now, you need to upload the input files you want to use.<br>" + "Supported input files are: "
				+ "<ul>";
		for (final FileFormat format : FileFormat.values()) {
			text1 += "<li>" + format.getName() + "</li>";
		}
		text1 += "</ul>";

		final InlineHTML label1 = new InlineHTML(text1);
		label1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(0, 0, label1);

		return ret;
	}

	@Override
	public void onPageAdd(WizardPageHelper<PintContext> helper) {
		super.onPageAdd(helper);
		// select input files
		selectInputFiles = new NewSelectInputFilesPanel(getWizard());
		panel.setWidget(1, 0, selectInputFiles);
	}

	@Override
	public void beforeFirstShow() {
		selectInputFiles.setUploadURL(getImportID());
		super.beforeFirstShow();
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
