package edu.scripps.yates.client.gui.components.projectItems;

import java.util.Collections;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.ProjectBean;

public class MSRunsItemPanel extends AbstractItemPanel<ProjectBean, MSRunBean> {
	private static MSRunsItemPanel instance;

	public static MSRunsItemPanel getInstance(ProjectBean projectBean, boolean resetItems) {
		if (instance == null) {
			instance = new MSRunsItemPanel(projectBean);
		} else {
			instance.updateParent(projectBean, resetItems);
		}
		return instance;
	}

	private final Label labelMSRun;
	private final Label label;
	protected Label pathLabel;
	private final Label label2;
	private final DateLabel dateLabel;

	private MSRunsItemPanel(ProjectBean projectBean) {
		super("MS runs", projectBean);
		// add description panel in the right
		final FlexTable rightFlexTable = new FlexTable();
		labelMSRun = new Label("");
		rightFlexTable.setWidget(0, 0, labelMSRun);
		rightFlexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		labelMSRun.setStyleName("ProjectItemContentTitle");
		label = new Label("Path:");
		label.setStyleName("ProjectItemIndividualItemTitle");
		label.setVisible(false);
		rightFlexTable.setWidget(1, 0, label);
		rightFlexTable.getFlexCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		pathLabel = new Label();
		rightFlexTable.setWidget(1, 1, pathLabel);
		rightFlexTable.getFlexCellFormatter().setAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		label2 = new Label("Date:");
		label2.setStyleName("ProjectItemIndividualItemTitle");
		label2.setVisible(false);
		rightFlexTable.setWidget(2, 0, label2);
		rightFlexTable.getFlexCellFormatter().setAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);

		dateLabel = new DateLabel(com.google.gwt.i18n.client.DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG));
		rightFlexTable.setWidget(2, 1, dateLabel);
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

			final List<MSRunBean> msRuns = projectBean.getMsRuns();
			Collections.sort(msRuns);
			for (final MSRunBean msrunBean : msRuns) {
				addItemToList(msrunBean.getId(), msrunBean);
			}
			final String plural = msRuns.size() > 1 ? "s" : "";
			setCaption(msRuns.size() + " MS run" + plural);
			selectFirstItem();
		} else {
			unselectItems();
		}
	}

	@Override
	public void selectItem(MSRunBean msRunBean) {
		if (msRunBean != null) {
			label.setVisible(true);
			final String path = getStringNotAvailable("path", msRunBean.getPath());
			pathLabel.setText(path);
			labelMSRun.setText("MS run '" + msRunBean.getId() + "':");

			if (msRunBean.getDate() != null) {
				label2.setVisible(true);
				dateLabel.setValue(msRunBean.getDate());
			} else {
				label2.setVisible(false);
				dateLabel.setValue(null);
			}
			selectedItemStatsPanel = ProjectStatsFromMSRunItemPanel.getInstance(msRunBean, true);
		} else {
			label.setVisible(false);
			pathLabel.setText(null);
			labelMSRun.setText(null);
			label2.setVisible(false);
			dateLabel.setValue(null);
		}
	}

}
