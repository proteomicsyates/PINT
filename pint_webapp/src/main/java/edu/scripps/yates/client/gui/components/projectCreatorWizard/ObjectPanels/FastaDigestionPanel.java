package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;

public class FastaDigestionPanel extends Composite implements RepresentsObject<FastaDigestionBean> {
	private final TextBox enzymeResiduesTextBox;
	private final TextBox enzymeNoCutTextBox;
	private final ListBox enzymeOffsetListBox;
	private final ListBox numMaxMissedCleavagesListBox;
	private final SimpleCheckBox crosslinkerPeptidesCheckBox;
	private FastaDigestionBean fastaDigestionBean;

	public FastaDigestionPanel() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("fastaDigestionPanel");
		initWidget(mainPanel);

		Grid grid = new Grid(5, 2);
		mainPanel.add(grid);

		Label lblEnzymeResidues = new Label("Enzyme residues:");
		grid.setWidget(0, 0, lblEnzymeResidues);

		enzymeResiduesTextBox = new TextBox();
		enzymeResiduesTextBox.setText("KR");
		grid.setWidget(0, 1, enzymeResiduesTextBox);

		Label lblEnzymeNoCut = new Label("Enzyme no cut residues:");
		grid.setWidget(1, 0, lblEnzymeNoCut);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);

		enzymeNoCutTextBox = new TextBox();
		enzymeNoCutTextBox.setText("P");
		grid.setWidget(1, 1, enzymeNoCutTextBox);
		grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);

		Label lblEnzymeOffset = new Label("Enzyme offset:");
		grid.setWidget(2, 0, lblEnzymeOffset);
		grid.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		enzymeOffsetListBox = new ListBox();
		grid.setWidget(2, 1, enzymeOffsetListBox);
		enzymeOffsetListBox.setVisibleItemCount(1);
		grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);

		Label lblNumMaxMissedcleavages = new Label("Num. Max. Missedcleavages:");
		grid.setWidget(3, 0, lblNumMaxMissedcleavages);

		numMaxMissedCleavagesListBox = new ListBox();
		numMaxMissedCleavagesListBox.setVisibleItemCount(1);
		grid.setWidget(3, 1, numMaxMissedCleavagesListBox);
		grid.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		Label lblCrosslinkerPeptides = new Label("Crosslinker peptides:");
		grid.setWidget(4, 0, lblCrosslinkerPeptides);
		grid.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);

		crosslinkerPeptidesCheckBox = new SimpleCheckBox();
		grid.setWidget(4, 1, crosslinkerPeptidesCheckBox);
		initCombos();
		updateRepresentedObject();
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

	@Override
	public FastaDigestionBean getObject() {
		updateRepresentedObject();
		return fastaDigestionBean;
	}

	@Override
	public void updateRepresentedObject() {
		if (fastaDigestionBean == null)
			fastaDigestionBean = new FastaDigestionBean();
		fastaDigestionBean.setCleavageAAs(enzymeResiduesTextBox.getValue());
		fastaDigestionBean.setEnzymeNoCutResidues(enzymeNoCutTextBox.getValue());
		int selectedIndex = enzymeOffsetListBox.getSelectedIndex();
		fastaDigestionBean.setEnzymeOffset(Integer.valueOf(enzymeOffsetListBox.getItemText(selectedIndex)));
		fastaDigestionBean.setH2OPlusProtonAdded(!crosslinkerPeptidesCheckBox.getValue());

		selectedIndex = numMaxMissedCleavagesListBox.getSelectedIndex();
		fastaDigestionBean.setMisscleavages(Integer.valueOf(numMaxMissedCleavagesListBox.getItemText(selectedIndex)));
	}

	@Override
	public void updateGUIFromObjectData(FastaDigestionBean dataObject) {
		fastaDigestionBean = dataObject;
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {
		if (fastaDigestionBean != null) {
			enzymeResiduesTextBox.setValue(fastaDigestionBean.getCleavageAAs());
			enzymeNoCutTextBox.setValue(fastaDigestionBean.getEnzymeNoCutResidues());
			ProjectCreatorWizardUtil.selectInCombo(enzymeOffsetListBox,
					String.valueOf(fastaDigestionBean.getEnzymeOffset()));

			ProjectCreatorWizardUtil.selectInCombo(numMaxMissedCleavagesListBox,
					String.valueOf(fastaDigestionBean.getMisscleavages()));
			crosslinkerPeptidesCheckBox.setValue(!fastaDigestionBean.isH2OPlusProtonAdded());
		}

	}
}
