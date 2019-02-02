package edu.scripps.yates.client.ui.wizard.view.widget;

import com.google.gwt.user.client.ui.DeckPanel;

import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.Wizard.Display;
import edu.scripps.yates.client.ui.wizard.view.HasIndexedWidgets;

/**
 * A WizardDeckPanel is simply a regular GWT DeckPanel that implements
 * {@link HasIndexedWidgets} for use in the {@link Wizard}'s {@link Display}.
 * Since DeckPanel already implements all the methods in
 * {@link HasIndexedWidgets}, there is no body to this class.
 * 
 * @author Brandon Tilley
 *
 */
public class WizardDeckPanel extends DeckPanel implements HasIndexedWidgets {
}
