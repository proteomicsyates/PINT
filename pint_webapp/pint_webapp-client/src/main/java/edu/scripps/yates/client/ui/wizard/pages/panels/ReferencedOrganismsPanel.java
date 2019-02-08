package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ItemDraggableLabel;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;

public class ReferencedOrganismsPanel extends AbstractReferencedItemPanel<OrganismTypeBean> {

	public ReferencedOrganismsPanel(Wizard<PintContext> wizard) {
		super(wizard, "organism", DroppableFormat.ORGANISM);
	}

	@Override
	protected List<OrganismTypeBean> getItemsBeansFromContext(PintContext context) {
		final List<OrganismTypeBean> organisms = PintImportCfgUtil.getOrganisms(context.getPintImportConfiguration());
		return organisms;
	}

	@Override
	protected ItemDraggableLabel createDraggableLabel(OrganismTypeBean OrganismTypeBean) {
		final ItemDraggableLabel label = new ItemDraggableLabel(OrganismTypeBean.getId(), DroppableFormat.ORGANISM,
				OrganismTypeBean.getId());
		if (OrganismTypeBean.getDescription() != null) {
			label.setTitle(OrganismTypeBean.getDescription());
		}
		return label;
	}

}
