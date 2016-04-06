package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class IncrementableTextBox extends Composite {
	private final IntegerBox integerBox;
	private final Button buttonPlus;
	private final Button buttonMinus;
	private final Grid grid;

	public IncrementableTextBox(int defaultValue) {

		grid = new Grid(1, 3);
		grid.setCellSpacing(5);
		initWidget(grid);
		grid.setWidth("100px");

		integerBox = new IntegerBox();
		integerBox.setAlignment(TextAlignment.RIGHT);
		integerBox.setVisibleLength(2);
		integerBox.setValue(defaultValue);
		integerBox.setReadOnly(true);

		grid.setWidget(0, 0, integerBox);
		integerBox.setWidth("30px");

		buttonMinus = new Button("-");
		buttonMinus.setStyleName("incrementalTextBoxButton");
		grid.setWidget(0, 2, buttonMinus);
		buttonMinus.setSize("27px", "26px");
		buttonMinus.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Integer newValue = integerBox.getValue() - 1;
				if (newValue < 0)
					newValue = 0;
				integerBox.setValue(newValue);

			}
		});

		buttonPlus = new Button("+");
		buttonPlus.setStyleName("incrementalTextBoxButton");
		grid.setWidget(0, 1, buttonPlus);
		buttonPlus.setSize("27px", "26px");
		grid.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setVerticalAlignment(0, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		grid.getCellFormatter().setVerticalAlignment(0, 1,
				HasVerticalAlignment.ALIGN_MIDDLE);
		grid.getCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setVerticalAlignment(0, 2,
				HasVerticalAlignment.ALIGN_MIDDLE);
		buttonPlus.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Integer newValue = integerBox.getValue() + 1;
				if (newValue < 0)
					newValue = 0;
				integerBox.setValue(newValue);

			}
		});
	}

	/**
	 * Gets the current integer number on {@link TextBox}. Returns 0 if some
	 * parsing error is found or if negative number
	 * 
	 * @return
	 */
	public int getIntegerNumber() {
		final Integer value = integerBox.getValue();
		if (value != null)
			return value;
		return 0;
	}

	/**
	 * Adds a handler for clicking on plus button
	 * 
	 * @param handler
	 */
	public void addPlusButtonHandler(ClickHandler handler) {
		buttonPlus.addClickHandler(handler);
	}

	/**
	 * Adds a handler for clicking on minus button
	 * 
	 * @param handler
	 */
	public void addMinusButtonHandler(ClickHandler handler) {
		buttonMinus.addClickHandler(handler);
	}

	/**
	 * Sets the value in the {@link TextBox}
	 * 
	 * @param num
	 */
	public void setValue(int num) {
		integerBox.setValue(num);
	}
}
