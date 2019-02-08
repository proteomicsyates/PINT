package edu.scripps.yates.client.ui.wizard.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.ui.wizard.Wizard.Display;
import edu.scripps.yates.client.ui.wizard.event.handler.HandlerFactory;
import edu.scripps.yates.client.ui.wizard.view.HasIndexedWidgets;
import edu.scripps.yates.client.ui.wizard.view.HasWizardButtons;
import edu.scripps.yates.client.ui.wizard.view.HasWizardTitles;
import edu.scripps.yates.client.ui.wizard.view.widget.WizardDeckPanel;
import edu.scripps.yates.client.ui.wizard.view.widget.WizardNavigationPanel;
import edu.scripps.yates.client.ui.wizard.view.widget.WizardPageList;

/**
 * The default implementation of {@link Display}.
 * <p>
 * WizardView provides some CSS selectors to assist in styling it:
 * <ul>
 * <li><code>.hasBorder</code> is applied to every element that has a
 * border</li>
 * <li><code>.wizardTitle</code> is applied to the panel that contains the
 * caption</li>
 * <li><code>.wizardPages</code> is applied to the panel that contains the list
 * of page titles</li>
 * <li><code>.wizardNagivation</code> is applied to the panel that contains the
 * naviation buttons</li>
 * <li><code>.wizardContent</code> is applied to the panel that contains the
 * main wizard content</li>
 * </ul>
 * 
 * @author Brandon Tilley
 *
 */
public class WizardView extends Composite implements Display {

	private final DockLayoutPanel outerPanel;

	private final HorizontalPanel titlePanel;
	private final HTML titleHtml;
	private final Image workingIndicatorImage;
	private final ScrollPanel pageNamePanelContainer;
	private final WizardPageList pageNamePanel;
	private final WizardDeckPanel pagePanel;
	private final WizardNavigationPanel navigationPanel;

	private final HandlerFactory<Display> handlers;

	private static final WizardViewImages images = GWT.create(WizardViewImages.class);

	public WizardView() {
		// Handlers
		handlers = new HandlerFactory<Display>(this);

		// outer panel
		outerPanel = new DockLayoutPanel(Unit.PX);
		initWidget(outerPanel);

		// title panel and contained html
		titlePanel = new HorizontalPanel();
		titlePanel.addStyleName("wizardTitle");
//		titlePanel.addStyleName("hasBorder");

		titleHtml = new HTML("&nbsp;");
		titleHtml.setWidth("100%");
		titlePanel.add(titleHtml);
		workingIndicatorImage = new Image(MyClientBundle.INSTANCE.roundedLoader());// images.workingIndicator());
		workingIndicatorImage.setVisible(false);
		titlePanel.add(workingIndicatorImage);
		titlePanel.setCellVerticalAlignment(workingIndicatorImage, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setWidth("100%");

		// list of page names on the left
		pageNamePanelContainer = new ScrollPanel();
		pageNamePanelContainer.addStyleName("wizardPages");
		pageNamePanel = new WizardPageList();
		pageNamePanelContainer.add(pageNamePanel);

		// the navigation panel at the bottom
		navigationPanel = new WizardNavigationPanel();
		navigationPanel.addStyleName("wizardNavigation");

		// the main content panel
		pagePanel = new WizardDeckPanel();
		pagePanel.addStyleName("wizardContent");
		pagePanel.setAnimationEnabled(true);

		// add all the panels
		outerPanel.addNorth(titlePanel, 50);
		outerPanel.addWest(pageNamePanelContainer, 180);
		outerPanel.addSouth(navigationPanel, 50);
		outerPanel.add(pagePanel);
		outerPanel.getWidgetContainerElement(navigationPanel);
		outerPanel.getWidgetContainerElement(titlePanel).addClassName("valignMiddle");

		// add borders directly to the TD's
		// each element also gets "hasBorder" so injected styles
		// can override the color, width, etc. of the borders in one selector
		titlePanel.getElement().getParentElement().addClassName("bottomBorder");
//		titlePanel.getElement().getParentElement().addClassName("hasBorder");
		pageNamePanelContainer.getElement().getParentElement().addClassName("rightBorder");
		pageNamePanelContainer.getElement().getParentElement().addClassName("topBorder");
//		pageNamePanelContainer.getElement().getParentElement().addClassName("hasBorder");
//		navigationPanel.getElement().getParentElement().addClassName("topBorder");
		navigationPanel.getElement().getParentElement().addClassName("leftBorder");
//		navigationPanel.getElement().getParentElement().addClassName("hasBorder");
	}

	@Override
	public HasWizardButtons getButtonBar() {
		return navigationPanel;
	}

	@Override
	public HasWizardTitles getPageList() {
		return pageNamePanel;
	}

	@Override
	public HasHTML getCaption() {
		return titleHtml;
	}

	@Override
	public HasIndexedWidgets getContent() {
		return pagePanel;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void startProcessing() {
		workingIndicatorImage.setVisible(true);
	}

	@Override
	public void stopProcessing() {
		workingIndicatorImage.setVisible(false);
	}

	@Override
	public HandlerFactory<Display> getHandlerFactory() {
		return handlers;
	}

	@Override
	public HorizontalPanel getTitlePanel() {
		return titlePanel;
	}
}
