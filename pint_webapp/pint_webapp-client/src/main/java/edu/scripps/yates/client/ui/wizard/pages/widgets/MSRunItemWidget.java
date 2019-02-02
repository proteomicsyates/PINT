package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.Date;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;

public class MSRunItemWidget extends AbstractItemWidget<MsRunTypeBean> {

	public MSRunItemWidget(MsRunTypeBean msRun, PintContext context) {
		super(msRun, context, true);

		// date
		final ItemDateBoxPropertyWidget<MsRunTypeBean> dateItemLongPropertyWidget = new ItemDateBoxPropertyWidget<MsRunTypeBean>(
				"Date:",
				"Approximate date in which the experiment was performed.\n" + "This is just to keep this on a record.",
				msRun, false) {

			@Override
			public void updateItemObjectProperty(MsRunTypeBean item, Date propertyValue) {
				item.setDate(propertyValue);
			}

			@Override
			public Date getPropertyValueFromItem(MsRunTypeBean item) {
				return item.getDate();
			}
		};
		addPropertyWidget(dateItemLongPropertyWidget);
		// path
		final ItemTextBoxPropertyWidget<MsRunTypeBean> pathPropertyWidget = new ItemTextBoxPropertyWidget<MsRunTypeBean>(
				"Path/location:", "The path of the experiment's raw files. Just to keep the record.", msRun, false) {

			@Override
			public void updateItemObjectProperty(MsRunTypeBean item, String propertyValue) {
				item.setPath(propertyValue);
			}

			@Override
			public String getPropertyValueFromItem(MsRunTypeBean item) {
				return item.getPath();
			}

			@Override
			public void beforeEditProperty(boolean edit) {
				// do nothing
			}
		};
		addPropertyWidget(pathPropertyWidget);

	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox, String sessionID) {
		// do nothing
	}

	@Override
	protected String getIDFromItemBean(MsRunTypeBean item) {
		return item.getId();
	}

	@Override
	protected void updateIDFromItemBean(String newId) throws PintException {
		PintImportCfgUtil.updateMSRun(getContext().getPintImportConfiguration(), getItemBean().getId(), newId);
	}

	@Override
	public void updateReferencedItemBeanID(String data, DroppableFormat format) {
		// do nothing

	}

	@Override
	protected MsRunTypeBean duplicateItemBean(MsRunTypeBean itemBean) {
		final MsRunTypeBean ret = new MsRunTypeBean();
		ret.setId(getNewID(itemBean.getId()));
		ret.setDate(itemBean.getDate());
		ret.setEnzymeNocutResidues(itemBean.getEnzymeNocutResidues());
		ret.setEnzymeResidues(itemBean.getEnzymeResidues());
		ret.setFastaFileRef(itemBean.getFastaFileRef());
		ret.setPath(itemBean.getPath());
		return ret;
	}

}
