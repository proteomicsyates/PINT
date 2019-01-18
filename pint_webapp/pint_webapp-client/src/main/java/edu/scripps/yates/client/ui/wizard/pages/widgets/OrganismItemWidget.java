package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;

public class OrganismItemWidget extends AbstractItemWidget<OrganismTypeBean> {
	private edu.scripps.yates.ImportWizardServiceAsync service;
	private ItemLongPropertyWidget<OrganismTypeBean> descriptionItemLongPropertyWidget;

	public OrganismItemWidget(OrganismTypeBean organism, PintContext context) {
		super(organism, context, false);

		// description
		descriptionItemLongPropertyWidget = new ItemLongPropertyWidget<OrganismTypeBean>("description:", organism) {

			@Override
			public void updateItemObjectProperty(OrganismTypeBean item, String propertyValue) {
				item.setDescription(propertyValue);
			}

			@Override
			public String getPropertyValueFromItem(OrganismTypeBean item) {
				return item.getDescription();
			}
		};
		super.addPropertyWidget(descriptionItemLongPropertyWidget);
	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox, String sessionID) {
		if (service == null) {
			service = ImportWizardServiceAsync.Util.getInstance();
		}
		service.getOrganismList(sessionID, new AsyncCallback<List<String>>() {

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
	protected String getIDFromItemBean(OrganismTypeBean item) {
		return item.getId();
	}

	@Override
	protected void updateIDFromItemBean(String newId) {
		PintImportCfgUtil.updateOrganism(getContext().getPintImportConfiguration(), getItemBean().getId(), newId);
	}

	@Override
	public void updateReferencedItemBeanID(String data, DroppableFormat format) {
		// do nothing

	}

	@Override
	protected OrganismTypeBean duplicateItemBean(OrganismTypeBean itemBean) {
		// not duplicable
		return null;
	}

}
