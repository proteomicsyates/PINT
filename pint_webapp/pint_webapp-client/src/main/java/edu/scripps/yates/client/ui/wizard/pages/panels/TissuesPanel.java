package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.widgets.TissueItemWidget;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;

public class TissuesPanel extends AbstractItemPanel<TissueItemWidget, TissueTypeBean> {

	private ImportWizardServiceAsync service;
	private List<String> organismSuggestions;

	public TissuesPanel(Wizard<PintContext> wizard) {
		super(wizard, "tissue/cell line");

	}

	@Override
	protected TissueTypeBean createNewItemBean(String itemName) {
		final TissueTypeBean organism = new TissueTypeBean();
		organism.setId(itemName);
		return organism;
	}

	@Override
	protected void addItemBeanToPintImportConfiguration(PintContext context, TissueTypeBean tissue)
			throws PintException {
		// add it to the cfg object
		PintImportCfgUtil.addTissue(context.getPintImportConfiguration(), tissue);
	}

	@Override
	protected TissueItemWidget createNewItemWidget(TissueTypeBean tissue) {

		final TissueItemWidget tissueWidget = new TissueItemWidget(tissue, getWizard().getContext());

		return tissueWidget;
	}

	@Override
	protected DoSomethingTask2<TissueTypeBean> getDoSomethingTaskOnRemoveItemBean(PintContext context) {

		return new DoSomethingTask2<TissueTypeBean>() {

			@Override
			public Void doSomething(TissueTypeBean item) {

				PintImportCfgUtil.removeTissue(context.getPintImportConfiguration(), item);

				if (context.getPintImportConfiguration().getProject().getExperimentalDesign().getTissueSet().getTissue()
						.isEmpty()) {
					TissuesPanel.super.setNoItemsCreatedYetLabel();

				}
				return null;
			}
		};

	}

	@Override
	protected List<TissueTypeBean> getItemBeansFromContext(PintContext context) {
		return PintImportCfgUtil.getTissues(context.getPintImportConfiguration());
	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox) {
		if (organismSuggestions != null) {
			ClientGUIUtil.addSuggestionsDeferred(organismSuggestions, nameTextBox);
			return;
		}
		if (service == null) {
			service = ImportWizardServiceAsync.Util.getInstance();
		}
		service.getTissueList(getWizard().getContext().getSessionID(), new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				GWT.log("received " + result.size() + " tissues");
				ClientGUIUtil.addSuggestionsDeferred(result, nameTextBox);
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
			}
		});
	}

	@Override
	public void isReady() throws PintException {
		if (getItemBeansFromContext(getWizard().getContext()).isEmpty()) {
			throw new PintException("Create at least one Tissue/Cell line", PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
		}
	}

	@Override
	public String getID() {
		return getClass().getName();
	}

}
