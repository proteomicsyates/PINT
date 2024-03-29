package edu.scripps.yates.client.gui.components.projectItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.reactome.ReactomePanel;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class OrganismsItemPanel extends AbstractItemPanel<ProjectBean, OrganismBean> {
	private static OrganismsItemPanel instance;

	public static OrganismsItemPanel getInstance(ProjectBean projectBean, boolean resetItems) {
		if (instance == null) {
			instance = new OrganismsItemPanel(projectBean);
		} else {
			instance.updateParent(projectBean, resetItems);
		}
		return instance;
	}

	private final Label label;
	private final Label descriptionLabel;
	private final Map<String, Set<OrganismBean>> organismBeansByProjectTag = new HashMap<String, Set<OrganismBean>>();

	private OrganismsItemPanel(ProjectBean projectBean) {
		super("Organism(s)", projectBean, true, true);
		// add description panel in the right
		final FlexTable flexTable = new FlexTable();
		final Label labelCondition = new Label("");
		flexTable.setWidget(0, 0, labelCondition);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		labelCondition.setStyleName("ProjectItemContentTitle");
		label = new Label("NCBI tax ID:");
		label.setStyleName("ProjectItemIndividualItemTitle");
		label.setVisible(false);
		flexTable.setWidget(1, 0, label);
		flexTable.getFlexCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		descriptionLabel = new Label();
		flexTable.setWidget(1, 1, descriptionLabel);
		flexTable.getFlexCellFormatter().setAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		updateParent(projectBean, false);

		addRightPanel(flexTable);

		selectFirstItem();
	}

	@Override
	public void updateParent(ProjectBean projectBean, boolean resetItems) {
		if (resetItems) {
			clearItemList();
		}
		if ((projectBean != null && !projectBean.equals(currentParent)) || getItems().isEmpty()) {
			currentParent = projectBean;

			askForOrganismInProject(projectBean.getTag());
			selectFirstItem();

		} else {
			unselectItems();
		}
	}

	private void askForOrganismInProject(final String projectTag) {
		clearItemList();
		if (organismBeansByProjectTag.containsKey(projectTag)) {
			final Set<OrganismBean> organisms = organismBeansByProjectTag.get(projectTag);
			updateOrganisms(organisms);
		} else {
			proteinRetrievingService.getOrganismsByProject(projectTag, new AsyncCallback<Set<OrganismBean>>() {

				@Override
				public void onSuccess(Set<OrganismBean> result) {
					updateOrganisms(result);
					organismBeansByProjectTag.put(projectTag, result);
					if (result.size() == 1) {
						selectFirstItem();
					}
					final String plural = result.size() > 1 ? "s" : "";
					setCaption(result.size() + " Organism" + plural);

					// set the species in the reactome panel
					if (!result.isEmpty()) {
						final String speciesName = result.iterator().next().getId();
						ReactomePanel.getInstance(null).setProjectDataSpecies(speciesName);
						ReactomePanel.getInstance(null).setDataSpecies(speciesName);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					StatusReportersRegister.getInstance()
							.notifyStatusReporters("Error retrieving the organism for this project");
				}
			});
		}
	}

	private void updateOrganisms(Set<OrganismBean> organisms) {
		final List<OrganismBean> list = new ArrayList<OrganismBean>();
		list.addAll(organisms);
		Collections.sort(list);
		for (final OrganismBean organismBean : list) {

			addItemToList(organismBean.getId(), organismBean, "Click here to go to NCBI taxonomy browser");
			// mouseclickhandler to open ncbi
			final ClickHandler clickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					final String urlString = SharedConstants.TAXONOMY_LINK_BY_ID + organismBean.getNcbiTaxID();
					Window.open(urlString, "_blank", "");
				}
			};
			addMouseClickHandlerToItem(organismBean, clickHandler, ClickEvent.getType());

		}

	}

	@Override
	public void selectItem(OrganismBean organismBean) {
		if (organismBean != null) {
			label.setVisible(true);
			descriptionLabel.setText(getStringNotAvailable("NCBI tax ID", organismBean.getNcbiTaxID()));
		} else {
			label.setVisible(false);
			descriptionLabel.setText(null);
		}
	}

}
