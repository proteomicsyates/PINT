package edu.scripps.yates.client.pint.wizard.pages;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.ui.wizard.view.widget.WizardFormPanel;
import edu.scripps.yates.client.util.forms.project.IsQuantititativeForm;

public class WizardPageIsQuantitative extends AbstractWizardPage {

	private static final String text1 = "Now, you need to answer some questions about the Experimental Design of your dataset.";

	private FlexTable panel;

	public WizardPageIsQuantitative() {
		super("Experiment type");

	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		ret.setWidget(panel);
		panel.setStyleName(WizardStyles.wizardRegularPage);

		final Label welcomeLabel1 = new Label(text1);
		welcomeLabel1.setStyleName(WizardStyles.WizardRegularText);
		panel.setWidget(0, 0, welcomeLabel1);

		return ret;
	}

	@Override
	public void beforeFirstShow() {
		GWT.log("beforeFirstShow" + getClass().getName());
		final IsQuantititativeForm isQuantForm = new IsQuantititativeForm(getContext());
		final WizardFormPanel formPanel = new WizardFormPanel(isQuantForm, getWizard());
		panel.setWidget(1, 0, formPanel);

		// disable next until user response
		getWizard().setButtonEnabled(ButtonType.BUTTON_NEXT, false);
		getWizard().setButtonEnabled(ButtonType.BUTTON_PREVIOUS, true);
		// we have to override this, otherwise it doesnt work
		getWizard().setButtonOverride(true);
		super.beforeFirstShow();
	}

	@Override
	public void beforeShow() {
		// TODO is this necessary?
		getWizard().setButtonOverride(false);
		super.beforeShow();
	}

}
