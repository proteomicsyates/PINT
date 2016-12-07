package edu.scripps.yates.client.gui.components.projectItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;

/**
 * Class that represents a panel with a left panel with a list of items of type
 * Y and that in the right has a right panel that is updated when an item in the
 * list is selected.<br>
 * Items in the list are depending on a parent object that can be updated and
 * that is of type T
 *
 * @author Salva
 *
 * @param <Y>
 * @param <T>
 */
public abstract class AbstractItemPanel<Y, T> extends Composite {
	private final List<Y> items = new ArrayList<Y>();
	private final Map<Y, FlowPanel> panelsByItem = new HashMap<Y, FlowPanel>();
	private final FlowPanel listPanel;
	private final FlexTable flexTable;
	protected final edu.scripps.yates.client.ProteinRetrievalServiceAsync proteinRetrievingService = ProteinRetrievalServiceAsync.Util
			.getInstance();
	protected T currentParent;
	private final boolean includeList;
	protected final List<DoSomethingTask2<Y>> doSomethingTaskAfterSelection = new ArrayList<DoSomethingTask2<Y>>();
	private CaptionPanel captionPanel;
	protected AbstractProjectStatsItemPanel<Y> selectedItemStatsPanel;
	private Y selectedItem;
	private Y clickedItem;

	/**
	 * Default {@link AbstractItemPanel} that includes an item list in the left
	 * and a right panel.
	 *
	 * @param title
	 * @param projectBean
	 */
	public AbstractItemPanel(String title, T parent) {
		this(title, parent, true, false);
	}

	/**
	 * Customizable {@link AbstractItemPanel}
	 *
	 * @param title
	 * @param projectBean
	 * @param includeList
	 *            if false, there will not be an item list and only the right
	 *            panel will be shown
	 * @param smallList
	 *            if true, the panel height will be limited according to CSS
	 *            rule ProjectItemListPanel-small
	 *
	 */
	public AbstractItemPanel(String title, T parent, boolean includeList, boolean smallList) {
		this.includeList = includeList;
		currentParent = parent;
		captionPanel = new CaptionPanel(title);
		initWidget(captionPanel);
		setStyleName("ProjectItemMainPanel");
		// if (includeList) {
		// flexTable = new Grid(1, 2);
		// } else {
		// flexTable = new Grid(1, 1);
		// }
		flexTable = new FlexTable();
		flexTable.setStyleName("ProjectItemContentPanel");

		captionPanel.add(flexTable);
		listPanel = new FlowPanel();
		if (smallList) {
			listPanel.setStyleName("ProjectItemListPanel-small");
		} else {
			listPanel.setStyleName("ProjectItemListPanel");
		}
		if (includeList) {
			flexTable.setWidget(0, 0, listPanel);
		}
		flexTable.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

	}

	public void addRightPanel(Widget widget) {
		int row = 0;
		int col = 1;
		if (!includeList) {
			col = 0;
		}
		flexTable.setWidget(row, col, widget);
		flexTable.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		widget.setStyleName("ProjectItemRightPanel");

	}

	/**
	 * Adds an item to the list, and adds a mouse over handler which will call
	 * to selectItem(item)
	 *
	 * @param itemName
	 * @param item
	 */
	protected void addItemToList(String itemName, final Y item) {
		addItemToList(itemName, item, null);
	}

