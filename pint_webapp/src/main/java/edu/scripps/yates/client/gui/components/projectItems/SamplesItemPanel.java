package edu.scripps.yates.client.gui.components.projectItems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.LabelBean;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.SampleBean;

public class SamplesItemPanel extends AbstractItemPanel<SampleBean, ProjectBean> {
	private static SamplesItemPanel instance;

	public static SamplesItemPanel getInstance(ProjectBean projectBean) {
		if (instance == null) {
			instance = new SamplesItemPanel(projectBean);
		} else {
			instance.updateParent(projectBean);
		}
		return instance;
	}

	private final Label labelCondition;
	private final Label label;
	protected Label descriptionLabel;
	private final Label labeledLabel;
	private final Label label2;
	private final NumberFormat formatter = NumberFormat.getDecimalFormat().getFormat("#.##");

	private SamplesItemPanel(ProjectBean projectBean) {
		super("Samples", projectBean);
		// add description panel in the right
		FlexTable flexTable = new FlexTable();
		labelCondition = new Label("");
		flexTable.setWidget(0, 0, labelCondition);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		labelCondition.setStyleName("ProjectItemContentTitle");
		label = new Label("Description:");
		label.setStyleName("ProjectItemIndividualItemTitle");
		label.setVisible(false);
		flexTable.setWidget(1, 0, label);
		flexTable.getFlexCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		descriptionLabel = new Label();
		flexTable.setWidget(1, 1, descriptionLabel);
		flexTable.getFlexCellFormatter().setAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		label2 = new Label("Labeled:");
		label2.setStyleName("ProjectItemIndividualItemTitle");
		label2.setVisible(false);
		flexTable.setWidget(2, 0, label2);
		flexTable.getFlexCellFormatter().setAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		labeledLabel = new Label();
		flexTable.setWidget(2, 1, labeledLabel);
		flexTable.getFlexCellFormatter().setAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		updateParent(projectBean);

		addRightPanel(flexTable);

		selectFirstItem();
	}

	@Override
	public void updateParent(ProjectBean projectBean) {
		clearItemList();
		if ((projectBean != null && !projectBean.equals(currentParent)) || getItems().isEmpty()) {
			currentParent = projectBean;
			int numSamples = 0;
			final List<ExperimentalConditionBean> conditions = projectBean.getConditions();
			Set<SampleBean> sampleSet = new HashSet<SampleBean>();
			for (final ExperimentalConditionBean conditionBean : conditions) {
				final SampleBean sample = conditionBean.getSample();
				if (sample != null) {
					if (!sampleSet.contains(sample)) {
						sampleSet.add(sample);
						addItemToList(sample.getId(), sample);
						numSamples++;
					}
				}
			}
			String plural = numSamples > 1 ? "s" : "";
			setCaption(numSamples + " Sample" + plural);
			selectFirstItem();
		} else {
			unselectItems();
		}
	}

	@Override
	public void selectItem(SampleBean sample) {
		if (sample != null) {
			label.setVisible(true);
			String description = getStringNotAvailable("description", sample.getDescription());
			descriptionLabel.setText(description);
			labelCondition.setText("Sample '" + sample.getId() + "':");

			label2.setVisible(true);
			String labeled = sample.getLabel() != null ? getLabelDescription(sample.getLabel()) : "-";
			labeledLabel.setText(labeled);
			selectedItemStatsPanel = ProjectStatsFromSampleItemPanel.getInstance(sample);
		} else {
			label.setVisible(false);
			descriptionLabel.setText(null);
			labelCondition.setText(null);
		}

	}

	private String getLabelDescription(LabelBean label) {
		StringBuilder sb = new StringBuilder();
		if (label != null) {
			sb.append(label.getName());
			if (label.getMassDiff() != null) {
				sb.append(" (" + formatter.format(label.getMassDiff()) + ")");
			}
		}
		return sb.toString();
	}

}
