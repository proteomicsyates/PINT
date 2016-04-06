package edu.scripps.yates.client.gui.components;

/*
 * Copyright 2010 Traction Software, Inc. Copyright 2010 clazzes.org Project
 * Based on TractionDialogBox by Traction Software, Inc. Renamed to WindowBox
 * and added resize support by clazzes.org Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;

/**
 * Extension of the standard GWT DialogBox to provide a more "window"-like
 * functionality. By default, the WindowBox has two control-buttons in the top
 * right corner of the header, which allow the box to be reduced to it's header
 * ("minimize") or the whole box to be hidden ("close"). The visiblity of these
 * controls can be toggled seperately with
 * {@link #setMinimizeIconVisible(boolean)} and
 * {@link #setCloseIconVisible(boolean)} respectively. <br>
 * <br>
 * The WindowBox relies on the css settings of {@link DialogBox} for styling of
 * the border and header. It also uses the following classes to style the
 * additional elements:
 *
 * <pre>
 *  .gwt-extras-WindowBox
 *      the box itself
 *  .gwt-extras-WindowBox .gwt-extras-dialog-container
 *      the div holding the contents of the box
 *  .gwt-extras-WindowBox .gwt-extras-dialog-controls
 *      the div holding the window-controls - PLEASE NOTE: on the DOM-tree, this div is located inside the center-center
 *      cell of the windowBox table, not in the top-center (where the header-text is). Therefore the css has a negative
 *      top-value to position the controls on the header
 *  .gwt-extras-WindowBox .gwt-extras-dialog-controls a.gwt-extras-dialog-close
 *  .gwt-extras-WindowBox .gwt-extras-dialog-controls a.gwt-extras-dialog-close:hover
 *  .gwt-extras-WindowBox .gwt-extras-dialog-controls a.gwt-extras-dialog-minimize
 *  .gwt-extras-WindowBox .gwt-extras-dialog-controls a.gwt-extras-dialog-minimize:hover
 *  .gwt-extras-WindowBox .gwt-extras-dialog-controls a.gwt-extras-dialog-maximize
 *  .gwt-extras-WindowBox .gwt-extras-dialog-controls a.gwt-extras-dialog-maximize:hover
 *      the controls in the header. A background image sprite is used to create the mouseover- and clicking-effects.
 *      When the window is minimized, the style-name of the corresponding control changes to "gwt-extras-dialog-maximize"
 *      and vice-versa
 * </pre>
 */
public class WindowBox extends DialogBox implements HasOpenHandlers<WindowBox> {

	private static final int MIN_WIDTH = 100;
	private static final int MIN_HEIGHT = 100;

	private final FlowPanel container;
	// private FlowPanel content;
	private final FlowPanel controls;
	private final Anchor close;
	private final Anchor minimize;

	private int dragX;
	private int dragY;

	private int minWidth = MIN_WIDTH;
	private int minHeight = MIN_HEIGHT;

	private int dragMode;

	private boolean resizable;

	private boolean minimized;
	private final List<DoSomethingTask<?>> closeClickTasks = new ArrayList<DoSomethingTask<?>>();

	/**
	 * Static helper method to change the cursor for a given element when
	 * resizing is enabled.
	 *
	 * @param dm
	 *            The code describing the position of the element in question
	 * @param element
	 *            The {@link com.google.gwt.dom.client.Element} to set the
	 *            cursor on
	 */
	protected static void updateCursor(int dm, com.google.gwt.dom.client.Element element) {
		Cursor cursor;

		switch (dm) {
		case 0:
			cursor = Cursor.NW_RESIZE;
			break;

		case 1:
			cursor = Cursor.N_RESIZE;
			break;

		case 2:
			cursor = Cursor.NE_RESIZE;
			break;

		case 3:
			cursor = Cursor.W_RESIZE;
			break;

		case 5:
			cursor = Cursor.E_RESIZE;
			break;

		case 6:
			cursor = Cursor.SW_RESIZE;
			break;

		case 7:
			cursor = Cursor.S_RESIZE;
			break;

		case 8:
			cursor = Cursor.SE_RESIZE;
			break;

		default:
			cursor = Cursor.AUTO;
			break;
		}

		element.getStyle().setCursor(cursor);
	}

