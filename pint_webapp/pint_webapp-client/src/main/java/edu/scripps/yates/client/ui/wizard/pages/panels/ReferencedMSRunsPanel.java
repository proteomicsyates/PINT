package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ItemDraggableLabel;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;

public class ReferencedMSRunsPanel extends AbstractReferencedItemPanel<MsRunTypeBean> {

	public ReferencedMSRunsPanel(Wizard<PintContext> wizard) {
		super(wizard, "Experiment/Replicate", DroppableFormat.MSRUN);
	}

	@Override
	protected List<MsRunTypeBean> getItemsBeansFromContext(PintContext context) {
		final List<MsRunTypeBean> msRuns = PintImportCfgUtil.getMSRuns(context.getPintImportConfiguration());
		return msRuns;
	}

	@Override
	protected ItemDraggableLabel createDraggableLabel(MsRunTypeBean msrun) {
		final ItemDraggableLabel labelObj = new ItemDraggableLabel(msrun.getId(), DroppableFormat.MSRUN, msrun.getId());
		labelObj.setTitle("Experiment/Replicate: " + msrun.getId());
		if (msrun.getPath() != null) {
			labelObj.setTitle(labelObj.getTitle() + "\nPath: " + msrun.getPath());
		}
		if (msrun.getDate() != null) {
			labelObj.setTitle(labelObj.getTitle() + "\nDate: " + msrun.getDate());
		}
		return labelObj;
	}

}
