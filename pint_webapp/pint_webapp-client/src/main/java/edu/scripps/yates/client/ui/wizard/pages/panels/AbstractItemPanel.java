package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

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
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
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
		implements StatusReporter {
	private final Wizard<PintContext> wizard;
	private Label createNewItemLabel;
	private SuggestBox createNewItemTextbox;
	private VerticalPanel createdItemsPanel;
	private Label createdOrNotItems;
	private final String NO_ITEM_CREATED_YET;
	private final String itemName;

	/**
	 * Creates an {@link AbstractItemPanel} with a wizard and an itemName which will
	 * be used in some labels of the panel
	 * 
	 * @param wizard
	 * @param itemName
	 */
	public AbstractItemPanel(Wizard<PintContext> wizard, String itemName) {
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
						|| event.getNativeKeyCode() == KeyCodes.KEY_MAC_ENTER) {
					if ("".equals(createNewItemTextbox.getValue())) {
						editItemName(false);
					} else {
						createItemBeanAndWidget(createNewItemTextbox.getValue());
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
						createItemBeanAndWidget(createNewItemTextbox.getValue());
						editItemName(false);
					}
				}
			}
		});
		createdItemsPanel = new VerticalPanel();
		createdItemsPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		createdOrNotItems = new Label(NO_ITEM_CREATED_YET);
		createdOrNotItems.setStyleName(WizardStyles.WizardExplanationLabel);
		createdItemsPanel.add(createdOrNotItems);
		setWidget(1, 0, createdItemsPanel);
		getFlexCellFormatter().setColSpan(1, 0, 2);

		updateCreatedItems(wizard.getContext());

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
		final IB sampleObj = createNewItemBean(itemName);
		try {
			addItemBeanToPintImportConfiguration(wizard.getContext(), sampleObj);
		} catch (final PintException e) {
			e.printStackTrace();
			StatusReportersRegister.getInstance().notifyStatusReporters(e);
			return false;
		}
		createItemWidget(sampleObj);

		return true;
	}

	private void createItemWidget(IB itemBean) {
		final IW itemWidget = createNewItemWidget(itemBean);
		itemWidget.addOnRemoveTask(getDoSomethingTaskOnRemove(wizard.getContext()));

		createdItemsPanel.add(itemWidget);
		createdOrNotItems.setText("Created " + itemName + "s:");
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
	protected abstract DoSomethingTask2<IB> getDoSomethingTaskOnRemove(PintContext context);

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
	}

	protected Wizard<PintContext> getWizard() {
		return this.wizard;
	}

	public abstract void isReady() throws PintException;
}
