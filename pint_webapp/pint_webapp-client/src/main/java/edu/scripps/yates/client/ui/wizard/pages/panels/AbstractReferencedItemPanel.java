package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReporter;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.WizardPage.PageID;
import edu.scripps.yates.client.ui.wizard.pages.PageTitleController;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ItemDraggableLabel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public abstract class AbstractReferencedItemPanel<IB> extends FlexTable implements StatusReporter {
	private final Wizard<PintContext> wizard;
	private final String itemName;
	private final DroppableFormat format;

	public AbstractReferencedItemPanel(Wizard<PintContext> wizard, String itemName, DroppableFormat format) {
		this.wizard = wizard;
		this.itemName = itemName;
		this.format = format;
		setStyleName(WizardStyles.WizardQuestionPanel);
		this.getElement().getStyle().setPaddingTop(10, Unit.PX);
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
		return AbstractReferencedItemPanel.class.getName();
	}

	private void init() {
		final Label label2 = new Label("Drag these items to the appropiate target in order to make the association:");
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(0, 0, label2);
		getFlexCellFormatter().setColSpan(0, 0, 2);

		final Label label1 = new Label("Created " + itemName + "s:");
		label1.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(1, 0, label1);

		// button to go to the page to create these items
		final String pageTitle = PageTitleController.getPageTitleByPageID(getWizardPageIDToJumpByFormat(format));
		final Label jumpToPageButton = new Label("Go to " + pageTitle);
		jumpToPageButton.setTitle("Click here to go to " + pageTitle);
		jumpToPageButton.setStyleName(WizardStyles.WizardJumpToPage);
		jumpToPageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				wizard.showPage(getWizardPageIDToJumpByFormat(format));
			}
		});
		setWidget(1, 1, jumpToPageButton);
		getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		int row = 2;
		final List<IB> items = getItemsBeansFromContext(wizard.getContext());
		for (final IB item : items) {
			final ItemDraggableLabel draggableLabel = createDraggableLabel(item);
			setWidget(row, 0, draggableLabel);
			getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
		}

	}

	protected abstract PageID getWizardPageIDToJumpByFormat(DroppableFormat format2);

	/**
	 * Method to create an {@link ItemDraggableLabel}, in which the data parameter
	 * in its constructor will be the ID of the item bean
	 * 
	 * @param item
	 * @return
	 */
	protected abstract ItemDraggableLabel createDraggableLabel(IB item);

	/**
	 * Gets the item bean fron the {@link PintContext} from the
	 * {@link PintImportCfgBean}, usually using {@link PintImportCfgUtil} class
	 * 
	 * @param context
	 * @return
	 */
	protected abstract List<IB> getItemsBeansFromContext(PintContext context);

}
