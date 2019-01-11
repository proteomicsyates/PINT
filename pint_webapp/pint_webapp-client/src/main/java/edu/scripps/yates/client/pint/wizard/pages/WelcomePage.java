package edu.scripps.yates.client.pint.wizard.pages;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.WizardPageHelper;
import edu.scripps.yates.client.ui.wizard.event.NavigationEvent;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WelcomePage extends AbstractWizardPage {

	private static final String welcomeText = "Welcome to the Project Creator Wizard for PINT\n"
			+ "Here you will be able to upload your proteomics datasets to PINT.";
	private static final String welcomeText2 = "In the bottom of the page you have the navigation buttons in order to move forward through this process step by step.";
	private static final String welcomeText3 = "The purpose of this is that you can define the experimental design of your project (experimental conditions, MS runs, quantitative ratios, etc...) and to be able to upload the files with the actual data and assign them to the proper information in the experimental design.";
	private static final String welcomeText4 = "Click 'Next' to start.";

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
		welcomeLabel2.setStyleName(WizardStyles.WizardRegularText);
		flowPanel.add(welcomeLabel2);

		final Label welcomeLabel3 = new Label(welcomeText3);
		welcomeLabel3.setStyleName(WizardStyles.WizardRegularText);
		flowPanel.add(welcomeLabel3);

		final Label welcomeLabel4 = new Label(welcomeText4);
		welcomeLabel4.setStyleName(WizardStyles.WizardBottomMessageLabel);
		flowPanel.add(welcomeLabel4);
		return flowPanel;
	}

	@Override
	public void onPageAdd(WizardPageHelper<PintContext> helper) {
		GWT.log("onPageAdd" + getClass().getName());
		super.onPageAdd(helper);
	}

	@Override
	public PintContext getContext() {
		GWT.log("getContext");
		return super.getContext();
	}

	@Override
	public Wizard<PintContext> getWizard() {
		GWT.log("getWizard");
		return super.getWizard();
	}

	@Override
	public void beforeFirstShow() {
		GWT.log("beforeFirstShow");
		super.beforeFirstShow();
	}

	@Override
	public void beforeShow() {
		GWT.log("beforeShow");
		super.beforeShow();
	}

	@Override
	public void beforeNext(NavigationEvent event) {
		GWT.log("beforeNext");
		super.beforeNext(event);
	}

	@Override
	public void afterNext() {
		GWT.log("afterNext");
		super.afterNext();
	}
}
