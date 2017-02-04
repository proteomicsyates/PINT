package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

/**
 * Represents a {@link ExperimentalConditionTypeBean} and contains the GUI for
 * adding new {@link IdentificationInfoEditorPanel} and new
 * {@link QuantificationInfoEditorPanel}.
 *
 * @author Salva
 *
 */
public class ExperimentalConditionEditorPanel extends Composite
		implements ReferencesDataObject, ContainsImportJobID, ProvidesResize, RequiresResize {
	private final ExperimentalConditionTypeBean conditionBean;
	private TabNameChanger tabNameChanger;
	private final IncrementableTextBox quantDataIncrementableTextBox;
	private final IncrementableTextBox idDataIncrementableTextBox;
	private int importJobID;
	private final List<IdentificationInfoEditorPanel> identificationInfoEditors = new ArrayList<IdentificationInfoEditorPanel>();
	private final List<QuantificationInfoEditorPanel> quantificationInfoEditors = new ArrayList<QuantificationInfoEditorPanel>();

	private final DecoratedButtonsPanel identPanel;
	private final DecoratedButtonsPanel quantPanel;
	private int numIdData = 1;
	private int numQuantData = 1;
	private final FlowPanel identAndQuantSplitPanel;
	private ClickHandler selectIdDataHandler;
	private ClickHandler selectQuantDataHandler;
	private final Label lblproteinInformationAccessions;
	private final Label lblNewLabel;
	private final String sessionID;
	private final FlowPanel mainPanel;

	public ExperimentalConditionEditorPanel(String sessionID, int importJobID,
			ExperimentalConditionTypeBean conditionBean) {
		this.sessionID = sessionID;
		this.importJobID = importJobID;
		this.conditionBean = conditionBean;
		mainPanel = new FlowPanel();
		mainPanel.setStyleName("ExperimentalConditionEditorPanel");
		initWidget(mainPanel);
		mainPanel.setSize("100%", "100%");

		FlowPanel header = new FlowPanel();
		header.setStyleName("ExperimentalConditionEditorPanel_Header");
		mainPanel.add(header);
		header.setSize("100%", "120px");

		FlexTable grid = new FlexTable();
		grid.setCellPadding(10);
		header.add(grid);

		InlineLabel idDataLabel = new InlineLabel("Number of identification datasets:");
		grid.setWidget(0, 0, idDataLabel);
		// incrementable textbox for identification data
		idDataIncrementableTextBox = new IncrementableTextBox(0);
		grid.setWidget(0, 1, idDataIncrementableTextBox);
		idDataIncrementableTextBox.addPlusButtonHandler(addIdDataPanelHandler());
		idDataIncrementableTextBox.addMinusButtonHandler(removeIdDataPanelHandler());

		lblproteinInformationAccessions = new Label(
				"(Protein and peptide identification datasets containing protein, peptide and/or PSM-level information such: as protein accessions, descriptions, peptide sequences, PTMs and associated detection scores)");
		grid.setWidget(0, 2, lblproteinInformationAccessions);

		InlineLabel quantDataLabel = new InlineLabel("Number of quantification values datasets:");
		grid.setWidget(1, 0, quantDataLabel);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		// incrementable textbox for quantitative data
		quantDataIncrementableTextBox = new IncrementableTextBox(0);
		grid.setWidget(1, 1, quantDataIncrementableTextBox);
		grid.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_MIDDLE);

		lblNewLabel = new Label(
				"(Use this for entering absolute or relative abundance values for protein, peptide or PSM-level, as well as any associated confidence score)\r\n(For importing relative quantitative ratios, use the Quantitative ratios element in the project definition item list)");
		grid.setWidget(1, 2, lblNewLabel);
		grid.getCellFormatter().setVerticalAlignment(1, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		grid.getCellFormatter().setHorizontalAlignment(1, 2, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);

		identAndQuantSplitPanel = new FlowPanel();
		mainPanel.add(identAndQuantSplitPanel);
		identAndQuantSplitPanel.setSize("100%", "750px");
		identPanel = new DecoratedButtonsPanel("Identification datasets");
		identAndQuantSplitPanel.add(identPanel);
		identPanel.setSize("100%", "250px");

		quantPanel = new DecoratedButtonsPanel("Quantification datasets (not relative ratios datasets)");
		identAndQuantSplitPanel.add(quantPanel);
		quantPanel.setSize("100%", "250px");
		quantDataIncrementableTextBox.addPlusButtonHandler(addQuantDataPanelHandler());
		quantDataIncrementableTextBox.addMinusButtonHandler(removeQuantDataPanelHandler());

		updateGUIReferringToDataObjects();
	}

	/**
	 * @param importJobID
	 *            the importJobID to set
	 */
	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;
		for (QuantificationInfoEditorPanel editor : quantificationInfoEditors) {
			editor.setImportJobID(importJobID);
		}
		for (IdentificationInfoEditorPanel editor : identificationInfoEditors) {
			editor.setImportJobID(importJobID);
		}

	}

	private ClickHandler removeQuantDataPanelHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// remove from parent the last editor
				if (!quantificationInfoEditors.isEmpty()) {
					final QuantificationInfoEditorPanel quantificationInfoEditor = quantificationInfoEditors
							.get(quantificationInfoEditors.size() - 1);
					quantPanel.removeButton(quantificationInfoEditor);
					quantificationInfoEditor.removeFromParent();
					quantificationInfoEditor.unregisterAsListener();
					quantificationInfoEditors.remove(quantificationInfoEditor);
					numQuantData--;
				}

			}
		};
	}

	private ClickHandler addQuantDataPanelHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addNewQuantificationInfoEditorPanel();

			}
		};
	}

	/**
	 * Creates a new {@link QuantificationInfoEditorPanel} and add it to the
	 * quantificationInfoEditors list and attach to the parent container
	 *
	 * @return
	 *
	 * @return
	 */
	protected QuantificationInfoEditorPanel addNewQuantificationInfoEditorPanel() {
		final QuantificationInfoEditorPanel quantificationDataflowPanel = new QuantificationInfoEditorPanel(sessionID,
				importJobID);

		quantPanel.addButton("Quantification data (" + numQuantData++ + ")", quantificationDataflowPanel);

		ChangeHandler handler = new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				final Object source = event.getSource();
				if (source instanceof ListBox) {
					ListBox listBox = (ListBox) source;
					if (listBox.getSelectedIndex() > 0) {
						quantPanel.setUpFaceImage(new Image(MyClientBundle.INSTANCE.experimentIconFull()),
								quantificationDataflowPanel);
					} else {
						quantPanel.setUpFaceImage(new Image(MyClientBundle.INSTANCE.experimentIconBlank()),
								quantificationDataflowPanel);
					}
				}
			}
		};
		// when some checkbox is selected, set the full icon in the image
		quantificationDataflowPanel.addChangeHandlerForComboBoxes(handler);
		quantificationInfoEditors.add(quantificationDataflowPanel);
		return quantificationDataflowPanel;

	}

	private ClickHandler removeIdDataPanelHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// remove from parent the last editor
				if (!identificationInfoEditors.isEmpty()) {
					final IdentificationInfoEditorPanel identificationInfoEditor = identificationInfoEditors
							.get(identificationInfoEditors.size() - 1);
					identificationInfoEditor.removeFromParent();
					identificationInfoEditor.unregisterAsListener();
					identPanel.removeButton(identificationInfoEditor);
					identificationInfoEditors.remove(identificationInfoEditor);
					numIdData--;
				}

			}
		};
	}

	private ClickHandler addIdDataPanelHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addNewIdentificationDataPanel();

			}
		};
	}

	/**
	 * Creates a new {@link IdentificationInfoEditorPanel} and add it to the
	 * identificationInfoEditors list and attach to the parent container
	 *
	 * @return
	 */
	protected IdentificationInfoEditorPanel addNewIdentificationDataPanel() {
		final IdentificationInfoEditorPanel identificationDataflowPanel = new IdentificationInfoEditorPanel(sessionID,
				importJobID);

		identPanel.addButton("Identification data (" + numIdData++ + ")", identificationDataflowPanel);
		ChangeHandler handler = new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				final Object source = event.getSource();
				if (source instanceof ListBox) {
					ListBox listBox = (ListBox) source;
					if (listBox.getSelectedIndex() > 0) {
						identPanel.setUpFaceImage(new Image(MyClientBundle.INSTANCE.experimentIconFull()),
								identificationDataflowPanel);
					} else {
						identPanel.setUpFaceImage(new Image(MyClientBundle.INSTANCE.experimentIconBlank()),
								identificationDataflowPanel);
					}
				}
			}
		};
		// when some checkbox is selected, set the full icon in the image
		identificationDataflowPanel.addChangeHandlerForComboBoxes(handler);

		identificationInfoEditors.add(identificationDataflowPanel);
		return identificationDataflowPanel;

	}

	private void updateExperimentalConditionTypeBeanFromGUI() {
		// identification info
		if (identificationInfoEditors.isEmpty()) {
			conditionBean.setIdentificationInfo(null);
		} else {
			if (conditionBean.getIdentificationInfo() == null) {
				conditionBean.setIdentificationInfo(new IdentificationInfoTypeBean());
			}
			conditionBean.getIdentificationInfo().getExcelIdentInfo().clear();
			conditionBean.getIdentificationInfo().getRemoteFilesIdentInfo().clear();
			for (IdentificationInfoEditorPanel identificationInfoEditor : identificationInfoEditors) {
				final IdentificationExcelTypeBean excelIdentificationInfoBean = identificationInfoEditor
						.getIdentificationExcelTypeBean();
				if (excelIdentificationInfoBean != null) {
					conditionBean.getIdentificationInfo().getExcelIdentInfo().add(excelIdentificationInfoBean);
				}
				final RemoteInfoTypeBean remoteFileInfoBean = identificationInfoEditor.getRemoteInfoTypeBean();
				if (remoteFileInfoBean != null) {
					conditionBean.getIdentificationInfo().getRemoteFilesIdentInfo().add(remoteFileInfoBean);
				}
			}
		}
		if (quantificationInfoEditors.isEmpty()) {
			conditionBean.setQuantificationInfo(null);
		} else {
			// quantification info
			if (conditionBean.getQuantificationInfo() == null) {
				conditionBean.setQuantificationInfo(new QuantificationInfoTypeBean());
			}
			conditionBean.getQuantificationInfo().getExcelQuantInfo().clear();
			conditionBean.getQuantificationInfo().getRemoteFilesQuantInfo().clear();
			for (QuantificationInfoEditorPanel quantificationInfoEditor : quantificationInfoEditors) {
				final QuantificationExcelTypeBean excelQuantificationInfoBean = quantificationInfoEditor
						.getQuantificationExcelTypeBean();
				if (excelQuantificationInfoBean != null) {
					conditionBean.getQuantificationInfo().getExcelQuantInfo().add(excelQuantificationInfoBean);
				}
				final RemoteInfoTypeBean remoteFileInfoBean = quantificationInfoEditor.getRemoteInfoTypeBean();
				if (remoteFileInfoBean != null) {
					conditionBean.getQuantificationInfo().getRemoteFilesQuantInfo().add(remoteFileInfoBean);
				}
			}
		}
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		if (tabNameChanger != null) {
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(this);
			final String newConditionName = projectObjectRepresenter.getID();
			tabNameChanger.changeTabName(newConditionName);
		}
		if (conditionBean.getIdentificationInfo() != null) {

			for (IdentificationExcelTypeBean identificationExcelTypeBean : conditionBean.getIdentificationInfo()
					.getExcelIdentInfo()) {

				// search in the already created objects
				boolean found = false;
				for (IdentificationInfoEditorPanel identificationInfoEditorPanel : identificationInfoEditors) {
					if (identificationExcelTypeBean
							.equals(identificationInfoEditorPanel.getIdentificationExcelTypeBean())) {
						found = true;
					}
				}
				if (!found) {
					final IdentificationInfoEditorPanel newIdentificationDataPanel = addNewIdentificationDataPanel();
					newIdentificationDataPanel.setIdentificationExcelTypeBean(identificationExcelTypeBean);
					idDataIncrementableTextBox.setValue(idDataIncrementableTextBox.getIntegerNumber() + 1);
				}
			}

			for (RemoteInfoTypeBean remoteInfoTypeBean : conditionBean.getIdentificationInfo()
					.getRemoteFilesIdentInfo()) {
				// look the file refs if they exists, otherwise, set them to
				// null in order to match with the remoteInfoType objects
				// returned by the editors
				final Iterator<String> fileRefsIterator = remoteInfoTypeBean.getFileRefs().iterator();
				while (fileRefsIterator.hasNext()) {
					final String fileRef = fileRefsIterator.next();
					if (!ProjectCreatorRegister.containsAnyObjectRepresenterWithId(fileRef)) {
						fileRefsIterator.remove();
					}
				}

				// search in the already created objects
				boolean found = false;
				for (IdentificationInfoEditorPanel identificationInfoEditorPanel : identificationInfoEditors) {
					final RemoteInfoTypeBean remoteInfoTypeBean2 = identificationInfoEditorPanel
							.getRemoteInfoTypeBean();
					if (remoteInfoTypeBean2 != null && remoteInfoTypeBean2.equals(remoteInfoTypeBean)) {
						found = true;
					}
				}
				if (!found) {
					final IdentificationInfoEditorPanel newIdentificationDataPanel = addNewIdentificationDataPanel();
					newIdentificationDataPanel.setRemoteInfoTypeBean(remoteInfoTypeBean);
					idDataIncrementableTextBox.setValue(idDataIncrementableTextBox.getIntegerNumber() + 1);
				}
			}

		}
		if (conditionBean.getQuantificationInfo() != null) {

			for (QuantificationExcelTypeBean quantificationExcelTypeBean : conditionBean.getQuantificationInfo()
					.getExcelQuantInfo()) {

				// search in the already created objects
				boolean found = false;
				for (QuantificationInfoEditorPanel quantificationInfoEditorPanel : quantificationInfoEditors) {
					if (quantificationInfoEditorPanel.getQuantificationExcelTypeBean()
							.equals(quantificationExcelTypeBean)) {
						found = true;
					}
				}
				if (!found) {
					final QuantificationInfoEditorPanel newQuantificationInfoEditorPanel = addNewQuantificationInfoEditorPanel();
					newQuantificationInfoEditorPanel.setQuantificationExcelTypeBean(quantificationExcelTypeBean);
					quantDataIncrementableTextBox.setValue(quantDataIncrementableTextBox.getIntegerNumber() + 1);
				}
			}

			for (RemoteInfoTypeBean remoteInfoTypeBean : conditionBean.getQuantificationInfo()
					.getRemoteFilesQuantInfo()) {
				// look the file refs if they exists, otherwise, set them to
				// null in order to match with the remoteInfoType objects
				// returned by the editors
				final Iterator<String> fileRefsIterator = remoteInfoTypeBean.getFileRefs().iterator();
				while (fileRefsIterator.hasNext()) {
					final String fileRef = fileRefsIterator.next();
					if (!ProjectCreatorRegister.containsAnyObjectRepresenterWithId(fileRef)) {
						fileRefsIterator.remove();
					}
				}
				// search in the already created objects
				boolean found = false;
				for (QuantificationInfoEditorPanel quantificationInfoEditorPanel : quantificationInfoEditors) {
					final RemoteInfoTypeBean remoteInfoTypeBean2 = quantificationInfoEditorPanel
							.getRemoteInfoTypeBean();
					if (remoteInfoTypeBean2 != null && remoteInfoTypeBean2.equals(remoteInfoTypeBean)) {
						found = true;
					}
				}
				if (!found) {
					final QuantificationInfoEditorPanel newQuantificationInfoEditorPanel = addNewQuantificationInfoEditorPanel();
					newQuantificationInfoEditorPanel.setRemoteInfoTypeBean(remoteInfoTypeBean);
					quantDataIncrementableTextBox.setValue(quantDataIncrementableTextBox.getIntegerNumber() + 1);
				}
			}

		}
	}

	/**
	 * Sets the {@link TabNameChanger} which will be called when
	 * updateGUIReferringToDataObjects is called.
	 *
	 * @param tabNameChanger
	 *            the tabNameChanger to set
	 */
	public void setTabNameChanger(TabNameChanger tabNameChanger) {
		this.tabNameChanger = tabNameChanger;
	}

	public ExperimentalConditionTypeBean getExperimentalConditionTypeBean() {
		updateExperimentalConditionTypeBeanFromGUI();
		return conditionBean;
	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}

	@Override
	public void onResize() {
		for (int i = 0; i < mainPanel.getWidgetCount(); i++) {
			Widget child = mainPanel.getWidget(i);
			if (child instanceof RequiresResize) {
				((RequiresResize) child).onResize();
			}
		}
	}

}
