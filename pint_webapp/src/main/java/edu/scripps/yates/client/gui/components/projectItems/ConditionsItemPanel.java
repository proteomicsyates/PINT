package edu.scripps.yates.client.gui.components.projectItems;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.ProjectBean;

public class ConditionsItemPanel extends AbstractItemPanel<ProjectBean, ExperimentalConditionBean> {
	private static ConditionsItemPanel instance;

	public static ConditionsItemPanel getInstance(ProjectBean projectBean, boolean resetItems) {
		if (instance == null) {
			instance = new ConditionsItemPanel(projectBean);
		} else {
			instance.updateParent(projectBean, resetItems);
		}
		return instance;
	}

	private final Label labelCondition;
	private final Label label;
	protected Label descriptionLabel;
	private final Label label2;
	private final Label sampleLabel;

	private ConditionsItemPanel(ProjectBean projectBean) {
		super("Conditions", projectBean);
		// add description panel in the right
		final FlexTable rightFlexTable = new FlexTable();
		labelCondition = new Label("");
		rightFlexTable.setWidget(0, 0, labelCondition);
		rightFlexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		labelCondition.setStyleName("ProjectItemContentTitle");
		label = new Label("Description:");
		label.setStyleName("ProjectItemIndividualItemTitle");
		label.setVisible(false);
		rightFlexTable.setWidget(1, 0, label);
		rightFlexTable.getFlexCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		descriptionLabel = new Label();
		rightFlexTable.setWidget(1, 1, descriptionLabel);
		rightFlexTable.getFlexCellFormatter().setAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		label2 = new Label("Sample:");
		label2.setStyleName("ProjectItemIndividualItemTitle");
		label2.setVisible(false);
		rightFlexTable.setWidget(2, 0, label2);
		rightFlexTable.getFlexCellFormatter().setAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		sampleLabel = new Label();
		rightFlexTable.setWidget(2, 1, sampleLabel);
		rightFlexTable.getFlexCellFormatter().setAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		updateParent(projectBean, true);

		addRightPanel(rightFlexTable);

		selectFirstItem();
	}

	@Override
	public void updateParent(ProjectBean projectBean, boolean resetItems) {
		if (resetItems) {
			clearItemList();
		}
		if ((projectBean != null && !projectBean.equals(currentParent)) || getItems().isEmpty()) {
			currentParent = projectBean;

			final List<ExperimentalConditionBean> conditions = projectBean.getConditions();
			Collections.sort(conditions);
			for (final ExperimentalConditionBean conditionBean : conditions) {
				addItemToList(conditionBean.getId(), conditionBean);
			}
			final String plural = conditions.size() > 1 ? "s" : "";
			setCaption(conditions.size() + " Condition" + plural);
			selectFirstItem();
		} else {
			unselectItems();
		}
	}

	@Override
	public void selectItem(ExperimentalConditionBean conditionBean) {
		if (conditionBean != null) {
			label.setVisible(true);
			final String description = getStringNotAvailable("description", conditionBean.getDescription());
			descriptionLabel.setText(description);
			labelCondition.setText("Condition '" + conditionBean.getId() + "':");

			if (conditionBean.getSample() != null) {
				label2.setVisible(true);
				sampleLabel.setText(conditionBean.getSample().getId());
			} else {
				label2.setVisible(false);
				sampleLabel.setText(null);
			}
			selectedItemStatsPanel = ProjectStatsFromConditionItemPanel.getInstance(conditionBean, true);
		} else {
			label.setVisible(false);
			descriptionLabel.setText(null);
			labelCondition.setText(null);
			label2.setVisible(false);
			sampleLabel.setText(null);
		}
	}

}
