package edu.scripps.yates.client.gui.components;

import com.google.gwt.user.client.ui.Hyperlink;

import edu.scripps.yates.client.history.TargetHistory;

public class MyHyperlink extends Hyperlink {

	public MyHyperlink(String text, TargetHistory targetHistory) {
		super(text, false, targetHistory.getTargetHistory());
	}
}
