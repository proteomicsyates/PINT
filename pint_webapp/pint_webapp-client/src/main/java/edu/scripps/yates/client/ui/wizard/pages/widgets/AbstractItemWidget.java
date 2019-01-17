package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public abstract class AbstractItemWidget<IB> extends FlexTable {
	private final IB itemBean;
	private final Label nameLabel;
	private final SuggestBox nameTextBox;
	protected String previousNameValue;
	private final List<DoSomethingTask2<IB>> onRemoveTasks = new ArrayList<DoSomethingTask2<IB>>();
	private int numProperties = 0;
	private final Map<DroppableFormat, ItemDropLabel> targetLabelsByFormat = new HashMap<DroppableFormat, ItemDropLabel>();
	private final PintContext context;

	protected AbstractItemWidget(IB itemBean, PintContext context) {
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
				;
			}
		});

		// if looses focus:
		// if text is empty, keep previous
		nameTextBox.getValueBox().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {

				if ("".equals(nameTextBox.getValue())) {
					nameTextBox.setValue(previousNameValue);
					// reset previous name
					previousNameValue = null;
				}
				if (!nameTextBox.isSuggestionListShowing()) {
					editName(false);
				}
			}
		});
		// if ESCAPE key, just come back to label
		// if ENTER key, come back to label is no text or create sample if text
		nameTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					editName(false);
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
						|| event.getNativeKeyCode() == KeyCodes.KEY_MAC_ENTER) {
					editName(false);
				} else {
					updateIDFromItemBean(nameTextBox.getValue());
					nameLabel.setText(nameTextBox.getValue());
					if (previousNameValue == null) {
						previousNameValue = nameTextBox.getValue();
					}
				}
			}
		});
		final Button deleteButton = new Button("delete");
		deleteButton.setStyleName(WizardStyles.WizardButton);
		deleteButton.setTitle(deleteButton.getText());
		setWidget(0, 1, deleteButton);
		getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onRemoveItem(itemBean);
			}
		});
		// ask for list of organisms
		fillSuggestions(nameTextBox, context.getSessionID());
	}

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
			setWidget(0, 0, nameTextBox);
		} else {
			setWidget(0, 0, nameLabel);
		}
	}

	protected abstract String getIDFromItemBean(IB item);

	protected abstract void updateIDFromItemBean(String newId);

	protected IB getItemBean() {
		return itemBean;
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

	public void addPropertyWidget(ItemLongPropertyWidget<IB> itemLongPropertyWidget) {
		numProperties++;
		setWidget(numProperties, 0, itemLongPropertyWidget);
		getFlexCellFormatter().setColSpan(numProperties, 0, 2);
	}

	/**
	 * Call this method to add a dropping area to make a reference to other item
	 * bean
	 * 
	 * @param droppingAreaText
	 * @param format
	 */
	public void addDroppingAreaForReferencedItemBean(String droppingAreaText, DroppableFormat format) {

		numProperties++;
		final ItemDropLabel droppingLabel = new ItemDropLabel(droppingAreaText, format, this);
		this.targetLabelsByFormat.put(format, droppingLabel);
		droppingLabel.setTitle(droppingAreaText);
		droppingLabel.setStyleName(WizardStyles.WizardDragTargetLabel);
		setWidget(numProperties, 0, droppingLabel);
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
}
