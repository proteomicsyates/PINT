package edu.scripps.yates.client.ui.wizard.form;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.widgets.AbstractItemDropLabel;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ItemDraggableLabel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

/**
 * This panel is going to have a left part with the numerator and denominator
 * labels to drop off a condition in the right, we will have the dragable
 * conditions
 * 
 * @author salvador
 *
 */
public class RatioSelectorForFileForm extends FlexTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6242527427961501168L;

	private ExperimentalConditionTypeBean condition1;
	private ExperimentalConditionTypeBean condition2;

	private final List<DoSomethingTask<Void>> onTwoConditionsSelectedTasks = new ArrayList<DoSomethingTask<Void>>();
	private final List<DoSomethingTask<Void>> onNotTwoConditionsSelectedTasks = new ArrayList<DoSomethingTask<Void>>();

	public RatioSelectorForFileForm(PintContext context, FileTypeBean file) {

		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
				.getConditions(context.getPintImportConfiguration());

		final FlexTable leftTable = new FlexTable();
		setWidget(0, 0, leftTable);
		// right with the dragable conditions
		final VerticalPanel rightPanel = new VerticalPanel();
		setWidget(0, 1, rightPanel);

		int row = 0;
		final Label label1 = new Label("Relative ratio definition for file '" + file.getId() + "'");
		label1.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(row, 0, label1);
		leftTable.getFlexCellFormatter().setColSpan(row, 0, 3);

		//
		row++;
		Label label2 = new Label("Ratio numerator condition or sample:");
		label2.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(row, 0, label2);
		Label labelSample = new Label("-");
		labelSample.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(row, 2, labelSample);
		final AbstractItemDropLabel<RatioSelectorForFileForm> dropLabelNumerator = new AbstractItemDropLabel<RatioSelectorForFileForm>(
				"drop numerator Condition or Sample", DroppableFormat.SAMPLE, this) {

			@Override
			protected void updateItemWithData(String conditionID, DroppableFormat format,
					RatioSelectorForFileForm form) {
				condition1 = PintImportCfgUtil.getCondition(context.getPintImportConfiguration(), conditionID);
				// set sample in column 3
				if (condition1.getSampleRef() != null) {
					labelSample.setText(condition1.getSampleRef());
					labelSample.setStyleName(WizardStyles.WizardDraggableLabelFixed);
				} else {
					labelSample.setText("-");
					labelSample.setStyleName(WizardStyles.WizardExplanationLabel);
				}
				checkIfBothConditionsAreSelected();
			}
		};
		leftTable.setWidget(row, 1, dropLabelNumerator);
		//
		row++;
		Label label3 = new Label("Ratio denominator condition or sample:");
		label3.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(row, 0, label3);
		Label labelSample2 = new Label("-");
		labelSample2.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(row, 2, labelSample2);
		final AbstractItemDropLabel<RatioSelectorForFileForm> dropLabelDenominator = new AbstractItemDropLabel<RatioSelectorForFileForm>(
				"drop denominator Condition or Sample", DroppableFormat.SAMPLE, this) {

			@Override
			protected void updateItemWithData(String conditionID, DroppableFormat format,
					RatioSelectorForFileForm form) {
				condition2 = PintImportCfgUtil.getCondition(context.getPintImportConfiguration(), conditionID);
				// set sample in column 3
				if (condition2.getSampleRef() != null) {
					labelSample2.setText(condition2.getSampleRef());
					labelSample2.setStyleName(WizardStyles.WizardDraggableLabelFixed);
				} else {
					labelSample2.setText("-");
					labelSample2.setStyleName(WizardStyles.WizardExplanationLabel);
				}
				checkIfBothConditionsAreSelected();
			}
		};
		leftTable.setWidget(row, 1, dropLabelDenominator);

		// RIGHT PANEL with conditions to drag
		final Label label4 = new Label("Available Experimental Conditions and Samples:");
		label4.setStyleName(WizardStyles.WizardInfoMessage);
		rightPanel.add(label4);
		for (final ExperimentalConditionTypeBean condition : conditions) {
			String labelName = condition.getId();
			if (condition.getSampleRef() != null) {
				labelName += " (Sample: '" + condition.getSampleRef() + "')";
			}
			final ItemDraggableLabel conditionDraggableLabel = new ItemDraggableLabel(labelName, DroppableFormat.SAMPLE,
					condition.getId());
			rightPanel.add(conditionDraggableLabel);
		}
	}

	protected void checkIfBothConditionsAreSelected() {
		if (condition1 != null && condition2 != null) {
			for (DoSomethingTask<Void> doSomethingTask : onTwoConditionsSelectedTasks) {
				doSomethingTask.doSomething();
			}
		} else {
			for (DoSomethingTask<Void> doSomethingTask : onNotTwoConditionsSelectedTasks) {
				doSomethingTask.doSomething();
			}
		}
	}

	public void addOnTwoConditionsSelectedTask(DoSomethingTask<Void> doSomethingTask) {
		this.onTwoConditionsSelectedTasks.add(doSomethingTask);
	}

	public ExperimentalConditionTypeBean getCondition1() {
		return condition1;
	}

	public ExperimentalConditionTypeBean getCondition2() {
		return condition2;
	}

	public void addOnNotTwoConditionsSelectedTask(DoSomethingTask<Void> doSomethingTask) {
		this.onNotTwoConditionsSelectedTasks.add(doSomethingTask);

	}

}
