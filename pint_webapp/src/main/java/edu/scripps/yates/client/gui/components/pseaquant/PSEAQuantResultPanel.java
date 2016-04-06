package edu.scripps.yates.client.gui.components.pseaquant;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult;
import edu.scripps.yates.shared.util.SharedConstants;

public class PSEAQuantResultPanel extends Composite {
	private final Label lblAnEmailHas;

	public PSEAQuantResultPanel(PSEAQuantResult result, String email) {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("PSEAQuantPanel");
		initWidget(mainPanel);
		mainPanel.setWidth("593px");

		FlexTable flexTable = new FlexTable();
		mainPanel.add(flexTable);
		flexTable.setWidth("100%");

		lblAnEmailHas = new Label("An email has sent to '" + email + "' with the link to the results.");
		flexTable.setWidget(0, 0, lblAnEmailHas);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 3);

		Label lblNewLabel_1 = new Label("");
		flexTable.setWidget(1, 0, lblNewLabel_1);
		flexTable.getCellFormatter().setWidth(1, 0, "20px");

		Label lblNewLabel = new Label("You can access your PSEA-Quant results by following this link:");
		flexTable.setWidget(1, 1, lblNewLabel);
		flexTable.getCellFormatter().setWidth(1, 1, "380px");

		// Anchor anchor = new Anchor("PSEA-Quant result",
		// result.getLinkToResults());
		final SafeHtml link = HtmlTemplates.instance.link(UriUtils.fromString(result.getLinkToResults()), "link",
				"PSEA-Quant result", "PSEA-Quant result", "PSEA-Quant result");
		flexTable.setWidget(1, 2, new HTML(link));

		Label lblHereYouCan = new Label("Here you can download the data you sent to PSEA-Quant:");
		flexTable.setWidget(2, 1, lblHereYouCan);

		final String href = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/pint/download?"
				+ SharedConstants.FILE_TO_DOWNLOAD + "=" + result.getLinkToRatios() + "&" + SharedConstants.FILE_TYPE
				+ "=" + SharedConstants.PSEA_QUANT_DATA_FILE_TYPE;
		final SafeHtml link2 = HtmlTemplates.instance.link(UriUtils.fromString(href), "link",
				"Download data sent to PSEA-Quant", "Download data sent to PSEA-Quant", "data");
		// Anchor anchor2 = new Anchor("data", href);
		flexTable.setWidget(2, 2, new HTML(link2));
	}
}
