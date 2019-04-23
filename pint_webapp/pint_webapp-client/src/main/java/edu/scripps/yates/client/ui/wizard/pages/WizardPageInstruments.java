package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.InstrumentsPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPageInstruments extends AbstractWizardPage {

	private static final String text1 = "Define the instruments (spectrometers) used to generate this dataset.";
	private static final String text2 = "You can type the name and a list of predefined values will show up.";
	private FlexTable panel;

	public WizardPageInstruments() {
		super("Instruments");

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
		ret.add(panel);
		return ret;
	}

	@Override
	public void beforeFirstShow() {
		final InstrumentsPanel instrumentsPanel = new InstrumentsPanel(getWizard());
		super.registerItemPanel(instrumentsPanel);
		panel.setWidget(3, 0, instrumentsPanel);
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
