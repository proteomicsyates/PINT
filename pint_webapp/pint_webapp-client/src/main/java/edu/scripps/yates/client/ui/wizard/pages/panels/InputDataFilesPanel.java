package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.style.Color;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.pages.panels.summary.InputFileSummaryTable;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;

public class InputDataFilesPanel extends FlexTable {
	private final Wizard<PintContext> wizard;
	private final List<InputFileSummaryTable> inputFileSummaryTables = new ArrayList<InputFileSummaryTable>();

	public InputDataFilesPanel(Wizard<PintContext> wizard) {
		this.wizard = wizard;

		init();
	}

	private HorizontalPanel getHorizontalLine() {
		final HorizontalPanel horizontalLine1 = new HorizontalPanel();
		horizontalLine1.getElement().getStyle().setBackgroundColor(Color.LIGHT_GRAY.getHexValue());
		horizontalLine1.setWidth("100%");
		horizontalLine1.getElement().getStyle().setHeight(2, Unit.PX);
		horizontalLine1.getElement().getStyle().setMarginTop(5, Unit.PX);
		horizontalLine1.getElement().getStyle().setMarginBottom(5, Unit.PX);
		return horizontalLine1;
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
			final InputFileSummaryTable inputFileSummaryTable = new InputFileSummaryTable(wizard.getContext(), file,
					numInputFile++, true);
			inputFileSummaryTables.add(inputFileSummaryTable);
			verticalPanelInputFiles.add(inputFileSummaryTable);
		}

	}

	public void addOnUpdateContextTask(DoSomethingTask2<MsRunTypeBean> onContextUpdated) {
		for (final InputFileSummaryTable inputFileSummaryTable : inputFileSummaryTables) {
			inputFileSummaryTable.setOnContextUpdated(onContextUpdated);
		}
	}

}
