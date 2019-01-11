package edu.scripps.yates.client.ui.wizard.view.widget;

import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class SampleWidget extends ItemWidget<SampleTypeBean> {

	public SampleWidget(SampleTypeBean sampleObj) {
		super(sampleObj);
		// description
		super.addPropertyWidget(new ItemLongPropertyWidget<SampleTypeBean>("description:", sampleObj) {

			@Override
			public void updateItemObjectProperty(SampleTypeBean item, String propertyValue) {
				item.setDescription(propertyValue);
			}

			@Override
			public String getPropertyValueFromItem(SampleTypeBean item) {
				return item.getDescription();
			}
		});
	}

	@Override
	protected String getIdFromItem(SampleTypeBean item) {
		return item.getId();
	}

	@Override
	protected void updateIDFromItem(String newId) {
		getItemObj().setId(newId);
	}

}
