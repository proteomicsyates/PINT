package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class SampleItemWidget extends AbstractItemWidget<SampleTypeBean> {

	public SampleItemWidget(SampleTypeBean sampleObj, PintContext context) {
		super(sampleObj, context);
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
		// REFERENCES TO ORGANISM AND TISSUE
		addDroppingAreaForReferencedItemBean("drop organism here", DroppableFormat.ORGANISM);
		addDroppingAreaForReferencedItemBean("drop tissue/cell line here", DroppableFormat.TISSUE);
		// if there is a reference to a sample, set the dropping label to the actual
		// sample
		if (sampleObj.getOrganismRef() != null) {
			updateReferenceLabel(sampleObj.getOrganismRef(), DroppableFormat.ORGANISM);
		}
		if (sampleObj.getTissueRef() != null) {
			updateReferenceLabel(sampleObj.getTissueRef(), DroppableFormat.TISSUE);
		}
	}

	@Override
	protected String getIDFromItemBean(SampleTypeBean item) {
		return item.getId();
	}

	@Override
	protected void updateIDFromItemBean(String newID) {
		PintImportCfgUtil.updateSampleID(getContext().getPintImportConfiguration(), getItemBean().getId(), newID);
	}

	@Override
	public void updateReferencedItemBeanID(String itemBeanID, DroppableFormat format) {
		if (format == DroppableFormat.ORGANISM) {
			getItemBean().setOrganismRef(itemBeanID);
		}
		if (format == DroppableFormat.TISSUE) {
			getItemBean().setTissueRef(itemBeanID);
		}
	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox, String sessionID) {
		// do nothing
	}

	@Override
	protected SampleTypeBean duplicateItemBean(SampleTypeBean itemBean) {
		final SampleTypeBean ret = new SampleTypeBean();
		ret.setId(getNewID(itemBean.getId()));
		ret.setDescription(itemBean.getDescription());
		ret.setLabelRef(itemBean.getLabelRef());
		ret.setOrganismRef(itemBean.getOrganismRef());
		ret.setTissueRef(itemBean.getTissueRef());
		return ret;
	}

}
