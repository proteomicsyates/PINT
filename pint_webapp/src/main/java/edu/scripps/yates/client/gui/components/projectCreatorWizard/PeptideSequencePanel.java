package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class PeptideSequencePanel extends
		ContainsExcelColumnRefPanelAndTable<Pair<String, SequenceTypeBean>, SequenceTypeBean> {

	public PeptideSequencePanel(FileTypeBean excelFileBean, SequenceTypeBean peptideSequenceTypeBean) {
		super(excelFileBean, peptideSequenceTypeBean);
		FlexTable flexTable = new FlexTable();
		flexTable.setCellPadding(5);
		mainPanel.add(flexTable);

		Label lblSelectExcelColumn = new Label("Select Excel column:");
		flexTable.setWidget(0, 0, lblSelectExcelColumn);
		flexTable.setWidget(1, 0, getExcelColumnRefPanel());

		String tableHeader = "";
		Label lblProteinAccession = new Label(tableHeader);
		flexTable.setWidget(2, 0, lblProteinAccession);

		TextBox scoreNameTextBox = null;
		if (scoreNameTextBox == null)
			scoreNameTextBox = new TextBox();
		reloadTableIfChange(scoreNameTextBox);
		scoreNameTextBox.setAlignment(TextAlignment.LEFT);

		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);

		updateGUIFromObjectData();
	}

	@Override
	public List<CustomColumn<Pair<String, SequenceTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, SequenceTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, SequenceTypeBean>>>();
		// peptide sequence
		CustomColumn<Pair<String, SequenceTypeBean>> peptideSequenceColumn = new CustomColumn<Pair<String, SequenceTypeBean>>(
				"Peptide sequence") {
			@Override
			public String getValue(Pair<String, SequenceTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				return rawValue;
			}
		};
		ret.add(peptideSequenceColumn);
		return ret;
	}

	@Override
	public List<Header<String>> getInitHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRepresentedObject() {

		representedObject.setColumnRef(getExcelColumnRefPanel().getColumnRef());

	}

	@Override
	public boolean isValid() {
		updateRepresentedObject();
		if (representedObject.getColumnRef() == null)
			return false;
		return true;
	}

	@Override
	public SequenceTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(SequenceTypeBean dataObject) {
		representedObject = dataObject;
		super.setRepresentedObject(dataObject);
		updateGUIFromObjectData();

	}

	@Override
	public void updateGUIFromObjectData() {
		if (representedObject != null) {
			getExcelColumnRefPanel().selectExcelColumn(representedObject.getColumnRef());
		}
	}
}
