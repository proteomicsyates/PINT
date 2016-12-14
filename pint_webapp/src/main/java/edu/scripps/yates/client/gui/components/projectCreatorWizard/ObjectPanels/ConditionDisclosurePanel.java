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
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;

public class ConditionDisclosurePanel extends ClosableWithTitlePanel implements
		ReferencesDataObject {
	private ExperimentalConditionTypeBean condition = new ExperimentalConditionTypeBean();
	private final TextArea descriptionTextArea;
	private final ListBox sampleRefCombo;
	private static int numConditions = 1;

	public ConditionDisclosurePanel(String sessionID, int importJobID) {
		super(sessionID, importJobID, "Condition " + numConditions++, true);

		// description
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

		// sample ref
		Label sampleRefLabel = new Label("Sample in condition:");
		addWidget(sampleRefLabel);
		sampleRefCombo = new ListBox(false);
		sampleRefCombo.setVisibleItemCount(1);
		sampleRefCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				fireModificationEvent();
			}
		});

		// register this as a listener of samples
		ProjectCreatorRegister.registerAsListenerByObjectClass(
				SampleTypeBean.class, this);
		addWidget(sampleRefCombo);

		updateRepresentedObject();

		fireModificationEvent();
	}

	private void setSampleToConditionFromComboSelection() {
		final int selectedIndex = sampleRefCombo.getSelectedIndex();
		if (selectedIndex > 0) {
			final int sampleID = Integer.valueOf(sampleRefCombo
					.getValue(selectedIndex));
			final RepresentsDataObject projectObject = ProjectCreatorRegister
					.getProjectObjectRepresenter(sampleID);
			if (projectObject != null) {
				final SampleTypeBean sampleBean = (SampleTypeBean) projectObject
						.getObject();
				condition.setSampleRef(sampleBean.getId());
			}
		} else {
			condition.setSampleRef(null);
		}
	}

	@Override
	public ExperimentalConditionTypeBean getObject() {
		return condition;
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof ExperimentalConditionTypeBean) {
			condition = (ExperimentalConditionTypeBean) object;
			updateGUIFromObjectData();
		}
	}

	@Override
	public void updateGUIFromObjectData() {
		// set name
		setId(condition.getId());
		// set description
		descriptionTextArea.setText(condition.getDescription());
		// sample ref
		if (condition.getSampleRef() != null) {
			ProjectCreatorWizardUtil.selectInCombo(sampleRefCombo,
					condition.getSampleRef());
		}
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		// update samples combo
		ProjectCreatorWizardUtil.updateCombo(sampleRefCombo,
				SampleTypeBean.class);
	}

	@Override
	public void updateRepresentedObject() {
		condition.setId(getID());
		condition.setDescription(descriptionTextArea.getText());
		setSampleToConditionFromComboSelection();
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
		final ExperimentalConditionTypeBean condition = getObject();
		if (condition.getId() == null || "".equals(condition.getId()))
			return false;
		if (condition.getSampleRef() == null) {
			return false;
		}
		return true;
	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}

}
