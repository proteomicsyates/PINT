package edu.scripps.yates.client.ui.wizard.pages.panels;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class FastaDigestionPanelNew extends FlexTable {
	private final TextBox enzymeResiduesTextBox;
	private final TextBox enzymeNoCutTextBox;
	private final ListBox enzymeOffsetListBox;
	private final ListBox numMaxMissedCleavagesListBox;
	private final SimpleCheckBox crosslinkerPeptidesCheckBox;
	private final FileTypeBean file;
	private DoSomethingTask<Void> onDataUpdatedTask;

	public FastaDigestionPanelNew(FileTypeBean file) {
		this.file = file;
		if (this.file.getFastaDigestion() == null) {
			file.setFastaDigestion(new FastaDigestionBean());
		}
		int row = 0;
		setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		//
		final Label labelTitle = new Label("Fasta in-silico digestion parameters:");
		labelTitle.setStyleName(WizardStyles.WizardExplanationLabel);
		setWidget(row, 0, labelTitle);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		labelTitle.getElement().getStyle().setPaddingBottom(10, Unit.PX);
		//
		row++;
		final Label lblEnzymeResidues = new Label("Enzyme residues:");
		lblEnzymeResidues.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, lblEnzymeResidues);

		enzymeResiduesTextBox = new TextBox();
		enzymeResiduesTextBox.setText("KR");
		enzymeResiduesTextBox
				.setTitle("The aminoacids in which the enzyme will cut.\nFor example, for trypsin, it would be 'KR'.");
		enzymeResiduesTextBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				file.getFastaDigestion().setCleavageAAs(enzymeResiduesTextBox.getValue());
				if (onDataUpdatedTask != null) {
					onDataUpdatedTask.doSomething();
				}
			}
		});
		setWidget(row, 1, enzymeResiduesTextBox);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		//
		row++;
		final Label lblEnzymeNoCut = new Label("Enzyme no cut residues:");
		lblEnzymeNoCut.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, lblEnzymeNoCut);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		enzymeNoCutTextBox = new TextBox();
		enzymeNoCutTextBox.setText("P");
		enzymeNoCutTextBox.setTitle(
				"Aminoacid that if present just before the cleaveage site, will make the cleavage to not happen.");
		enzymeNoCutTextBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				file.getFastaDigestion().setEnzymeNoCutResidues(enzymeNoCutTextBox.getText());
				if (onDataUpdatedTask != null) {
					onDataUpdatedTask.doSomething();
				}
			}
		});
		setWidget(row, 1, enzymeNoCutTextBox);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);

		//
		row++;
		final Label lblNumMaxMissedcleavages = new Label("Num. Max. Missedcleavages:");
		lblNumMaxMissedcleavages.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, lblNumMaxMissedcleavages);

		numMaxMissedCleavagesListBox = new ListBox();
		numMaxMissedCleavagesListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				file.getFastaDigestion()
						.setMisscleavages(Integer.valueOf(numMaxMissedCleavagesListBox.getSelectedValue()));
				if (onDataUpdatedTask != null) {
					onDataUpdatedTask.doSomething();
				}
			}
		});
		numMaxMissedCleavagesListBox.setVisibleItemCount(1);
		setWidget(row, 1, numMaxMissedCleavagesListBox);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		//
		row++;
		final Label lblEnzymeOffset = new Label("Enzyme offset:");
		lblEnzymeOffset.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, lblEnzymeOffset);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		enzymeOffsetListBox = new ListBox();
		enzymeOffsetListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				file.getFastaDigestion().setEnzymeOffset(Integer.valueOf(enzymeOffsetListBox.getSelectedValue()));
				if (onDataUpdatedTask != null) {
					onDataUpdatedTask.doSomething();
				}
			}
		});
		setWidget(row, 1, enzymeOffsetListBox);
		enzymeOffsetListBox.setVisibleItemCount(1);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);

		//
		row++;

		final Label lblCrosslinkerPeptides = new Label("Crosslinker peptides:");
		lblCrosslinkerPeptides.setStyleName(WizardStyles.WizardInfoMessage);
//		setWidget(row, 0, lblCrosslinkerPeptides);
		crosslinkerPeptidesCheckBox = new SimpleCheckBox();
		crosslinkerPeptidesCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				file.getFastaDigestion().setH2OPlusProtonAdded(!crosslinkerPeptidesCheckBox.getValue());
				if (onDataUpdatedTask != null) {
					onDataUpdatedTask.doSomething();
				}
			}
		});
//		setWidget(row, 1, crosslinkerPeptidesCheckBox);
//		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
//		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		initCombos();
		updateGUIFromObjectData();
	}

	private void initCombos() {

		// max num missedcleavages 0-6
		for (int i = 0; i <= 6; i++) {
			numMaxMissedCleavagesListBox.addItem(String.valueOf(i), String.valueOf(i));
		}
		ProjectCreatorWizardUtil.selectInCombo(numMaxMissedCleavagesListBox, "2");

		// max num offset 0-1
		for (int i = 0; i <= 1; i++) {
			enzymeOffsetListBox.addItem(String.valueOf(i), String.valueOf(i));
		}
	}

	private void updateGUIFromObjectData() {
		final FastaDigestionBean fastaDigestionBean = file.getFastaDigestion();
		enzymeResiduesTextBox.setValue(fastaDigestionBean.getCleavageAAs());
		if ("".equals(enzymeResiduesTextBox.getValue())) {
			enzymeResiduesTextBox.setValue("KR");
			fastaDigestionBean.setCleavageAAs(enzymeResiduesTextBox.getValue());
		}

		enzymeNoCutTextBox.setValue(fastaDigestionBean.getEnzymeNoCutResidues());
		ProjectCreatorWizardUtil.selectInCombo(enzymeOffsetListBox,
				String.valueOf(fastaDigestionBean.getEnzymeOffset()));

		ProjectCreatorWizardUtil.selectInCombo(numMaxMissedCleavagesListBox,
				String.valueOf(fastaDigestionBean.getMisscleavages()));
		crosslinkerPeptidesCheckBox.setValue(!fastaDigestionBean.isH2OPlusProtonAdded());

	}

	public void setOnDataUpdatedTask(DoSomethingTask<Void> task) {
		this.onDataUpdatedTask = task;
	}
}
