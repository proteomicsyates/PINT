package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import java.util.List;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.cache.ClientCacheGeneralObjects;
import edu.scripps.yates.client.cache.GeneralObject;
import edu.scripps.yates.shared.model.interfaces.HasId;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;

public class TissueDisclosurePanel extends ClosableWithTitlePanel {
	private TissueTypeBean tissue = new TissueTypeBean();
	private final TextBox descriptionTextBox;
	private static int numTissues = 1;
	private final edu.scripps.yates.ImportWizardServiceAsync service = ImportWizardServiceAsync.Util
			.getInstance();

	public TissueDisclosurePanel(String sessionID, int importJobID) {
		super(sessionID, importJobID, "Sample origin " + numTissues++, true);
		Label descriptionLabel = new Label("Description:");
		addWidget(descriptionLabel);
		descriptionTextBox = new TextBox();
		descriptionTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireModificationEvent();
			}
		});
		addWidget(descriptionTextBox);
		// look into general objects cache first
		if (ClientCacheGeneralObjects.getInstance().contains(GeneralObject.TISSUE_LIST)) {
			loadTissueListInSuggestionBox(
					ClientCacheGeneralObjects.getInstance().getFromCache(GeneralObject.TISSUE_LIST));
			return;
		}

		updateRepresentedObject();

		fireModificationEvent();
	}

	private void loadTissueListInSuggestionBox(List<String> result) {
		if (result != null) {
			final MultiWordSuggestOracle suggestOracle = (MultiWordSuggestOracle) textBox.getSuggestOracle();
			suggestOracle.addAll(result);
		}
	}

	@Override
	public TissueTypeBean getObject() {
		return tissue;
	}

	@Override
	public void updateRepresentedObject() {
		tissue.setId(getID());
		tissue.setDescription(descriptionTextBox.getText());
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof TissueTypeBean) {
			tissue = (TissueTypeBean) object;
			updateGUIFromObjectData();
		}

	}

	@Override
	public void updateGUIFromObjectData() {

		// set name
		setId(tissue.getId());
		descriptionTextBox.setText(tissue.getDescription());

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
		final TissueTypeBean tissue = getObject();
		if (tissue.getId() == null || "".equals(tissue.getId()))
			return false;
		return true;

	}

}
