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
import edu.scripps.yates.client.ui.wizard.pages.widgets.OrganismItemWidget;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;

public class OrganismsPanel extends AbstractItemPanel<OrganismItemWidget, OrganismTypeBean> {

	private ImportWizardServiceAsync service;
	private List<String> organismSuggestions;

	public OrganismsPanel(Wizard<PintContext> wizard) {
		super(wizard, "organism");

	}

	@Override
	protected OrganismTypeBean createNewItemBean(String itemName) {
		final OrganismTypeBean organism = new OrganismTypeBean();
		organism.setId(itemName);
		return organism;
	}

	@Override
	protected void addItemBeanToPintImportConfiguration(PintContext context, OrganismTypeBean organism)
			throws PintException {
		// add it to the cfg object
		PintImportCfgUtil.addOrganism(context.getPintImportConfiguration(), organism);
	}

	@Override
	protected OrganismItemWidget createNewItemWidget(OrganismTypeBean organism) {
		final OrganismItemWidget organismWidget = new OrganismItemWidget(organism, getWizard().getContext());

		return organismWidget;
	}

	@Override
	protected DoSomethingTask2<OrganismTypeBean> getDoSomethingTaskOnRemoveItemBean(PintContext context) {

		return new DoSomethingTask2<OrganismTypeBean>() {

			@Override
			public Void doSomething(OrganismTypeBean item) {

				PintImportCfgUtil.removeOrganism(context.getPintImportConfiguration(), item);

				if (context.getPintImportConfiguration().getProject().getExperimentalDesign().getOrganismSet()
						.getOrganism().isEmpty()) {
					OrganismsPanel.super.setNoItemsCreatedYetLabel();

				}
				return null;
			}
		};

	}

	@Override
	protected List<OrganismTypeBean> getItemBeansFromContext(PintContext context) {
		return PintImportCfgUtil.getOrganisms(context.getPintImportConfiguration());
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
		service.getOrganismList(getWizard().getContext().getSessionID(), new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				GWT.log("received " + result.size() + " suggestions");
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
			throw new PintException("Create at least one organism", PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
		}
	}

	@Override
	public String getID() {
		return getClass().getName();
	}
}
