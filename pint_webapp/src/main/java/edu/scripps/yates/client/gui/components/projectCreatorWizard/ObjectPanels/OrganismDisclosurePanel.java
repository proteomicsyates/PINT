package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import java.util.List;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;

import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.HasId;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;

public class OrganismDisclosurePanel extends ClosableWithTitlePanel {
	private OrganismTypeBean organism = new OrganismTypeBean();
	private final TextBox taxID;
	private static int numOrganism = 1;
	private final edu.scripps.yates.client.ImportWizardServiceAsync service = ImportWizardServiceAsync.Util
			.getInstance();

	public OrganismDisclosurePanel(String sessionID, int importJobID) {
		super(sessionID, importJobID, "Organism " + numOrganism++, true);

		super.textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireModificationEvent();
			}
		});
		Label tissueIDLabel = new Label("Organism Taxonomy ID:");
		final String title = "The taxonomy ID is automatically filled when writting the appropiate organism name in the item box title";
		tissueIDLabel.setTitle(title);
		addWidget(tissueIDLabel);
		taxID = new TextBox();
		taxID.setEnabled(false);
		taxID.setTitle(title);
		addWidget(taxID);
		service.getOrganismList(sessionID, new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				final MultiWordSuggestOracle suggestOracle = (MultiWordSuggestOracle) textBox.getSuggestOracle();
				suggestOracle.addAll(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);

			}
		});
		// add selection handler
		textBox.addSelectionHandler(getSuggestionSelectionHandler());
		updateRepresentedObject();

		fireModificationEvent();

	}

	private SelectionHandler<Suggestion> getSuggestionSelectionHandler() {
		SelectionHandler<Suggestion> handler = new SelectionHandler<Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				final Suggestion selectedItem = event.getSelectedItem();
				if (selectedItem != null) {
					String selectedOrganism = selectedItem.getReplacementString();
					// ask for the tax ID
					service.getTaxId(sessionID, selectedOrganism, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(String result) {
							taxID.setText(result);
						}
					});
				}
			}
		};
		return handler;
	}

	@Override
	public OrganismTypeBean getObject() {
		return organism;
	}

	@Override
	public void updateRepresentedObject() {
		organism.setDescription(taxID.getText());
		organism.setId(getID());
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof OrganismTypeBean) {
			organism = (OrganismTypeBean) object;
			updateGUIFromObjectData();
		}

	}

	@Override
	public void updateGUIFromObjectData() {

		// set name
		setId(organism.getId());
		// set tax ID
		taxID.setText(organism.getDescription());

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
		final OrganismTypeBean organism = getObject();
		if (organism.getId() == null || "".equals(organism.getId()))
			return false;
		return true;
	}

}
