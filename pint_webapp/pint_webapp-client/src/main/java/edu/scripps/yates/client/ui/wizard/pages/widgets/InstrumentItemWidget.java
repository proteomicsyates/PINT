package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.exceptions.PintException;

public class InstrumentItemWidget extends AbstractItemWidget<String> {
	private edu.scripps.yates.ImportWizardServiceAsync service;

	public InstrumentItemWidget(String instrument, PintContext context) {
		super(instrument, context, false);

	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox, String sessionID) {
		if (service == null) {
			service = ImportWizardServiceAsync.Util.getInstance();
		}
		service.getInstrumentList(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				ClientGUIUtil.addSuggestionsDeferred(result, nameTextBox);
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
			}
		});
	}

	@Override
	protected String getIDFromItemBean(String item) {
		return item;
	}

	@Override
	protected void updateIDFromItemBean(String newId) throws PintException {
		PintImportCfgUtil.updateInstrument(getContext().getPintImportConfiguration(), getItemBean(), newId);
	}

	@Override
	public void updateReferencedItemBeanID(String data, DroppableFormat format) {
		// do nothing

	}

	@Override
	protected String duplicateItemBean(String itemBean) {
		// not duplicable
		return null;
	}

}
