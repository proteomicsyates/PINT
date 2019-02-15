package edu.scripps.yates.client.ui.wizard.pages.widgets.excel;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public abstract class ExcelObjectPanel<T> extends FlexTable {
	private final PintContext context;
	protected final FileTypeBean file;
	protected final T object;
	private final String name;
	private final String excelSheet;

	public ExcelObjectPanel(PintContext context, FileTypeBean file, String excelSheet, T object) {
		this(null, context, file, excelSheet, object);
	}

	public ExcelObjectPanel(String name, PintContext context, FileTypeBean file, String excelSheet, T object) {
		this.name = name;
		this.context = context;
		this.file = file;
		this.excelSheet = excelSheet;
		this.object = object;
		setStyleName(WizardStyles.WizardQuestionPanelLessRoundCornersGreen);
		getElement().getStyle().setPaddingTop(10, Unit.PX);
		init();
		updateGUIFromContext();
	}

	protected abstract void updateGUIFromContext();

	protected abstract void init();

	protected ListBox createConditionListBox() {
		final ListBox ret = new ListBox();
		ret.setMultipleSelect(false);
		// load conditions
		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
				.getConditions(getContext().getPintImportConfiguration());
		for (final ExperimentalConditionTypeBean condition : conditions) {
			ret.addItem(condition.getId());
		}
		return ret;
	}

	public T getObject() {
		return object;
	}

	public PintContext getContext() {
		return context;
	}

	public abstract boolean isReady();

	protected boolean isEmpty(String value) {
		if (value == null || "".equals(value)) {
			return true;
		}
		return false;
	}

	public boolean selectInListBox(ListBox listBox, String itemValue) {
		for (int index = 0; index < listBox.getItemCount(); index++) {
			final String value = listBox.getValue(index);
			if (value.equals(itemValue)) {
				listBox.setSelectedIndex(index);
				return true;
			}
		}
		return false;
	}

	protected String getName() {
		return name;
	}

	protected String getExcelSheet() {
		return excelSheet;
	}
}
