package edu.scripps.yates.client.ui.wizard.view.widget;

import com.google.gwt.user.client.ui.Button;

import edu.scripps.yates.client.ui.wizard.view.HasWizardButtonMethods;

/**
 * An extension of the GWT Button that implements
 * {@link HasWizardButtonMethods}.
 * 
 * @author Brandon Tilley
 *
 */
public class WizardButton extends Button implements HasWizardButtonMethods {

	public WizardButton() {
		super();
	}

	public WizardButton(String html) {
		super(html);
		this.setStyleName("wizard-Button");
	}

}
