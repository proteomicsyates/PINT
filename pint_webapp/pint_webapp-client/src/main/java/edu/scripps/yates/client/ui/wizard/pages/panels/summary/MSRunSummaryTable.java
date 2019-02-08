package edu.scripps.yates.client.ui.wizard.pages.panels.summary;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;

public class MSRunSummaryTable extends AbstractSummaryTable {

	public MSRunSummaryTable(PintContext context, MsRunTypeBean msRun, int number) {
		super(number);
		int row = 0;
		// ms run number
		final Label msRunNumber = new Label(number + ".");
		msRunNumber.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(row, 0, msRunNumber);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
		// ms run name
		final Label msRunLabel = new Label("Experiment/Replicate name:");
		msRunLabel.setStyleName(WizardStyles.WizardInfoMessage);

		setWidget(row, 1, msRunLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label msRunNameLabel = new Label(msRun.getId());
		final String title = PintImportCfgUtil.getTitleMSRun(msRun);
		msRunNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickableRed);
		msRunNameLabel.setTitle(title);
		setWidget(row, 2, msRunNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_LEFT);
		// date
		row++;
		if (msRun.getDate() != null) {
			final Label dateLabel = new Label("Creation date:");
			dateLabel.setTitle("Date in which the experiment / replicate '" + msRun.getId() + "' was created");
			dateLabel.setStyleName(WizardStyles.WizardInfoMessage);
			setWidget(row, 0, dateLabel);
			getFlexCellFormatter().setColSpan(row, 0, 2);
			getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

			final Label dateNameLabel = new Label(PintImportCfgUtil.dateFormatter.format(msRun.getDate()));

			dateNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			setWidget(row, 1, dateNameLabel);
			getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		}
		// path
		row++;
		if (msRun.getPath() != null) {
			final Label pathLabel = new Label("Path:");
			pathLabel.setTitle("Path in which the experiment / replicate '" + msRun.getId() + "' was created");
			pathLabel.setStyleName(WizardStyles.WizardInfoMessage);
			setWidget(row, 0, pathLabel);
			getFlexCellFormatter().setColSpan(row, 0, 2);
			getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

			final Label pathNameLabel = new Label(msRun.getPath());
			pathNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			setWidget(row, 1, pathNameLabel);
			getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		}
	}

}
