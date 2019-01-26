package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.widgets.LabelItemWidget;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;

public class LabelsPanel extends AbstractItemPanel<LabelItemWidget, LabelTypeBean> {

	private List<String> labelsSuggestions;

	public LabelsPanel(Wizard<PintContext> wizard) {
		super(wizard, "isobaric label");
		setShowSuggestionsWithNoTyping(true);
	}

	@Override
	protected LabelTypeBean createNewItemBean(String itemName) {
		final LabelTypeBean organism = new LabelTypeBean();
		organism.setId(itemName);
		return organism;
	}

	@Override
	protected void addItemBeanToPintImportConfiguration(PintContext context, LabelTypeBean label) throws PintException {
		// add it to the cfg object
		PintImportCfgUtil.addLabel(context.getPintImportConfiguration(), label);
	}

	@Override
	protected LabelItemWidget createNewItemWidget(LabelTypeBean label) {

		final LabelItemWidget labelWidget = new LabelItemWidget(label, getWizard().getContext());

		return labelWidget;
	}

	@Override
	protected DoSomethingTask2<LabelTypeBean> getDoSomethingTaskOnRemoveItemBean(PintContext context) {

		return new DoSomethingTask2<LabelTypeBean>() {

			@Override
			public Void doSomething(LabelTypeBean item) {

				PintImportCfgUtil.removeLabel(context.getPintImportConfiguration(), item);

				if (context.getPintImportConfiguration().getProject().getExperimentalDesign().getTissueSet().getTissue()
						.isEmpty()) {
					LabelsPanel.super.setNoItemsCreatedYetLabel();

				}
				return null;
			}
		};

	}

	@Override
	protected List<LabelTypeBean> getItemBeansFromContext(PintContext context) {
		return PintImportCfgUtil.getLabels(context.getPintImportConfiguration());
	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox) {
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
	public void isReady() throws PintException {
		// this is optional, so I dont do anything
	}

	@Override
	public String getID() {
		return getClass().getName();
	}

}
