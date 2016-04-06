package edu.scripps.yates.client.gui.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;

import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.model.ProteinProjection;
import edu.scripps.yates.shared.util.sublists.QueryResultSubLists;

public class MyQueryEditorPanel extends FlowPanel {
	private final CommandSuggestion simpleQueryCommandSuggestion = new CommandSuggestion();
	private final CommandSuggestion commandSuggestion = new CommandSuggestion();

	private final TextArea textArea;
	// private final SuggestBox suggestBox ;

	private final InlineHTML inlinelabelSendingStatus;
	private final Anchor linkToProteinResults;
	private final Anchor linkToProteinGroupResults;
	private final ListBox uniprotVersionListBox;
	private final FlowPanel flowPanelLinks;
	private final Label lblProteins;
	private final Label lblProteinGroups;
	private static final String defaultInlineSendingStatus = "Write a query and click on 'Send query'";
	private final Label lblDownloadDataHere;
	private final FlexTable resultSummaryGrid;
	private final Label lblNumberOfProteinGroups;
	private final Label numProteinGroupsLabel;
	private final Label lblNumberOfProteins;
	private final Label numProteinsLabel;
	private final Label lblNumberOfPsms;
	private final Label numPSMsLabel;
	private final Label lblNumberOfDifferent;
	private final Label numDifferentSequencesLabel;
	private final FlowPanel flowPanel_1;
	private final FlexTable flexTable;
	private final Label lblNumberOfFeatures;

	private final SuggestBox simpleQueryTextBox;

	private final Label queryDisabledLabel;

	private final Button buttonSendQuery;
	private final Map<String, Set<ProteinProjection>> proteinProjectionsByProteinAcc = new HashMap<String, Set<ProteinProjection>>();
	private final Map<String, Set<ProteinProjection>> proteinProjectionsByProteinDescription = new HashMap<String, Set<ProteinProjection>>();
	private final Map<String, Set<ProteinProjection>> proteinProjectionsByGeneName = new HashMap<String, Set<ProteinProjection>>();
	private final Image simpleQueryReadyState;
	private final Button sendSimpleQueryButton;

