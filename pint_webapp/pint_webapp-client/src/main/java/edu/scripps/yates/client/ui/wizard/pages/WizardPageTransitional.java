package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.pint.wizard.PintContext;

/**
 * This page doesn't do anything. It can be done in order to set it up after a
 * wizard page in which the method afterNext() is checking how is the status of
 * the {@link PintContext} and then, depending on it, it will remove this page
 * and add others.
 * 
 * @author salvador
 *
 */
public class WizardPageTransitional extends AbstractWizardPage {

	public WizardPageTransitional(String title) {
		super(title);
	}

	@Override
	protected Widget createPage() {

		return new SimplePanel();
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
