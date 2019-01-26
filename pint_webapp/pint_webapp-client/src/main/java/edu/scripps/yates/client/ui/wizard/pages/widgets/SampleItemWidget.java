package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class SampleItemWidget extends AbstractItemWidget<SampleTypeBean> {

	public SampleItemWidget(SampleTypeBean sampleObj, PintContext context) {
		super(sampleObj, context);
		// description
		super.addPropertyWidget(new ItemTextAreaPropertyWidget<SampleTypeBean>("Description:",
				"Brief description of the sample", sampleObj, false) {

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
		addDroppingAreaForReferencedItemBean("Organism", "Organism from which the sample is from.",
				"drop organism here", DroppableFormat.ORGANISM, true);
		addDroppingAreaForReferencedItemBean("Tissue/Cell line", "Tissue or cell line from which the sample is from.",
				"drop tissue/cell line here", DroppableFormat.TISSUE, true);
		addDroppingAreaForReferencedItemBean("Isobaric label", "Isobaric label used in this sample (OPTIONAL).",
				"drop isobaric label here", DroppableFormat.LABEL, false);
		// if there is a reference to a sample, set the dropping label to the actual
		// sample
		if (sampleObj.getOrganismRef() != null) {
			updateReferenceLabel(sampleObj.getOrganismRef(), DroppableFormat.ORGANISM);
		}
		if (sampleObj.getTissueRef() != null) {
			updateReferenceLabel(sampleObj.getTissueRef(), DroppableFormat.TISSUE);
		}
		if (sampleObj.getLabelRef() != null) {
			updateReferenceLabel(sampleObj.getLabelRef(), DroppableFormat.LABEL);
		}
	}

	@Override
	protected String getIDFromItemBean(SampleTypeBean item) {
		return item.getId();
	}

	@Override
	protected void updateIDFromItemBean(String newID) throws PintException {
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
		if (format == DroppableFormat.LABEL) {
			getItemBean().setLabelRef(itemBeanID);
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
