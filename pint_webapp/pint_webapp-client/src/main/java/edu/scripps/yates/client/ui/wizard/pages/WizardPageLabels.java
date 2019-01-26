package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.LabelsPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPageLabels extends AbstractWizardPage {

	private static final String text1 = "In case some of the input files contain encoded quantification information encoded with quantitative labels"
			+ "you may have to define them here.";
	private static final String text2 = "A label will be associated to each of the sample items if necessary but it is not mandatory.";
	private static final String text3 = "For example, census-out.txt files may contain quantitative ratios LIGHT over HEAVY and the system will have to know which sample is the one "
			+ "labelled as LIGHT and which sample is labelled as HEAVY.";
	private FlexTable panel;

	public WizardPageLabels() {
		super("Isobaric label");

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
		ret.add(panel);
		return ret;
	}

	@Override
	public void beforeFirstShow() {
		final LabelsPanel labelsPanel = new LabelsPanel(getWizard());
		super.registerItemPanel(labelsPanel);

		panel.setWidget(3, 0, labelsPanel);
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
