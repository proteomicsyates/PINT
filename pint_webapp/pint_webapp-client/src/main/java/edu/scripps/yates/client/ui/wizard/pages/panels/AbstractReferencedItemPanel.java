package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReporter;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.WizardPage.PageID;
import edu.scripps.yates.client.ui.wizard.pages.PageIDController;
import edu.scripps.yates.client.ui.wizard.pages.PageTitleController;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageLabels;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageMSRuns;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageOrganisms;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageSamples;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageTissues;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ItemDraggableLabel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public abstract class AbstractReferencedItemPanel<IB> extends FlexTable implements StatusReporter {
	private final Wizard<PintContext> wizard;
	private final String itemName;
	private final DroppableFormat format;
	private FlowPanel dragableLabelsPanel;

	public AbstractReferencedItemPanel(Wizard<PintContext> wizard, String itemName, DroppableFormat format) {
		this.wizard = wizard;
		this.itemName = itemName;
		this.format = format;
		setStyleName(WizardStyles.WizardQuestionPanel);
		this.getElement().getStyle().setPaddingTop(10, Unit.PX);
//		this.getElement().getStyle().setWidth(470, Unit.PX); // this more or less the width of the message of label2 in
		// init() method
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
		final Label label2 = new Label("Drag items to the appropiate target to make the association:");
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(0, 0, label2);
		getFlexCellFormatter().setColSpan(0, 0, 2);

		final Label label1 = new Label("Created " + itemName + "s:");
		label1.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(1, 0, label1);

		// button to go to the page to create these items
		final String pageTitle = PageTitleController.getPageTitleByPageID(getWizardPageIDToJumpByFormat(format));
		final Label jumpToPageButton = new Label("Go to " + pageTitle);
		jumpToPageButton.setTitle("Click here to go to " + pageTitle + " wizard page");
		jumpToPageButton.setStyleName(WizardStyles.WizardJumpToPage);
		jumpToPageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// because we don't want to validate this page now, we skip the validation
				// occurring in BeforeNext
				final boolean fireBeforeNext = false;
				wizard.showPage(getWizardPageIDToJumpByFormat(format), fireBeforeNext, true, true);
			}
		});
		setWidget(1, 1, jumpToPageButton);
		getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		dragableLabelsPanel = new FlowPanel();
		setWidget(2, 0, dragableLabelsPanel);
		getFlexCellFormatter().setColSpan(2, 0, 2);
		// fill the dragable labels
		setDraggableLabels();
	}

	/**
	 * Creates and set the draggable labels according to the items in the
	 * {@link PintImportCfgBean} of the {@link PintContext}.<br>
	 * This should be called every time the page is shown, since some object in the
	 * {@link PintImportCfgBean} should have changed.
	 */
	public void setDraggableLabels() {
//		int startingRow = 2;
		dragableLabelsPanel.clear();
		final List<IB> items = getItemsBeansFromContext(wizard.getContext());
		for (final IB item : items) {
			final ItemDraggableLabel draggableLabel = createDraggableLabel(item);
			dragableLabelsPanel.add(draggableLabel);
			draggableLabel.getElement().getStyle().setMarginRight(10, Unit.PX);
			draggableLabel.getElement().getStyle().setMarginBottom(5, Unit.PX);
//			getFlexCellFormatter().setColSpan(startingRow, 0, 2);
//			startingRow++;
		}
	}

	private PageID getWizardPageIDToJumpByFormat(DroppableFormat format2) {
		switch (format2) {
		case LABEL:
			return PageIDController.getPageIDByPageClass(WizardPageLabels.class);
		case MSRUN:
			return PageIDController.getPageIDByPageClass(WizardPageMSRuns.class);
		case ORGANISM:
			return PageIDController.getPageIDByPageClass(WizardPageOrganisms.class);
		case SAMPLE:
			return PageIDController.getPageIDByPageClass(WizardPageSamples.class);
		case TISSUE:
			return PageIDController.getPageIDByPageClass(WizardPageTissues.class);
		default:
			StatusReportersRegister.getInstance()
					.notifyStatusReporters(new PintException(
							format2 + " not supported to be dropped...at least it is not implemented where to jump",
							PINT_ERROR_TYPE.INTERNAL_ERROR));
			return null;
		}

	};

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
