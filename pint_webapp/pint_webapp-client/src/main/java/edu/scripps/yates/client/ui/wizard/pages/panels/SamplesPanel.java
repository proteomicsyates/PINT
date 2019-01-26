package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.widgets.SampleItemWidget;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class SamplesPanel extends AbstractItemPanel<SampleItemWidget, SampleTypeBean> {

	public SamplesPanel(Wizard<PintContext> wizard) {
		super(wizard, "sample");
		// because we want to keep previous name to help to fill new sample name
		setResetNameForEachNewItemCreated(false);
	}

	@Override
	protected SampleTypeBean createNewItemBean(String itemName) {
		final SampleTypeBean sampleObj = new SampleTypeBean();
		sampleObj.setId(itemName);
		return sampleObj;
	}

	@Override
	protected void addItemBeanToPintImportConfiguration(PintContext context, SampleTypeBean sampleObj)
			throws PintException {
		// add it to the cfg object

		PintImportCfgUtil.addSample(context.getPintImportConfiguration(), sampleObj);

	}

	@Override
	protected SampleItemWidget createNewItemWidget(SampleTypeBean sampleObj) {
		final SampleItemWidget sampleWidget = new SampleItemWidget(sampleObj, getWizard().getContext());
		return sampleWidget;
	}

	@Override
	protected DoSomethingTask2<SampleTypeBean> getDoSomethingTaskOnRemoveItemBean(PintContext context) {

		return new DoSomethingTask2<SampleTypeBean>() {

			@Override
			public Void doSomething(SampleTypeBean item) {

				PintImportCfgUtil.removeSample(context.getPintImportConfiguration(), item);

				if (context.getPintImportConfiguration().getProject().getExperimentalDesign().getSampleSet().getSample()
						.isEmpty()) {
					SamplesPanel.super.setNoItemsCreatedYetLabel();

				}
				return null;
			}
		};

	}

	@Override
	protected List<SampleTypeBean> getItemBeansFromContext(PintContext context) {
		return PintImportCfgUtil.getSamples(context.getPintImportConfiguration());
	}

	@Override
	protected void fillSuggestions(SuggestBox createNewItemTextbox2) {

	}

	@Override
	public void isReady() throws PintException {
		final List<SampleTypeBean> samples = getItemBeansFromContext(getWizard().getContext());
		if (samples.isEmpty()) {
			throw new PintException("Create at least one sample", PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
		}
		for (final SampleTypeBean sample : samples) {
			if (sample.getOrganismRef() == null || "".equals(sample.getOrganismRef())) {
				throw new PintException("Sample '" + sample.getId() + "' need to have a referenced Organism.",
						PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
			}
			if (sample.getTissueRef() == null || "".equals(sample.getTissueRef())) {
				throw new PintException("Sample '" + sample.getId() + "' need to have a referenced Tissue/Cell line.",
						PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
			}
// then, the reference to a label is optional
		}
	}

	@Override
	public String getID() {
		return getClass().getName();
	}
}
