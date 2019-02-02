package edu.scripps.yates.client.ui.wizard.view;

import edu.scripps.yates.client.ui.wizard.WizardPage;

/**
 * An interface that indicates that the implementing class has the ability to
 * add and remove {@link WizardPage} titles (as {@link String}s) to and from its
 * display and also has the ability to indicate that a specific page title is
 * the currently active one.
 * 
 * @author Brandon Tilley
 *
 */
public interface HasWizardTitles {

	/**
	 * Adds a page title.
	 * 
	 * @param title the title of the page to add
	 */
	public void addPage(String title);

	/**
	 * By Salva:<br>
	 * Adds a page title with custom styles
	 * 
	 * @param title
	 * @param styleNameActive
	 * @param styleNameInactive
	 */
	public void addPage(String title, String styleNameActive, String styleNameInactive);

	/**
	 * Sets a page title as the currently active title.
	 * 
	 * @param title the title to set as active
	 */
	public void setCurrentPage(String title);

	public void removePage(String title);

}
