package edu.scripps.yates.client.ui.wizard.view.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class ItemWidget<T> extends FlexTable {
	private final T itemObj;
	private final Label nameLabel;
	private final TextBox nameTextBox;
	protected String previousNameValue;
	private final List<DoSomethingTask2<T>> onRemoveTasks = new ArrayList<DoSomethingTask2<T>>();
	private int numProperties = 0;

	protected ItemWidget(T itemObj) {
		this.itemObj = itemObj;
		this.setStyleName(WizardStyles.WizardItemWidget);

		nameLabel = new Label(getIdFromItem(itemObj));
		nameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabel);
		setWidget(0, 0, nameLabel);

		nameTextBox = new TextBox();
		nameTextBox.setStyleName(WizardStyles.WizardItemWidgetNameTextBox);
		nameTextBox.setValue(getIdFromItem(itemObj));

		// when clicked, show textBox and focus on it
		nameLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editName(true);

				// give focus to textbox
				nameTextBox.setFocus(true);
			}
		});
		// if looses focus:
		// if text is empty, keep previous
		nameTextBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if ("".equals(nameTextBox.getValue())) {
					nameTextBox.setValue(previousNameValue);
					// reset previous name
					previousNameValue = null;
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
					updateIDFromItem(nameTextBox.getValue());
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
				onRemoveItem(itemObj);
			}
		});
	}

	private void editName(boolean edit) {
		if (edit) {
			setWidget(0, 0, nameTextBox);
		} else {
			setWidget(0, 0, nameLabel);
		}
	}

	protected abstract String getIdFromItem(T item);

	protected abstract void updateIDFromItem(String newId);

	protected T getItemObj() {
		return itemObj;
	}

	public void onRemoveItem(T item) {
		for (final DoSomethingTask2<T> task : onRemoveTasks) {
			task.doSomething(item);
		}
	}

	public void addOnRemoveTask(DoSomethingTask2<T> task) {
		this.onRemoveTasks.add(task);
	}

	public void addPropertyWidget(ItemLongPropertyWidget<T> itemLongPropertyWidget) {
		numProperties++;
		setWidget(numProperties, 0, itemLongPropertyWidget);
		getFlexCellFormatter().setColSpan(numProperties, 0, 2);
	}
}
