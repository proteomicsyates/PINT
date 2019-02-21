package edu.scripps.yates.client.ui.wizard.pages.widgets.excel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.pages.widgets.NewExcelReferenceWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ExtendedTextBox;
import edu.scripps.yates.client.util.TextChangeEvent;
import edu.scripps.yates.client.util.TextChangeEventHandler;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean;

public class ProteinAccessionPanel extends ExcelObjectPanel<ProteinAccessionTypeBean> {
	private NewExcelReferenceWidget proteinAccessionSelector;
	private CheckBox containsMultipleAccessions;
	private int rowForSeparator;
	private ExtendedTextBox separatorForMultipleAccessions;

	public ProteinAccessionPanel(PintContext context, FileTypeBean file, String excelSheet,
			ProteinAccessionTypeBean proteinAccession) {
		super(context, file, excelSheet, proteinAccession);
	}

	@Override
	protected void init() {
		int row = 0;

		final Label label00 = new Label("Proteins in Excel file:");
		label00.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(row, 0, label00);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		//
		row++;
		final Label label0 = new Label("Column for protein accession:");
		label0.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label0);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		proteinAccessionSelector = new NewExcelReferenceWidget(file, getExcelSheet());
		setWidget(row, 1, proteinAccessionSelector);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);

		proteinAccessionSelector.addExcelSheetsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// if sheet is changed, the column ref will be null until selected it
				object.setColumnRef(null);

			}
		});
		proteinAccessionSelector.addExcelColumnsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// set the column ref of the ratio score
				final String columnRef = proteinAccessionSelector.getColumnRef();
				object.setColumnRef(columnRef);

			}
		});
		//
		row++;

		containsMultipleAccessions = new CheckBox("Does the column contain multiple protein accessions?");
		containsMultipleAccessions.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, containsMultipleAccessions);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		containsMultipleAccessions.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				getFlexCellFormatter().setVisible(rowForSeparator, 0, event.getValue());
				getFlexCellFormatter().setVisible(rowForSeparator, 1, event.getValue());
				// update object
				object.setGroups(event.getValue());
				if (event.getValue()) {
					object.setGroupSeparator(separatorForMultipleAccessions.getValue());
					object.setRegexp(".*");
				} else {
					object.setGroupSeparator(null);
					object.setRegexp(null);
				}
			}
		});

		//
		row++;
		final Label label1 = new Label("Separator character between accessions:");
		label1.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, label1);
		rowForSeparator = row;
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		separatorForMultipleAccessions = new ExtendedTextBox();
		setWidget(row, 1, separatorForMultipleAccessions);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		separatorForMultipleAccessions.addTextChangeEventHandler(new TextChangeEventHandler() {

			@Override
			public void onTextChange(TextChangeEvent event) {
				object.setGroupSeparator(separatorForMultipleAccessions.getValue());
			}
		});
		//
		updateGUIFromContext();
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
		this.proteinAccessionSelector.selectExcelColumn(object.getColumnRef());
		if (object.isGroups()) {
			containsMultipleAccessions.setValue(true, true);
			separatorForMultipleAccessions.setValue(object.getGroupSeparator());
		} else {
			containsMultipleAccessions.setValue(false, true);
			separatorForMultipleAccessions.setValue(null);
		}
	}

	@Override
	public ProteinAccessionTypeBean getObject() {
		object.setRegexp(".*");
		return super.getObject();
	}

}
