package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReporter;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.AbstractWizardPage;
import edu.scripps.yates.client.ui.wizard.pages.widgets.AbstractItemWidget;
import edu.scripps.yates.client.ui.wizard.pages.widgets.MaximizedStatus;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ExtendedTextBox;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

/**
 * This class will have to be extended by any other class that is used as a main
 * panel in an implementation of an {@link AbstractWizardPage}. <br>
 * The parameterized classes are IW as an extension of an
 * {@link AbstractItemWidget} and IB a itemBean, object part of the
 * {@link PintImportCfgBean}
 * 
 * @author salvador
 *
 * @param <IW>
 * @param <IB>
 */
public abstract class AbstractItemPanel<IW extends AbstractItemWidget<IB>, IB> extends FlexTable
		implements StatusReporter, IDGenerator {
	private final Wizard<PintContext> wizard;
	private Label createNewItemLabel;
	private SuggestBox createNewItemTextbox;
	private VerticalPanel createdItemsPanel;
	private Label createdOrNotItems;
	private final String NO_ITEM_CREATED_YET;
	private final String itemName;
	private final List<IW> itemWidgets = new ArrayList<IW>();
	private HorizontalPanel filterByIDPanel;
	private boolean resetNameForEachNewItemCreated = true;
	private boolean showSuggestionsWithNoTyping = false;

	/**
	 * Creates an {@link AbstractItemPanel} with a wizard and an itemName which will
	 * be used in some labels of the panel
	 * 
	 * @param wizard
	 * @param itemName
	 */
	public AbstractItemPanel(Wizard<PintContext> wizard, String itemName) {
//		setSize("300", "400");
		this.itemName = itemName;
		NO_ITEM_CREATED_YET = "No " + itemName + " created yet";
		this.wizard = wizard;

		setStyleName(WizardStyles.WizardQuestionPanel);
		init();

	}

	@Override
	public void showMessage(String message) {
		Window.confirm(message);
	}

	@Override
	public void showErrorMessage(Throwable throwable) {
		Window.alert(throwable.getMessage());
	}

	@Override
	public String getStatusReporterKey() {
		return getClass().getName();
	}

	private void editItemName(boolean edit) {
		if (edit) {
			if (resetNameForEachNewItemCreated) {
				createNewItemTextbox.setText(null);
			}
			setWidget(0, 0, createNewItemTextbox);
			createNewItemTextbox.setFocus(true);
		} else {

			setWidget(0, 0, createNewItemLabel);
			try {
				getWidget(0, 1);
			} catch (final IndexOutOfBoundsException e) {
				final Label widget = new Label(" ");
				widget.getElement().getStyle().setWidth(100, Unit.PX);
				setWidget(0, 1, widget);
			}
			createNewItemTextbox.hideSuggestionList();
		}
	}

	private void init() {

		createNewItemLabel = new Label("Click here to create a new " + itemName);
		createNewItemLabel.setStyleName(WizardStyles.WizardNewItemLabel);
		createNewItemTextbox = new SuggestBox();
		createNewItemTextbox.setStyleName(WizardStyles.WizardNewItemLabel);
		fillSuggestions(createNewItemTextbox);
		final SelectionHandler<Suggestion> suggestionSelectionHandler = getSuggestionSelectionHandler(
				createNewItemTextbox);
		if (suggestionSelectionHandler != null) {
			createNewItemTextbox.addSelectionHandler(suggestionSelectionHandler);
		}
		editItemName(false);

		createNewItemLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editItemName(true);
				if (createNewItemTextbox.getValue() == null || "".equals(createNewItemTextbox.getValue())) {
					if (showSuggestionsWithNoTyping) {
						createNewItemTextbox.showSuggestionList();
					}
				}
			}
		});
		// if ESCAPE key, just come back to label
		// if ENTER key, come back to label is no text or create sample if text
		createNewItemTextbox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					editItemName(false);
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
						|| event.getNativeKeyCode() == KeyCodes.KEY_MAC_ENTER
						|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					if ("".equals(createNewItemTextbox.getValue())) {
						editItemName(false);
					} else {
						if (!createNewItemTextbox.getValue().equals(createNewItemLabel.getText())) {
							// just if something changed
							createItemBeanAndWidget(createNewItemTextbox.getValue());
						}
						editItemName(false);
					}
				}

			}
		});

		// if loses focus and it is empty, come back to label
		// if it is not empty, create a new sample and return to label
		createNewItemTextbox.getValueBox().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				GWT.log(" Blur");
				if ("".equals(createNewItemTextbox.getValue())) {
					editItemName(false);
				} else {
					if (!createNewItemTextbox.isSuggestionListShowing()) {
						if (!createNewItemTextbox.getValue().equals(createNewItemLabel.getText())) {
							// just if something changed
							createItemBeanAndWidget(createNewItemTextbox.getValue());
						}
						editItemName(false);
					}
				}
			}
		});
		filterByIDPanel = new HorizontalPanel();
		filterByIDPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		filterByIDPanel.setVisible(false); // not visible until having created items
		setWidget(1, 0, filterByIDPanel);
		getFlexCellFormatter().setColSpan(1, 0, 2);
		setWidget(2, 0, createdItemsPanel);
		getFlexCellFormatter().setColSpan(2, 0, 2);

		// FILTER
		final String filterTitle = "Type here to filter created items";
		final Label label = new Label("Filter:");
		label.setTitle(filterTitle);
		label.getElement().getStyle().setMarginRight(10, Unit.PX);
		label.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		filterByIDPanel.add(label);
		final ExtendedTextBox filterTextBox = new ExtendedTextBox();
		filterTextBox.setTitle(filterTitle);
		filterByIDPanel.add(filterTextBox);
		final String NO_FILTER = "no filter";
		final Label numberOfVisibleItems = new Label(NO_FILTER);
		numberOfVisibleItems.setTitle(numberOfVisibleItems.getText());
		numberOfVisibleItems.setStyleName(WizardStyles.WizardItemWidgetPropertyNameLabel);
		filterTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					filterTextBox.setValue("");
				}
				final String filterString = filterTextBox.getValue();
				final int visibles = filterCreatedItems(filterString);
				if (visibles != itemWidgets.size()) {
					numberOfVisibleItems.setText("showing " + visibles + "/" + itemWidgets.size());
				} else {
					numberOfVisibleItems.setText(NO_FILTER);
				}
				numberOfVisibleItems.setTitle(numberOfVisibleItems.getText());
			}
		});
		filterByIDPanel.add(numberOfVisibleItems);
		numberOfVisibleItems.getElement().getStyle().setMarginLeft(10, Unit.PX);

		createdItemsPanel = new VerticalPanel();
		createdItemsPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		createdOrNotItems = new Label(NO_ITEM_CREATED_YET);
		createdOrNotItems.setStyleName(WizardStyles.WizardExplanationLabel);
		createdItemsPanel.add(createdOrNotItems);
		setWidget(3, 0, createdItemsPanel);
		getFlexCellFormatter().setColSpan(3, 0, 2);

		updateCreatedItems(wizard.getContext());

	}

	protected int filterCreatedItems(String filterString) {
		int numVisibles = 0;
		for (final IW itemWidget : itemWidgets) {
			final String itemBeanID = itemWidget.getItemBeanID();
			if ("".equals(filterString) || itemBeanID.toLowerCase().contains(filterString.toLowerCase())) {
				itemWidget.setVisible(true);
				numVisibles++;
			} else {
				itemWidget.setVisible(false);
			}
		}
		return numVisibles;
	}

	public void updateCreatedItems(PintContext context) {
		// remove created items that are in rows 1 or greater
		createdItemsPanel.clear();

		final List<IB> itemBeans = getItemBeansFromContext(context);
		for (final IB ib : itemBeans) {
			createItemWidget(ib);
		}
	}

	/**
	 * This method will be fired each time an item from the suggestions is selected
	 * 
	 * @return
	 */
	protected SelectionHandler<Suggestion> getSuggestionSelectionHandler(SuggestBox nameTextBox) {
		final SelectionHandler<Suggestion> handler = new SelectionHandler<Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				final Suggestion selectedItem = event.getSelectedItem();
				if (selectedItem != null) {
					final String selectedOrganism = selectedItem.getReplacementString();
					nameTextBox.setText(selectedOrganism);
				}
			}
		};
		return handler;

	}

	/**
	 * Method to add a list of items to the suggestBox
	 * 
	 * @param createNewItemTextbox2
	 */
	protected abstract void fillSuggestions(SuggestBox createNewItemTextbox2);

	/**
	 * Gets the item beans existing in the current {@link PintImportCfgBean} of the
	 * {@link PintContext}
	 * 
	 * @param context
	 * @return
	 */
	protected abstract List<IB> getItemBeansFromContext(PintContext context);

	private boolean createItemBeanAndWidget(String itemName) {
		final IB itemBean = createNewItemBean(itemName);
		try {
			addItemBeanToPintImportConfiguration(wizard.getContext(), itemBean);
		} catch (final PintException e) {
			e.printStackTrace();
			StatusReportersRegister.getInstance().notifyStatusReporters(e);
			return false;
		}
		createItemWidget(itemBean);

		return true;
	}

	private void createItemWidget(IB itemBean) {
		final IW itemWidget = createNewItemWidget(itemBean);
		itemWidget.addOnRemoveTask(getDoSomethingTaskOnRemoveItemBean(wizard.getContext()));
		itemWidget.addOnRemoveTask(getRemoveItemWidgetTaskOnRemoveItemBean(itemWidget));
		itemWidget.addOnDuplicateItemBeanTask(getDoSomethingTaskOnDuplicateIteamBean());
		itemWidget.setIDGenerator(this);
		itemWidgets.add(itemWidget);
		createdItemsPanel.add(itemWidget);
		createdOrNotItems.setText("Created " + itemName + "s:");
		this.filterByIDPanel.setVisible(true);
	}

	private DoSomethingTask2<IB> getRemoveItemWidgetTaskOnRemoveItemBean(IW itemWidget) {
		final DoSomethingTask2<IB> ret = new DoSomethingTask2<IB>() {

			@Override
			public Void doSomething(IB t) {
				itemWidgets.remove(itemWidget);
				return null;
			}
		};
		return ret;
	}

	private DoSomethingTask2<IB> getDoSomethingTaskOnDuplicateIteamBean() {
		final DoSomethingTask2<IB> ret = new DoSomethingTask2<IB>() {

			@Override
			public Void doSomething(IB itemBean) {
				try {
					addItemBeanToPintImportConfiguration(wizard.getContext(), itemBean);
				} catch (final PintException e) {
					e.printStackTrace();
					StatusReportersRegister.getInstance().notifyStatusReporters(e);
					return null;
				}
				createItemWidget(itemBean);
				return null;
			}
		};
		return ret;
	}

	/**
	 * Method with the tasks that will remove the item bean IB from the
	 * {@link PintContext} of the {@link PintImportCfgBean}.<br>
	 * Also, after removal, it should check whether the the
	 * {@link PintImportCfgBean} contains more items like IB and if not, it should
	 * call to super.setNoItemsCreatedYetLabel();
	 * 
	 * @return
	 */
	protected abstract DoSomethingTask2<IB> getDoSomethingTaskOnRemoveItemBean(PintContext context);

	/**
	 * Method to create the new {@link AbstractItemWidget} usually, using the item
	 * Bean IB
	 * 
	 * @param itemBean
	 * @return
	 */
	protected abstract IW createNewItemWidget(IB itemBean);

	/**
	 * Method to state how to add the item Bean IB to the {@link PintImportCfgBean}
	 * in the {@link PintContext}, usually using the {@link PintImportCfgUtil} which
	 * will throw an exception if there is already an item with the same id in the
	 * {@link PintImportCfgBean}
	 * 
	 * @param context
	 * @param itemBean
	 * @throws PintException if there is already an item with the same id in the
	 *                       {@link PintImportCfgBean} of the {@link PintContext}
	 */
	protected abstract void addItemBeanToPintImportConfiguration(PintContext context, IB itemBean) throws PintException;

	/**
	 * Method to create a new ItemBean (such as {@link SampleTypeBean or
	 * ExperimentalConditionTypeBean} usually, using the item name to set the ID
	 * 
	 * @param itemName
	 * @return
	 */
	protected abstract IB createNewItemBean(String itemName);

	final protected void setNoItemsCreatedYetLabel() {
		createdOrNotItems.setText(NO_ITEM_CREATED_YET);
		this.filterByIDPanel.setVisible(false);
	}

	protected Wizard<PintContext> getWizard() {
		return this.wizard;
	}

	public abstract void isReady() throws PintException;

	@Override
	public String getNewID(String id) {
		final Set<String> ids = new HashSet<String>();
		for (final IW itemWidget : itemWidgets) {
			ids.add(itemWidget.getItemBeanID());
		}
		String newID = id;
		while (id.equals(newID) || ids.contains(newID)) {
			// check whether the last character is a number
			try {
				int num = Integer.valueOf(newID.substring(newID.length() - 1));
				// then substitute that one with the next number
				newID = newID.substring(0, newID.length() - 1) + (++num);
			} catch (final NumberFormatException e) {
				newID = newID + "_2";
			}

		}
		return newID;
	}

	/**
	 * Creates a {@link MaximizedStatus} object with the status of maximization of
	 * each {@link AbstractItemWidget}
	 * 
	 * @return
	 */
	public MaximizedStatus getMaximizedStatus() {
		final MaximizedStatus ret = new MaximizedStatus();
		for (final IW itemWidget : itemWidgets) {
			final boolean maximized = itemWidget.isMaximized();
			final String itemBeanID = itemWidget.getItemBeanID();
			ret.addMaximizedStatus(itemBeanID, maximized);
		}
		return ret;
	}

	/**
	 * Sets the {@link MaximizedStatus} to the {@link AbstractItemWidget} in this
	 * panel
	 * 
	 * @param maximizedStatus
	 */
	public void setMaximizedStatus(MaximizedStatus maximizedStatus) {
		if (maximizedStatus != null) {
			for (final IW itemWidget : itemWidgets) {
				final String itemBeanID = itemWidget.getItemBeanID();
				final Boolean maximized = maximizedStatus.getMaximizedStatus(itemBeanID);
				if (maximized != null) {
					if (maximized) {
						itemWidget.maximize();
					} else {
						itemWidget.minimize();
					}
				}
			}
		}
	}

	public abstract String getID();

	public boolean isResetNameForEachNewItemCreated() {
		return resetNameForEachNewItemCreated;
	}

	public void setResetNameForEachNewItemCreated(boolean resetNameForEachNewItemCreated) {
		this.resetNameForEachNewItemCreated = resetNameForEachNewItemCreated;
	}

	public boolean isShowSuggestionsWithNoTyping() {
		return showSuggestionsWithNoTyping;
	}

	public void setShowSuggestionsWithNoTyping(boolean showSuggestionsWithNoTyping) {
		this.showSuggestionsWithNoTyping = showSuggestionsWithNoTyping;
	}
}
