package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;

import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.projectCreator.excel.AmountCombinationTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class AmountPanel extends ContainsExcelColumnRefPanelAndTable<Pair<String, AmountTypeBean>, AmountTypeBean> {
	private final ListBox combinationTypeComboBox;
	private final SimpleCheckBox isACombinationCheckBox;
	private final ListBox amountTypeComboBox;

	public AmountPanel(FileTypeBean excelFileBean, String headerName, AmountTypeBean amountType) {
		super(excelFileBean, amountType);
		FlexTable flexTable = new FlexTable();
		flexTable.setCellPadding(5);
		mainPanel.add(flexTable);

		Label lblSelectExcelColumn = new Label("Select Excel column:");
		flexTable.setWidget(0, 0, lblSelectExcelColumn);
		flexTable.setWidget(1, 0, getExcelColumnRefPanel());

		Label lblHeader = new Label(headerName);
		flexTable.setWidget(2, 0, lblHeader);

		Label lblRegularExpression = new Label("Amount type:");
		flexTable.setWidget(3, 0, lblRegularExpression);

		amountTypeComboBox = new ListBox();
		flexTable.setWidget(3, 1, amountTypeComboBox);

		Label lblType = new Label("Is a combination of other amount values:");
		flexTable.setWidget(4, 0, lblType);
		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1, HasHorizontalAlignment.ALIGN_LEFT);

		Label lblNewLabel = new Label("Combination type:");
		final String title1 = "Check this option if more than one protein accession is present in each one single Excel cell";

		isACombinationCheckBox = new SimpleCheckBox();
		isACombinationCheckBox.setTitle(title1);
		flexTable.setWidget(4, 1, isACombinationCheckBox);

		flexTable.setWidget(5, 0, lblNewLabel);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		combinationTypeComboBox = new ListBox();
		combinationTypeComboBox.setEnabled(false);
		flexTable.setWidget(5, 1, combinationTypeComboBox);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);

		addHandlers();
		initCombos();
		updateGUIFromObjectData();
	}

	private void initCombos() {
		// amount type combo
		final AmountType[] values = AmountType.values();
		for (AmountType amountType : values) {
			amountTypeComboBox.addItem(amountType.getDescription(), amountType.name());
		}

		// combination type combo
		final AmountCombinationTypeBean[] values2 = AmountCombinationTypeBean.values();
		for (AmountCombinationTypeBean amountcombinationType : values2) {
			switch (amountcombinationType) {
			case AVERAGE:
				combinationTypeComboBox.addItem("Averaging", amountcombinationType.name());
				break;
			case SUM:
				combinationTypeComboBox.addItem("Summing", amountcombinationType.name());
				break;
			case NO_COMBINATION:
				combinationTypeComboBox.addItem("No combination", amountcombinationType.name());
			default:
				break;
			}
		}

	}

	public void addCombinationTypeChangeHandler(ChangeHandler handler) {
		combinationTypeComboBox.addChangeHandler(handler);
	}

	public void addAmountTypeChangeHandler(ChangeHandler handler) {
		amountTypeComboBox.addChangeHandler(handler);
	}

	public void addIsACombinationCheckBox(ValueChangeHandler<Boolean> handler) {
		isACombinationCheckBox.addValueChangeHandler(handler);
	}

	private void addHandlers() {
		// handlers
		isACombinationCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				combinationTypeComboBox.setEnabled(event.getValue());
			}
		});
	}

	private AmountCombinationTypeBean getCombinationType() {
		if (combinationTypeComboBox.getSelectedIndex() >= 0) {
			final AmountCombinationTypeBean fromValue = AmountCombinationTypeBean
					.fromValue(combinationTypeComboBox.getValue(combinationTypeComboBox.getSelectedIndex()));
			return fromValue;
		}
		return null;
	}

	private AmountType getAmountType() {
		if (amountTypeComboBox.getSelectedIndex() >= 0) {
			final AmountType fromValue = AmountType
					.fromValue(amountTypeComboBox.getValue(amountTypeComboBox.getSelectedIndex()));
			return fromValue;
		}
		return null;
	}

	@Override
	public List<CustomColumn<Pair<String, AmountTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, AmountTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, AmountTypeBean>>>();

		CustomColumn<Pair<String, AmountTypeBean>> proteinAmountColumn = new CustomColumn<Pair<String, AmountTypeBean>>(
				"Amount value") {
			@Override
			public String getValue(Pair<String, AmountTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				return rawValue;
			}
		};
		ret.add(proteinAmountColumn);

		CustomColumn<Pair<String, AmountTypeBean>> amountTypeColumn = new CustomColumn<Pair<String, AmountTypeBean>>(
				"Amount type") {
			@Override
			public String getValue(Pair<String, AmountTypeBean> pair) {
				final AmountTypeBean proteinAmountType = pair.getSecondElement();

				if (proteinAmountType != null) {
					if (proteinAmountType.getType() != null) {
						return proteinAmountType.getType().name();
					}
				}
				return null;
			}
		};
		ret.add(amountTypeColumn);

		CustomColumn<Pair<String, AmountTypeBean>> isCombinationColumn = new CustomColumn<Pair<String, AmountTypeBean>>(
				"Is a combination of amount values?") {
			@Override
			public String getValue(Pair<String, AmountTypeBean> pair) {
				final AmountTypeBean proteinAmountType = pair.getSecondElement();

				if (proteinAmountType != null) {
					if (proteinAmountType.getCombinationType() != null) {
						return proteinAmountType.getCombinationType().name();
					}
				}
				return null;
			}
		};
		ret.add(isCombinationColumn);
		return ret;
	}

	@Override
	public List<Header<String>> getInitHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRepresentedObject() {
		final String columnRef = getExcelColumnRefPanel().getColumnRef();

		representedObject.setColumnRef(columnRef);
		final Boolean isCombination = isACombinationCheckBox.getValue();
		if (isCombination != null && isCombination) {
			representedObject.setCombinationType(getCombinationType());
		} else {
			representedObject.setCombinationType(AmountCombinationTypeBean.NO_COMBINATION);
		}

		final AmountType amountType = getAmountType();
		representedObject.setType(amountType);
	}

	@Override
	public boolean isValid() {
		updateRepresentedObject();
		if (representedObject.getColumnRef() == null)
			return false;
		if (representedObject.getCombinationType() == null)
			return false;
		if (representedObject.getType() == null)
			return false;
		return true;
	}

	@Override
	public AmountTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(AmountTypeBean dataObject) {
		representedObject = dataObject;
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {
		if (representedObject != null) {
			getExcelColumnRefPanel().selectExcelColumn(representedObject.getColumnRef());
			isACombinationCheckBox.setValue(representedObject.getCombinationType() != null
					&& representedObject.getCombinationType() != AmountCombinationTypeBean.NO_COMBINATION);
			if (representedObject.getCombinationType() != null) {
				ProjectCreatorWizardUtil.selectInComboByValue(combinationTypeComboBox,
						representedObject.getCombinationType().name());
				combinationTypeComboBox.setEnabled(true);
			}
			if (representedObject.getType() != null) {
				ProjectCreatorWizardUtil.selectInComboByValue(amountTypeComboBox, representedObject.getType().name());
				combinationTypeComboBox.setEnabled(false);
			}

		} else {
			isACombinationCheckBox.setValue(false);
			ProjectCreatorWizardUtil.selectInCombo(amountTypeComboBox, "");
			ProjectCreatorWizardUtil.selectInCombo(combinationTypeComboBox, "");
		}
	}

}