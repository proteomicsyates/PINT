package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.Collection;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.gui.templates.MyClientBundle;

public class RegularExpressionTextBox extends Composite {
	private final SuggestBox suggestBox;
	private final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;
	private final Image greenTick;
	private final Image redCross;
	private int MINIMUM_LENTH_TO_CHECK = 4;

	/**
	 * Stating the minimum lenth from which the checking is done automatically
	 *
	 * @param text
	 * @param minimumLengthToStartCheck
	 */
	public RegularExpressionTextBox(String text, int minimumLengthToStartCheck) {
		this(text);
		MINIMUM_LENTH_TO_CHECK = minimumLengthToStartCheck;
	}

	/**
	 * Using a default lenth of 4 as the minimum lenth from which the checking
	 * is done automatically
	 *
	 * @param text
	 * @wbp.parser.constructor
	 */
	public RegularExpressionTextBox(String text) {
		FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);
		Grid grid = new Grid(1, 3);
		mainPanel.add(grid);
		suggestBox = new SuggestBox();
		suggestBox.setStyleName("RegularExpressionTextBox_Black");
		suggestBox.setValue(text);
		grid.setWidget(0, 0, suggestBox);
		greenTick = new Image(myClientBundle.greenTick());
		// hidden by default
		greenTick.setVisible(false);
		grid.setWidget(0, 1, greenTick);
		redCross = new Image(myClientBundle.redCross());
		// hidden by default
		redCross.setVisible(false);
		grid.setWidget(0, 2, redCross);
		addHandlers();

		// add suggestions
		addDefaultRegularExpressionSuggestions();
	}

	public void addRegularExpressionSuggestion(String suggestion) {
		final MultiWordSuggestOracle suggestOracle = (MultiWordSuggestOracle) suggestBox.getSuggestOracle();
		suggestOracle.add(suggestion);
	}

	public void addRegularExpressionSuggestion(Collection<String> suggestions) {
		final MultiWordSuggestOracle suggestOracle = (MultiWordSuggestOracle) suggestBox.getSuggestOracle();
		suggestOracle.addAll(suggestions);
	}

	private void addDefaultRegularExpressionSuggestions() {
		final MultiWordSuggestOracle suggestOracle = (MultiWordSuggestOracle) suggestBox.getSuggestOracle();
		for (ProteinAccessionDefaultRegularExpressions acc : ProteinAccessionDefaultRegularExpressions.values()) {
			suggestOracle.add(acc.getRegularExpressionString());
		}
	}

	private void addHandlers() {
		// if key up in text box, remove images and start checking
		suggestBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				// font black
				suggestBox.setStyleName("RegularExpressionTextBox_Black");

				// start check
				checkRegularExpression();
			}
		});

	}

	public boolean checkRegularExpression() {
		final String currentRegularExpressionText = getCurrentRegularExpressionText();
		// emtpy is valid
		if ("".equals(currentRegularExpressionText)) {
			return true;
		}
		if (currentRegularExpressionText.length() >= MINIMUM_LENTH_TO_CHECK) {
			try {
				RegExp.compile(currentRegularExpressionText);
				// show gree tick
				greenTick.setVisible(true);
				redCross.setVisible(false);
				return true;
			} catch (RuntimeException e) {
				// show red cross
				greenTick.setVisible(false);
				redCross.setVisible(true);
				// font red
				suggestBox.setStyleName("RegularExpressionTextBox_Red");

				return false;
			}
		} else {
			// hide images
			redCross.setVisible(false);
			greenTick.setVisible(false);
			return false;
		}

	}

	/**
	 * Gets current regular expression in text box. Note that this not assure
	 * that it is valid
	 *
	 * @return
	 */
	public String getCurrentRegularExpressionText() {
		return suggestBox.getText();
	}

	/**
	 * Returns a {@link RegExp} if is a valid regular expression or null if not.
	 *
	 * @return
	 */
	public RegExp getCurrentRegularExpression() {
		if (checkRegularExpression())
			return RegExp.compile(getCurrentRegularExpressionText());
		return null;
	}

	public void setEnabled(boolean b) {
		suggestBox.setEnabled(b);
	}

	public SuggestBox getSuggestBox() {
		return suggestBox;
	}

	public void setText(String text) {
		suggestBox.setText(text);
	}
}
