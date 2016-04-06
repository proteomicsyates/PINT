package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

/**
 * This abstract class represents a Composite containing a
 * {@link ExcelColumnRefPanel}. It provides the getter and seetter for that
 * panel, as well as some methods for adding {@link ChangeHandler} for the
 * different combos of the {@link ExcelColumnRefPanel}
 *
 * @author Salva
 * @param <T>
 *
 */
public abstract class ContainsExcelColumnRefPanelAndTable<T, Y> extends Composite implements RepresentsObject<Y> {
	protected Y representedObject;
	private final ExcelColumnRefPanel excelColumnRefPanel;
	private final MyExcelColumnCellTable<T> table = new MyExcelColumnCellTable<T>();
	private MyExcelSectionFlowPanel relatedExcelSectionFlowPanel;
	protected final FlowPanel mainPanel;

	public ContainsExcelColumnRefPanelAndTable(FileTypeBean excelFileBean, Y representedObject) {
		this.representedObject = representedObject;
		excelColumnRefPanel = new ExcelColumnRefPanel(excelFileBean);
		mainPanel = new FlowPanel();
		initWidget(mainPanel);

		// if something change on the ExcelColumnRefPanel, reset the data of the
		// table
		ChangeHandler resetDataHandler = new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				table.clearData();
			}
		};
		addExcelReferenceChangeHandler(resetDataHandler);

		final List<CustomColumn<T>> initColumns = getInitColumns();
		final List<Header<String>> initHeaders = getInitHeaders();
		int index = 0;
		for (CustomColumn<T> customColumn : initColumns) {
			if (initHeaders != null && initHeaders.size() >= index + 1) {
				table.addColumn(customColumn, initHeaders.get(index));
			} else {
				table.addColumn(customColumn);
			}

			index++;
		}
	}

	public abstract List<CustomColumn<T>> getInitColumns();

	public abstract List<Header<String>> getInitHeaders();

	public void setExcelFileBean(FileTypeBean excelFileBean2) {
		excelColumnRefPanel.setExcelFileBean(excelFileBean2);
	}

	/**
	 * @return the excelColumnRefPanel
	 */
	public ExcelColumnRefPanel getExcelColumnRefPanel() {
		return excelColumnRefPanel;
	}

	/**
	 *
	 * @param changeHandler
	 */
	public void addExcelReferenceChangeHandler(ChangeHandler changeHandler) {

		excelColumnRefPanel.addExcelFilesChangeHandler(changeHandler);
		excelColumnRefPanel.addExcelSheetsChangeHandler(changeHandler);
		excelColumnRefPanel.addExcelColumnsChangeHandler(changeHandler);
	}

	public void addExcelFilesChangeHandler(ChangeHandler changeHandler) {
		excelColumnRefPanel.addExcelFilesChangeHandler(changeHandler);

	}

	public void addExcelSheetsChangeHandler(ChangeHandler changeHandler) {
		excelColumnRefPanel.addExcelSheetsChangeHandler(changeHandler);
	}

	public void addExcelColumnsChangeHandler(ChangeHandler changeHandler) {
		excelColumnRefPanel.addExcelColumnsChangeHandler(changeHandler);
	}

	/**
	 * Overrided in order to also remove from parent the
	 * {@link ExcelColumnRefPanel} and the {@link MyExcelColumnCellTable}, and
	 * the related {@link MyExcelSectionFlowPanel} (if present)
	 */
	@Override
	public void removeFromParent() {
		// excelColumnRefPanel.removeFromParent();
		// if (relatedExcelSectionFlowPanel != null)
		// relatedExcelSectionFlowPanel.removeFromParent();
		// table.removeFromParent();
		super.removeFromParent();
	}

	/**
	 * @return the relatedExcelSectionFlowPanel
	 */
	public MyExcelSectionFlowPanel getRelatedExcelSectionFlowPanel() {
		return relatedExcelSectionFlowPanel;
	}

	/**
	 * @param relatedExcelSectionFlowPanel
	 *            the relatedExcelSectionFlowPanel to set
	 */
	public void setRelatedExcelSectionFlowPanel(MyExcelSectionFlowPanel relatedExcelSectionFlowPanel) {
		this.relatedExcelSectionFlowPanel = relatedExcelSectionFlowPanel;
	}

	/**
	 * @return the table
	 */
	public MyExcelColumnCellTable<T> getTable() {

		return table;
	}

	/**
	 * Makes the table to reload if a {@link ChangeEvent} occurrs in the
	 * {@link ListBox}
	 *
	 * @param listBox
	 */
	public void reloadTableIfChange(ListBox listBox) {
		listBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				redrawTable();
			}
		});
	}

	/**
	 * Makes the table to reload if a {@link KeyUpHandler} occurrs in the
	 * {@link TextBox}
	 *
	 * @param textBox
	 */
	public void reloadTableIfChange(TextBox textBox) {
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				redrawTable();
			}
		});
	}

	/**
	 * Makes the table to reload if a {@link KeyUpHandler} occurrs in the
	 * {@link TextArea}
	 *
	 * @param textArea
	 */
	public void reloadTableIfChange(TextArea textArea) {
		textArea.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				redrawTable();
			}
		});
	}

	/**
	 * Makes the table to reload if a {@link ValueChangeHandler} occurrs in the
	 * {@link HasValueChangeHandlers}
	 *
	 * @param hasValueChangeHandler
	 */
	public void reloadTableIfChange(HasValueChangeHandlers hasValueChangeHandler) {
		hasValueChangeHandler.addValueChangeHandler(new ValueChangeHandler() {

			@Override
			public void onValueChange(ValueChangeEvent event) {
				redrawTable();
			}
		});
	}

	public void redrawTable() {
		System.out.println("Refressing data in table");
		updateRepresentedObject();
		table.getDataProvider().refresh();
	}

	/**
	 * Returns true if the represented object is currently valid or not
	 *
	 * @return
	 */
	public abstract boolean isValid();

	/**
	 * Gets the represented object after update it using the options selected in
	 * the GUI and while it is valid (return true by the overrided method
	 * isValid())
	 *
	 * @return
	 */
	public Y getRepresentedObject() {
		updateRepresentedObject();
		if (isValid())
			return getObject();
		return null;
	}

	public void setRepresentedObject(Y representedObject) {
		this.representedObject = representedObject;
	}

	public void resetData() {
		getTable().clearData();
	}
}
