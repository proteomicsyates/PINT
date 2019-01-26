package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;

public class LabelItemWidget extends AbstractItemWidget<LabelTypeBean> {
	private List<String> labelsSuggestions;

	public LabelItemWidget(LabelTypeBean label, PintContext context) {
		super(label, context, false);
		setShowSuggestionsWithNoTyping(true);
		// description
		final ItemDoublePropertyWidget<LabelTypeBean> massDiffItemPropertyWidget = new ItemDoublePropertyWidget<LabelTypeBean>(
				"Mass diff:", "Mass (usually in Daltons) of the label.\nOnly numbers are allowed here.", label, false) {

			@Override
			public void updateItemObjectProperty(LabelTypeBean item, Double propertyValue) {
				item.setMassDiff(propertyValue);
			}

			@Override
			public Double getPropertyValueFromItem(LabelTypeBean item) {
				return item.getMassDiff();
			}
		};
		super.addPropertyWidget(massDiffItemPropertyWidget);
	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox, String sessionID) {
		if (labelsSuggestions != null) {
			ClientGUIUtil.addSuggestionsDeferred(labelsSuggestions, nameTextBox);
			return;
		}
		labelsSuggestions = new ArrayList<String>();
		labelsSuggestions.add("LIGHT");
		labelsSuggestions.add("HEAVY");
		labelsSuggestions.add("MEDIUM");
		labelsSuggestions.add("TMT_6PLEX_126");
		labelsSuggestions.add("TMT_6PLEX_127");
		labelsSuggestions.add("TMT_6PLEX_128");
		labelsSuggestions.add("TMT_6PLEX_129");
		labelsSuggestions.add("TMT_6PLEX_130");
		labelsSuggestions.add("TMT_6PLEX_131");
		//
		labelsSuggestions.add("TMT_10PLEX_126_127726");
		labelsSuggestions.add("TMT_10PLEX_127_124761");
		labelsSuggestions.add("TMT_10PLEX_127_131081");
		labelsSuggestions.add("TMT_10PLEX_128_128116");
		labelsSuggestions.add("TMT_10PLEX_128_134436");
		labelsSuggestions.add("TMT_10PLEX_129_131471");
		labelsSuggestions.add("TMT_10PLEX_129_13779");
		labelsSuggestions.add("TMT_10PLEX_130_134825");
		labelsSuggestions.add("TMT_10PLEX_130_141145");
		labelsSuggestions.add("TMT_10PLEX_131_13818");
		//
		labelsSuggestions.add("N14");
		labelsSuggestions.add("N15");
		ClientGUIUtil.addSuggestionsDeferred(labelsSuggestions, nameTextBox);
	}

	@Override
	protected String getIDFromItemBean(LabelTypeBean item) {
		return item.getId();
	}

	@Override
	protected void updateIDFromItemBean(String newId) throws PintException {
		PintImportCfgUtil.updateLabel(getContext().getPintImportConfiguration(), getItemBean().getId(), newId);
	}

	@Override
	public void updateReferencedItemBeanID(String data, DroppableFormat format) {
		// do nothing

	}

	@Override
	protected LabelTypeBean duplicateItemBean(LabelTypeBean itemBean) {
// do not duplicable
		return null;
	}

}
