package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class MyExcelSectionFlowPanel extends FlowPanel {
	private final SimplePanel optionPanelContainer;
	private final ContainsExcelColumnRefPanelAndTable optionPanel;
	private MouseOverHandler mouseOverHandler;
	private final SimplePanel tableContainer;

	/**
	 * Creates a FlowPanel with a title (if labelText is not null). It will have
	 * a MouseOverHandler that when the mouse is over the optionPanel (
	 * {@link ContainsExcelColumnRefPanelAndTable} ) will be set over the
	 * {@link SimplePanel} of optionPanelContainer. A {@link ScrollPanel} is
	 * also provided that will get the table from the optionPanel when that
	 * event occurs.
	 *
	 * @param labelText
	 *            if null, no label will be created and added
	 * @param optionPanelContainer
	 * @param optionPanel
	 * @param tableContainer
	 */
	public MyExcelSectionFlowPanel(String labelText, SimplePanel optionPanelContainer,
			ContainsExcelColumnRefPanelAndTable optionPanel, SimplePanel tableContainer) {
		this.optionPanel = optionPanel;
		this.optionPanelContainer = optionPanelContainer;
		this.tableContainer = tableContainer;
		if (optionPanel != null)
			this.optionPanel.setRelatedExcelSectionFlowPanel(this);

		addDomHandler(getMouseOverHandler(), MouseOverEvent.getType());
		setStyleName("IdentificationInfoFromExcelHoverPanel");

		if (labelText != null) {
			Label label = new Label(labelText);
			this.add(label);
		}
	}

	private MouseOverHandler getMouseOverHandler() {
		if (mouseOverHandler == null) {
			mouseOverHandler = new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					MyExcelSectionFlowPanelSelector.getInstance().selectPanel(MyExcelSectionFlowPanel.this);
					optionPanelContainer.setWidget(optionPanel);

					ScrollPanel scrollPanel = new ScrollPanel();
					scrollPanel.setHeight("400px");
					scrollPanel.add(optionPanel.getTable());

					tableContainer.setWidget(scrollPanel);
				}
			};
		}
		return mouseOverHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#removeFromParent()
	 */
	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub
		super.removeFromParent();
	}

	public void unselect() {
		setStyleName("IdentificationInfoFromExcelHoverPanel");

	}

	public void select() {
		setStyleName("IdentificationInfoFromExcelHoverPanelSelected");
	}

}
