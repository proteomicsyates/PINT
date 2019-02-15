package edu.scripps.yates.client.ui.wizard.pages.widgets.excel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.pages.widgets.NewExcelReferenceWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean;

public class PeptideSequencePanel extends ExcelObjectPanel<SequenceTypeBean> {
	private NewExcelReferenceWidget peptideSequenceSelector;

	public PeptideSequencePanel(PintContext context, FileTypeBean file, String excelSheet,
			SequenceTypeBean peptideSequence) {
		super(context, file, excelSheet, peptideSequence);
	}

	@Override
	protected void init() {
		int row = 0;

		final Label label00 = new Label("Peptides in Excel file:");
		label00.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(row, 0, label00);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		//
		row++;
		final Label label01 = new Label("You also need to define a column with the protein accession(s)");
		label01.setTitle("Peptides need to be associated with proteins in order to be incorporated in the system");
		label01.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(row, 0, label01);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		//
		row++;
		final Label label0 = new Label("Column for peptide sequence:");
		label0.setTitle(
				"The column for peptide sequences can contain peptide sequences with PTMs encoded on it, as well as PRE and POST aminoacids");
		label0.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label0);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		peptideSequenceSelector = new NewExcelReferenceWidget(file, getExcelSheet());
		setWidget(row, 1, peptideSequenceSelector);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);

		peptideSequenceSelector.addExcelSheetsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// if sheet is changed, the column ref will be null until selected it
				object.setColumnRef(null);

			}
		});
		peptideSequenceSelector.addExcelColumnsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// set the column ref of the ratio score
				final String columnRef = peptideSequenceSelector.getColumnRef();
				object.setColumnRef(columnRef);

			}
		});

	}

	@Override
	public boolean isReady() {
		if (isEmpty(object.getColumnRef())) {
			return false;
		}

		return true;
	}

	@Override
	protected void updateGUIFromContext() {
		this.peptideSequenceSelector.selectExcelColumn(object.getColumnRef());
	}

}
