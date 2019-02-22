package edu.scripps.yates.client.ui.wizard.pages.panels.summary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.widgets.AbstractItemDropLabel;
import edu.scripps.yates.client.ui.wizard.pages.widgets.DroppableFormat;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class InputFileSummaryTable extends AbstractSummaryTable {

	private final int nextRowWidget;
	private final FileTypeBean file;
	private final PintContext context;
	private final List<MsRunTypeBean> associatedMSRuns = new ArrayList<MsRunTypeBean>();
	private DoSomethingTask2<MsRunTypeBean> onContextUpdated;
	private final String sheetName;

	public InputFileSummaryTable(PintContext context, FileTypeBean fileType, int number, boolean addDropMSRunLabel) {
		this(context, fileType, null, number, addDropMSRunLabel);
	}

	public InputFileSummaryTable(PintContext context, FileTypeBean fileType, String sheetName, int number,
			boolean addDropMSRunLabel) {
		super(number);
		this.file = fileType;
		this.context = context;
		this.sheetName = sheetName;
		int row = 0;
		// file number
		final Label fileNumber = new Label(number + ".");
		fileNumber.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(row, 0, fileNumber);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
		// file name
		final Label fileLabel = new Label("Input file name:");
		fileLabel.setStyleName(WizardStyles.WizardInfoMessage);
		fileLabel.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);

		setWidget(row, 1, fileLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label fileNameLabel = new Label(fileType.getName());
		final String title = PintImportCfgUtil.getTitleInputFile(fileType);
		fileNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickableRed);
		fileNameLabel.setTitle(title);
		setWidget(row, 2, fileNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_LEFT);
		getFlexCellFormatter().setColSpan(row, 2, 2);
		// date
		row++;

		final Label formatLabel = new Label("Format:");
		formatLabel.setTitle("Format of the input file '" + fileType.getName() + "'");
		formatLabel.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, formatLabel);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		final Label formatNameLabel = new Label(fileType.getFormat().getName());
		formatNameLabel.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		formatNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(row, 1, formatNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		getFlexCellFormatter().setColSpan(row, 1, 2);

		// num sheets
		row++;
		if (fileType.getFormat() == FileFormat.EXCEL) {
			final Label excelNumSheetsLabel = new Label("Sheet:");
			excelNumSheetsLabel.setTitle("Excel sheet '" + sheetName + "'");
			excelNumSheetsLabel.setStyleName(WizardStyles.WizardInfoMessage);
			setWidget(row, 0, excelNumSheetsLabel);
			getFlexCellFormatter().setColSpan(row, 0, 2);
			getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

			final Label excelNumSheetsNameLabel = new Label(sheetName);
			excelNumSheetsNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			setWidget(row, 1, excelNumSheetsNameLabel);
			getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
			getFlexCellFormatter().setColSpan(row, 1, 2);
		}
		nextRowWidget = ++row;
		if (addDropMSRunLabel) {
			addDropMSRunLabel();
			// add the associations that are already made
			final List<MsRunTypeBean> msRuns = PintImportCfgUtil
					.getMSRunsAssociatedWithFile(context.getPintImportConfiguration(), file.getId(), sheetName);
			for (final MsRunTypeBean msRunTypeBean : msRuns) {
				addAssociatedMSRun(msRunTypeBean);
			}
		}

	}

	private void addDropMSRunLabel() {
		final AbstractItemDropLabel<FileTypeBean> dropLabel = new AbstractItemDropLabel<FileTypeBean>(
				"drop Experiment/Replicate", DroppableFormat.MSRUN, this.file, true, false) {

			@Override
			protected void updateItemWithData(String msRunID, DroppableFormat format, FileTypeBean file) {
				if (format == DroppableFormat.MSRUN) {
					final MsRunTypeBean msRun = PintImportCfgUtil.getMSRun(context.getPintImportConfiguration(),
							msRunID);

					addAssociatedMSRun(msRun);

				}
			}
		};
		setWidget(nextRowWidget, 0, dropLabel);
		getFlexCellFormatter().setHorizontalAlignment(nextRowWidget, 0, HasHorizontalAlignment.ALIGN_CENTER);
		getFlexCellFormatter().setColSpan(nextRowWidget, 0, 4);

	}

	protected void addAssociatedMSRun(MsRunTypeBean msRun) {
		if (associatedMSRuns.contains(msRun)) {
			return;
		}
		addAssociatedMSRunToContext(msRun);
		associatedMSRuns.add(msRun);
		final int indexOf = associatedMSRuns.indexOf(msRun);
		final Label label = new Label(msRun.getId());
		label.setStyleName(WizardStyles.WizardDraggableLabelFixed);
		label.setTitle(PintImportCfgUtil.getTitleMSRun(msRun));
		final int row = nextRowWidget + indexOf + 1;
		setWidget(row, 0, label);
		getFlexCellFormatter().setColSpan(row, 0, 3);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		// remove button
		// delete button
		final Image deleteButton = new Image(MyClientBundle.INSTANCE.redCross());
		deleteButton.setTitle("Click here to remove the association between file '" + file.getName()
				+ "' and the Experiment/Replicate '" + msRun.getId() + "'");
		deleteButton.setStyleName(WizardStyles.CLICKABLE);
		setWidget(row, 1, deleteButton);
		getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_LEFT);
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final int index = associatedMSRuns.indexOf(msRun);
				final int row2 = index + nextRowWidget + 1;
				removeRow(row2);

				removeMSRunFromFile(file, msRun);
				if (getOnContextUpdatedTask() != null) {
					getOnContextUpdatedTask().doSomething(msRun);
				}

				associatedMSRuns.remove(msRun);
			}
		});
		if (getOnContextUpdatedTask() != null) {
			getOnContextUpdatedTask().doSomething(msRun);
		}
	}

	private void addAssociatedMSRunToContext(MsRunTypeBean msRun) {
		final List<RemoteFilesRatioTypeBean> ratios = PintImportCfgUtil
				.getRemoteFilesRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId());
		for (final RemoteFilesRatioTypeBean ratio : ratios) {
			ratio.setMsRunRef(PintImportCfgUtil.addMSRunRef(ratio.getMsRunRef(), msRun.getId()));
		}
		final List<ExcelAmountRatioTypeBean> excelRatios = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName, true, true, true);
		for (final ExcelAmountRatioTypeBean ratio : excelRatios) {
			ratio.setMsRunRef(PintImportCfgUtil.addMSRunRef(ratio.getMsRunRef(), msRun.getId()));
		}
		final Set<IdentificationExcelTypeBean> excelIDs = PintImportCfgUtil
				.getExcelIDAssociatedWithThisFile(context.getPintImportConfiguration(), file.getId(), sheetName);
		for (final IdentificationExcelTypeBean id : excelIDs) {
			id.setMsRunRef(PintImportCfgUtil.addMSRunRef(id.getMsRunRef(), msRun.getId()));
		}
		final List<RemoteInfoTypeBean> ids = PintImportCfgUtil
				.getRemoteInfoTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId());
		for (final RemoteInfoTypeBean id : ids) {
			id.setMsRunRef(PintImportCfgUtil.addMSRunRef(id.getMsRunRef(), msRun.getId()));
		}
		final List<QuantificationExcelTypeBean> excelQuants = PintImportCfgUtil
				.getQuantificationExcelTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName);
		for (final QuantificationExcelTypeBean quant : excelQuants) {
			quant.setMsRunRef(PintImportCfgUtil.addMSRunRef(quant.getMsRunRef(), msRun.getId()));
		}
		if (getOnContextUpdatedTask() != null) {
			getOnContextUpdatedTask().doSomething(msRun);
		}
	}

	protected void removeMSRunFromFile(FileTypeBean file, MsRunTypeBean msRun) {
		final String msRunID = msRun.getId();
		final List<RemoteFilesRatioTypeBean> ratios = PintImportCfgUtil
				.getRemoteFilesRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId());
		for (final RemoteFilesRatioTypeBean ratio : ratios) {
			ratio.setMsRunRef(PintImportCfgUtil.removeMSRunRef(ratio.getMsRunRef(), msRunID));
		}
		final List<ExcelAmountRatioTypeBean> excelRatios = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName, true, true, true);
		for (final ExcelAmountRatioTypeBean ratio : excelRatios) {
			ratio.setMsRunRef(PintImportCfgUtil.removeMSRunRef(ratio.getMsRunRef(), msRunID));
		}
		final Set<IdentificationExcelTypeBean> excelIDs = PintImportCfgUtil
				.getExcelIDAssociatedWithThisFile(context.getPintImportConfiguration(), file.getId(), sheetName);
		for (final IdentificationExcelTypeBean id : excelIDs) {
			id.setMsRunRef(PintImportCfgUtil.removeMSRunRef(id.getMsRunRef(), msRunID));
		}
		final List<RemoteInfoTypeBean> ids = PintImportCfgUtil
				.getRemoteInfoTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId());
		for (final RemoteInfoTypeBean id : ids) {
			id.setMsRunRef(PintImportCfgUtil.removeMSRunRef(id.getMsRunRef(), msRunID));
		}
		final List<QuantificationExcelTypeBean> excelQuants = PintImportCfgUtil
				.getQuantificationExcelTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName);
		for (final QuantificationExcelTypeBean quant : excelQuants) {
			quant.setMsRunRef(PintImportCfgUtil.removeMSRunRef(quant.getMsRunRef(), msRunID));
		}

	}

	protected DoSomethingTask2<MsRunTypeBean> getOnContextUpdatedTask() {
		return onContextUpdated;
	}

	public void setOnContextUpdated(DoSomethingTask2<MsRunTypeBean> onContextUpdated) {
		this.onContextUpdated = onContextUpdated;
	}
}
