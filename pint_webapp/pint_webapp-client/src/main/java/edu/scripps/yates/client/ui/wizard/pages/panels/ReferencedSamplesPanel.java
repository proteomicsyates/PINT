package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.WizardPage.PageID;
import edu.scripps.yates.client.ui.wizard.pages.PageIDController;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageSamples;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ItemDraggableLabel;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class ReferencedSamplesPanel extends AbstractReferencedItemPanel<SampleTypeBean> {

	public ReferencedSamplesPanel(Wizard<PintContext> wizard) {
		super(wizard, "sample", DroppableFormat.SAMPLE);
	}

	@Override
	protected List<SampleTypeBean> getItemsBeansFromContext(PintContext context) {
		final List<SampleTypeBean> samples = PintImportCfgUtil.getSamples(context.getPintImportConfiguration());
		return samples;
	}

	@Override
	protected ItemDraggableLabel createDraggableLabel(SampleTypeBean sampleTypeBean) {
		final ItemDraggableLabel label = new ItemDraggableLabel(sampleTypeBean.getId(), DroppableFormat.SAMPLE,
				sampleTypeBean.getId());
		if (sampleTypeBean.getDescription() != null) {
			label.setTitle(sampleTypeBean.getDescription());
		}
		return label;
	}

	@Override
	protected PageID getWizardPageIDToJumpByFormat(DroppableFormat format2) {
		if (format2 == DroppableFormat.SAMPLE) {
			return PageIDController.getPageIDByPageClass(WizardPageSamples.class);
		}
		return null;
	}

}
