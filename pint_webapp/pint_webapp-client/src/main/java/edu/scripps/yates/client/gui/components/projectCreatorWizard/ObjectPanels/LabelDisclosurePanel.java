package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.shared.model.interfaces.HasId;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;

public class LabelDisclosurePanel extends ClosableWithTitlePanel {
	private LabelTypeBean label = new LabelTypeBean();
	private final DoubleBox massDiff;
	private static int numLabels = 1;

	public LabelDisclosurePanel(String sessionID, int importJobID) {
		super(sessionID, importJobID, "Label " + numLabels++, true);
		Label massDiffLabel = new Label("Mass differente (Da):");
		addWidget(massDiffLabel);
		massDiff = new DoubleBox();
		massDiff.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireModificationEvent();
			}
		});
		addWidget(massDiff);

		// suggest the labels
		List<String> suggestionsforName = new ArrayList<String>();
		suggestionsforName.add("LIGHT");
		suggestionsforName.add("MEDIUM");
		suggestionsforName.add("HEAVY");
		suggestionsforName.add("TMT_6PLEX_126");
		suggestionsforName.add("TMT_6PLEX_127");
		suggestionsforName.add("TMT_6PLEX_128");
		suggestionsforName.add("TMT_6PLEX_129");
		suggestionsforName.add("TMT_6PLEX_130");
		suggestionsforName.add("TMT_6PLEX_131");
		suggestionsforName.add("N14");
		suggestionsforName.add("N15");
		addSuggestions(suggestionsforName);

		updateRepresentedObject();

		fireModificationEvent();
	}

	@Override
	public LabelTypeBean getObject() {
		return label;
	}

	@Override
	public void updateRepresentedObject() {
		label.setId(getID());
		label.setMassDiff(massDiff.getValue());
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof LabelTypeBean) {
			label = (LabelTypeBean) object;
			updateGUIFromObjectData();
		}

	}

	@Override
	public void updateGUIFromObjectData() {
		// set name
		setId(label.getId());
		// set massDiff
		massDiff.setValue(label.getMassDiff());
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
		final LabelTypeBean label = getObject();
		if (label.getId() == null || "".equals(label.getId()))
			return false;
		return true;
	}
}
