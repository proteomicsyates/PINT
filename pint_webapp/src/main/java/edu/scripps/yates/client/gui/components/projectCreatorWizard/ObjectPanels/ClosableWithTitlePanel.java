package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.interfaces.HasId;

public abstract class ClosableWithTitlePanel extends Composite implements
		RepresentsDataObject, ContainsImportJobID {
	private FlowPanel disclosurePanel;
	private Label label;
	protected SuggestBox textBox;
	private Grid headerPanel;
	private FlowPanel contentPanel;
	private final int id;
	private Button button;
	protected int importJobID;
	protected Label lblclickToEdit;
	private final List<ClickHandler> methodsToExecuteWhenClose = new ArrayList<ClickHandler>();
	private final List<ExecutableTaskOnChangeID> methodsToExecuteOnChangeID = new ArrayList<ExecutableTaskOnChangeID>();
	private boolean textBoxIsShown = false;
	protected final String sessionID;

	/**
	 * @wbp.parser.constructor
	 */
	public ClosableWithTitlePanel(String sessionID, int importJobID) {
		this(sessionID, importJobID, "Mi editable disclosure panel", true);
	}

	public ClosableWithTitlePanel(String sessionID, int importJobID,
			String title, boolean isOpen) {
		this.importJobID = importJobID;
		this.sessionID = sessionID;
		// get new ID
		id = ProjectCreatorRegister.getNextAvailableID();
		// register the pro
		ProjectCreatorRegister.registerProjectObjectRepresenter(id, this);

		// Init Disclosure GUI
		initDisclosurePanelWidget(title, isOpen);
		initWidget(disclosurePanel);
	}

	/**
	 * Delete the register of any representation of this
	 * ProjectObjectRepresenter, remove the widget from the parent and fires the
	 * corresponding modification event to the potential listeners
	 */
	public void close() {
		// remove the registered objects
		ProjectCreatorRegister.deleteProjectObjectRepresenter(getInternalID());

		// remove as a listener if it is a Referencer
		if (this instanceof ReferencesDataObject) {
			((ReferencesDataObject) this).unregisterAsListener();
		}

		// remove from parent
		removeFromParent();

		// update all objects referring to this
		fireModificationEvent();

		// look into the list of handlers that have to be executed here
		for (ClickHandler clickHandler : methodsToExecuteWhenClose) {
			clickHandler.onClick(null);
		}
	}

	/**
	 * The header widget will be a panel, that by default contains a label with
	 * the title. If clicked, it will turn into a textbox to edit the text. If
	 * return key is pressed, or any other click in other position of the screen
	 * not in the header is performed, then it will return to be a label.
	 * 
	 * @param title
	 * @param isOpen
	 * @return
	 */
	private void initDisclosurePanelWidget(String title, boolean isOpen) {
		headerPanel = new Grid(1, 3);
		headerPanel.setStyleName("EditableDisclosurePanelHeader");
		disclosurePanel = new FlowPanel();
		disclosurePanel.setStyleName("EditableDisclosurePanel");
		// disclosurePanel.setOpen(isOpen);
		// disclosurePanel.setHeader(headerPanel);
		disclosurePanel.add(headerPanel);
		headerPanel.setWidth("100%");

		headerPanel.setWidth("100%");

		contentPanel = new FlowPanel();
		contentPanel.setStyleName("EditableDisclosurePanelContainer");
		disclosurePanel.add(contentPanel);

		// label
		label = new Label(title);

		// button
		ClickHandler listenerToClose = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		};
		button = new Button("x", listenerToClose);
		button.setSize("20px", "20px");
		setHeaderPanel(label, button);

		lblclickToEdit = new Label("(click to edit the name)");
		lblclickToEdit.setStyleName("EditableDisclosurePanelHeader-smallFont");
		lblclickToEdit
				.setTitle("Click here to edit the header of the item. Click on the button to remove this item.");
		headerPanel.setWidget(0, 1, lblclickToEdit);

		headerPanel.getCellFormatter().setVerticalAlignment(0, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		headerPanel.getCellFormatter().setVerticalAlignment(0, 1,
				HasVerticalAlignment.ALIGN_MIDDLE);
		headerPanel.getCellFormatter().setVerticalAlignment(0, 2,
				HasVerticalAlignment.ALIGN_MIDDLE);

		headerPanel.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT);
		headerPanel.getCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		headerPanel.getCellFormatter().setHorizontalAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// textbox
		textBox = new SuggestBox();
		textBox.setText(title);

		final ClickHandler clickHandler = getClickHandler();
		label.addClickHandler(clickHandler);
		lblclickToEdit.addClickHandler(clickHandler);
		final KeyUpHandler keyUpHandler = getKeyUpHandler();
		textBox.addKeyUpHandler(keyUpHandler);

		// blurhandler to be called when lose the focus
		textBox.getValueBox().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				// if suggestion display is shown, not trigger the
				// methodsToExecuteOnChangeID
				final SuggestionDisplay suggestionDisplay = textBox
						.getSuggestionDisplay();
				if (suggestionDisplay instanceof DefaultSuggestionDisplay) {
					DefaultSuggestionDisplay defaultSuggestionDisplay = (DefaultSuggestionDisplay) suggestionDisplay;
					if (defaultSuggestionDisplay.isSuggestionListShowing())
						return;
				}
				// execute if any the ExecutableTaskOnChangeID
				for (ExecutableTaskOnChangeID executableTaskOnChangeID : methodsToExecuteOnChangeID) {
					final String oldID = label.getText();
					final String newID = textBox.getText();
					executableTaskOnChangeID.executeTask(oldID, newID);
				}
				showLabel();
				fireModificationEvent();
			}
		});

		// task to be run when change id, to check if that id is already used as
		// id in any other objectPanel
		addOnChangeIDTask(new ExecutableTaskOnChangeID() {

			@Override
			public void executeTask(String oldID, String newID) {
				if (oldID.equals(newID))
					return;
				if (ProjectCreatorRegister
						.containsAnyObjectRepresenterWithId(newID)) {
					label.setText(oldID);
					textBox.setText(oldID);
					updateRepresentedObject();
					StatusReportersRegister.getInstance()
							.notifyStatusReporters(
									"'" + newID
											+ "' identifier is already in use");

				}
			}
		});
	}

	/**
	 * Adds a new {@link ClickHandler} to the close button
	 * 
	 * @param handler
	 * @param executeAlsoWhenCloseMethodIsCalled
	 *            whether this method will be executed when
	 *            {@link ClosableWithTitlePanel}.close() method is called or
	 *            not.
	 */
	public void addCloseClickHandler(ClickHandler handler,
			boolean executeAlsoWhenCloseMethodIsCalled) {
		button.addClickHandler(handler);
		if (executeAlsoWhenCloseMethodIsCalled) {
			methodsToExecuteWhenClose.add(handler);
		}
	}

	private void setHeaderPanel(Widget widget1, Widget widget2) {
		headerPanel.clear();
		headerPanel.setWidget(0, 0, widget1);
		headerPanel.setWidget(0, 1, lblclickToEdit);
		headerPanel.setWidget(0, 2, widget2);
	}

	private KeyUpHandler getKeyUpHandler() {
		final KeyUpHandler keyUpHandler = new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				keyEventMethod(event.getNativeKeyCode());
			}
		};
		return keyUpHandler;
	}

	private void keyEventMethod(int keyCode) {

		if (keyCode == KeyCodes.KEY_ENTER) {
			GWT.log("enter key detected. Change ID from " + label.getText()
					+ " to " + textBox.getText());
			// execute if any the ExecutableTaskOnChangeID
			if (!label.getText().equals(textBox.getText())) {
				for (ExecutableTaskOnChangeID executableTaskOnChangeID : methodsToExecuteOnChangeID) {
					executableTaskOnChangeID.executeTask(label.getText(),
							textBox.getText());
				}
			}
			showLabel();
			fireModificationEvent();
		} else if (keyCode == KeyCodes.KEY_TAB) {
			showLabel();
			fireModificationEvent();
		}
	}

	/**
	 * Add {@link ExecutableTaskOnChangeID} to execute on a changeID
	 * 
	 * @param task
	 */
	public void addOnChangeIDTask(ExecutableTaskOnChangeID task) {
		methodsToExecuteOnChangeID.add(task);
	}

	private void showLabel() {
		label.setText(textBox.getText());
		setHeaderPanel(label, button);
		lblclickToEdit.setText("(click to edit the name)");
		textBoxIsShown = false;
	}

	private ClickHandler getClickHandler() {
		final ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				GWT.log("click on label");
				if (!textBoxIsShown) {
					showTextBox();
					textBox.getValueBox().selectAll();
					// set the focus on textbox
					textBox.setFocus(true);
				} else {
					showLabel();
				}
			}
		};
		return clickHandler;
	}

	private void showTextBox() {
		textBox.setText(label.getText());
		lblclickToEdit.setText("(Press ENTER to save edition)");
		setHeaderPanel(textBox, button);
		textBoxIsShown = true;
	}

	/**
	 * Adds a widget to the content of the disclosure panel, as a vertical
	 * component.
	 * 
	 * @param widget
	 */
	public void addWidget(Widget widget) {
		widget.setStyleName("EditableDisclosurePanelContentElement");
		contentPanel.add(widget);

	}

	@Override
	public int getInternalID() {
		return id;
	}

	@Override
	public String getID() {
		return label.getText();
	}

	protected void setId(String id) {
		label.setText(id);
	}

	@Override
	public abstract HasId getObject();

	/**
	 * Updates the represented object and then fires the modification to the
	 * potential listeners registered in the {@link ProjectCreatorRegister}
	 */
	public final void fireModificationEvent() {
		// update object
		updateRepresentedObject();

		updateHeaderColor();

		// then fire modification events
		final Class<? extends HasId> class1 = getObject().getClass();
		ProjectCreatorRegister.fireModificationEvent(class1);

		ProjectCreatorRegister.fireModificationEvent(getInternalID());

	}

	public void updateHeaderColor() {
		if (isValid()) {
			headerPanel.setStyleName("EditableDisclosurePanelHeader-green");
		} else {
			headerPanel.setStyleName("EditableDisclosurePanelHeader");
		}
	}

	public void updateHeaderColor(boolean valid) {
		if (valid) {
			headerPanel.setStyleName("EditableDisclosurePanelHeader-green");
		} else {
			headerPanel.setStyleName("EditableDisclosurePanelHeader");
		}
	}

	/**
	 * Returns true if the current represented object is valid or false if not.
	 * 
	 * @return
	 */
	protected abstract boolean isValid();

	/**
	 * Add suggestions to the {@link SuggestBox} of the name of the
	 * {@link ClosableWithTitlePanel}
	 * 
	 * @param suggestionsforName
	 */
	protected void addSuggestions(Collection<String> suggestionsforName) {
		MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) textBox
				.getSuggestOracle();
		oracle.addAll(suggestionsforName);
	}

	/**
	 * @return the importJobID
	 */
	public int getImportJobID() {
		return importJobID;
	}

	/**
	 * @param importJobID
	 *            the importJobID to set
	 */
	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;
	}

	/**
	 * Adds a {@link KeyUpHandler} to the title
	 * 
	 * @param handler
	 */
	public void addTitleOnKeyUpHandler(KeyUpHandler handler) {
		textBox.addKeyUpHandler(handler);
	}

	/**
	 * Adds a {@link KeyDownHandler} to the title
	 * 
	 * @param handler
	 */
	public void addTitleOnKeyDownHandler(KeyDownHandler handler) {
		textBox.addKeyDownHandler(handler);
	}

	public interface ExecutableTaskOnChangeID {
		public void executeTask(String oldID, String newID);
	}

}
