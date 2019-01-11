package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.shared.model.interfaces.HasId;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatioDescriptorTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;

public class RatioDisclosurePanel extends ClosableWithTitlePanel implements ReferencesDataObject {
	private RatioDescriptorTypeBean ratioDescriptorBean = new RatioDescriptorTypeBean();
	private final ListBox numeratorConditionRefCombo;
	private final ListBox denominatorRefCombo;
	private ExcelAmountRatioTypeBean excelAmountRatioTypeBean;
	private RemoteFilesRatioTypeBean remoteFilesRatioTypeBean;

	private static int numRatio = 1;

	public RatioDisclosurePanel(String sessionID, int importJobID) {
		super(sessionID, importJobID, "Ratio " + numRatio++, true);

		// condition 1 ref
		Label condition1RefLabel = new Label("Condition 1 (numerator):");
		addWidget(condition1RefLabel);
		numeratorConditionRefCombo = new ListBox();
		numeratorConditionRefCombo.setVisibleItemCount(1);
		numeratorConditionRefCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				fireModificationEvent();
			}
		});
		addWidget(numeratorConditionRefCombo);

		// condition 2 ref
		Label condition2RefLabel = new Label("Condition 2 (denominator):");
		addWidget(condition2RefLabel);
		denominatorRefCombo = new ListBox();
		denominatorRefCombo.setVisibleItemCount(1);
		denominatorRefCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				fireModificationEvent();
			}
		});
		addWidget(denominatorRefCombo);

		// register this as a listener of conditions
		ProjectCreatorRegister.registerAsListenerByObjectClass(ExperimentalConditionTypeBean.class, this);

		updateRepresentedObject();

		fireModificationEvent();
	}

	private void setConditionsToRatioFromComboSelection() {
		final int selectedIndex = numeratorConditionRefCombo.getSelectedIndex();
		if (selectedIndex > 0) {
			final int conditionID = Integer.valueOf(numeratorConditionRefCombo.getValue(selectedIndex));
			final RepresentsDataObject projectObject = ProjectCreatorRegister.getProjectObjectRepresenter(conditionID);
			if (projectObject != null) {
				final ExperimentalConditionTypeBean condition1Bean = (ExperimentalConditionTypeBean) projectObject
						.getObject();
				ratioDescriptorBean.setCondition1(condition1Bean);
			} else {
				ratioDescriptorBean.setCondition1(null);
			}
		} else {
			ratioDescriptorBean.setCondition1(null);
		}
		final int selectedIndex2 = denominatorRefCombo.getSelectedIndex();
		if (selectedIndex2 > 0) {
			final int conditionID = Integer.valueOf(numeratorConditionRefCombo.getValue(selectedIndex2));
			final RepresentsDataObject projectObject = ProjectCreatorRegister.getProjectObjectRepresenter(conditionID);
			if (projectObject != null) {
				final ExperimentalConditionTypeBean condition2Bean = (ExperimentalConditionTypeBean) projectObject
						.getObject();
				ratioDescriptorBean.setCondition2(condition2Bean);
			} else {
				ratioDescriptorBean.setCondition2(null);
			}
		} else {
			ratioDescriptorBean.setCondition2(null);
		}
		// update the excelAmountRatio if exists
		if (excelAmountRatioTypeBean != null) {
			excelAmountRatioTypeBean.setNumerator(getNumeratorConditionRef());
			excelAmountRatioTypeBean.setDenominator(getDenominatorCondition2Ref());
			excelAmountRatioTypeBean.setName(ratioDescriptorBean.getId());
		} else if (remoteFilesRatioTypeBean != null) {
			// update the remoteFilesRatioTypeBean
			remoteFilesRatioTypeBean.setId(ratioDescriptorBean.getId());
			remoteFilesRatioTypeBean.setNumerator(getNumeratorConditionRef());
			remoteFilesRatioTypeBean.setDenominator(getDenominatorCondition2Ref());
		}
	}

	@Override
	public RatioDescriptorTypeBean getObject() {
		return ratioDescriptorBean;
	}

	// public ExcelAmountRatioTypeBean getExcelAmountRatioTypeBean() {
	// return excelAmountRatioTypeBean;
	// }
	//
	// public RemoteFilesRatioTypeBean getRemoteFilesRatioTypeBean() {
	// return remoteFilesRatioTypeBean;
	// }

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof RatioDescriptorTypeBean) {
			ratioDescriptorBean = (RatioDescriptorTypeBean) object;

		} else if (object instanceof RemoteFilesRatioTypeBean) {
			remoteFilesRatioTypeBean = (RemoteFilesRatioTypeBean) object;
			ExperimentalConditionTypeBean experimentalCondition1 = null;
			ExperimentalConditionTypeBean experimentalCondition2 = null;
			RemoteFilesRatioTypeBean remoteRatioTypeBean = (RemoteFilesRatioTypeBean) object;
			ratioDescriptorBean = new RatioDescriptorTypeBean();
			ratioDescriptorBean.setDescription(remoteRatioTypeBean.getId());
			final List<RepresentsDataObject> experimentalConditionRepresenters = ProjectCreatorRegister
					.getProjectObjectsRepresentingClass(ExperimentalConditionTypeBean.class);
			for (RepresentsDataObject representsDataObject : experimentalConditionRepresenters) {
				if (representsDataObject instanceof ConditionDisclosurePanel) {
					if (((ConditionDisclosurePanel) representsDataObject).getObject().getId()
							.equals(remoteRatioTypeBean.getNumerator())) {
						experimentalCondition1 = (ExperimentalConditionTypeBean) representsDataObject.getObject();
					}
					if (((ConditionDisclosurePanel) representsDataObject).getObject().getId()
							.equals(remoteRatioTypeBean.getDenominator())) {
						experimentalCondition2 = (ExperimentalConditionTypeBean) representsDataObject.getObject();
					}
				}
			}
			ratioDescriptorBean.setCondition1(experimentalCondition1);
			ratioDescriptorBean.setCondition2(experimentalCondition2);
		} else if (object instanceof ExcelAmountRatioTypeBean) {
			excelAmountRatioTypeBean = (ExcelAmountRatioTypeBean) object;
			ExperimentalConditionTypeBean experimentalCondition1 = null;
			ExperimentalConditionTypeBean experimentalCondition2 = null;
			ratioDescriptorBean = new RatioDescriptorTypeBean();
			ratioDescriptorBean.setDescription(excelAmountRatioTypeBean.getId());
			final List<RepresentsDataObject> experimentalConditionRepresenters = ProjectCreatorRegister
					.getProjectObjectsRepresentingClass(ExperimentalConditionTypeBean.class);
			for (RepresentsDataObject representsDataObject : experimentalConditionRepresenters) {
				if (representsDataObject instanceof ConditionDisclosurePanel) {
					if (((ConditionDisclosurePanel) representsDataObject).getObject().getId()
							.equals(excelAmountRatioTypeBean.getNumerator())) {
						experimentalCondition1 = (ExperimentalConditionTypeBean) representsDataObject.getObject();
					}
					if (((ConditionDisclosurePanel) representsDataObject).getObject().getId()
							.equals(excelAmountRatioTypeBean.getDenominator())) {
						experimentalCondition2 = (ExperimentalConditionTypeBean) representsDataObject.getObject();
					}
				}
			}
			ratioDescriptorBean.setCondition1(experimentalCondition1);
			ratioDescriptorBean.setCondition2(experimentalCondition2);
		}
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {
		// set name
		setId(ratioDescriptorBean.getId());
		// condition 1 ref
		if (ratioDescriptorBean.getCondition1() != null) {
			ProjectCreatorWizardUtil.selectInCombo(numeratorConditionRefCombo,
					ratioDescriptorBean.getCondition1().getId());
		}
		// condition 2 ref
		if (ratioDescriptorBean.getCondition2() != null) {
			ProjectCreatorWizardUtil.selectInCombo(denominatorRefCombo, ratioDescriptorBean.getCondition2().getId());
		}
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		// update conditions combos
		ProjectCreatorWizardUtil.updateCombo(numeratorConditionRefCombo, ExperimentalConditionTypeBean.class);
		ProjectCreatorWizardUtil.updateCombo(denominatorRefCombo, ExperimentalConditionTypeBean.class);

	}

	@Override
	public void updateRepresentedObject() {
		ratioDescriptorBean.setDescription(getID());
		setConditionsToRatioFromComboSelection();
	}

	/**
	 * Decrease the counter and then call to editableDisclosurePanel.close()
	 */
	@Override
	public void close() {
		numRatio--;
		super.close();
	}

	public String getNumeratorConditionRef() {

		final int selectedIndex = numeratorConditionRefCombo.getSelectedIndex();
		if (selectedIndex > 0) {
			final String selectedCondition = numeratorConditionRefCombo.getItemText(selectedIndex);
			return selectedCondition;
		}
		return null;
	}

	public String getDenominatorCondition2Ref() {
		final int selectedIndex = denominatorRefCombo.getSelectedIndex();
		if (selectedIndex > 0) {
			final String selectedCondition = denominatorRefCombo.getItemText(selectedIndex);
			return selectedCondition;
		}
		return null;
	}

	public void addNumeratorConditionValueChanger(ChangeHandler handler) {
		numeratorConditionRefCombo.addChangeHandler(handler);
	}

	public void addDenominatorConditionValueChanger(ChangeHandler handler) {
		denominatorRefCombo.addChangeHandler(handler);
	}

	@Override
	protected boolean isValid() {
		updateRepresentedObject();
		final RatioDescriptorTypeBean ratio = getObject();
		if (ratio.getId() == null || "".equals(ratio.getId()))
			return false;
		if (ratio.getCondition1() == null)
			return false;
		if (ratio.getCondition2() == null)
			return false;
		return true;

	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}
}
