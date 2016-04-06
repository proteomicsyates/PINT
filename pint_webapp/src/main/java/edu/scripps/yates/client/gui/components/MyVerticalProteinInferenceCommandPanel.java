package edu.scripps.yates.client.gui.components;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyVerticalProteinInferenceCommandPanel extends VerticalPanel {

	private final CheckBox checkBoxSeparateNonConclusiveProteins;

	public MyVerticalProteinInferenceCommandPanel(ClickHandler clickHandler) {
		setSize("100%", "100%");

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Button button = new Button("Click here to group proteins");
		button.addClickHandler(clickHandler);
		this.add(button);
		checkBoxSeparateNonConclusiveProteins = new CheckBox(new SafeHtmlBuilder().appendEscapedLines(
				"Separate subset groups to \n" + "distinguish 'non conclusive' proteins").toSafeHtml());
		this.add(checkBoxSeparateNonConclusiveProteins);

	}

	public boolean isSeparateNonConclusiveProteins() {
		return checkBoxSeparateNonConclusiveProteins.getValue();
	}

}
