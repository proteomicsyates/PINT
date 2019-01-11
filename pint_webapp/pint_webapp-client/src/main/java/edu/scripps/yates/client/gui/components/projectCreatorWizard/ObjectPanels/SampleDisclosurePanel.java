package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.shared.model.interfaces.HasId;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;

public class SampleDisclosurePanel extends ClosableWithTitlePanel implements
		ReferencesDataObject {
	private SampleTypeBean sample = new SampleTypeBean();
	private final TextArea descriptionTextArea;
	private final ListBox labelRefCombo;
	private final ListBox tissueRefCombo;
	private final ListBox organismRefCombo;

	private static int numSamples = 1;

	public SampleDisclosurePanel(String sessionID, int importJobID) {
		super(sessionID, importJobID, "Sample " + numSamples++, true);

		Label descriptionLabel = new Label("Description:");
		addWidget(descriptionLabel);
		descriptionTextArea = new TextArea();
		descriptionTextArea.setCharacterWidth(40);
		descriptionTextArea.setVisibleLines(4);
		descriptionTextArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireModificationEvent();
			}
		});
		descriptionTextArea.setStyleName("projectCreatorWizardTextArea");
		addWidget(descriptionTextArea);

		// label ref
		Label labelRefLabel = new Label("Label in sample:");
		addWidget(labelRefLabel);
		labelRefCombo = new ListBox(false);
		labelRefCombo.setVisibleItemCount(1);
		labelRefCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				fireModificationEvent();
			}
		});
		// register this as a listener of labels
		ProjectCreatorRegister.registerAsListenerByObjectClass(
				LabelTypeBean.class, this);
		addWidget(labelRefCombo);

		// tissue ref
		Label tissueRefLabel = new Label("Sample origin (tissue/cell line):");
		addWidget(tissueRefLabel);
		tissueRefCombo = new ListBox(false);
		tissueRefCombo.setVisibleItemCount(1);
		tissueRefCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				fireModificationEvent();
			}
		});
		// register this as a listener of tissues
		ProjectCreatorRegister.registerAsListenerByObjectClass(
				TissueTypeBean.class, this);
		addWidget(tissueRefCombo);

		// organism ref
		Label organismRefLabel = new Label("Organism:");
		addWidget(organismRefLabel);
		organismRefCombo = new ListBox(false);
		organismRefCombo.setVisibleItemCount(1);
		organismRefCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				fireModificationEvent();
			}
		});
		// register this as a listener of organisms
		ProjectCreatorRegister.registerAsListenerByObjectClass(
				OrganismTypeBean.class, this);
		addWidget(organismRefCombo);

		updateRepresentedObject();

		fireModificationEvent();
	}

	private void setLabelToSampleFromComboSelection() {
		final int selectedIndex = labelRefCombo.getSelectedIndex();
		if (selectedIndex >= 0) {
			final int sampleID = Integer.valueOf(labelRefCombo
					.getValue(selectedIndex));
			final RepresentsDataObject projectObject = ProjectCreatorRegister
					.getProjectObjectRepresenter(sampleID);
			if (projectObject != null) {
				final LabelTypeBean labelBean = (LabelTypeBean) projectObject
						.getObject();
				sample.setLabelRef(labelBean.getId());
			}
		} else {
			sample.setLabelRef(null);
		}

	}

	private void setTissueToSampleFromComboSelection() {
		final int selectedIndex = tissueRefCombo.getSelectedIndex();
		if (selectedIndex >= 0) {
			final int tissueID = Integer.valueOf(tissueRefCombo
					.getValue(selectedIndex));
			final RepresentsDataObject projectObject = ProjectCreatorRegister
					.getProjectObjectRepresenter(tissueID);
			if (projectObject != null) {
				final TissueTypeBean tissueBean = (TissueTypeBean) projectObject
						.getObject();
				sample.setTissueRef(tissueBean.getId());
			}
		} else {
			sample.setTissueRef(null);
		}

	}

	private void setOrganismToSampleFromComboSelection() {
		final int selectedIndex = organismRefCombo.getSelectedIndex();
		if (selectedIndex >= 0) {
			final int organismID = Integer.valueOf(organismRefCombo
					.getValue(selectedIndex));
			final RepresentsDataObject projectObject = ProjectCreatorRegister
					.getProjectObjectRepresenter(organismID);
			if (projectObject != null) {
				final OrganismTypeBean organismBean = (OrganismTypeBean) projectObject
						.getObject();
				sample.setOrganismRef(organismBean.getId());
			}
		} else {
			sample.setOrganismRef(null);
		}

	}

	@Override
	public SampleTypeBean getObject() {
		return sample;
	}

	@Override
	public void updateRepresentedObject() {
		sample.setId(getID());
		sample.setDescription(descriptionTextArea.getText());
		setLabelToSampleFromComboSelection();
		setTissueToSampleFromComboSelection();
		setOrganismToSampleFromComboSelection();
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		// update labels combo
		ProjectCreatorWizardUtil
				.updateCombo(labelRefCombo, LabelTypeBean.class);
		// update tissues combo
		ProjectCreatorWizardUtil.updateCombo(tissueRefCombo,
				TissueTypeBean.class);
		// update organisms combo
		ProjectCreatorWizardUtil.updateCombo(organismRefCombo,
				OrganismTypeBean.class);
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof SampleTypeBean) {
			sample = (SampleTypeBean) object;
			updateGUIFromObjectData();
		}

	}

	@Override
	public void updateGUIFromObjectData() {

		// set name
		setId(sample.getId());
		descriptionTextArea.setText(sample.getDescription());
		// label ref
		if (sample.getLabelRef() != null) {
			ProjectCreatorWizardUtil.selectInCombo(labelRefCombo,
					sample.getLabelRef());
		}
		// tissue ref
		if (sample.getTissueRef() != null) {
			ProjectCreatorWizardUtil.selectInCombo(tissueRefCombo,
					sample.getTissueRef());
		}
		// organism ref
		if (sample.getOrganismRef() != null) {
			ProjectCreatorWizardUtil.selectInCombo(organismRefCombo,
					sample.getOrganismRef());
		}
	}

	/**
	 * Decrease the counter and then call to editableDisclosurePanel.close()
	 */
	@Override
	public void close() {
		super.close();
	}

	@Override
	protected boolean isValid() {
		updateRepresentedObject();
		final SampleTypeBean sample = getObject();
		if (sample.getId() == null || "".equals(sample.getId()))
			return false;
		// label is optional
		// if (sample.getLabelRef() == null)
		// return false;
		if (sample.getOrganismRef() == null)
			return false;
		// tissue is optional
		// if (sample.getTissueRef() == null)
		// return false;
		return true;

	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}
}
