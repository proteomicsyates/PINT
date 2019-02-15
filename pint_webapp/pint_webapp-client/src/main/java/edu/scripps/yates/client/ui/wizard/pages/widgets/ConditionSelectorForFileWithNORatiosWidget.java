package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

/**
 * This panel is going to have a left part with the numerator and denominator
 * labels to drop off a condition in the right, we will have the dragable
 * conditions
 * 
 * @author salvador
 *
 */
public class ConditionSelectorForFileWithNORatiosWidget extends AbstractConditionSelectorForFileWidget {

	private int rowForNewCondition;
	private final List<ExperimentalConditionTypeBean> conditionsInRows = new ArrayList<ExperimentalConditionTypeBean>();
	private final int firstRowForNewCondition;
	private final FlexTable leftTable;

	public ConditionSelectorForFileWithNORatiosWidget(PintContext context, FileTypeBean file,
			DoSomethingTask2<ExperimentalConditionTypeBean> onConditionAddedTask,
			DoSomethingTask2<ExperimentalConditionTypeBean> onConditionRemovedTask, String sheetName) {
		super(context, file, onConditionAddedTask, onConditionRemovedTask);
		this.setWidth("100%");
		leftTable = new FlexTable();
		leftTable.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		setWidget(0, 0, leftTable);
		getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		leftTable.getElement().getStyle().setPadding(20, Unit.PX);

		// LEFT ARROW
		final Image leftArrow = new Image(MyClientBundle.INSTANCE.doubleLeftArrow());
		leftArrow.setTitle("Drag the conditions to the let table in order to make the association");
		setWidget(0, 1, leftArrow);
		getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);

