package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ClientNumberFormat;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.FileSummary;
import edu.scripps.yates.shared.model.SheetSummary;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class InputFileSummaryPanel extends FlexTable {
	private final static Image imageLoader = new Image(MyClientBundle.INSTANCE.roundedLoader());
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();

	private Label associatedConditionsValues;
	private final List<ExperimentalConditionTypeBean> conditions = new ArrayList<ExperimentalConditionTypeBean>();
	private DoSomethingTask<Void> onFileSummaryReceivedTask;
	private final ClientNumberFormat formatter = new ClientNumberFormat("###,###");
	private final String sheetName;

	public InputFileSummaryPanel(PintContext context, FileTypeBean file, String sheetName) {
		this.sheetName = sheetName;
		final Label label = new Label("This is what we got from this file...");
		label.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(0, 0, label);
		final Label label2 = new Label("Processing file information...");
		setWidget(1, 0, label2);
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(1, 1, imageLoader);
		service.getFileSummary(context.getPintImportConfiguration().getImportID(), context.getSessionID(), file,
				new AsyncCallback<FileSummary>() {

					@Override
					public void onFailure(Throwable caught) {
						removeCell(1, 1); // remove loading image
						setWidget(1, 0, getErrorPanel(caught));

					}

					@Override
					public void onSuccess(FileSummary result) {
						removeCell(1, 1); // remove loading image
						setWidget(1, 0, getFileSummaryPanel(result));
						if (onFileSummaryReceivedTask != null) {
							onFileSummaryReceivedTask.doSomething();
						}
					}
				});
	}

	private FlexTable getErrorPanel(Throwable caught) {
		final FlexTable table = new FlexTable();
		table.setCellPadding(20);
		final Label errorLabel1 = new Label("Error reading input file!");
		errorLabel1.setStyleName(WizardStyles.WizardErrorLabel1);
		table.setWidget(0, 0, errorLabel1);
		final Label errorLabel2 = new Label(caught.getMessage());
		errorLabel1.setStyleName(WizardStyles.WizardErrorLabel1);
		table.setWidget(1, 0, errorLabel2);
		return table;
	}

	private FlexTable getFileSummaryPanel(FileSummary result) {
		final FlexTable table = new FlexTable();
		table.setCellPadding(0);
		//
		int row = 0;
		final Label fileNameName = new Label("File name:");
		fileNameName.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(row, 0, fileNameName);
		final Label fileNameValue = new Label(String.valueOf(result.getFileTypeBean().getName()));
		fileNameValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		table.setWidget(row, 1, fileNameValue);
		//
		row++;
		final Label fileFormatName = new Label("File format:");
		fileFormatName.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(row, 0, fileFormatName);
		final Label fileFormatValue = new Label(String.valueOf(result.getFileTypeBean().getFormat().getName()));
		fileFormatValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		table.setWidget(row, 1, fileFormatValue);
		//
		row++;
		final Label fileSizeName = new Label("File size:");
		fileSizeName.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(row, 0, fileSizeName);
		final Label fileSizeValue = new Label(String.valueOf(result.getFileSizeString()));
		fileSizeValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		table.setWidget(row, 1, fileSizeValue);
		//
		if (result.getFileTypeBean().getFormat() != FileFormat.EXCEL) {
			row++;
			final Label numProteinsName = new Label("Number of proteins:");
			numProteinsName.setStyleName(WizardStyles.WizardInfoMessage);
			table.setWidget(row, 0, numProteinsName);
			final Label numProteinsValue = new Label(
					String.valueOf(parsePositiveNonZeroNumber(result.getNumProteins())));
			numProteinsValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			table.setWidget(row, 1, numProteinsValue);
			//
			row++;
			final Label numPeptidesName = new Label("Number of peptides:");
			numPeptidesName.setStyleName(WizardStyles.WizardInfoMessage);
			table.setWidget(row, 0, numPeptidesName);
			final Label numPeptidesValue = new Label(
					String.valueOf(parsePositiveNonZeroNumber(result.getNumPeptides())));
			numPeptidesValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			table.setWidget(row, 1, numPeptidesValue);
			//
			row++;
			final Label numPSMsName = new Label("Number of PSMs:");
			numPSMsName.setStyleName(WizardStyles.WizardInfoMessage);
			table.setWidget(row, 0, numPSMsName);
			final Label numPSMsValue = new Label(String.valueOf(parsePositiveNonZeroNumber(result.getNumPSMs())));
			numPSMsValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			table.setWidget(row, 1, numPSMsValue);
		}
		//
		row++;
		final Label conditionsLabel = new Label("Experimental conditions:");
		conditionsLabel.setTitle("Experimental conditions from which the data in this input file is coming from");
		conditionsLabel.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(row, 0, conditionsLabel);
		if (associatedConditionsValues == null) {
			associatedConditionsValues = new Label("not associated with any condition yet");
			associatedConditionsValues.setStyleName(WizardStyles.WizardCriticalMessage);
		} else {
			updateAssociatedConditionsLabel();
		}
		table.setWidget(row, 1, associatedConditionsValues);

		if (result.getFileTypeBean().getFormat() == FileFormat.EXCEL) {
			//
			row++;
			final Label numSheets = new Label("Number of Sheets:");
			numSheets.setStyleName(WizardStyles.WizardInfoMessage);
			table.setWidget(row, 0, numSheets);
			final Label numSheetsValue = new Label(String.valueOf(parsePositiveNonZeroNumber(result.getNumSheets())));
			numSheetsValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			table.setWidget(row, 1, numSheetsValue);
			//
			for (final String sheetKeyName : result.getSheetMap().keySet()) {
				if (sheetName != null && !sheetName.equals(sheetKeyName)) {
					continue;
				}
				//
				row++;
				final Label sheetName = new Label("Sheet:");
				sheetName.setStyleName(WizardStyles.WizardInfoMessage);
				table.setWidget(row, 0, sheetName);
				final Label sheetNameValue = new Label("'" + sheetKeyName + "'");
				sheetNameValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
				table.setWidget(row, 1, sheetNameValue);
				//
				row++;
				final Label sheetColumns = new Label("Columns:");
				sheetColumns.setStyleName(WizardStyles.WizardInfoMessage);
				table.setWidget(row, 0, sheetColumns);
				final SheetSummary sheetSummary = result.getSheetMap().get(sheetKeyName);
				final Label sheetColumnsValue = new Label(sheetSummary.toString());
				sheetColumnsValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
				table.setWidget(row, 1, sheetColumnsValue);
			}

		}

		return table;
	}

	private String parsePositiveNonZeroNumber(int number) {
		if (number > 0) {
			return String.valueOf(formatter.format(number));
		} else {
			return "-";
		}
	}

	public void addAssociatedCondition(ExperimentalConditionTypeBean condition) {
		if (!conditions.contains(condition)) {
			this.conditions.add(condition);
			updateAssociatedConditionsLabel();
		}
	}

	public List<ExperimentalConditionTypeBean> getAssociatedconditions() {
		return conditions;
	}

	public void removeAssociatedCondition(ExperimentalConditionTypeBean condition) {
		this.conditions.remove(condition);
		updateAssociatedConditionsLabel();
	}

	private void updateAssociatedConditionsLabel() {
		if (associatedConditionsValues == null) {
			associatedConditionsValues = new Label();
		}
		if (conditions.isEmpty()) {
			associatedConditionsValues.setText("not associated with any condition yet");
			associatedConditionsValues.setStyleName(WizardStyles.WizardCriticalMessage);
		} else {
			final StringBuilder sb = new StringBuilder();
			for (final ExperimentalConditionTypeBean condition : conditions) {
				if (!"".equals(sb.toString())) {
					sb.append(",");
				}
				sb.append(condition.getId());
			}
			associatedConditionsValues.setText(sb.toString());
			associatedConditionsValues.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		}
	}

	public void setOnFileSummaryReceivedTask(DoSomethingTask<Void> onFileSummaryReceivedTask) {
		this.onFileSummaryReceivedTask = onFileSummaryReceivedTask;
	}

	public void showAssociatedConditionsRow(boolean show) {
		if (associatedConditionsValues != null) {
			if (!show) {
				associatedConditionsValues.setText("N/A");
			}
		}
	}
}
