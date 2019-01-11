package edu.scripps.yates.client.gui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.history.TargetHistory;

public class NavigationHorizontalPanel extends FlowPanel implements ProvidesResize, RequiresResize {
	private TargetHistory currentPage;
	private final Hyperlink currentHyperlink;
	private final InlineLabel inlineLabel;
	private final String ARROW = "  -->  ";
	private final Hyperlink homeHyperlink;

	public NavigationHorizontalPanel() {

		super();
		this.setStyleName("navigationBar");
		// setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// setSize("148px", "36px");

		homeHyperlink = new Hyperlink("Home", false, "home");
		homeHyperlink.setStyleName("horizontalComponent");
		homeHyperlink.setDirectionEstimator(false);
		this.add(homeHyperlink);
		// setCellHorizontalAlignment(homeHyperlink,
		// HasHorizontalAlignment.ALIGN_CENTER);
		// setCellWidth(homeHyperlink, "50px");
		homeHyperlink.setWidth("50px");
		// setCellVerticalAlignment(homeHyperlink,
		// HasVerticalAlignment.ALIGN_MIDDLE);

		inlineLabel = new InlineLabel(ARROW);
		inlineLabel.setStyleName("horizontalComponent");
		this.add(inlineLabel);
		// setCellHorizontalAlignment(inlineLabel,
		// HasHorizontalAlignment.ALIGN_CENTER);
		inlineLabel.setWidth("30px");
		// setCellWidth(inlineLabel, "30px");
		// setCellVerticalAlignment(inlineLabel,
		// HasVerticalAlignment.ALIGN_MIDDLE);

		currentHyperlink = new Hyperlink("to_set", false, "to_set");
		currentHyperlink.setStyleName("horizontalComponent");
		currentHyperlink.setDirectionEstimator(false);
		this.add(currentHyperlink);
		// setCellHorizontalAlignment(currentHyperlink,
		// HasHorizontalAlignment.ALIGN_CENTER);
		// setCellVerticalAlignment(currentHyperlink,
		// HasVerticalAlignment.ALIGN_MIDDLE);
		// setCellWidth(currentHyperlink, "50px");
		currentHyperlink.setWidth("73px");
	}

	/**
	 * @wbp.parser.constructor
	 * @param currentPage
	 */
	public NavigationHorizontalPanel(TargetHistory currentPage) {
		this();

		this.currentPage = currentPage;

		updateLink();

	}

	public void changePage(TargetHistory currentPage) {
		this.currentPage = currentPage;
		updateLink();
	}

	private void updateLink() {
		updateLink(false);
	}

	public void updateLink(boolean reload) {
		if (currentPage == TargetHistory.HOME) {
			currentHyperlink.setText("");
			currentHyperlink.setTargetHistoryToken("");
			inlineLabel.setText("");
		} else {
			currentHyperlink.setText(currentPage.name());
			currentHyperlink.setTargetHistoryToken(currentPage.getTargetHistory());
			currentHyperlink.setTitle(currentPage.name());
			inlineLabel.setText(ARROW);
		}

		if (reload) {
			homeHyperlink.setTargetHistoryToken(
					TargetHistory.HOME.getTargetHistory() + "-" + TargetHistory.RELOAD.getTargetHistory());
		} else {
			homeHyperlink.setTargetHistoryToken(TargetHistory.HOME.getTargetHistory());
		}
	}

	@Override
	public void onResize() {

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				int li_width = getOffsetWidth();
				int li_height = getOffsetHeight();
				System.out.println("ResizableFlowPanel:: width : " + li_width + "; height: " + li_height);
				for (Widget child : getChildren()) {
					if (child instanceof RequiresResize) {
						((RequiresResize) child).onResize();
					}
				}
			}
		});

	}
}