	/**
	 * Creates a {@link WindowBox} with a {@link Widget} and a title, with the
	 * appropiate resize handler
	 *
	 * @param widget
	 * @param title
	 */
	public WindowBox(Widget widget, String title) {
		this(true, true, true, true, true);
		setAnimationEnabled(true);
		setGlassEnabled(true);

		setWidget(widget);
		setText(title);
		widget.addHandler(new ResizeHandler() {
			@Override
			public void onResize(final ResizeEvent event) {
				resize();
			}

		}, ResizeEvent.getType());

	}

	/**
	 * Creates a DialogBoxEx which is permanent (no auto-hide), non-modal, has a
	 * "minimize"- and "close"-button in the top-right corner and is not
	 * resizeable. The dialog box should not be shown until a child widget has
	 * been added using {@link #add(com.google.gwt.user.client.ui.IsWidget)}.
	 * <br>
	 * <br>
	 * This is the equivalent for calling
	 * <code>DialogBoxEx(false, false, true, false)</code>.
	 *
	 * @see WindowBox#DialogBoxEx(boolean, boolean, boolean, boolean)
	 */
	public WindowBox() {
		this(false, false, true, true, false);
	}

	/**
	 * Creates a DialogBoxEx which is permanent, non-modal, has a "minimize"-
	 * and "close"-button and is optionally resizeable. The dialog box should
	 * not be shown until a child widget has been added using
	 * {@link #setWidget(Widget)}.
	 *
	 * @see WindowBox#DialogBoxEx(boolean, boolean, boolean, boolean)
	 *
	 * @param resizeable
	 *            <code>true</code> to allow resizing by dragging the borders
	 */
	public WindowBox(boolean resizeable) {
		this(false, false, true, true, resizeable);
	}

	/**
	 * Creates a DialogBoxEx which is permanent and nonmodal, optionally
	 * resizeable and/or has a "minimize"- and "close"-button. The dialog box
	 * should not be shown until a child widget has been added using
	 * {@link #setWidget(Widget)}.
	 *
	 * @see WindowBox#DialogBoxEx(boolean, boolean, boolean, boolean)
	 *
	 * @param resizeable
	 *            <code>true</code> to allow resizing by dragging the borders
	 * @param showCloseIcon
	 *            <code>true</code> to show "close"-icon in the top right corner
	 *            of the header
	 */
	public WindowBox(boolean showCloseIcon, boolean resizeable) {
		this(false, false, true, showCloseIcon, resizeable);
	}

	/**
	 * Creates a DialogBoxEx which is permanent and nonmodal, optionally
	 * resizeable and/or has a "minimize"- and "close"-button. The dialog box
	 * should not be shown until a child widget has been added using
	 * {@link #setWidget(Widget)}.
	 *
	 * @see WindowBox#DialogBoxEx(boolean, boolean, boolean, boolean)
	 *
	 * @param showMinimizeIcon
	 *            <code>true</code> to show "minimize"-icon int the top right
	 *            corner of the header
	 * @param resizeable
	 *            <code>true</code> to allow resizing by dragging the borders
	 * @param showCloseIcon
	 *            <code>true</code> to show "close"-icon in the top right corner
	 *            of the header
	 */
	public WindowBox(boolean showMinimizeIcon, boolean showCloseIcon, boolean resizeable) {
		this(false, false, showMinimizeIcon, showCloseIcon, resizeable);
	}

	/**
	 * Creates a DialogBoxEx which is permanent, optionally modal, resizeable
	 * and/or has a "minimize"- and "close"-button. The dialog box should not be
	 * shown until a child widget has been added using
	 * {@link #setWidget(Widget)}.
	 *
	 * @param modal
	 *            <code>true</code> if keyboard and mouse events for widgets not
	 *            contained by the dialog should be ignored
	 * @param showMinimizeIcon
	 *            <code>true</code> to show "minimize"-icon int the top right
	 *            corner of the header
	 * @param resizeable
	 *            <code>true</code> to allow resizing by dragging the borders
	 * @param showCloseIcon
	 *            <code>true</code> to show "close"-icon in the top right corner
	 *            of the header
	 */
	public WindowBox(boolean modal, boolean showMinimizeIcon, boolean showCloseIcon, boolean resizeable) {
		this(false, modal, showMinimizeIcon, showCloseIcon, resizeable);
	}

