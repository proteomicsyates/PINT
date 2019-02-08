package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ItemDraggableLabel;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;

public class ReferencedLabelsPanel extends AbstractReferencedItemPanel<LabelTypeBean> {

	public ReferencedLabelsPanel(Wizard<PintContext> wizard) {
		super(wizard, "label", DroppableFormat.LABEL);
	}

	@Override
	protected List<LabelTypeBean> getItemsBeansFromContext(PintContext context) {
		final List<LabelTypeBean> labels = PintImportCfgUtil.getLabels(context.getPintImportConfiguration());
		return labels;
	}

	@Override
	protected ItemDraggableLabel createDraggableLabel(LabelTypeBean label) {
		final ItemDraggableLabel labelObj = new ItemDraggableLabel(label.getId(), DroppableFormat.LABEL, label.getId());
		if (label.getMassDiff() != null) {
			labelObj.setTitle("Label: " + label.getId() + "\nMass difference: " + label.getMassDiff());
		} else {
			labelObj.setTitle("Label: " + label.getId());
		}
		return labelObj;
	}

}