	public MyQueryEditorPanel(ClickHandler sendQueryclickHandler, ClickHandler sendSimpleQueryclickHandler) {
		setStyleName("QueryPanel");
		setWidth("100%");
		// tabLayoutPanel.add(this, "Query", false);

		InlineHTML nlnhtmlWriteYourQuery = new InlineHTML("Write your queries in the boxes below:");
		this.add(nlnhtmlWriteYourQuery);

		CaptionPanel captionPanelSimpleQueryEditor = new CaptionPanel("Simple Query Editor");
		captionPanelSimpleQueryEditor.setStyleName("QueryPanel-horizontal");
		this.add(captionPanelSimpleQueryEditor);
		captionPanelSimpleQueryEditor.setSize("90%", "30%");
		FlowPanel flow1 = new FlowPanel();
		flow1.setStyleName("horizontalComponent");
		Label label = new Label("Type here to search for protein names, protein accessions or gene names: ");
		label.getElement().getStyle().setProperty("margin", "10px");
		label.setStyleName("horizontalComponent");
		flow1.add(label);
		simpleQueryTextBox = new SuggestBox(simpleQueryCommandSuggestion);
		simpleQueryTextBox.setEnabled(false);
		simpleQueryTextBox.setLimit(50);
		simpleQueryTextBox.setStyleName("horizontalComponent");
		((DefaultSuggestionDisplay) simpleQueryTextBox.getSuggestionDisplay()).setAnimationEnabled(true);
		simpleQueryTextBox.getElement().getStyle().setProperty("margin", "10px");
		flow1.add(simpleQueryTextBox);
		simpleQueryReadyState = new Image(MyClientBundle.INSTANCE.smallLoader());
		simpleQueryReadyState.setTitle("Initializing simple queries");
		flow1.add(simpleQueryReadyState);
		sendSimpleQueryButton = new Button("Go");
		sendSimpleQueryButton.setEnabled(false);
		sendSimpleQueryButton.addClickHandler(sendSimpleQueryclickHandler);
		sendSimpleQueryButton.getElement().getStyle().setProperty("margin", "10px");
		flow1.add(sendSimpleQueryButton);
		captionPanelSimpleQueryEditor.setContentWidget(flow1);

		CaptionPanel captionPanelQueryEditor = new CaptionPanel("Advanced Query Editor");
		captionPanelQueryEditor.setStyleName("QueryPanel-horizontal");
		this.add(captionPanelQueryEditor);
		captionPanelQueryEditor.setSize("90%", "30%");

		FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName("verticalComponent");
		queryDisabledLabel = new Label(
				"Regular queries are disabled for this project. Try inspecting single proteins in the text editor above");
		queryDisabledLabel.setVisible(false);
		flowPanel.add(queryDisabledLabel);
		// suggestBox = new SuggestBox(commandSuggestion,
		// new com.google.gwt.user.client.ui.TextArea());
		textArea = new TextArea();
		textArea.setHeight("100%");
		textArea.setWidth("100%");
		flowPanel.add(textArea);

		captionPanelQueryEditor.setContentWidget(flowPanel);
		getQueryTextBox().setSize("400px", "132px");

		// uniprot versions
		FlowPanel flow2 = new FlowPanel();
		flow2.setStyleName("QueryPanel-vertical");
		InlineHTML inLineHtml = new InlineHTML("Available Uniprot Annotations for the selected projects:");
		flow2.add(inLineHtml);
		uniprotVersionListBox = new ListBox(false);
		flow2.add(uniprotVersionListBox);
		this.add(flow2);

		flowPanel = new FlowPanel();
		flowPanel.setStyleName("QueryPanel-vertical");
		add(flowPanel);

		buttonSendQuery = new Button("Send query");
		buttonSendQuery.addClickHandler(sendQueryclickHandler);

		inlinelabelSendingStatus = new InlineHTML(defaultInlineSendingStatus);

		inlinelabelSendingStatus.setWidth("354px");

		Grid buttonAndStatusGrid = new Grid(1, 2);
		buttonAndStatusGrid.setCellSpacing(5);
		flowPanel.add(buttonAndStatusGrid);
		buttonAndStatusGrid.setWidget(0, 0, inlinelabelSendingStatus);
		buttonAndStatusGrid.setWidget(0, 1, buttonSendQuery);

		flowPanel_1 = new FlowPanel();
		flowPanel_1.setStyleName("QueryPanel-vertical");
		add(flowPanel_1);

		flexTable = new FlexTable();
		flowPanel_1.add(flexTable);

		Label lblForHelpAbout = new Label(
				"Use the pulldown menus on the left for building your queries. For more complex queries download guide here:");
		lblForHelpAbout.setWordWrap(false);
		flexTable.setWidget(0, 0, lblForHelpAbout);

		Anchor lblLink = new Anchor(true);
		lblLink.setHref("PINT_help_Query_Commands.pdf");
		lblLink.setTarget("_blank");
		lblLink.setText("(here)");
		lblLink.setStyleName("linkPINT");
		flexTable.setWidget(0, 1, lblLink);

		flowPanelLinks = new FlowPanel();
		flowPanelLinks.setStyleName("QueryPanel-vertical");
		add(flowPanelLinks);
		flowPanelLinks.setSize("100%", "142px");

		resultSummaryGrid = new FlexTable();
		resultSummaryGrid.setStyleName("QueryPanel-horizontal");
		flowPanelLinks.add(resultSummaryGrid);
		resultSummaryGrid.setWidth("");

		lblNumberOfFeatures = new Label("Number of features in current dataset:");
		resultSummaryGrid.setWidget(0, 0, lblNumberOfFeatures);

		lblNumberOfProteinGroups = new Label("Number of protein groups:");
		resultSummaryGrid.setWidget(1, 0, lblNumberOfProteinGroups);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(4, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		numProteinGroupsLabel = new Label("-");
		resultSummaryGrid.setWidget(1, 1, numProteinGroupsLabel);
		resultSummaryGrid.getCellFormatter().setWidth(4, 1, "");

		lblNumberOfProteins = new Label("Number of proteins:");
		resultSummaryGrid.setWidget(2, 0, lblNumberOfProteins);

		numProteinsLabel = new Label("-");
		resultSummaryGrid.setWidget(2, 1, numProteinsLabel);

		lblNumberOfPsms = new Label("Number of PSMs:");
		resultSummaryGrid.setWidget(3, 0, lblNumberOfPsms);

		numPSMsLabel = new Label("-");
		resultSummaryGrid.setWidget(3, 1, numPSMsLabel);

		lblNumberOfDifferent = new Label("Number of different sequences:");
		resultSummaryGrid.setWidget(4, 0, lblNumberOfDifferent);

		numDifferentSequencesLabel = new Label("-");
		resultSummaryGrid.setWidget(4, 1, numDifferentSequencesLabel);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(4, 1, HasVerticalAlignment.ALIGN_MIDDLE);

		lblDownloadDataHere = new Label("Download data here:");
		resultSummaryGrid.setWidget(0, 2, lblDownloadDataHere);

		lblProteins = new Label("Proteins:");
		resultSummaryGrid.setWidget(1, 2, lblProteins);

		lblProteinGroups = new Label("Protein Groups:");
		resultSummaryGrid.setWidget(2, 2, lblProteinGroups);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(2, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(3, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(1, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(2, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(2, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(3, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(3, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getFlexCellFormatter().setColSpan(1, 2, 1);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getFlexCellFormatter().setColSpan(0, 0, 2);

		linkToProteinResults = new Anchor(true);
		linkToProteinResults.setStyleName("linkPINT");
		resultSummaryGrid.setWidget(1, 3, linkToProteinResults);

		linkToProteinGroupResults = new Anchor(true);
		linkToProteinGroupResults.setStyleName("linkPINT");
		resultSummaryGrid.setWidget(2, 3, linkToProteinGroupResults);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(1, 3, HasHorizontalAlignment.ALIGN_LEFT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(1, 3, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(2, 3, HasHorizontalAlignment.ALIGN_LEFT);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(2, 3, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(1, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		resultSummaryGrid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		resultSummaryGrid.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_MIDDLE);
		resultSummaryGrid.getFlexCellFormatter().setColSpan(0, 2, 2);

	}

	public void enableQueries(boolean enable) {
		queryDisabledLabel.setVisible(!enable);
		textArea.setVisible(enable);
		buttonSendQuery.setEnabled(enable);
	}

	/**
	 * @return the inlinelabelSendingStatus
	 */
	public void setSendingStatusText(String text) {
		if (text != null)
			inlinelabelSendingStatus.setText(text);
		else
			inlinelabelSendingStatus.setText(defaultInlineSendingStatus);
	}

	/**
	 * @return the textAreaQuery
	 */
	public TextBoxBase getQueryTextBox() {
		return textArea;
	}

	public void addSuggestions(Collection<String> text) {
		commandSuggestion.addAll(text);
	}

	public void addSuggestion(String text) {
		commandSuggestion.add(text);
	}

	private void addSimpleQuerySuggestions(Collection<String> texts) {
		for (String text : texts) {
			addSimpleQuerySuggestion(text);
		}
	}

	private void addSimpleQuerySuggestion(String text) {
		if (text != null) {
			simpleQueryCommandSuggestion.add(text);
		}
	}

	public void addSimpleQuerySuggestionsAsProteinProjections(Collection<ProteinProjection> suggestions) {
		for (ProteinProjection proteinProjection : suggestions) {
			addSimpleQuerySuggestion(proteinProjection.getAcc());
			addSimpleQuerySuggestion(proteinProjection.getDescription());
			addSimpleQuerySuggestion(proteinProjection.getGene());
			addProteinProjection(proteinProjection);
		}

	}

	public void addUniprotVersion(String item, String value) {
		uniprotVersionListBox.addItem(item, value);
	}

	public void addUniprotVersion(String item) {
		uniprotVersionListBox.addItem(item);
		uniprotVersionListBox.setSelectedIndex(0);
	}

	public void clearUniprotVersionList() {
		uniprotVersionListBox.clear();

	}

	public String getSelectedUniprotVersion() {
		final int selectedIndex = uniprotVersionListBox.getSelectedIndex();
		if (selectedIndex > -1)
			return uniprotVersionListBox.getValue(selectedIndex);
		return "";
	}

	public void setLinksToResultsVisible(boolean visible) {
		linkToProteinGroupResults.setVisible(visible);
		linkToProteinResults.setVisible(visible);
	}

	public void updateQueryResult(QueryResultSubLists queryResult) {
		if (queryResult != null) {
			numProteinGroupsLabel.setText(String.valueOf(queryResult.getProteinGroupSubList().getTotalNumber()));
			numProteinsLabel.setText(String.valueOf(queryResult.getProteinSubList().getTotalNumber()));
			numPSMsLabel.setText(String.valueOf(queryResult.getPsmSubList().getTotalNumber()));
			StringBuilder numdiffSequencesText = new StringBuilder();
			numdiffSequencesText.append(queryResult.getNumDifferentSequences());
			if (queryResult.getNumDifferentSequences() != queryResult
					.getNumDifferentSequencesDistinguishingModifieds()) {
				numdiffSequencesText.append(" (" + queryResult.getNumDifferentSequencesDistinguishingModifieds() + ")");
			}
			numDifferentSequencesLabel.setText(numdiffSequencesText.toString());
			numDifferentSequencesLabel.setTitle(
					"Between parenthesis is the number of " + "different peptide sequences by considering different "
							+ "sequences differentially modified peptides");
		} else {
			numProteinGroupsLabel.setText("-");
			numProteinsLabel.setText("-");
			numPSMsLabel.setText("-");
			numDifferentSequencesLabel.setText("-");
		}
	}

	public void setLinksToProteinResults(String href, String text) {

		linkToProteinResults.setVisible(true);
		linkToProteinResults.setHref(href);
		linkToProteinResults.setTarget("_blank");
		linkToProteinResults.setText(text);

	}

	public void setLinksToProteinGroupResults(String href, String text) {

		linkToProteinGroupResults.setVisible(true);
		linkToProteinGroupResults.setHref(href);
		linkToProteinGroupResults.setTarget("_blank");
		linkToProteinGroupResults.setText(text);

	}

	/**
	 * @return the simpleQueryTextBox
	 */
	public SuggestBox getSimpleQueryTextBox() {
		return simpleQueryTextBox;
	}

	private void addProteinProjection(ProteinProjection p) {
		addToMap(proteinProjectionsByGeneName, p, p.getGene());
		addToMap(proteinProjectionsByGeneName, p, p.getAcc());
		addToMap(proteinProjectionsByGeneName, p, p.getDescription());
	}

	private void addToMap(Map<String, Set<ProteinProjection>> map, ProteinProjection p, String key) {
		if (map.containsKey(key)) {
			map.get(key).add(p);
		} else {
			Set<ProteinProjection> set = new HashSet<ProteinProjection>();
			set.add(p);
			map.put(key, set);
		}

	}

	private Set<ProteinProjection> getProteinProjectionsByKey(String key) {
		Set<ProteinProjection> set = new HashSet<ProteinProjection>();
		if (proteinProjectionsByGeneName.containsKey(key)) {
			set.addAll(proteinProjectionsByGeneName.get(key));
		}
		if (proteinProjectionsByProteinAcc.containsKey(key)) {
			set.addAll(proteinProjectionsByProteinAcc.get(key));
		}
		if (proteinProjectionsByProteinDescription.containsKey(key)) {
			set.addAll(proteinProjectionsByProteinDescription.get(key));
		}
		return set;
	}

	public String getTranslatedQuery() {
		final String queryText = simpleQueryTextBox.getText();
		final Set<ProteinProjection> proteinProjectionsByKey = getProteinProjectionsByKey(queryText);
		Set<String> accs = new HashSet<String>();
		// get all the accs
		for (ProteinProjection p : proteinProjectionsByKey) {
			accs.add(p.getAcc());
		}
		// build the query
		StringBuilder sb = new StringBuilder();
		sb.append("ACC[");
		int i = 0;
		for (String acc : accs) {
			i++;
			sb.append(acc);
			if (i != accs.size()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public void enableSimpleQueries(boolean enable) {
		simpleQueryTextBox.setEnabled(enable);
		sendSimpleQueryButton.setEnabled(true);
		simpleQueryReadyState.setVisible(false);

	}
}