		// right with the dragable conditions
		final FlexTable rightTable = new FlexTable();
		rightTable.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		setWidget(0, 2, rightTable);
		rightTable.getElement().getStyle().setPadding(20, Unit.PX);
		getFlexCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
		getFlexCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);

		final Label label1 = new Label("Associated experimental conditions:");
		label1.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(0, 0, label1);
		leftTable.getFlexCellFormatter().setColSpan(0, 0, 3);

		//
		final Label label12 = new Label("Drop to make the association:");
		label12.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(1, 0, label12);
		leftTable.getFlexCellFormatter().setColSpan(1, 0, 3);
		label12.getElement().getStyle().setMarginBottom(10, Unit.PX);

		//

		final AbstractItemDropLabel<ConditionSelectorForFileWithNORatiosWidget> dropLabelCondition = new AbstractItemDropLabel<ConditionSelectorForFileWithNORatiosWidget>(
				"drop Experimental Condition", DroppableFormat.SAMPLE, this, true, false) {

			@Override
			protected void updateItemWithData(String conditionID, DroppableFormat format,
					ConditionSelectorForFileWithNORatiosWidget form) {

				addAssociatedCondition(conditionID);

			}
		};
		leftTable.setWidget(2, 0, dropLabelCondition);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		leftTable.getFlexCellFormatter().setColSpan(2, 0, 3);
		//
		final Label label2 = new Label("Condition");
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(3, 0, label2);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_CENTER);

		final Label label3 = new Label("Sample");
		label3.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(3, 1, label3);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_CENTER);

		final Label label32 = new Label("Label");
		label32.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(3, 2, label32);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(3, 2, HasHorizontalAlignment.ALIGN_CENTER);

		firstRowForNewCondition = 4;
		rowForNewCondition = firstRowForNewCondition;// for associated conditions

		// RIGHT PANEL with conditions to drag
		final Label label4 = new Label("Experimental Conditions created:");
		label4.setStyleName(WizardStyles.WizardExplanationLabel);
		rightTable.setWidget(0, 0, label4);
		rightTable.getFlexCellFormatter().setColSpan(0, 0, 3);
		//
		final Label label42 = new Label("Drag and drop to make the association");
		label42.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(1, 0, label42);
		rightTable.getFlexCellFormatter().setColSpan(1, 0, 3);
		label42.getElement().getStyle().setMarginBottom(10, Unit.PX);

		//
		final Label label5 = new Label("Condition");
		label5.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(2, 0, label5);
		rightTable.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		final Label label6 = new Label("Sample");
		label6.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(2, 1, label6);
		rightTable.getFlexCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_CENTER);
		final Label label7 = new Label("Label");
		label7.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(2, 2, label7);
		rightTable.getFlexCellFormatter().setHorizontalAlignment(2, 2, HasHorizontalAlignment.ALIGN_CENTER);
		//
		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
				.getConditions(context.getPintImportConfiguration());
		int i = firstRowForNewCondition;
		for (final ExperimentalConditionTypeBean condition : conditions) {
			final ItemDraggableLabel conditionDraggableLabel = new ItemDraggableLabel(condition.getId(),
					DroppableFormat.SAMPLE, condition.getId());
			rightTable.setWidget(i, 0, conditionDraggableLabel);
			rightTable.getFlexCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);

			final Label label8 = new Label("-");
			label8.setStyleName(WizardStyles.WizardInfoMessage);
			final Label label9 = new Label("-");
			label9.setStyleName(WizardStyles.WizardInfoMessage);

			if (condition.getSampleRef() != null) {
				label8.setText(condition.getSampleRef());
				label8.setStyleName(WizardStyles.WizardDraggableLabelFixed);
				final SampleTypeBean sample = PintImportCfgUtil.getSample(context.getPintImportConfiguration(),
						condition.getSampleRef());
				if (sample.getLabelRef() != null) {
					label9.setText(sample.getLabelRef());
					label9.setStyleName(WizardStyles.WizardDraggableLabelFixed);
				}
			}
			rightTable.setWidget(i, 1, label8);
			rightTable.getFlexCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER);
			rightTable.setWidget(i, 2, label9);
			rightTable.getFlexCellFormatter().setHorizontalAlignment(i, 2, HasHorizontalAlignment.ALIGN_CENTER);

			i++;
		}
		List<ExperimentalConditionTypeBean> conditionsAssociatedWithFile = null;
		if (sheetName != null) {
			conditionsAssociatedWithFile = PintImportCfgUtil.getConditionsAssociatedWithExcelFile(
					context.getPintImportConfiguration(), file.getId(), sheetName);
		} else {
			conditionsAssociatedWithFile = PintImportCfgUtil
					.getConditionsAssociatedWithFile(context.getPintImportConfiguration(), file.getId());
		}
		if (conditionsAssociatedWithFile != null) {
			for (final ExperimentalConditionTypeBean condition : conditionsAssociatedWithFile) {
				addAssociatedCondition(condition.getId());
			}
		}
	}

	private void addAssociatedCondition(String conditionID) {
		final ExperimentalConditionTypeBean condition = PintImportCfgUtil
				.getCondition(context.getPintImportConfiguration(), conditionID);

		if (conditionsInRows.contains(condition)) {
			return;
		}

		conditionsInRows.add(condition);
		for (final DoSomethingTask2<ExperimentalConditionTypeBean> task : getOnConditionAddedTasks()) {
			task.doSomething(condition);
		}

		final Label labelCondition = new Label(condition.getId());
		labelCondition.setStyleName(WizardStyles.WizardDraggableLabelFixed);
		leftTable.setWidget(rowForNewCondition, 0, labelCondition);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(rowForNewCondition, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

		if (condition.getSampleRef() != null) {
			final Label labelSample = new Label(condition.getSampleRef());
			labelSample.setStyleName(WizardStyles.WizardDraggableLabelFixed);
			leftTable.setWidget(rowForNewCondition, 1, labelSample);
		} else {
			final Label labelSample = new Label("-");
			labelSample.setStyleName(WizardStyles.WizardInfoMessage);
			leftTable.setWidget(rowForNewCondition, 1, labelSample);
		}
		leftTable.getFlexCellFormatter().setHorizontalAlignment(rowForNewCondition, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		// delete button
		final Image deleteButton = new Image(MyClientBundle.INSTANCE.redCross());
		deleteButton.setTitle("Click here to remove the association between file '" + file.getName()
				+ "' and the Experimental condition '" + condition.getId() + "'");
		deleteButton.setStyleName(WizardStyles.CLICKABLE);
		leftTable.setWidget(rowForNewCondition, 2, deleteButton);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(rowForNewCondition, 2,
				HasHorizontalAlignment.ALIGN_CENTER);
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final int index = conditionsInRows.indexOf(condition);
				leftTable.removeRow(index + firstRowForNewCondition);

				PintImportCfgUtil.removeFileFromCondition(file.getId(), condition);
				for (final DoSomethingTask2<ExperimentalConditionTypeBean> task : getOnConditionRemovedTasks()) {
					task.doSomething(condition);
				}
				rowForNewCondition--;
				conditionsInRows.remove(condition);
			}
		});
		rowForNewCondition++;
	}

	@Override
	public List<ExperimentalConditionTypeBean> getConditions() {
		return this.conditionsInRows;
	}

}
