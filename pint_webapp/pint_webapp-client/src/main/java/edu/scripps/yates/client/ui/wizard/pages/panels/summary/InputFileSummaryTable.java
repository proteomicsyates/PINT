package edu.scripps.yates.client.ui.wizard.pages.panels.summary;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class InputFileSummaryTable extends AbstractSummaryTable {

	public InputFileSummaryTable(PintContext context, FileTypeBean fileType, int number) {
		super(number);
		int row = 0;
		// file number
		final Label fileNumber = new Label(number + ".");
		fileNumber.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(row, 0, fileNumber);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
		// file name
		final Label fileLabel = new Label("Input file name:");
		fileLabel.setStyleName(WizardStyles.WizardInfoMessage);

		setWidget(row, 1, fileLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label fileNameLabel = new Label(fileType.getName());
		final String title = PintImportCfgUtil.getTitleInputFile(fileType);
		fileNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickableRed);
		fileNameLabel.setTitle(title);
		setWidget(row, 2, fileNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_LEFT);
		// date
		row++;

		final Label formatLabel = new Label("Format:");
		formatLabel.setTitle("For of the input file '" + fileType.getId() + "'");
		formatLabel.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, formatLabel);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		final Label formatNameLabel = new Label(fileType.getFormat().getName());

		formatNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(row, 1, formatNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);

		// num sheets
		row++;
		if (fileType.getFormat() == FileFormat.EXCEL) {
			final Label excelNumSheetsLabel = new Label("Number of sheets:");
			excelNumSheetsLabel.setTitle("Number of sheets contained in Excel file '" + fileType.getId() + "'");
			excelNumSheetsLabel.setStyleName(WizardStyles.WizardInfoMessage);
			setWidget(row, 0, excelNumSheetsLabel);
			getFlexCellFormatter().setColSpan(row, 0, 2);
			getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

			final Label excelNumSheetsNameLabel = new Label(String.valueOf(fileType.getSheets().getSheet().size()));
			excelNumSheetsNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			setWidget(row, 1, excelNumSheetsNameLabel);
			getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		}
	}

}
