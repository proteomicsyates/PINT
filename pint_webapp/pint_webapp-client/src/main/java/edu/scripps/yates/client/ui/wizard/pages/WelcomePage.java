package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.SelectImportConfFilePanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WelcomePage extends AbstractWizardPage {

	private static final String welcomeText = "Welcome to the Project Creator Wizard for PINT\n"
			+ "Here you will be able to upload your proteomics datasets to PINT.";
	private static final String welcomeText2 = "In the bottom of the page you have the navigation buttons in order to move forward and backwards through this process step by step.";
	private static final String welcomeText3 = "The purpose of this is that you can define the experimental design of your project (experimental conditions, MS runs, quantitative ratios, etc...), to be able to upload the files with the actual data and assign them to the proper information in the experimental design.";
	private static final String welcomeText4 = "Click 'Next' to start, or...";

	public WelcomePage() {
		super("Introduction");
	}

	@Override
	protected Widget createPage() {
		final FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName(WizardStyles.wizardWelcome);
		final Label welcomeLabel1 = new Label(welcomeText);
		welcomeLabel1.setStyleName(WizardStyles.WizardWelcomeLabel1);
		flowPanel.add(welcomeLabel1);

		final Label welcomeLabel2 = new Label(welcomeText2);
		welcomeLabel2.setStyleName(WizardStyles.WizardWelcomeLabel2);
		flowPanel.add(welcomeLabel2);

		final Label welcomeLabel3 = new Label(welcomeText3);
		welcomeLabel3.setStyleName(WizardStyles.WizardWelcomeLabel2);
		flowPanel.add(welcomeLabel3);

		final Label welcomeLabel4 = new Label(welcomeText4);
		welcomeLabel4.setStyleName(WizardStyles.WizardBottomMessageLabel);
		flowPanel.add(welcomeLabel4);

		return flowPanel;
	}

	@Override
	public void beforeFirstShow() {
		final SelectImportConfFilePanel panel = new SelectImportConfFilePanel(getWizard());

		final FlowPanel flowPanel = (FlowPanel) asWidget();
		flowPanel.add(panel);
		panel.getElement().getStyle().setMarginTop(20, Unit.PX);
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
