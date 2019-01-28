package edu.scripps.yates.client.ui.wizard.pages.panels;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class FastaDigestionPanelNew extends Composite {
	private final TextBox enzymeResiduesTextBox;
	private final TextBox enzymeNoCutTextBox;
	private final ListBox enzymeOffsetListBox;
	private final ListBox numMaxMissedCleavagesListBox;
	private final SimpleCheckBox crosslinkerPeptidesCheckBox;
	private final FileTypeBean file;

	public FastaDigestionPanelNew(FileTypeBean file) {
		this.file = file;
		if (this.file.getFastaDigestion() == null) {
			file.setFastaDigestion(new FastaDigestionBean());
		}
		final FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);

		final FlexTable table = new FlexTable();
		mainPanel.add(table);

		final Label lblEnzymeResidues = new Label("Enzyme residues:");
		lblEnzymeResidues.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(0, 0, lblEnzymeResidues);

		enzymeResiduesTextBox = new TextBox();
		enzymeResiduesTextBox.setText("KR");
		table.setWidget(0, 1, enzymeResiduesTextBox);

		final Label lblEnzymeNoCut = new Label("Enzyme no cut residues:");
		lblEnzymeNoCut.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(1, 0, lblEnzymeNoCut);
		table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		table.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);

		enzymeNoCutTextBox = new TextBox();
		enzymeNoCutTextBox.setText("P");
		enzymeNoCutTextBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				file.getFastaDigestion().setEnzymeNoCutResidues(enzymeNoCutTextBox.getText());
			}
		});
		table.setWidget(1, 1, enzymeNoCutTextBox);
		table.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);

		final Label lblEnzymeOffset = new Label("Enzyme offset:");
		lblEnzymeOffset.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(2, 0, lblEnzymeOffset);
		table.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		enzymeOffsetListBox = new ListBox();
		enzymeOffsetListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				file.getFastaDigestion().setEnzymeOffset(Integer.valueOf(enzymeOffsetListBox.getSelectedValue()));
			}
		});
		table.setWidget(2, 1, enzymeOffsetListBox);
		enzymeOffsetListBox.setVisibleItemCount(1);
		table.getFlexCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);

		final Label lblNumMaxMissedcleavages = new Label("Num. Max. Missedcleavages:");
		lblNumMaxMissedcleavages.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(3, 0, lblNumMaxMissedcleavages);

		numMaxMissedCleavagesListBox = new ListBox();
		numMaxMissedCleavagesListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				file.getFastaDigestion()
						.setMisscleavages(Integer.valueOf(numMaxMissedCleavagesListBox.getSelectedValue()));
			}
		});
		numMaxMissedCleavagesListBox.setVisibleItemCount(1);
		table.setWidget(3, 1, numMaxMissedCleavagesListBox);
		table.getFlexCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		table.getFlexCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		table.getFlexCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		final Label lblCrosslinkerPeptides = new Label("Crosslinker peptides:");
		lblCrosslinkerPeptides.setStyleName(WizardStyles.WizardInfoMessage);
		table.setWidget(4, 0, lblCrosslinkerPeptides);
		table.getFlexCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);

		crosslinkerPeptidesCheckBox = new SimpleCheckBox();
		crosslinkerPeptidesCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				file.getFastaDigestion().setH2OPlusProtonAdded(!crosslinkerPeptidesCheckBox.getValue());
			}
		});
		table.setWidget(4, 1, crosslinkerPeptidesCheckBox);
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
		enzymeNoCutTextBox.setValue(fastaDigestionBean.getEnzymeNoCutResidues());
		ProjectCreatorWizardUtil.selectInCombo(enzymeOffsetListBox,
				String.valueOf(fastaDigestionBean.getEnzymeOffset()));

		ProjectCreatorWizardUtil.selectInCombo(numMaxMissedCleavagesListBox,
				String.valueOf(fastaDigestionBean.getMisscleavages()));
		crosslinkerPeptidesCheckBox.setValue(!fastaDigestionBean.isH2OPlusProtonAdded());

	}
}
