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
import edu.scripps.yates.client.ui.wizard.pages.widgets.InstrumentItemWidget;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;

public class InstrumentsPanel extends AbstractItemPanel<InstrumentItemWidget, String> {

	private ImportWizardServiceAsync service;
	private List<String> instrumentSuggestions;

	public InstrumentsPanel(Wizard<PintContext> wizard) {
		super(wizard, "instrument");

	}

	@Override
	protected String createNewItemBean(String itemName) {
		return itemName;
	}

	@Override
	protected void addItemBeanToPintImportConfiguration(PintContext context, String instrument) throws PintException {
		// add it to the cfg object
		PintImportCfgUtil.addInstrument(context.getPintImportConfiguration(), instrument);
	}

	@Override
	protected InstrumentItemWidget createNewItemWidget(String instrument) {
		final InstrumentItemWidget instrumentWidget = new InstrumentItemWidget(instrument, getWizard().getContext());

		return instrumentWidget;
	}

	@Override
	protected DoSomethingTask2<String> getDoSomethingTaskOnRemoveItemBean(PintContext context) {

		return new DoSomethingTask2<String>() {

			@Override
			public Void doSomething(String item) {

				PintImportCfgUtil.removeInstrument(context.getPintImportConfiguration(), item);

				if (context.getPintImportConfiguration().getProject().getInstruments().isEmpty()) {
					InstrumentsPanel.super.setNoItemsCreatedYetLabel();

				}
				return null;
			}
		};

	}

	@Override
	protected List<String> getItemBeansFromContext(PintContext context) {
		return PintImportCfgUtil.getInstruments(context.getPintImportConfiguration());
	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox) {
		if (instrumentSuggestions != null) {
			ClientGUIUtil.addSuggestionsDeferred(instrumentSuggestions, nameTextBox);
			return;
		}
		if (service == null) {
			service = ImportWizardServiceAsync.Util.getInstance();
		}
		service.getInstrumentList(new AsyncCallback<List<String>>() {

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
			throw new PintException("Create at least one instrument", PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
		}
	}

	@Override
	public String getID() {
		return getClass().getName();
	}
}
