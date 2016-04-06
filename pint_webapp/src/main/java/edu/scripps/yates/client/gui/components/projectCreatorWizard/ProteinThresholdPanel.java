package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.widgetideas.graphics.client.Color;

import edu.scripps.yates.client.util.Pair;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean;

public class ProteinThresholdPanel
		extends ContainsExcelColumnRefPanelAndTable<Pair<String, ProteinThresholdTypeBean>, ProteinThresholdTypeBean> {
	private TextBox thresholdNameTextBox;
	private final TextBox yesValueTextBox;
	private final TextArea descriptionTextArea;
	private CustomColumn<Pair<String, ProteinThresholdTypeBean>> passThresholdColumn;

	public ProteinThresholdPanel(FileTypeBean excelFileBean, ProteinThresholdTypeBean proteinThresholdTypeBean) {
		super(excelFileBean, proteinThresholdTypeBean);
		FlexTable flexTable = new FlexTable();
		flexTable.setCellPadding(5);
		mainPanel.add(flexTable);

		Label lblSelectExcelColumn = new Label("Select Excel column:");
		flexTable.setWidget(0, 0, lblSelectExcelColumn);
		flexTable.setWidget(1, 0, getExcelColumnRefPanel());

		Label lblProteinAccession = new Label("Protein threshold");
		flexTable.setWidget(2, 0, lblProteinAccession);

		Label lblGroupSeparator = new Label("Threshold name:");
		flexTable.setWidget(3, 0, lblGroupSeparator);
		if (thresholdNameTextBox == null) {
			thresholdNameTextBox = new TextBox();
			thresholdNameTextBox.setText("My threshold");
		}
		thresholdNameTextBox.setAlignment(TextAlignment.LEFT);
		reloadTableIfChange(thresholdNameTextBox);
		flexTable.setWidget(3, 1, thresholdNameTextBox);

		Label lblType = new Label("Threshold description:");
		flexTable.setWidget(4, 0, lblType);
		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);

		descriptionTextArea = new TextArea();
		flexTable.setWidget(4, 1, descriptionTextArea);

		Label lblyesValue = new Label("\"Yes\" value:");
		flexTable.setWidget(5, 0, lblyesValue);

		yesValueTextBox = new TextBox();
		yesValueTextBox.setValue("true");
		reloadTableIfChange(yesValueTextBox);
		flexTable.setWidget(5, 1, yesValueTextBox);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setVerticalAlignment(4, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setVerticalAlignment(5, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setVerticalAlignment(5, 1, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setVerticalAlignment(4, 1, HasVerticalAlignment.ALIGN_TOP);

		addHandlers();

		if (proteinThresholdTypeBean != null) {
			updateGUIFromObjectData();
		} else {
			updateRepresentedObject();
		}
		if ("".equals(thresholdNameTextBox.getText())) {
			thresholdNameTextBox.setText("My threshold");
		}
	}

	private void addHandlers() {
		// if name is typed in the threshold name, change the column header
		// name
		thresholdNameTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				getTable().redrawHeaders();
			}
		});

	}

	@Override
	public List<CustomColumn<Pair<String, ProteinThresholdTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, ProteinThresholdTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, ProteinThresholdTypeBean>>>();

		CustomColumn<Pair<String, ProteinThresholdTypeBean>> proteinAccColumn = new CustomColumn<Pair<String, ProteinThresholdTypeBean>>(
				"Protein threshold") {
			@Override
			public String getValue(Pair<String, ProteinThresholdTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				final ProteinThresholdTypeBean proteinThresholdType = pair.getSecondElement();
				if (proteinThresholdType != null) {
					// TODO
					return rawValue;
				}
				return null;
			}
		};
		ret.add(proteinAccColumn);

		passThresholdColumn = new CustomColumn<Pair<String, ProteinThresholdTypeBean>>("Pass threshold?") {
			@Override
			public String getValue(Pair<String, ProteinThresholdTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				final ProteinThresholdTypeBean proteinThresholdType = pair.getSecondElement();
				if (proteinThresholdType != null) {
					if (proteinThresholdType.getYesValue().equalsIgnoreCase(rawValue)) {
						return "YES";
					} else {
						return "NO";
					}

				}
				return null;
			}
		};
		// any value will be displayed in RED
		passThresholdColumn.addCustomColorCell("NO", Color.RED);
		passThresholdColumn.addCustomColorCell("YES", Color.GREEN);
		ret.add(passThresholdColumn);
		return ret;
	}

	@Override
	public List<Header<String>> getInitHeaders() {
		List<Header<String>> ret = new ArrayList<Header<String>>();
		if (thresholdNameTextBox == null) {
			thresholdNameTextBox = new TextBox();
			thresholdNameTextBox.setText("My threshold");
		}

		ret.add(new MyHeader(thresholdNameTextBox));

		return ret;
	}

	@Override
	public void updateRepresentedObject() {
		representedObject.setColumnRef(getExcelColumnRefPanel().getColumnRef());
		representedObject.setYesValue(yesValueTextBox.getValue());
		representedObject.setName(thresholdNameTextBox.getValue());
		representedObject.setDescription(descriptionTextArea.getValue());
	}

	@Override
	public boolean isValid() {
		updateRepresentedObject();
		if (representedObject.getColumnRef() == null)
			return false;
		if (representedObject.getName() == null || "".equals(representedObject))
			return false;
		return true;
	}

	@Override
	public ProteinThresholdTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(ProteinThresholdTypeBean dataObject) {
		setRepresentedObject(dataObject);
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {
		if (representedObject != null) {
			thresholdNameTextBox.setValue(representedObject.getName());
			yesValueTextBox.setValue(representedObject.getYesValue());
			descriptionTextArea.setValue(representedObject.getDescription());
			getExcelColumnRefPanel().selectExcelColumn(representedObject.getColumnRef());
		} else {
			yesValueTextBox.setValue("");
			thresholdNameTextBox.setValue("");
			descriptionTextArea.setValue("");
		}
	}
}
