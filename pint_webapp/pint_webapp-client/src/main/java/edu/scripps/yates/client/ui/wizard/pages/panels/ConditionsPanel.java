package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.PageIDController;
import edu.scripps.yates.client.ui.wizard.pages.PageTitleController;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageSamples;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ConditionItemWidget;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;

public class ConditionsPanel extends AbstractItemPanel<ConditionItemWidget, ExperimentalConditionTypeBean> {

	public ConditionsPanel(Wizard<PintContext> wizard) {
		super(wizard, "experimental condition");

	}

	@Override
	protected DoSomethingTask2<ExperimentalConditionTypeBean> getDoSomethingTaskOnRemoveItemBean(PintContext context) {

		return new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean item) {
				PintImportCfgUtil.removeCondition(context.getPintImportConfiguration(), item);

				if (context.getPintImportConfiguration().getProject().getExperimentalConditions()
						.getExperimentalCondition().isEmpty()) {
					ConditionsPanel.super.setNoItemsCreatedYetLabel();
				}
				return null;
			}
		};
	}

	@Override
	protected ConditionItemWidget createNewItemWidget(ExperimentalConditionTypeBean itemBean) {
		final ConditionItemWidget widget = new ConditionItemWidget(itemBean, getWizard().getContext());
		return widget;
	}

	@Override
	protected void addItemBeanToPintImportConfiguration(PintContext context, ExperimentalConditionTypeBean itemBean)
			throws PintException {
		// use util
		PintImportCfgUtil.addCondition(context.getPintImportConfiguration(), itemBean);

	}

	@Override
	protected ExperimentalConditionTypeBean createNewItemBean(String itemName) {
		final ExperimentalConditionTypeBean itemBean = new ExperimentalConditionTypeBean();
		itemBean.setId(itemName);
		return itemBean;
	}

	@Override
	protected List<ExperimentalConditionTypeBean> getItemBeansFromContext(PintContext context) {
		return PintImportCfgUtil.getConditions(context.getPintImportConfiguration());
	}

	@Override
	protected void fillSuggestions(SuggestBox createNewItemTextbox2) {

	}

	@Override
	public void isReady() throws PintException {
		final List<ExperimentalConditionTypeBean> conditions = getItemBeansFromContext(getWizard().getContext());
		if (conditions.isEmpty()) {
			throw new PintException("Create at least one experimental condition",
					PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
		}
		// to check that there is no a sample in multiple conditions
		final Set<String> sampleIDs = new HashSet<String>();

		for (final ExperimentalConditionTypeBean condition : conditions) {
			if (condition.getSampleRef() == null || "".equals(condition.getSampleRef())) {
				throw new PintException(
						"Experimental condition '" + condition.getId() + "' need to be associated to a Sample.",
						PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);
			} else {
				if (sampleIDs.contains(condition.getSampleRef())) {
					String message = "Sample '" + condition.getSampleRef()
							+ "' cannot be associated in more than one experimental condition.\n";

					if (PintImportCfgUtil.getSamples(getWizard().getContext().getPintImportConfiguration())
							.size() < PintImportCfgUtil
									.getConditions(getWizard().getContext().getPintImportConfiguration()).size()) {
						message += "\nClick on 'Go To "
								+ PageTitleController.getPageTitleByPageID(
										PageIDController.getPageIDByPageClass(WizardPageSamples.class))
								+ "' yellow button to create an additional sample.";
					} else {
						message += "\nYou may want to use a different sample associated with each experimental condition.";
					}
					throw new PintException(message, PINT_ERROR_TYPE.WIZARD_PAGE_INCOMPLETE);

				}
				sampleIDs.add(condition.getSampleRef());
			}
		}
	}

	@Override
	public String getID() {
		return getClass().getName();
	}
}
