package edu.scripps.yates.client.ui.wizard.form;

import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

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

	public RatioSelectorForFileForm(PintContext context, FileTypeBean file, String question, String explanation) {

		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
				.getConditions(context.getPintImportConfiguration());
		final VerticalPanel leftPanel = new VerticalPanel();
		setWidget(0, 0, leftPanel);

		final Label label1 = new Label("Relative ratio definition for file '" + file.getId() + "'");
		label1.setStyleName(WizardStyles.WizardInfoMessage);
		leftPanel.add(label1);

		final AbstractItemDropLabel<ExperimentalConditionTypeBean> dropLabelNumerator = new AbstractItemDropLabel<ExperimentalConditionTypeBean>(
				"drop numerator Condition or Sample", DroppableFormat.SAMPLE, this) {

			@Override
			protected void updateItemWithData(String data, DroppableFormat format,
					ExperimentalConditionTypeBean itemWidget2) {
				@Override
				protected void updateItemWithData(String conditionID, DroppableFormat format,
						RatioSelectorForFileForm form) {
					condition1 = PintImportCfgUtil.getCondition(context.getPintImportConfiguration(), conditionID);
				}
			}

		};

		// right with the dragable conditions
		final VerticalPanel rightPanel = new VerticalPanel();
		setWidget(0, 1, rightPanel);
		final Label label2 = new Label("Available Experimental Conditions and Samples:");
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		rightPanel.add(label2);
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

}
