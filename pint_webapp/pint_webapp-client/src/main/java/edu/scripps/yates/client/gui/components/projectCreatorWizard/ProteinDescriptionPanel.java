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
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class ProteinDescriptionPanel extends
		ContainsExcelColumnRefPanelAndTable<Pair<String, ProteinDescriptionTypeBean>, ProteinDescriptionTypeBean> {
	private final SimpleCheckBox containsSeveralProteinsCheckBox;
	private final TextBox groupSeparatorTextBox;
	private final RegularExpressionTextBox regularExpression;

	public ProteinDescriptionPanel(FileTypeBean excelFileBean, ProteinDescriptionTypeBean proteinDescriptionTypeBean) {
		super(excelFileBean, proteinDescriptionTypeBean);
		FlexTable flexTable = new FlexTable();
		flexTable.setCellPadding(5);
		mainPanel.add(flexTable);

		Label lblSelectExcelColumn = new Label("Select Excel column:");
		flexTable.setWidget(0, 0, lblSelectExcelColumn);

		flexTable.setWidget(1, 0, getExcelColumnRefPanel());

		Label lblProteinAccession = new Label("Protein description");
		flexTable.setWidget(2, 0, lblProteinAccession);

		Label lblRegularExpression = new Label("Regular expression:");
		flexTable.setWidget(3, 0, lblRegularExpression);

		regularExpression = new RegularExpressionTextBox(".*", 1);
		reloadTableIfChange(regularExpression.getSuggestBox());
		flexTable.setWidget(3, 1, regularExpression);
		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);

		Label lblNewLabel = new Label("Contains several proteins:");
		final String title1 = "Check this option if more than one protein accession is present in each one single Excel cell";
		lblNewLabel.setTitle(title1);
		flexTable.setWidget(4, 0, lblNewLabel);

		containsSeveralProteinsCheckBox = new SimpleCheckBox();
		reloadTableIfChange(containsSeveralProteinsCheckBox);
		containsSeveralProteinsCheckBox.setTitle(title1);
		flexTable.setWidget(4, 1, containsSeveralProteinsCheckBox);

		Label lblGroupSeparator = new Label("Group separator:");
		flexTable.setWidget(5, 0, lblGroupSeparator);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		groupSeparatorTextBox = new TextBox();
		reloadTableIfChange(groupSeparatorTextBox);
		groupSeparatorTextBox.setEnabled(false);
		groupSeparatorTextBox.setAlignment(TextAlignment.RIGHT);
		groupSeparatorTextBox.setVisibleLength(3);
		groupSeparatorTextBox.setText(",");
		flexTable.setWidget(5, 1, groupSeparatorTextBox);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);

		// handlers
		containsSeveralProteinsCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				groupSeparatorTextBox.setEnabled(event.getValue());
			}
		});
		reloadTableIfChange(containsSeveralProteinsCheckBox);

		updateGUIFromObjectData();
	}

	@Override
	public List<CustomColumn<Pair<String, ProteinDescriptionTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, ProteinDescriptionTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, ProteinDescriptionTypeBean>>>();
		// protein description
		CustomColumn<Pair<String, ProteinDescriptionTypeBean>> proteinDescriptionColumn = new CustomColumn<Pair<String, ProteinDescriptionTypeBean>>(
				"Protein description") {
			@Override
			public String getValue(Pair<String, ProteinDescriptionTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				final ProteinDescriptionTypeBean proteinDescription = pair.getSecondElement();
				if (proteinDescription != null) {
					if (proteinDescription.getRegexp() != null) {
						GWT.log("Regular expression for protein description: " + proteinDescription.getRegexp());
						RegExp regexp = null;
						if (proteinDescription.getRegexp() != null && !"".equals(proteinDescription.getRegexp())) {
							try {
								regexp = RegExp.compile(proteinDescription.getRegexp());
							} catch (RuntimeException e) {
								GWT.log(e.getMessage());
							}
						}
						List<String> rawValues = new ArrayList<String>();
						if (proteinDescription.isGroups() && proteinDescription.getGroupSeparator() != null
								&& !"".equals(proteinDescription.getGroupSeparator())) {
							// separate into groups
							final String[] split = rawValue.split(proteinDescription.getGroupSeparator());
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
				}
				return null;
			}
		};
		ret.add(proteinDescriptionColumn);
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
		final Boolean containsGroups = containsSeveralProteinsCheckBox.getValue();
		if (containsGroups != null) {
			representedObject.setGroups(containsGroups);
			if (containsGroups) {
				representedObject.setGroupSeparator(groupSeparatorTextBox.getValue());
			}
		}
		if (!"".equals(regularExpression.getCurrentRegularExpressionText())) {
			representedObject.setRegexp(regularExpression.getCurrentRegularExpressionText());
		} else {
			representedObject.setRegexp(".*");

		}

	}

	@Override
	public boolean isValid() {
		updateRepresentedObject();
		if (representedObject.getColumnRef() == null)
			return false;
		if (representedObject.getRegexp() != null && !"".equals(representedObject.getRegexp())
				&& !regularExpression.checkRegularExpression())
			return false;

		if (representedObject.isGroups()) {
			if (representedObject.getGroupSeparator() == null || "".equals(representedObject.getGroupSeparator()))
				return false;
		}
		return true;
	}

	@Override
	public ProteinDescriptionTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(ProteinDescriptionTypeBean dataObject) {
		super.setRepresentedObject(dataObject);
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
				groupSeparatorTextBox.setValue(representedObject.getGroupSeparator());
			} else {
				groupSeparatorTextBox.setValue("");
			}
		} else {

		}

	}

}