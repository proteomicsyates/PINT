package edu.scripps.yates.client.ui.wizard.pages.panels;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileSummary;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardExtractIdentificationDataFromDTASelectPanel extends FlexTable {
	private final static Image imageLoader = new Image(MyClientBundle.INSTANCE.roundedLoader());
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();
	private boolean extractNSAF;
	private PintContext context;
	private FileTypeBean file;

	public WizardExtractIdentificationDataFromDTASelectPanel(PintContext context, FileTypeBean file,
			boolean createNSAFQuantValues) {
		this.context = context;
		this.file = file;
		this.extractNSAF = createNSAFQuantValues;
		final Label label = new Label("This is what we got from this file...");
		label.setStyleName(WizardStyles.WizardWelcomeLabel2);
		setWidget(0, 0, label);
		setWidget(1, 0, imageLoader);
		context.setExtractNSAF(file.getId(), extractNSAF);
		service.getFileSummary(context.getPintImportConfiguration().getImportID(), context.getSessionID(), file,
				new AsyncCallback<FileSummary>() {

					@Override
					public void onFailure(Throwable caught) {
						setWidget(1, 0, getErrorPanel(caught));
					}

					@Override
					public void onSuccess(FileSummary result) {
						setWidget(1, 0, getFileSummaryPanel(result));
					}
				});
	}

	protected FlexTable getErrorPanel(Throwable caught) {
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

	protected FlexTable getFileSummaryPanel(FileSummary result) {
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
		row++;
		final Label numProteinsName = new Label("Number of proteins:");
		numProteinsName.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(row, 0, numProteinsName);
		final Label numProteinsValue = new Label(String.valueOf(result.getNumProteins()));
		numProteinsValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		table.setWidget(row, 1, numProteinsValue);
		//
		row++;
		final Label numPeptidesName = new Label("Number of peptides:");
		numPeptidesName.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(row, 0, numPeptidesName);
		final Label numPeptidesValue = new Label(String.valueOf(result.getNumPeptides()));
		numPeptidesValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		table.setWidget(row, 1, numPeptidesValue);
		//
		row++;
		final Label numPSMsName = new Label("Number of PSMs:");
		numPSMsName.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(row, 0, numPSMsName);
		final Label numPSMsValue = new Label(String.valueOf(result.getNumPSMs()));
		numPSMsValue.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		table.setWidget(row, 1, numPSMsValue);
		//
		row++;
		final Label extractQuantLabel = new Label("Extract NSAF values:");
		extractQuantLabel.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(row, 0, extractQuantLabel);
		final SimpleCheckBox extractNSAFCheckBox = new SimpleCheckBox();

		extractNSAFCheckBox.setValue(extractNSAF);
		extractNSAFCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				extractNSAF = event.getValue();
				context.setExtractNSAF(file.getId(), extractNSAF);
			}
		});
		table.setWidget(row, 1, extractNSAFCheckBox);
		return table;
	}

}