	/**
	 * Creates an empty DialogBoxEx with all configuration options. The dialog
	 * box should not be shown until a child widget has been added using
	 * {@link #setWidget(Widget)}.
	 *
	 * @see DialogBox#DialogBox()
	 * @see DialogBox#DialogBox(boolean)
	 * @see DialogBox#DialogBox(boolean, boolean)
	 * @see DialogBox#DialogBox(boolean, boolean, boolean)
	 *
	 * @param autoHide
	 *            <code>true</code> if the dialog should be automatically hidden
	 *            when the user clicks outside of it
	 * @param modal
	 *            <code>true</code> if keyboard and mouse events for widgets not
	 *            contained by the dialog should be ignored
	 * @param showMinimizeIcon
	 *            <code>true</code> to show "minimize"-icon int the top right
	 *            corner of the header
	 * @param showCloseIcon
	 *            <code>true</code> to show "close"-icon in the top right corner
	 *            of the header
	 * @param resizeable
	 *            <code>true</code> to allow resizing by dragging the borders
	 */
	public WindowBox(boolean autoHide, boolean modal, boolean showMinimizeIcon, boolean showCloseIcon,
			boolean resizeable) {
		super(autoHide, modal);

		this.setStyleName("gwt-extras-WindowBox", true);

		container = new FlowPanel();
		container.addStyleName("gwt-extras-dialog-container");

		// this.content = new FlowPanel();

		close = new Anchor();
		close.setStyleName("gwt-extras-dialog-close");
		close.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onCloseClick(event);
			}
		});
		setCloseIconVisible(showCloseIcon);

		minimize = new Anchor();
		minimize.setStyleName("gwt-extras-dialog-minimize");
		minimize.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onMinimizeClick(event);
			}
		});
		setMinimizeIconVisible(showMinimizeIcon);

		Grid ctrlGrid = new Grid(1, 2);
		ctrlGrid.setWidget(0, 0, minimize);
		ctrlGrid.setWidget(0, 1, close);

		controls = new FlowPanel();
		controls.setStyleName("gwt-extras-dialog-controls");
		controls.add(ctrlGrid);
		dragMode = -1;

		resizable = resizeable;
	}

	/**
	 * Sets the cursor to indicate resizability for a specified "drag-mode"
	 * (i.e. how the box is being resized) on the dialog box. The position is
	 * described by an integer, as follows:
	 *
	 * <pre>
	 *  0-- --1-- --2
	 *  |           |
	 *
	 *  |           |
	 *  3    -1     5
	 *  |           |
	 *
	 *  |           |
	 *  6-- --7-- --8
	 * </pre>
	 *
	 * passing <code>-1</code> resets the cursor to the default.
	 *
	 * @param dragMode
	 */
	protected void updateCursor(int dragMode) {
		if (resizable) {
			updateCursor(dragMode, getElement());

			com.google.gwt.dom.client.Element top = getCellElement(0, 1);
			updateCursor(dragMode, top);

			top = Element.as(top.getFirstChild());
			if (top != null)
				updateCursor(dragMode, top);
		}
	}

	/**
	 * Returns whether the dialog box is mouse-resizeable
	 *
	 * @return <code>true</code> if the user can resize the dialog with the
	 *         mouse
	 */
	public boolean isResizable() {
		return resizable;
	}

	/**
	 * Set the dialog box to be resizeable by the user
	 *
	 * @param resizable
	 *            <code>true</code> if the user can resize the dialog with the
	 *            mouse
	 */
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.client.ui.DialogBox#onBrowserEvent(com.google.gwt
	 * .user.client.Event)
	 */
	@Override
	public void onBrowserEvent(Event event) {

		// If we're not yet dragging, only trigger mouse events if the event
		// occurs
		// in the caption wrapper
		if (resizable) {
			switch (event.getTypeInt()) {
			case Event.ONMOUSEDOWN:
			case Event.ONMOUSEUP:
			case Event.ONMOUSEMOVE:
			case Event.ONMOUSEOVER:
			case Event.ONMOUSEOUT:

				if (dragMode >= 0 || calcDragMode(event.getClientX(), event.getClientY()) >= 0) {
					// paste'n'copy from Widget.onBrowserEvent
					switch (DOM.eventGetType(event)) {
					case Event.ONMOUSEOVER:
						// Only fire the mouse over event if it's coming from
						// outside this
						// widget.
					case Event.ONMOUSEOUT:
						// Only fire the mouse out event if it's leaving this
						// widget.
						Element related = event.getRelatedEventTarget().cast();
						if (related != null && getElement().isOrHasChild(related)) {
							return;
						}
						break;
					}
					DomEvent.fireNativeEvent(event, this, getElement());
					return;
				}
				if (dragMode < 0)
					this.updateCursor(dragMode);
			}
		}

		super.onBrowserEvent(event);
	}

	/**
	 *
	 * @param resize
	 * @param clientX
	 * @return
	 */
	private int getRelX(com.google.gwt.dom.client.Element resize, int clientX) {
		return clientX - resize.getAbsoluteLeft() + resize.getScrollLeft() + resize.getOwnerDocument().getScrollLeft();
	}

	/**
	 *
	 * @param resize
	 * @param clientY
	 * @return
	 */
	private int getRelY(com.google.gwt.dom.client.Element resize, int clientY) {
		return clientY - resize.getAbsoluteTop() + resize.getScrollTop() + resize.getOwnerDocument().getScrollTop();
	}

	/**
	 * Calculates the position of the mouse relative to the dialog box, and
	 * returns the corresponding "drag-mode" integer, which describes which area
	 * of the box is being resized.
	 *
	 * @param clientX
	 *            The x-coordinate of the mouse in screen pixels
	 * @param clientY
	 *            The y-coordinate of the mouse in screen pixels
	 * @return A value in range [-1..8] describing the position of the mouse
	 *         (see {@link #updateCursor(int)} for more information)
	 */
	protected int calcDragMode(int clientX, int clientY) {
		com.google.gwt.dom.client.Element resize = getCellElement(2, 2).getParentElement();
		int xr = getRelX(resize, clientX);
		int yr = getRelY(resize, clientY);

		int w = resize.getClientWidth();
		int h = resize.getClientHeight();

		if ((xr >= 0 && xr < w && yr >= -5 && yr < h) || (yr >= 0 && yr < h && xr >= -5 && xr < w))
			return 8;

		resize = getCellElement(2, 0).getParentElement();
		xr = getRelX(resize, clientX);
		yr = getRelY(resize, clientY);

		if ((xr >= 0 && xr < w && yr >= -5 && yr < h) || (yr >= 0 && yr < h && xr >= 0 && xr < w + 5))
			return 6;

		resize = getCellElement(0, 2).getParentElement();
		xr = getRelX(resize, clientX);
		yr = getRelY(resize, clientY);

		if ((xr >= 0 && xr < w && yr >= 0 && yr < h + 5) || (yr >= 0 && yr < h && xr >= -5 && xr < w))
			return 2;

		resize = getCellElement(0, 0).getParentElement();
		xr = getRelX(resize, clientX);
		yr = getRelY(resize, clientY);

		if ((xr >= 0 && xr < w && yr >= 0 && yr < h + 5) || (yr >= 0 && yr < h && xr >= 0 && xr < w + 5))
			return 0;

		resize = getCellElement(0, 1).getParentElement();
		xr = getRelX(resize, clientX);
		yr = getRelY(resize, clientY);

		if (yr >= 0 && yr < h)
			return 1;

		resize = getCellElement(1, 0).getParentElement();
		xr = getRelX(resize, clientX);
		yr = getRelY(resize, clientY);

		if (xr >= 0 && xr < w)
			return 3;

		resize = getCellElement(2, 1).getParentElement();
		xr = getRelX(resize, clientX);
		yr = getRelY(resize, clientY);

		if (yr >= 0 && yr < h)
			return 7;

		resize = getCellElement(1, 2).getParentElement();
		xr = getRelX(resize, clientX);
		yr = getRelY(resize, clientY);

		if (xr >= 0 && xr < w)
			return 5;

		return -1;
	}

	/**
	 * Convenience method to set the height, width and position of the given
	 * widget
	 *
	 * @param panel
	 * @param dx
	 * @param dy
	 */
	protected void dragResizeWidget(PopupPanel panel, int dx, int dy) {
		int x = getPopupLeft();
		int y = getPopupTop();

		Widget widget = panel.getWidget();

		// left + right
		if ((dragMode % 3) != 1) {
			int w = widget.getOffsetWidth();

			// left edge -> move left
			if ((dragMode % 3) == 0) {
				x += dx;
				w -= dx;
			} else {
				w += dx;
			}

			w = w < minWidth ? minWidth : w;

			widget.setWidth(w + "px");
		}

		// up + down
		if ((dragMode / 3) != 1) {
			int h = widget.getOffsetHeight();

			// up = dy is negative
			if ((dragMode / 3) == 0) {
				y += dy;
				h -= dy;
			} else {
				h += dy;
			}

			h = h < minHeight ? minHeight : h;

			widget.setHeight(h + "px");
		}

		if (dragMode / 3 == 0 || dragMode % 3 == 0)
			panel.setPopupPosition(x, y);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.client.ui.DialogBox#beginDragging(com.google.gwt.
	 * event.dom.client.MouseDownEvent)
	 */
	@Override
	protected void beginDragging(MouseDownEvent event) {
		int dm = -1;

		if (resizable && !minimized)
			dm = calcDragMode(event.getClientX(), event.getClientY());

		if (resizable && dm >= 0) {
			dragMode = dm;

			DOM.setCapture(getElement());

			dragX = event.getClientX();
			dragY = event.getClientY();

			updateCursor(dm, RootPanel.get().getElement());

		} else {
			super.beginDragging(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.client.ui.DialogBox#continueDragging(com.google.gwt
	 * .event.dom.client.MouseMoveEvent)
	 */
	@Override
	protected void continueDragging(MouseMoveEvent event) {
		if (dragMode >= 0 && resizable) {
			this.updateCursor(dragMode);

			int dx = event.getClientX() - dragX;
			int dy = event.getClientY() - dragY;

			dragX = event.getClientX();
			dragY = event.getClientY();

			dragResizeWidget(this, dx, dy);
		} else {
			// this updates the cursor when dragging is NOT activated
			if (!minimized) {
				int dm = calcDragMode(event.getClientX(), event.getClientY());
				this.updateCursor(dm);
			}
			super.continueDragging(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.client.ui.DialogBox#onPreviewNativeEvent(com.google
	 * .gwt.user.client.Event.NativePreviewEvent)
	 */
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		if (resizable) {
			// We need to preventDefault() on mouseDown events (outside of the
			// DialogBox content) to keep text from being selected when it
			// is dragged.
			NativeEvent nativeEvent = event.getNativeEvent();

			if (!event.isCanceled() && (event.getTypeInt() == Event.ONMOUSEDOWN)
					&& calcDragMode(nativeEvent.getClientX(), nativeEvent.getClientY()) >= 0) {
				nativeEvent.preventDefault();
			}
		}

		super.onPreviewNativeEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.client.ui.DialogBox#endDragging(com.google.gwt.event
	 * .dom.client.MouseUpEvent)
	 */
	@Override
	protected void endDragging(MouseUpEvent event) {
		if (dragMode >= 0 && resizable) {
			DOM.releaseCapture(getElement());

			dragX = event.getClientX() - dragX;
			dragY = event.getClientY() - dragY;

			dragMode = -1;
			this.updateCursor(dragMode);
			RootPanel.get().getElement().getStyle().setCursor(Cursor.AUTO);
		} else {
			super.endDragging(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.client.ui.DecoratedPopupPanel#setWidget(com.google
	 * .gwt.user.client.ui.Widget)
	 */
	@Override
	public void setWidget(Widget widget) {
		if (container.getWidgetCount() == 0) {
			// setup
			container.add(controls);
			// this.container.add(this.content);
			super.setWidget(container);
		} else {
			if (container.getWidgetCount() == 2)
				// remove the old one
				container.remove(1);
		}
		container.add(widget);

		// add the new widget
		// this.content.add(widget);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#getWidget()
	 */
	@Override
	public Widget getWidget() {
		if (container.getWidgetCount() > 1)
			return container.getWidget(1);
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.client.ui.DecoratedPopupPanel#remove(com.google.gwt
	 * .user.client.ui.Widget)
	 */
	@Override
	public boolean remove(Widget w) {
		return container.remove(w);
	}

	/**
	 * Set whether the "close"-button should appear in the top right corner of
	 * the header
	 *
	 * @param visible
	 *            <code>true</code> if a "close"-button should be shown
	 */
	public void setCloseIconVisible(boolean visible) {
		close.setVisible(visible);
	}

	/**
	 * Set whether the "minimize"-button should appear in the top right corner
	 * of the header
	 *
	 * @param visible
	 *            <code>true</code> if a "minimize"-button should be shown
	 */
	public void setMinimizeIconVisible(boolean visible) {
		minimize.setVisible(visible);
	}

	/**
	 * Returns the FlowPanel that contains the controls. More controls can be
	 * added directly to this.
	 */
	public FlowPanel getControlPanel() {
		return controls;
	}

	/**
	 * Called when the close icon is clicked. The default implementation hides
	 * the dialog box.
	 *
	 * @param event
	 *            The {@link ClickEvent} to handle
	 */
	protected void onCloseClick(ClickEvent event) {
		hide();
		for (DoSomethingTask<?> doSomethingTask : closeClickTasks) {
			doSomethingTask.doSomething();
		}
	}

	/**
	 * Adds a {@link DoSomethingTask} to execute when window is closed
	 *
	 * @param doSomethingTask
	 */
	public void addCloseEventDoSomethingTask(DoSomethingTask<?> doSomethingTask) {
		closeClickTasks.add(doSomethingTask);
	}

	/**
	 * Called when the minimize icon is clicked. The default implementation
	 * hides the container of the dialog box.
	 *
	 * @param event
	 *            The {@link ClickEvent} to handle
	 */
	protected void onMinimizeClick(ClickEvent event) {
		Widget widget = getWidget();

		if (widget == null)
			return;

		boolean visible = widget.isVisible();

		int offsetWidth = widget.getOffsetWidth();

		widget.setVisible(!visible);
		minimized = visible;

		if (visible) {
			container.setWidth(offsetWidth + "px");
			minimize.setStyleName("gwt-extras-dialog-maximize");
		} else {
			container.setWidth(null);
			minimize.setStyleName("gwt-extras-dialog-minimize");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.event.logical.shared.HasOpenHandlers#addOpenHandler(com
	 * .google.gwt.event.logical.shared.OpenHandler)
	 */
	@Override
	public HandlerRegistration addOpenHandler(OpenHandler<WindowBox> handler) {
		return addHandler(handler, OpenEvent.getType());
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.DialogBox#show()
	 */
	@Override
	public void show() {
		boolean fireOpen = !isShowing();
		super.show();
		if (fireOpen) {
			OpenEvent.fire(this, this);
		}
	}

	/**
	 * Sets the minimum width to which this widget can be resized by the user,
	 * if resizing is enabled. If the value is invalid, it is reset to
	 * {@link #MIN_WIDTH}
	 *
	 * @param minWidth
	 *            A positive int value
	 */
	public void setMinWidth(int minWidth) {
		if (minWidth < 1)
			minWidth = MIN_WIDTH;

		this.minWidth = minWidth;
	}

	/**
	 * Sets the minimum height to which this widget can be resized by the user,
	 * if resizing is enabled. If the value is invalid, it is reset to
	 * {@link #MIN_WIDTH}
	 *
	 * @param minHeight
	 *            A positive int value
	 */
	public void setMinHeight(int minHeight) {
		if (minHeight < 1)
			minHeight = MIN_HEIGHT;

		this.minHeight = minHeight;
	}

	/**
	 * Adapt its size to the content.
	 */
	public void resize() {
		// remove the last widget and add it again
		if (container.getWidgetCount() == 2) {
			final Widget widget = container.getWidget(1);
			container.remove(1);
			setWidget(widget);
		}
		if (isShowing())
			center();
	}
}
