package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ItemDraggableLabel;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;

public class ReferencedTissuesPanel extends AbstractReferencedItemPanel<TissueTypeBean> {

	public ReferencedTissuesPanel(Wizard<PintContext> wizard) {
		super(wizard, "tissue/cell line", DroppableFormat.TISSUE);
	}

	@Override
	protected List<TissueTypeBean> getItemsBeansFromContext(PintContext context) {
		final List<TissueTypeBean> organisms = PintImportCfgUtil.getTissues(context.getPintImportConfiguration());
		return organisms;
	}

	@Override
	protected ItemDraggableLabel createDraggableLabel(TissueTypeBean tissue) {
		final ItemDraggableLabel label = new ItemDraggableLabel(tissue.getId(), DroppableFormat.TISSUE, tissue.getId());
		if (tissue.getDescription() != null) {
			label.setTitle(tissue.getDescription());
		}
		return label;
	}

}