	/**
	 * Adds an item to the list, and adds a mouse over handler which will call
	 * to selectItem(item)
	 *
	 * @param itemName
	 * @param item
	 * @param titleOnItem
	 *            a title to add to the item
	 */
	protected void addItemToList(String itemName, final Y item, String titleOnItem) {
		FlowPanel panel = new FlowPanel();
		panel.setTitle(titleOnItem);
		Label label = new Label(itemName);
		label.setWordWrap(false);
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(label);
		label.setWidth("100%");
		panel.setStyleName("ProjectItemPanel");
		panelsByItem.put(item, panel);
		items.add(item);
		listPanel.add(panel);

		// mouseoverhandler to show description
		MouseOverHandler mouseOverHandler = new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (!item.equals(selectedItem) && selectedItem != null) {
					// deselect the previous one
					panelsByItem.get(selectedItem).setStyleName("ProjectItemPanel");
				}
				performSelection(item);
			}
		};
		addMouseOverHandlerToItem(item, mouseOverHandler, MouseOverEvent.getType());

		// onMouseOuthandler to unselect the item and select the selected item
		// if available
		MouseOutHandler mouseoutHandler = new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				panelsByItem.get(item).setStyleName("ProjectItemPanel");
				if (selectedItem != null) {
					if (clickedItem != null && !clickedItem.equals(selectedItem)) {
						selectedItem = clickedItem;

					}
					performSelection(selectedItem);
				}
			}
		};
		addMouseOutHandlerToItem(item, mouseoutHandler, MouseOutEvent.getType());

		// onClick handler
		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// if it was clicked before, remove the clicked
				if (clickedItem != null && clickedItem.equals(item)) {
					panelsByItem.get(clickedItem).setStyleName("ProjectItemPanel");
					clickedItem = null;
					selectedItem = null;
				} else {
					clickedItem = item;
					selectedItem = item;
					performSelection(selectedItem);
				}

			}
		};
		addMouseClickHandlerToItem(item, clickHandler, ClickEvent.getType());

	}

	/**
	 * Sets an unique item to the list with no events with the mouse Note that
	 * this call will clear the item list before.
	 *
	 * @param itemName
	 * @param item
	 * @param titleOnItem
	 *            a title to add to the item
	 */
	protected void setUniqueItemToList(Widget panel) {
		clearItemList();

		listPanel.add(panel);

	}

	public void addMouseOverHandlerToItem(Y item, MouseOverHandler handler, Type<MouseOverHandler> type) {
		if (panelsByItem.containsKey(item)) {
			panelsByItem.get(item).addDomHandler(handler, type);
		}
	}

	public void addMouseOutHandlerToItem(Y item, MouseOutHandler handler, Type<MouseOutHandler> type) {
		if (panelsByItem.containsKey(item)) {
			panelsByItem.get(item).addDomHandler(handler, type);
		}
	}

	public void addMouseClickHandlerToItem(Y item, ClickHandler handler, Type<ClickHandler> type) {
		if (panelsByItem.containsKey(item)) {
			panelsByItem.get(item).addDomHandler(handler, type);
		}
	}

	/**
	 * If the item is null or empty, returns saying that the itemName is not
	 * available.
	 *
	 * @param itemName
	 * @param item
	 * @return
	 */
	protected String getStringNotAvailable(String itemName, String item) {
		if (item == null || "".equals(item)) {
			return itemName + " not available.";
		}
		return item;
	}

	public void clearItemList() {
		panelsByItem.clear();
		listPanel.clear();
		items.clear();

	}

	protected void selectFirstItem() {
		if (!items.isEmpty()) {
			performSelection(items.get(0));
		}
	}

	/**
	 * Use this method to actually perform a selection of the item. It will call
	 * to selectItem(item) and doAfterSelection(item)
	 *
	 * @param item
	 */
	public void performSelection(Y item) {
		selectItem(item);
		panelsByItem.get(item).setStyleName("ProjectItemPanel-selected");
		selectedItem = item;
		doAfterSelection(item);
	}

	public void unselectItems() {
		performSelection(null);
	}

	public abstract void selectItem(Y item);

	public abstract void updateParent(T parent);

	private void doAfterSelection(Y item) {
		for (DoSomethingTask2<Y> doSomethingTask : doSomethingTaskAfterSelection) {
			doSomethingTask.doSomething(item);
		}
	}

	protected List<Y> getItems() {
		return items;
	}

	public void addOnItemSelectedEvent(DoSomethingTask2<Y> doSomethingTask) {
		doSomethingTaskAfterSelection.add(doSomethingTask);
	}

	public void setCaption(String captionTitle) {
		captionPanel.setCaptionText(captionTitle);
	}

	public final AbstractProjectStatsItemPanel<Y> getSelectedItemStatsPanel() {

		return selectedItemStatsPanel;
	}
}
