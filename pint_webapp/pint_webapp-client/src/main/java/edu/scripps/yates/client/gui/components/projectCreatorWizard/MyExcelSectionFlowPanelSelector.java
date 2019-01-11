package edu.scripps.yates.client.gui.components.projectCreatorWizard;

public class MyExcelSectionFlowPanelSelector {
	private MyExcelSectionFlowPanel selectedPanel;
	private static MyExcelSectionFlowPanelSelector instance;

	public static MyExcelSectionFlowPanelSelector getInstance() {
		if (instance == null) {
			instance = new MyExcelSectionFlowPanelSelector();
		}
		return instance;
	}

	public void selectPanel(MyExcelSectionFlowPanel panel) {
		if (selectedPanel != null) {
			selectedPanel.unselect();
		}
		if (panel != null) {
			selectedPanel = panel;
			selectedPanel.select();
		}
	}
}
