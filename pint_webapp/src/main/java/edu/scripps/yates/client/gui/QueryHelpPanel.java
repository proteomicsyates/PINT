package edu.scripps.yates.client.gui;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;

public class QueryHelpPanel extends ResizeLayoutPanel {

	private static QueryHelpPanel instance;

	private final String sessionID;

	public static QueryHelpPanel getInstance(String sessionID) {
		if (instance == null || (sessionID != null && !instance.sessionID.equals(sessionID))) {
			instance = new QueryHelpPanel(sessionID);
		}
		return instance;
	}

	private QueryHelpPanel(final String sessionID) {
		this.sessionID = sessionID;

		FlexTable panel = new FlexTable();
		panel.setStyleName("queryPanelHelp");
		Label label = new Label("If you have any question, problem or suggestion about PINT,");
		label.setStyleName("queryPanelHelpText");
		panel.setWidget(0, 0, label);
		panel.getFlexCellFormatter().setColSpan(0, 0, 2);
		Label label2 = new Label("please write to this email address: ");
		label2.setStyleName("queryPanelHelpText");
		panel.setWidget(1, 0, label2);
		Label labelEmail = new Label(getAdminEmail());
		labelEmail.setStyleName("queryPanelHelpEmail");
		panel.setWidget(1, 1, labelEmail);

		add(panel);
	}

	private String getAdminEmail() {
		// TODO eventually, we can customize this to get the address from a
		// configuration file in the server
		return "salvador at scripps.edu";
	}

}
