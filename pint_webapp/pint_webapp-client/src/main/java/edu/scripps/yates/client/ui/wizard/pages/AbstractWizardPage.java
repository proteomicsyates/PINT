package edu.scripps.yates.client.ui.wizard.pages;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.WizardPage;
import edu.scripps.yates.client.ui.wizard.WizardPageHelper;
import edu.scripps.yates.client.ui.wizard.event.NavigationEvent;
import edu.scripps.yates.client.ui.wizard.pages.panels.AbstractItemPanel;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public abstract class AbstractWizardPage extends WizardPage<PintContext> {
	private final String title;
	private final Widget widget;
	private final Set<AbstractItemPanel> itemPanels = new HashSet<AbstractItemPanel>();

	protected AbstractWizardPage(String title) {
		this(title, null);
	}

	private AbstractWizardPage(String title, PintContext context) {
		this.title = title;
		this.context = context;
		widget = createPage();
		getPageID();// to create the ID
		registerPageTitle(title);
	}

	/**
	 * Store the page title in the {@link PageTitleController}
	 * 
	 * @param title
	 */
	protected abstract void registerPageTitle(String title);

	@Override
	public void onPageAdd(WizardPageHelper<PintContext> helper) {

		super.onPageAdd(helper);

	}

	protected abstract Widget createPage();

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public PintContext getContext() {
		GWT.log("getContext");
		return super.getContext();
	}

	@Override
	public Wizard<PintContext> getWizard() {
		GWT.log("getWizard");
		return super.getWizard();
	}

	@Override
	public void beforeFirstShow() {
		GWT.log("beforeFirstShow");
		super.beforeFirstShow();
	}

	@Override
	public void beforeShow() {
		GWT.log("beforeShow");
		for (final AbstractItemPanel itemPanel : itemPanels) {
			itemPanel.updateCreatedItems(getContext());
		}
		super.beforeShow();
	}

	@Override
	public void beforeNext(NavigationEvent event) {
		try {
			isReady();
		} catch (final PintException e) {
			StatusReportersRegister.getInstance().notifyStatusReporters(e);
			event.cancel();
			return;
		}
		GWT.log("beforeNext");
		super.beforeNext(event);
	}

	@Override
	public void afterNext() {
		GWT.log("afterNext");
		super.afterNext();
	}

	public int getImportID() {
		final PintImportCfgBean pintImportConfg = getPintImportConfg();
		if (pintImportConfg != null) {
			return pintImportConfg.getImportID();
		}
		return -1;
	}

	public PintImportCfgBean getPintImportConfg() {
		if (getContext() != null) {
			return getContext().getPintImportConfiguration();
		}
		return null;
	}

	protected void goToNextPage() {
		getWizard().showNextPage();
	}

	/**
	 * Call this whenever the page has an implementation of an
	 * {@link AbstractItemPanel} just after creating it
	 * 
	 * @param itemPanel
	 */
	public void registerItemPanel(AbstractItemPanel itemPanel) {
		this.itemPanels.add(itemPanel);
	}

	public boolean isReady() throws PintException {
		/// look into all the itemPanels
		for (final AbstractItemPanel updatesItems : itemPanels) {
			updatesItems.isReady();
		}
		return true;
	}
}
