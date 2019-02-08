package edu.scripps.yates.client.ui.wizard.pages.widgets.excel;

import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public abstract class ExcelObjectPanel<T> extends FlexTable {
	private final PintContext context;
	protected final FileTypeBean file;
	protected final T object;

	public ExcelObjectPanel(PintContext context, FileTypeBean file, T object) {
		this.context = context;
		this.file = file;
		this.object = object;
		init();
	}

	protected abstract void init();

	protected ListBox createConditionListBox() {
		final ListBox ret = new ListBox();
		ret.setMultipleSelect(false);
		// load conditions
		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
				.getConditions(context.getPintImportConfiguration());
		for (final ExperimentalConditionTypeBean condition : conditions) {
			ret.addItem(condition.getId());
		}
		return ret;
	}

	public T getObject() {
		return object;
	}

}
