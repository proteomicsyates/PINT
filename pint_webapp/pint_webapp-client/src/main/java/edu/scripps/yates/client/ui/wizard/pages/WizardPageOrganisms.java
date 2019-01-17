package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.OrganismsPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPageOrganisms extends AbstractWizardPage {

	private static final String text1 = "Define which organisms (taxonomy) has been analyzed in your samples.";
	private static final String text2 = "You can type the name and a list of predefined values will show up.";
	private static final String text3 = "In case of having a mixed sample, you may need to define a 'mixed' organism in order to be able to associate an organism item to a sample.";
	private FlexTable panel;
	private OrganismsPanel organismPanel;

	public WizardPageOrganisms() {
		super("Sample organism(s)");

	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		panel.setStyleName(WizardStyles.wizardRegularPage);
		final Label welcomeLabel1 = new Label(text1);
		welcomeLabel1.setStyleName(WizardStyles.WizardRegularText);
		panel.setWidget(0, 0, welcomeLabel1);
		final InlineHTML labelExplanation1 = new InlineHTML(text2);
		labelExplanation1.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(1, 0, labelExplanation1);
		final InlineHTML labelExplanation2 = new InlineHTML(text3);
		labelExplanation2.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(2, 0, labelExplanation2);
		ret.add(panel);
		return ret;
	}

	@Override
	public void beforeFirstShow() {
		organismPanel = new OrganismsPanel(getWizard());
		super.registerItemPanel(organismPanel);
		panel.setWidget(3, 0, organismPanel);
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
