package edu.scripps.yates.client.gui.components;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.ContainsData;

public class ScrolledTabLayoutPanel extends TabLayoutPanel {
	private static final int STEP = 50;

	private static final int IMAGE_PADDING_PIXELS = 4;

	private final LayoutPanel panel;
	private FlowPanel tabBar;
	private Image scrollLeftButton;
	private Image scrollRightButton;
	private HandlerRegistration windowResizeHandler;

	private final ImageResource leftArrowImage;
	private final ImageResource rightArrowImage;

	private final boolean showArrowsWhenTabIsFull;

	/**
	 * Defaults, barHeight=30, barUnit=PX, showArrows=false
	 */
	public ScrolledTabLayoutPanel() {
		this(30, Unit.PX, false);
	}

	public ScrolledTabLayoutPanel(double barHeight, Unit barUnit, boolean showArrowsWhenTabIsFull) {
		this(barHeight, barUnit, showArrowsWhenTabIsFull, MyClientBundle.INSTANCE.smallLeftArrow(),
				MyClientBundle.INSTANCE.smallRigthArrow());
	}

	public ScrolledTabLayoutPanel(double barHeight, Unit barUnit, boolean showArrowsWhenTabIsFull,
			ImageResource leftArrowImage, ImageResource rightArrowImage) {
		super(barHeight, barUnit);
		this.showArrowsWhenTabIsFull = showArrowsWhenTabIsFull;
		this.leftArrowImage = leftArrowImage;
		this.rightArrowImage = rightArrowImage;

		// The main widget wrapped by this composite, which is a LayoutPanel
		// with the tab bar & the tab content
		panel = (LayoutPanel) getWidget();

		// Find the tab bar, which is the first flow panel in the LayoutPanel
		for (int i = 0; i < panel.getWidgetCount(); ++i) {
			final Widget widget = panel.getWidget(i);
			if (widget instanceof FlowPanel) {
				tabBar = (FlowPanel) widget;
				break; // tab bar found
			}
		}
		if (showArrowsWhenTabIsFull)
			initScrollButtons();
	}

	public ScrolledTabLayoutPanel(final PSMTablePanel psmWidget, final PeptideTablePanel peptideWidget) {

		this(30, Unit.PX, false, MyClientBundle.INSTANCE.leftArrow(), MyClientBundle.INSTANCE.rigthArrow());

		addStyleName("tab-panel");
		setSize("100%", "100%");
		setAnimationDuration(500);
		addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				final Integer selectedItem = event.getSelectedItem();
				// if it is the psmWidget
				if (psmWidget != null) {
					if (selectedItem == getWidgetIndex(psmWidget)) {
						psmWidget.hiddePSMPanel();
					} else {
						psmWidget.showPSMPanel();
					}
				}
				if (peptideWidget != null) {
					if (selectedItem == getWidgetIndex(peptideWidget)) {
						peptideWidget.hiddePeptidePanel();
					} else {
						peptideWidget.showPeptidePanel();
					}
				}
				final Widget widget = getWidget(selectedItem);
				if (widget instanceof ContainsData) {
					((ContainsData) widget).refreshData();
				}
			}
		});
	}

	@Override
	public void insert(final Widget child, Widget tab, int beforeIndex) {
		super.insert(child, tab, beforeIndex);
		checkIfScrollButtonsNecessary();
	}

	@Override
	public boolean remove(Widget w) {
		final boolean b = super.remove(w);
		checkIfScrollButtonsNecessary();
		return b;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		if (windowResizeHandler == null) {
			windowResizeHandler = Window.addResizeHandler(new ResizeHandler() {
				@Override
				public void onResize(ResizeEvent event) {
					checkIfScrollButtonsNecessary();
				}
			});
		}
	}

	@Override
	protected void onUnload() {
		super.onUnload();

		if (windowResizeHandler != null) {
			windowResizeHandler.removeHandler();
			windowResizeHandler = null;
		}
	}

	private ClickHandler createScrollClickHandler(final int diff) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final Widget lastTab = getLastTab();
				if (lastTab == null)
					return;

				final int newLeft = parsePosition(tabBar.getElement().getStyle().getLeft()) + diff;
				final int rightOfLastTab = getRightOfWidget(lastTab);

				// Prevent scrolling the last tab too far away form the right
				// border,
				// or the first tab further than the left border position
				if (newLeft <= 0 && (getTabBarWidth() - newLeft < (rightOfLastTab + STEP))) {
					scrollTo(newLeft);
				}
			}
		};
	}

	/** Create and attach the scroll button images with a click handler */
	private void initScrollButtons() {
		scrollLeftButton = new Image(leftArrowImage);
		final int leftImageWidth = scrollLeftButton.getWidth();

		panel.insert(scrollLeftButton, 0);
		panel.setWidgetLeftWidth(scrollLeftButton, 0, Unit.PX, leftImageWidth, Unit.PX);
		panel.setWidgetTopHeight(scrollLeftButton, 0, Unit.PX, scrollLeftButton.getWidth(), Unit.PX);

		scrollLeftButton.addClickHandler(createScrollClickHandler(+STEP));
		scrollLeftButton.setVisible(false);

		scrollRightButton = new Image(rightArrowImage);

		panel.insert(scrollRightButton, 0);
		panel.setWidgetLeftWidth(scrollRightButton, leftImageWidth + IMAGE_PADDING_PIXELS, Unit.PX,
				scrollRightButton.getWidth(), Unit.PX);
		panel.setWidgetTopHeight(scrollRightButton, 0, Unit.PX, scrollRightButton.getHeight(), Unit.PX);

		scrollRightButton.addClickHandler(createScrollClickHandler(-STEP));
		scrollRightButton.setVisible(false);
	}

	private void checkIfScrollButtonsNecessary() {
		if (!showArrowsWhenTabIsFull)
			return;
		// Defer size calculations until sizes are available, when calculating
		// immediately after
		// add(), all size methods return zero
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

			@Override
			public void execute() {
				final boolean isScrolling = isScrollingNecessary();
				// When the scroll buttons are being hidden, reset the scroll
				// position to zero to
				// make sure no tabs are still out of sight
				if (scrollRightButton.isVisible() && !isScrolling) {
					resetScrollPosition();
				}
				scrollRightButton.setVisible(isScrolling);
				scrollLeftButton.setVisible(isScrolling);
			}

		});

	}

	private void resetScrollPosition() {
		scrollTo(0);
	}

	private void scrollTo(int pos) {
		tabBar.getElement().getStyle().setLeft(pos, Unit.PX);
	}

	private boolean isScrollingNecessary() {
		final Widget lastTab = getLastTab();
		if (lastTab == null)
			return false;

		return getRightOfWidget(lastTab) > getTabBarWidth();
	}

	private int getRightOfWidget(Widget widget) {
		return widget.getElement().getOffsetLeft() + widget.getElement().getOffsetWidth();
	}

	private int getTabBarWidth() {
		return tabBar.getElement().getParentElement().getClientWidth();
	}

	private Widget getLastTab() {
		if (tabBar.getWidgetCount() == 0)
			return null;

		return tabBar.getWidget(tabBar.getWidgetCount() - 1);
	}

	private static int parsePosition(String positionString) {
		int position;
		try {
			for (int i = 0; i < positionString.length(); i++) {
				final char c = positionString.charAt(i);
				if (c != '-' && !(c >= '0' && c <= '9')) {
					positionString = positionString.substring(0, i);
				}
			}

			position = Integer.parseInt(positionString);
		} catch (final NumberFormatException ex) {
			position = 0;
		}
		return position;
	}
}