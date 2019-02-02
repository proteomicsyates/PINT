package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.pages.panels.IDGenerator;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public abstract class AbstractItemWidget<IB> extends FlexTable {
	private final IB itemBean;
	private final Label nameLabel;
	private final SuggestBox nameTextBox;
	protected String previousNameValue;
	private final List<DoSomethingTask2<IB>> onRemoveTasks = new ArrayList<DoSomethingTask2<IB>>();
	private int numProperties = 0;
	private final Map<DroppableFormat, AbstractItemDropLabel> targetLabelsByFormat = new HashMap<DroppableFormat, AbstractItemDropLabel>();
	private final PintContext context;
	private final List<DoSomethingTask2<IB>> doSomethingTaskOnDuplicateIteamBeanTasks = new ArrayList<DoSomethingTask2<IB>>();
	private IDGenerator idGenerator;
	private final List<FlexTable> propertyWidgets = new ArrayList<FlexTable>();
	private final List<FlexTable> referencedItems = new ArrayList<FlexTable>();
	private Button minimizeButton;
	private boolean maximized = true;
	private boolean showSuggestionsWithNoTyping = false;

	protected AbstractItemWidget(IB itemBean, PintContext context) {
		this(itemBean, context, true);
	}

	protected AbstractItemWidget(IB itemBean, PintContext context, boolean duplicable) {
		this.itemBean = itemBean;
		this.context = context;
		this.setStyleName(WizardStyles.WizardItemWidget);

		nameLabel = new Label(getIDFromItemBean(itemBean));
		nameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabel);
		setWidget(0, 0, nameLabel);

		nameTextBox = new SuggestBox();
		nameTextBox.setStyleName(WizardStyles.WizardItemWidgetNameTextBox);
		nameTextBox.setValue(getIDFromItemBean(itemBean));
		final SelectionHandler<Suggestion> suggestionSelectionHandler = getSuggestionSelectionHandler(nameTextBox);
		if (suggestionSelectionHandler != null) {
			nameTextBox.addSelectionHandler(suggestionSelectionHandler);
		}

		// when clicked, show textBox and focus on it
		nameLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editName(true);
				nameTextBox.setFocus(true);
				if (nameTextBox.getValue() == null || "".equals(nameTextBox.getValue())) {
					if (showSuggestionsWithNoTyping) {
						nameTextBox.showSuggestionList();
					}
				}
			}
		});

		// if looses focus:
		// if text is empty, keep previous
		nameTextBox.getValueBox().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {

				if ("".equals(nameTextBox.getValue())) {
					nameTextBox.setValue(previousNameValue);
				}
				if (!nameTextBox.isSuggestionListShowing()) {
					if (!nameTextBox.getValue().equals(nameLabel.getText())) {
						// just if something changed
						try {
							updateIDFromItemBean(nameTextBox.getValue());
						} catch (final PintException e) {
							StatusReportersRegister.getInstance().notifyStatusReporters(e);
							// set the previous
							nameTextBox.setValue(previousNameValue);
						}
					}
					editName(false);
				}
			}
		});
		// if ESCAPE key, just come back to label
		// if ENTER key, come back to label is no text or create sample if text
		nameTextBox.getValueBox().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					editName(false);
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
						|| event.getNativeKeyCode() == KeyCodes.KEY_MAC_ENTER) {
					if (!nameTextBox.getValue().equals(nameLabel.getText())) {
						// just if something changed
						try {
							updateIDFromItemBean(nameTextBox.getValue());
						} catch (final PintException e) {
							StatusReportersRegister.getInstance().notifyStatusReporters(e);
							// set the previous
							nameTextBox.setValue(previousNameValue);
							// reset previous name
							previousNameValue = null;
						}
					}

					editName(false);
					event.preventDefault();
					event.stopPropagation();
				}
			}
		});
		final HorizontalPanel buttons = new HorizontalPanel();
		setWidget(0, 1, buttons);
		getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		if (duplicable) {
			// duplicate button
			final Button duplicateButton = new Button("duplicate");
			duplicateButton.setStyleName(WizardStyles.WizardButtonSmall);
			duplicateButton.setTitle(duplicateButton.getText());
			buttons.add(duplicateButton);
			duplicateButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					duplicateItemBeanAndCreateWidget(itemBean);
				}
			});
		}
		// delete button
		final Button deleteButton = new Button("delete");
		deleteButton.setStyleName(WizardStyles.WizardButtonSmall);
		deleteButton.setTitle(deleteButton.getText());
		deleteButton.getElement().getStyle().setMarginLeft(10, Unit.PX);
		buttons.add(deleteButton);

		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onRemoveItem(itemBean);
			}
		});
		// minimize button
		minimizeButton = new Button("-");
		minimizeButton.setStyleName(WizardStyles.WizardButtonMini);
		minimizeButton.setTitle("minimize");
		minimizeButton.getElement().getStyle().setMarginLeft(10, Unit.PX);
		buttons.add(minimizeButton);

		minimizeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (maximized) {
					minimize();
				} else {
					maximize();
				}
			}
		});
		// ask for list of organisms
		fillSuggestions(nameTextBox, context.getSessionID());
	}

	public boolean isMaximized() {
		return maximized;
	}

	public void minimize() {
		maximized = false;
		// Hide any property of the item and any reference label
		setPropertyItemsVisible(false);
		setReferencedItemsVisible(false);
		// change minimize button to maximize
		minimizeButton.setText("+");
		minimizeButton.setTitle("maximize");
	}

	public void maximize() {
		maximized = true;
		// show any property of the item and any reference label
		setPropertyItemsVisible(true);
		setReferencedItemsVisible(true);
		// change minimize button to minimize
		minimizeButton.setText("-");
		minimizeButton.setTitle("minimize");
	}

	private void setPropertyItemsVisible(boolean visible) {
		for (final FlexTable propertyWidget : propertyWidgets) {
			propertyWidget.setVisible(visible);
		}
	}

	private void setReferencedItemsVisible(boolean visible) {
		for (final FlexTable table : referencedItems) {
			table.setVisible(visible);
		}
	}

	protected void duplicateItemBeanAndCreateWidget(IB itemBean) {
		final IB duplicatedItemBean = duplicateItemBean(itemBean);
		if (duplicatedItemBean == null) {
			// this is because the duplicateItemBean is not implemented because this item is
			// not duplicable
		} else {
			for (final DoSomethingTask2<IB> doSomethingTask2 : doSomethingTaskOnDuplicateIteamBeanTasks) {
				doSomethingTask2.doSomething(duplicatedItemBean);
			}
		}
	}

	protected abstract IB duplicateItemBean(IB itemBean);

	/**
	 * Implementation to add suggestions to the nameTextBox. This will not be called
	 * unless the implementing class does it.
	 * 
	 * @param nameTextBox
	 * @param sessionID   used if needed to submit to the server
	 */
	protected abstract void fillSuggestions(SuggestBox nameTextBox, String sessionID);

	private void editName(boolean edit) {
		if (edit) {
			previousNameValue = nameLabel.getText();
			setWidget(0, 0, nameTextBox);
			nameTextBox.setFocus(true);
			if (nameTextBox.getValue() != null && !"".equals(nameTextBox.getValue())) {
				nameTextBox.getValueBox().selectAll();
			}
		} else {
			nameLabel.setText(nameTextBox.getValue());
			// reset previous name
			previousNameValue = null;
			setWidget(0, 0, nameLabel);
			nameTextBox.hideSuggestionList();

		}
	}

	protected abstract String getIDFromItemBean(IB item);

	protected abstract void updateIDFromItemBean(String newId) throws PintException;

	protected IB getItemBean() {
		return itemBean;
	}

	public String getItemBeanID() {
		return getIDFromItemBean(getItemBean());
	}

	public void onRemoveItem(IB item) {
		for (final DoSomethingTask2<IB> task : onRemoveTasks) {
			task.doSomething(item);
		}
		// then, remove widget from parent
		this.removeFromParent();
	}

	public void addOnRemoveTask(DoSomethingTask2<IB> task) {
		this.onRemoveTasks.add(task);
	}

	public void addPropertyWidget(FlexTable propertyWidget) {
		numProperties++;
		setWidget(numProperties, 0, propertyWidget);
		getFlexCellFormatter().setColSpan(numProperties, 0, 2);
		propertyWidgets.add(propertyWidget);
	}

	/**
	 * Call this method to add a dropping area to make a reference to other item
	 * bean
	 * 
	 * @param droppingAreaText
	 * @param format
	 * @mandatory whether this association is mandatory or not
	 */
	public void addDroppingAreaForReferencedItemBean(String referencedItemBeanName, String referencedItemTitle,
			String droppingAreaText, DroppableFormat format, boolean mandatory) {

		numProperties++;
		final FlexTable table = new FlexTable();
		this.referencedItems.add(table);
		final ItemDropLabelForItemWidget droppingLabel = new ItemDropLabelForItemWidget(droppingAreaText, format, this,
				mandatory);
		droppingLabel.setTitle(referencedItemTitle);
		this.targetLabelsByFormat.put(format, droppingLabel);
		if (mandatory) {
			final Label referencedItemBeanNameLabel = new Label(referencedItemBeanName + ":");
			referencedItemBeanNameLabel.setTitle(referencedItemTitle);
			referencedItemBeanNameLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyNameLabel);
			table.setWidget(0, 0, referencedItemBeanNameLabel);
		} else {
			final Label referencedItemBeanNameLabel = new Label(referencedItemBeanName + ":");
			referencedItemBeanNameLabel.setTitle(referencedItemTitle);
			referencedItemBeanNameLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyNameLabel);
			final Label optionalLabel = new Label("(optional)");
			optionalLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyNameLabelSmaller);
			optionalLabel.setTitle("This reference is optional. You can leave it empty if you want.");
			optionalLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
			final HorizontalPanel panel = new HorizontalPanel();
			panel.add(referencedItemBeanNameLabel);
			panel.add(optionalLabel);
			table.setWidget(0, 0, panel);
		}
		table.setWidget(0, 1, droppingLabel);
		setWidget(numProperties, 0, table);
		getFlexCellFormatter().setColSpan(numProperties, 0, 2);

	}

	/**
	 * Sets the dropping area to an actual referenced item
	 * 
	 * @param itemBeanID
	 * @param format
	 */
	public void updateReferenceLabel(String itemBeanID, DroppableFormat format) {
		if (targetLabelsByFormat.containsKey(format)) {
			targetLabelsByFormat.get(format).setDroppedData(itemBeanID);
		}
	}

	/**
	 * Method used to make the association between two item beans of the
	 * {@link PintImportCfgBean}.<br>
	 * So, if the item bean IB has a reference to other item bean of format
	 * {@link DroppableFormat}, this method should set the itemBeanID to the
	 * reference.
	 * 
	 * @param itemBeanID
	 */
	public abstract void updateReferencedItemBeanID(String itemBeanID, DroppableFormat format);

	/**
	 * In case of using the nameTextBox as a suggestion box, this handler be fired
	 * when a selection is made
	 * 
	 * @param nameTextBox2
	 * 
	 * @return
	 */
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

	protected PintContext getContext() {
		return this.context;
	}

	public void addOnDuplicateItemBeanTask(DoSomethingTask2<IB> doSomethingTaskOnDuplicateIteamBeanTask) {
		this.doSomethingTaskOnDuplicateIteamBeanTasks.add(doSomethingTaskOnDuplicateIteamBeanTask);

	}

	public void setIDGenerator(IDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public String getNewID(String id) {
		return idGenerator.getNewID(id);
	}

	public boolean isShowSuggestionsWithNoTyping() {
		return showSuggestionsWithNoTyping;
	}

	public void setShowSuggestionsWithNoTyping(boolean showSuggestionsWithNoTyping) {
		this.showSuggestionsWithNoTyping = showSuggestionsWithNoTyping;
	}
}
