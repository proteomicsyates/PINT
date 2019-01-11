package edu.scripps.yates.client.pint.wizard.pages;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.WizardPage;
import edu.scripps.yates.client.ui.wizard.WizardPageHelper;
import edu.scripps.yates.client.ui.wizard.event.NavigationEvent;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public abstract class AbstractWizardPage extends WizardPage<PintContext> {
	private final String title;
	private final edu.scripps.yates.client.ui.wizard.WizardPage.PageID pageID = new PageID();
	private final Widget widget;

	protected AbstractWizardPage(String title) {
		this(title, null);
	}

	private AbstractWizardPage(String title, PintContext context) {
		this.title = title;
		this.context = context;
		widget = createPage();
	}

	@Override
	public void onPageAdd(WizardPageHelper<PintContext> helper) {

		super.onPageAdd(helper);

	}

	protected abstract Widget createPage();

	@Override
	public edu.scripps.yates.client.ui.wizard.WizardPage.PageID getPageID() {
		return pageID;
	}

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
		super.beforeShow();
	}

	@Override
	public void beforeNext(NavigationEvent event) {
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
}
