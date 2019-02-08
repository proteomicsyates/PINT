package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

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

		final String text1 = "Now, you need to upload the input files you want to use.";
		final Label label1 = new Label(text1);
		label1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(0, 0, label1);
		//
		String text2 = "Supported input files are:  <ul>";
		for (final FileFormat format : FileFormat.values()) {
			if (format == FileFormat.UNKNOWN) {
				continue;
			}
			text2 += "<li>" + format.getName() + "</li>";
		}
		text2 += "</ul>";

		final InlineHTML label2 = new InlineHTML(text2);
		label2.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(1, 0, label2);

		return ret;
	}

	@Override
	public void beforeShow() {

		// select input files
		selectInputFiles = new NewSelectInputFilesPanel(getWizard());
		panel.setWidget(2, 0, selectInputFiles);

		super.beforeShow();
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
