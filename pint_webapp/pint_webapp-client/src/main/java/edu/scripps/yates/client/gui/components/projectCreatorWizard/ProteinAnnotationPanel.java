package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class ProteinAnnotationPanel extends
		ContainsExcelColumnRefPanelAndTable<Pair<String, ProteinAnnotationTypeBean>, ProteinAnnotationTypeBean> {
	private TextBox annotationNameTextBox;
	private final SimpleCheckBox isBinaryCheckBox;
	private final TextBox yesValueTextBox;

	public ProteinAnnotationPanel(FileTypeBean excelFileBean, ProteinAnnotationTypeBean proteinAnnotationTypeBean) {
		super(excelFileBean, proteinAnnotationTypeBean);
		FlexTable flexTable = new FlexTable();
		flexTable.setCellPadding(5);
		mainPanel.add(flexTable);

		Label lblSelectExcelColumn = new Label("Select Excel column:");
		flexTable.setWidget(0, 0, lblSelectExcelColumn);
		flexTable.setWidget(1, 0, getExcelColumnRefPanel());

		Label lblProteinAccession = new Label("Protein annotation");
		flexTable.setWidget(2, 0, lblProteinAccession);

		Label lblGroupSeparator = new Label("Annotation name:");
		flexTable.setWidget(3, 0, lblGroupSeparator);
		if (annotationNameTextBox == null) {
			annotationNameTextBox = new TextBox();
			annotationNameTextBox.setText("My annotation");
		}
		annotationNameTextBox.setAlignment(TextAlignment.LEFT);
		reloadTableIfChange(annotationNameTextBox);
		flexTable.setWidget(3, 1, annotationNameTextBox);

		Label lblType = new Label("Is binary:");
		flexTable.setWidget(4, 0, lblType);
		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);

		isBinaryCheckBox = new SimpleCheckBox();
		reloadTableIfChange(isBinaryCheckBox);
		flexTable.setWidget(4, 1, isBinaryCheckBox);

		Label lblyesValue = new Label("\"Yes\" value:");
		flexTable.setWidget(5, 0, lblyesValue);

		yesValueTextBox = new TextBox();
		yesValueTextBox.setEnabled(false);
		reloadTableIfChange(yesValueTextBox);
		flexTable.setWidget(5, 1, yesValueTextBox);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1, HasHorizontalAlignment.ALIGN_LEFT);

		addHandlers();

		if (proteinAnnotationTypeBean != null) {
			updateGUIFromObjectData();
		} else {
			updateRepresentedObject();
		}
		if ("".equals(annotationNameTextBox.getText())) {
			annotationNameTextBox.setText("My annotation");
		}
	}

	private void addHandlers() {
		// if checkbox is selected, enable the yesvaluetextbox
		isBinaryCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				final Boolean value = event.getValue();
				if (value != null) {
					yesValueTextBox.setEnabled(value);
				}

			}
		});
		// if name is typed in the annotation name, change the column header
		// name
		annotationNameTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				getTable().redrawHeaders();
			}
		});
	}

	@Override
	public List<CustomColumn<Pair<String, ProteinAnnotationTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, ProteinAnnotationTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, ProteinAnnotationTypeBean>>>();

		CustomColumn<Pair<String, ProteinAnnotationTypeBean>> proteinAccColumn = new CustomColumn<Pair<String, ProteinAnnotationTypeBean>>(
				"Protein annotation") {
			@Override
			public String getValue(Pair<String, ProteinAnnotationTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				final ProteinAnnotationTypeBean proteinAnnotationType = pair.getSecondElement();
				if (proteinAnnotationType != null) {
					// TODO
					return rawValue;
				}
				return null;
			}
		};
		ret.add(proteinAccColumn);

		CustomColumn<Pair<String, ProteinAnnotationTypeBean>> containsAnnotationColumn = new CustomColumn<Pair<String, ProteinAnnotationTypeBean>>(
				"Contains annotation?") {
			@Override
			public String getValue(Pair<String, ProteinAnnotationTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				final ProteinAnnotationTypeBean proteinAnnotationType = pair.getSecondElement();
				if (proteinAnnotationType != null) {
					if (proteinAnnotationType.isBinary()) {
						if (proteinAnnotationType.getYesValue().equalsIgnoreCase(rawValue)) {
							return proteinAnnotationType.getName() + "=YES";
						} else {
							return proteinAnnotationType.getName() + "=NO";
						}
					} else {
						if (rawValue != null && !"".equals(rawValue))
							return proteinAnnotationType.getName() + "=YES";
						else
							return "-";
					}

				}
				return null;
			}
		};
		ret.add(containsAnnotationColumn);
		return ret;
	}

	@Override
	public List<Header<String>> getInitHeaders() {
		List<Header<String>> ret = new ArrayList<Header<String>>();
		if (annotationNameTextBox == null) {
			annotationNameTextBox = new TextBox();
			annotationNameTextBox.setText("My annotation");
		}

		ret.add(new MyHeader(annotationNameTextBox));

		return ret;
	}

	@Override
	public void updateRepresentedObject() {
		representedObject.setBinary(isBinaryCheckBox.getValue());

		representedObject.setColumnRef(getExcelColumnRefPanel().getColumnRef());
		if (isBinaryCheckBox.getValue())
			representedObject.setYesValue(yesValueTextBox.getText());
		else
			representedObject.setYesValue("");
		representedObject.setName(annotationNameTextBox.getText());

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
	public ProteinAnnotationTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(ProteinAnnotationTypeBean dataObject) {
		setRepresentedObject(dataObject);
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {
		if (representedObject != null) {
			annotationNameTextBox.setValue(representedObject.getName());
			isBinaryCheckBox.setValue(representedObject.isBinary());
			yesValueTextBox.setValue(representedObject.getYesValue());
			getExcelColumnRefPanel().selectExcelColumn(representedObject.getColumnRef());
		} else {
			yesValueTextBox.setValue("");
			annotationNameTextBox.setValue("");
		}
	}
}
