package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.form.PrincipalInvestigatorForm;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardFormPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class WizardPagePrincipalInvestigator extends AbstractWizardPage {

	private static final String text1 = "Secondly, enter the Principal Investigator contact information:";
	private static final String text2 = "This has been added to keep compatibility with the metadata requested by ProteomeXchange consortium";
	private FlexTable panel;

	public WizardPagePrincipalInvestigator() {
		super("Principal Investigator");

	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();

		final Label label1 = new Label(text1);
		label1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(0, 0, label1);

		final Label label2 = new Label(text2);
		label2.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(1, 0, label2);

		ret.add(panel);
		return ret;
	}

	@Override
	public void beforeShow() {
		final PrincipalInvestigatorForm piTextForm = new PrincipalInvestigatorForm(getContext());
		final WizardFormPanel textFormPanel2 = new WizardFormPanel(piTextForm, getWizard());
		panel.setWidget(2, 0, textFormPanel2);

		if (textFormPanel2.isReady()) {
			// disable next until user response
			super.getWizard().setButtonEnabled(ButtonType.BUTTON_NEXT, true);
			// we have to override this, otherwise it doesn't work
			getWizard().setButtonOverride(true);
		}

		super.beforeShow();
	}

	@Override
	public void beforeFirstShow() {
		// disable next until user response
		super.getWizard().setButtonEnabled(ButtonType.BUTTON_NEXT, false);
		// we have to override this, otherwise it doesn't work
		getWizard().setButtonOverride(true);
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
