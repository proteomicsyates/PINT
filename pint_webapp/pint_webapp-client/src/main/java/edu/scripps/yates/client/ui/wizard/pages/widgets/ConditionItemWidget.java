package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;

public class ConditionItemWidget extends AbstractItemWidget<ExperimentalConditionTypeBean> {

	public ConditionItemWidget(ExperimentalConditionTypeBean conditionObject, PintContext context) {
		super(conditionObject, context);
		// description
		super.addPropertyWidget(new ItemTextAreaPropertyWidget<ExperimentalConditionTypeBean>("Description:",
				"Brief description of the experimental condition", conditionObject, false) {

			@Override
			public void updateItemObjectProperty(ExperimentalConditionTypeBean item, String propertyValue) {
				item.setDescription(propertyValue);
			}

			@Override
			public String getPropertyValueFromItem(ExperimentalConditionTypeBean item) {
				return item.getDescription();
			}
		});
		// enable dropping
		addDroppingAreaForReferencedItemBean("Sample", "The sample analyzed in this experimental condition.\n"
				+ "Remember that is a one-to-one association, and therefore if you have \n"
				+ "several samples for the same experimental condition, you will probably\n"
				+ "need to just create one sample and then create multiple MSRuns per sample with the same condition.",
				"drop sample here", DroppableFormat.SAMPLE, true);
		// if there is a reference to a sample, set the dropping label to the actual
		// sample
		if (conditionObject.getSampleRef() != null) {
			updateReferenceLabel(conditionObject.getSampleRef(), DroppableFormat.SAMPLE);
		}
	}

	@Override
	protected String getIDFromItemBean(ExperimentalConditionTypeBean item) {
		return item.getId();
	}

	@Override
	protected void updateIDFromItemBean(String newId) throws PintException {
		PintImportCfgUtil.updateConditionID(getContext().getPintImportConfiguration(), getItemBean().getId(), newId);
	}

	@Override
	public void updateReferencedItemBeanID(String itemBeanID, DroppableFormat format) {
		if (format == DroppableFormat.SAMPLE) {
			getItemBean().setSampleRef(itemBeanID);
		}
	}

	@Override
	protected void fillSuggestions(SuggestBox suggestBox, String sessionID) {
// do nothing
	}

	@Override
	protected ExperimentalConditionTypeBean duplicateItemBean(ExperimentalConditionTypeBean condition) {
		final ExperimentalConditionTypeBean ret = new ExperimentalConditionTypeBean();
		ret.setId(super.getNewID(condition.getId()));
		ret.setDescription(condition.getDescription());
		ret.setIdentificationInfo(condition.getIdentificationInfo());
		ret.setQuantificationInfo(condition.getQuantificationInfo());
		ret.setSampleRef(condition.getSampleRef());
		return ret;
	}

}
