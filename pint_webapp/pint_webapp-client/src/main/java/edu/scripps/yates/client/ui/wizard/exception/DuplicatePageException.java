package edu.scripps.yates.client.ui.wizard.exception;

import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.WizardPage;

/**
 * Indicates that the {@link WizardPage} being added to the {@link Wizard}
 * has already been added.
 * @author Brandon Tilley
 *
 */
public class DuplicatePageException extends RuntimeException {

    private static final long serialVersionUID = -2121855831796245251L;

}
