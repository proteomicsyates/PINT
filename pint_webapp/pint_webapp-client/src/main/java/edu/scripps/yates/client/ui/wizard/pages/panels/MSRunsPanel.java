package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.widgets.MSRunItemWidget;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;

public class MSRunsPanel extends AbstractItemPanel<MSRunItemWidget, MsRunTypeBean> {

	public MSRunsPanel(Wizard<PintContext> wizard) {
		super(wizard, "experiment / replicate");

	}

	@Override
	protected MsRunTypeBean createNewItemBean(String itemName) {
		final MsRunTypeBean msRun = new MsRunTypeBean();
		msRun.setId(itemName);
		return msRun;
	}

	@Override
	protected void addItemBeanToPintImportConfiguration(PintContext context, MsRunTypeBean msRun) throws PintException {
		// add it to the cfg object
		PintImportCfgUtil.addMSRun(context.getPintImportConfiguration(), msRun);
	}

	@Override
	protected MSRunItemWidget createNewItemWidget(MsRunTypeBean msRun) {
		final MSRunItemWidget organismWidget = new MSRunItemWidget(msRun, getWizard().getContext());

		return organismWidget;
	}

	@Override
	protected DoSomethingTask2<MsRunTypeBean> getDoSomethingTaskOnRemoveItemBean(PintContext context) {

		return new DoSomethingTask2<MsRunTypeBean>() {

			@Override
			public Void doSomething(MsRunTypeBean item) {

				PintImportCfgUtil.removeMSRun(context.getPintImportConfiguration(), item);

				if (context.getPintImportConfiguration().getProject().getMsRuns().getMsRun().isEmpty()) {
					MSRunsPanel.super.setNoItemsCreatedYetLabel();

				}
				return null;
			}
		};

	}

	@Override
	protected List<MsRunTypeBean> getItemBeansFromContext(PintContext context) {
		return PintImportCfgUtil.getMSRuns(context.getPintImportConfiguration());
	}

	@Override
	protected void fillSuggestions(SuggestBox nameTextBox) {
		// do nothing
	}

	@Override
	public void isReady() throws PintException {
		if (getItemBeansFromContext(getWizard().getContext()).isEmpty()) {
			throw new PintException("Create at least one experiment / replicate",
					PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
		}
	}

	@Override
	public String getID() {
		return getClass().getName();
	}
}
