package edu.scripps.yates.client.ui.wizard.pages.panels.summary;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class ConditionSummaryTable extends AbstractSummaryTable {

	public ConditionSummaryTable(PintContext context, ExperimentalConditionTypeBean condition, int number) {
		super(number);

		// condition number
		final Label conditionNumber = new Label(number + ".");
		conditionNumber.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(0, 0, conditionNumber);
		getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		// condition name
		final Label conditionLabel = new Label("Experimental condition name:");
		conditionLabel.setStyleName(WizardStyles.WizardInfoMessage);
		conditionLabel.setTitle(condition.getDescription() != null ? condition.getDescription() : condition.getId());
		setWidget(0, 1, conditionLabel);
		getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label conditionNameLabel = new Label(condition.getId());
		conditionNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickableRed);
		conditionNameLabel.setTitle(PintImportCfgUtil.getConditionTitle(condition));
		setWidget(0, 2, conditionNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);
		// sample
		final Label sampleLabel = new Label("Sample analyzed under this condition:");
		sampleLabel.setTitle("Sample associated with experimental condition '" + condition.getId() + "'");
		sampleLabel.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(1, 0, sampleLabel);
		getFlexCellFormatter().setColSpan(1, 0, 2);
		getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label sampleNameLabel = new Label(condition.getSampleRef());
		final SampleTypeBean sample = PintImportCfgUtil.getSample(context.getPintImportConfiguration(),
				condition.getSampleRef());
		sampleNameLabel.setTitle(PintImportCfgUtil.getTitleSample(sample));
		sampleNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(1, 1, sampleNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
	}

}
