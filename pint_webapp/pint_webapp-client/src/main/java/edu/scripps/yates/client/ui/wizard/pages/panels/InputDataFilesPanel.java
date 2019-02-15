package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.panels.summary.InputFileSummaryTable;
import edu.scripps.yates.client.ui.wizard.pages.widgets.NewExcelReferenceWidget;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;

public class InputDataFilesPanel extends FlexTable {
	private final Wizard<PintContext> wizard;
	private final List<InputFileSummaryTable> inputFileSummaryTables = new ArrayList<InputFileSummaryTable>();

	public InputDataFilesPanel(Wizard<PintContext> wizard) {
		this.wizard = wizard;

		init();
	}

	private void init() {
		final int row = 0;
		// line 0
		// intput files
		List<FileTypeBean> files = PintImportCfgUtil.getFiles(wizard.getContext().getPintImportConfiguration());
		// discard FASTA files and UNKNOWN format
		files = files.stream()
				.filter(file -> file.getFormat() != FileFormat.FASTA && file.getFormat() != FileFormat.UNKNOWN)
				.collect(Collectors.toList());

		final VerticalPanel verticalPanelInputFiles = new VerticalPanel();
		verticalPanelInputFiles.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		setWidget(row, 0, verticalPanelInputFiles);
		getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		int numInputFile = 1;
		for (final FileTypeBean file : files) {
			if (file.getFormat() == FileFormat.EXCEL) {
				final List<SheetTypeBean> sheets = file.getSheets().getSheet();
				for (final SheetTypeBean sheet : sheets) {
					final InputFileSummaryTable inputFileSummaryTable = new InputFileSummaryTable(wizard.getContext(),
							file, NewExcelReferenceWidget.getSheetName(sheet.getId()), numInputFile++, true);
					inputFileSummaryTables.add(inputFileSummaryTable);
					verticalPanelInputFiles.add(inputFileSummaryTable);
				}
			} else {
				final InputFileSummaryTable inputFileSummaryTable = new InputFileSummaryTable(wizard.getContext(), file,
						numInputFile++, true);
				inputFileSummaryTables.add(inputFileSummaryTable);
				verticalPanelInputFiles.add(inputFileSummaryTable);
			}
		}

	}

	public void addOnUpdateContextTask(DoSomethingTask2<MsRunTypeBean> onContextUpdated) {
		for (final InputFileSummaryTable inputFileSummaryTable : inputFileSummaryTables) {
			inputFileSummaryTable.setOnContextUpdated(onContextUpdated);
		}
	}

}
