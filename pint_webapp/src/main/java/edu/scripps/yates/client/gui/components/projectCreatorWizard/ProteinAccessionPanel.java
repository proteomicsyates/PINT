package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class ProteinAccessionPanel
		extends ContainsExcelColumnRefPanelAndTable<Pair<String, ProteinAccessionTypeBean>, ProteinAccessionTypeBean> {
	private final SimpleCheckBox containsSeveralProteinsCheckBox;
	private final TextBox groupSeparatorTextBox;
	private final RegularExpressionTextBox regularExpression;

	public ProteinAccessionPanel(FileTypeBean excelFileBean, ProteinAccessionTypeBean proteinAccessionTypeBean) {
		super(excelFileBean, proteinAccessionTypeBean);
		mainPanel.setHeight("407px");
		FlexTable flexTable = new FlexTable();
		flexTable.setCellPadding(5);
		mainPanel.add(flexTable);

		Label lblSelectExcelColumn = new Label("Select Excel column:");
		flexTable.setWidget(0, 0, lblSelectExcelColumn);
		flexTable.setWidget(1, 0, getExcelColumnRefPanel());

		Label lblProteinAccession = new Label("Protein accession");
		flexTable.setWidget(2, 0, lblProteinAccession);

		Label lblRegularExpression = new Label("Regular expression:");
		flexTable.setWidget(3, 0, lblRegularExpression);

		regularExpression = new RegularExpressionTextBox(".*", 1);
		reloadTableIfChange(regularExpression.getSuggestBox());
		flexTable.setWidget(3, 1, regularExpression);
		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
		final String title1 = "Check this option if more than one protein accession is present in each one single Excel cell";

		Label lblNewLabel = new Label("Contains several proteins:");
		lblNewLabel.setTitle(title1);
		flexTable.setWidget(4, 0, lblNewLabel);

		containsSeveralProteinsCheckBox = new SimpleCheckBox();
		containsSeveralProteinsCheckBox.setTitle(title1);
		flexTable.setWidget(4, 1, containsSeveralProteinsCheckBox);
		reloadTableIfChange(containsSeveralProteinsCheckBox);

		// handlers
		containsSeveralProteinsCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				groupSeparatorTextBox.setEnabled(event.getValue());
			}
		});
		reloadTableIfChange(containsSeveralProteinsCheckBox);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		Label lblGroupSeparator = new Label("Group separator:");
		flexTable.setWidget(5, 0, lblGroupSeparator);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);

		groupSeparatorTextBox = new TextBox();
		groupSeparatorTextBox.setEnabled(false);
		groupSeparatorTextBox.setAlignment(TextAlignment.RIGHT);
		groupSeparatorTextBox.setVisibleLength(3);
		groupSeparatorTextBox.setText(",");
		reloadTableIfChange(groupSeparatorTextBox);

		flexTable.setWidget(5, 1, groupSeparatorTextBox);

		updateGUIFromObjectData();
	}

	@Override
	public void updateRepresentedObject() {
		final String columnRef = getExcelColumnRefPanel().getColumnRef();
		if (representedObject == null) {
			representedObject = new ProteinAccessionTypeBean();
		}
		representedObject.setColumnRef(columnRef);
		final Boolean containsGroups = containsSeveralProteinsCheckBox.getValue();
		if (containsGroups != null) {
			representedObject.setGroups(containsGroups);
			if (containsGroups) {
				representedObject.setGroupSeparator(groupSeparatorTextBox.getValue());
			}
		}

		final String currentRegularExpressionText = regularExpression.getCurrentRegularExpressionText();

		if (!"".equals(currentRegularExpressionText))
			representedObject.setRegexp(currentRegularExpressionText);
		else
			representedObject.setRegexp(".*");
	}

	@Override
	public List<CustomColumn<Pair<String, ProteinAccessionTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, ProteinAccessionTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, ProteinAccessionTypeBean>>>();

		CustomColumn<Pair<String, ProteinAccessionTypeBean>> proteinAccColumn = new CustomColumn<Pair<String, ProteinAccessionTypeBean>>(
				"Protein accession") {
			@Override
			public String getValue(Pair<String, ProteinAccessionTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				final ProteinAccessionTypeBean proteinAccessionType = pair.getSecondElement();
				if (proteinAccessionType != null) {
					RegExp regexp = null;
					if (proteinAccessionType.getRegexp() != null && !"".equals(proteinAccessionType.getRegexp())) {
						GWT.log("Regular expression for accessions: " + proteinAccessionType.getRegexp());
						try {
							regexp = RegExp.compile(proteinAccessionType.getRegexp());
						} catch (RuntimeException e) {
							GWT.log(e.getMessage());
						}
					}
					List<String> rawValues = new ArrayList<String>();
					if (proteinAccessionType.isGroups() && proteinAccessionType.getGroupSeparator() != null
							&& !"".equals(proteinAccessionType.getGroupSeparator())) {
						// separate into groups
						final String[] split = rawValue.split(proteinAccessionType.getGroupSeparator());
						for (String string : split) {
							rawValues.add(string);
						}
					} else {
						rawValues.add(rawValue);
					}
					// iterate rawValues
					StringBuilder sb = new StringBuilder();
					for (String rawValue2 : rawValues) {
						if (!"".equals(sb.toString()))
							sb.append("\n");
						if (regexp != null) {
							final MatchResult matcher = regexp.exec(rawValue2);
							if (matcher != null) {
								if (matcher.getGroupCount() > 1) {
									sb.append(matcher.getGroup(1));
								} else {
									sb.append(matcher.getGroup(0));
								}
							} else {
								sb.append("NOT MATCH");
							}
						} else {
							sb.append(rawValue2);
						}
					}
					return sb.toString();

				}
				return null;
			}
		};
		ret.add(proteinAccColumn);
		return ret;
	}

	@Override
	public List<Header<String>> getInitHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid() {
		updateRepresentedObject();
		if (isNullOrEmpty(representedObject.getColumnRef()))
			return false;

		return true;
	}

	private boolean isNullOrEmpty(String string) {
		if (string == null || "".equals(string))
			return true;
		return false;
	}

	@Override
	public ProteinAccessionTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(ProteinAccessionTypeBean dataObject) {
		representedObject = dataObject;
		setRepresentedObject(dataObject);
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {
		if (representedObject != null) {
			getExcelColumnRefPanel().selectExcelColumn(representedObject.getColumnRef());
			// regexp
			regularExpression.setText(representedObject.getRegexp());

			// contains groups
			containsSeveralProteinsCheckBox.setValue(representedObject.isGroups());
			if (representedObject.isGroups()) {
				// group separator
				groupSeparatorTextBox.setValue(representedObject.getGroupSeparator());
			} else {
				groupSeparatorTextBox.setValue("");
			}
		} else {
			regularExpression.setText("");
			groupSeparatorTextBox.setValue("");
			containsSeveralProteinsCheckBox.setValue(false);
		}
	}

}