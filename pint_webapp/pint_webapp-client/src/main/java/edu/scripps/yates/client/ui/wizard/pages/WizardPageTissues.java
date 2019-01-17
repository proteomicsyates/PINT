package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.TissuesPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPageTissues extends AbstractWizardPage {

	private static final String text1 = "Now, just define a little bit more about your sample by selecting the cell line or tissue that your samples are from.";
	private static final String text2 = "You can type the name and a list of predefined values will show up.";
	private static final String text3 = "You can also type any additional description instead of selecting a value from the predefined list.";
	private FlexTable panel;
	private TissuesPanel tissuesPanel;

	public WizardPageTissues() {
		super("Tissue/Cell line");

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
		tissuesPanel = new TissuesPanel(getWizard());
		super.registerItemPanel(tissuesPanel);

		panel.setWidget(3, 0, tissuesPanel);
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
