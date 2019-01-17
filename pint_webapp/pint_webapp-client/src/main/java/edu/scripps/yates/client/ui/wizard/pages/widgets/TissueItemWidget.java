package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;

public class TissueItemWidget extends AbstractItemWidget<TissueTypeBean> {
	private edu.scripps.yates.ImportWizardServiceAsync service;
	private ItemLongPropertyWidget<TissueTypeBean> descriptionItemLongPropertyWidget;

	public TissueItemWidget(TissueTypeBean tissue, PintContext context) {
		super(tissue, context);

		// description
		descriptionItemLongPropertyWidget = new ItemLongPropertyWidget<TissueTypeBean>("description:", tissue) {

			@Override
			public void updateItemObjectProperty(TissueTypeBean item, String propertyValue) {
				item.setDescription(propertyValue);
			}

			@Override
			public String getPropertyValueFromItem(TissueTypeBean item) {
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
		service.getTissueList(sessionID, new AsyncCallback<List<String>>() {

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
	protected String getIDFromItemBean(TissueTypeBean item) {
		return item.getId();
	}

	@Override
	protected void updateIDFromItemBean(String newId) {
		PintImportCfgUtil.updateTissue(getContext().getPintImportConfiguration(), getItemBean().getId(), newId);
	}

	@Override
	public void updateReferencedItemBeanID(String data, DroppableFormat format) {
		// do nothing

	}

}
