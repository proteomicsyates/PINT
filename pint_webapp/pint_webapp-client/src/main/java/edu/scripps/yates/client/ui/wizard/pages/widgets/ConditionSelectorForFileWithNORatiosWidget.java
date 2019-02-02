package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
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
public class ConditionSelectorForFileWithNORatiosWidget extends AbstractConditionSelectorForFileWidget {

	private int rowForNewCondition;

	public ConditionSelectorForFileWithNORatiosWidget(PintContext context, FileTypeBean file) {
		super(context, file);
		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
				.getConditions(context.getPintImportConfiguration());

		final FlexTable leftTable = new FlexTable();
		leftTable.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		setWidget(0, 0, leftTable);
		getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

		// right with the dragable conditions
		final FlexTable rightTable = new FlexTable();
		rightTable.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		setWidget(0, 1, rightTable);
		getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		final Label label1 = new Label("Associated experimental conditions:");
		label1.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(0, 0, label1);
		leftTable.getFlexCellFormatter().setColSpan(0, 0, 3);

		final Label label2 = new Label("Condition");
		label2.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(1, 0, label2);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);

		final Label label3 = new Label("Sample");
		label3.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(1, 1, label3);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);

		//

		final AbstractItemDropLabel<ConditionSelectorForFileWithNORatiosWidget> dropLabelCondition = new AbstractItemDropLabel<ConditionSelectorForFileWithNORatiosWidget>(
				"drop Experimental Condition", DroppableFormat.SAMPLE, this, true, false) {

			@Override
			protected void updateItemWithData(String conditionID, DroppableFormat format,
					ConditionSelectorForFileWithNORatiosWidget form) {

				final ExperimentalConditionTypeBean condition = PintImportCfgUtil
						.getCondition(context.getPintImportConfiguration(), conditionID);
				rowForNewCondition++;

				if (getOnConditionAddedTask() != null) {
					getOnConditionAddedTask().doSomething(condition);
				}

				final Label labelCondition = new Label(condition.getId());
				labelCondition.setStyleName(WizardStyles.WizardDraggableLabelFixed);
				leftTable.setWidget(rowForNewCondition, 0, labelCondition);
				leftTable.getFlexCellFormatter().setHorizontalAlignment(rowForNewCondition, 0,
						HasHorizontalAlignment.ALIGN_LEFT);

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
						HasHorizontalAlignment.ALIGN_LEFT);
				// delete button
				final Image deleteButton = new Image(MyClientBundle.INSTANCE.redCross());
				deleteButton.setTitle("Click here to remove the association between file '" + file.getName()
						+ "' and the Experimental condition '" + condition.getId() + "'");
				deleteButton.setStyleName(WizardStyles.CLICKABLE);
				leftTable.setWidget(rowForNewCondition, 2, deleteButton);
				leftTable.getFlexCellFormatter().setHorizontalAlignment(rowForNewCondition, 2,
						HasHorizontalAlignment.ALIGN_LEFT);
				final int thisRow = rowForNewCondition;
				deleteButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						leftTable.removeRow(thisRow);
						final esto falla
						keep final a list and final get the index final from it to final use in the table
						PintImportCfgUtil.removeFileFromCondition(file.getId(), condition);
						if (getOnConditionRemovedTask() != null) {
							getOnConditionRemovedTask().doSomething(condition);
						}
						rowForNewCondition--;
					}
				});

			}
		};
		dropLabelCondition.getElement().getStyle().setMarginLeft(10, Unit.PX);
		leftTable.setWidget(2, 0, dropLabelCondition);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		rowForNewCondition = 3;// for associated conditions

		// RIGHT PANEL with conditions to drag
		final Label label4 = new Label("Experimental Conditions created:");
		label4.setStyleName(WizardStyles.WizardExplanationLabel);
		rightTable.setWidget(0, 0, label4);
		rightTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		//
		final Label label42 = new Label("Drag and drop to make the association");
		label42.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(1, 0, label42);
		rightTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		label42.getElement().getStyle().setMarginTop(10, Unit.PX);
		//
		final Label label5 = new Label("Condition");
		label5.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(2, 0, label5);
		rightTable.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		final Label label6 = new Label("Sample");
		label6.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(2, 1, label6);
		rightTable.getFlexCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_CENTER);
		int i = 3;
		for (final ExperimentalConditionTypeBean condition : conditions) {
			final ItemDraggableLabel conditionDraggableLabel = new ItemDraggableLabel(condition.getId(),
					DroppableFormat.SAMPLE, condition.getId());
			rightTable.setWidget(i, 0, conditionDraggableLabel);
			rightTable.getFlexCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);

			final Label label7 = new Label("-");
			label7.setStyleName(WizardStyles.WizardInfoMessage);
			if (condition.getSampleRef() != null) {
				label7.setText(condition.getSampleRef());
				label7.setStyleName(WizardStyles.WizardDraggableLabelFixed);
			}
			rightTable.setWidget(i, 1, label7);
			rightTable.getFlexCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER);
			if (i == 3) {
				// just the first one
				conditionDraggableLabel.getElement().getStyle().setMarginTop(10, Unit.PX);
				label7.getElement().getStyle().setMarginTop(10, Unit.PX);
			}
			i++;
		}
	}

}
