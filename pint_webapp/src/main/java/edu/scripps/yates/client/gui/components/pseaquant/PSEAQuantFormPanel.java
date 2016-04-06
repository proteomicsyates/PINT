package edu.scripps.yates.client.gui.components.pseaquant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import edu.scripps.yates.client.ProteinRetrievalService;
import edu.scripps.yates.client.gui.components.ReplicatePickerPanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.IncrementableTextBox;
import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism;

public class PSEAQuantFormPanel extends PSEAQuantFormAbstractPanel {
	private static final String SEPARATOR = "&*&*&*&*";
	private final ListBox ratioTypesListbox;
	private final LongBox numberOfSamplingsBox;
	private final CheckBox chckbxLiteratureBias;
	private final DoubleBox cvTolFactorBox;
	private final RadioButton rdbtnCv;
	private final RadioButton rdbtnInd;
	private final List<RadioButton> annotationDBRadioButtons = new ArrayList<RadioButton>();
	private final List<RadioButton> quantTypeRadioButtons = new ArrayList<RadioButton>();
	private final Set<String> projectTags = new HashSet<String>();
	private final edu.scripps.yates.client.ProteinRetrievalServiceAsync service = GWT
			.create(ProteinRetrievalService.class);
	private final FlowPanel organismFlowPanel;
	private final IncrementableTextBox numReplicatesIncrementableTextBox;
	private final Map<String, RatioDescriptorBean> ratioDescriptorsByKey = new HashMap<String, RatioDescriptorBean>();
	private final List<RadioButton> organismsRatioButtons = new ArrayList<RadioButton>();
	private final FlowPanel replicatesFlowPanel;
	private final List<ReplicatePickerPanel> replicatePickerList = new ArrayList<ReplicatePickerPanel>();
	private final TextBox emailTextBox;
	private final static int numReplicatesByDefault = 2;
	private final FlowPanel flowPanelResults;
	private final Button sendButton;
	private boolean invalidOrganismError = false;

	public PSEAQuantFormPanel(final Collection<String> projectTags) {
		this.projectTags.addAll(projectTags);

		mainPanel.setStyleName("PSEAQuantPanel");
		mainPanel.setSize("100%", "100%");

		FlowPanel headerFlowPanel = new FlowPanel();
		headerFlowPanel.setStyleName("verticalComponent");
		mainPanel.add(headerFlowPanel);
		headerFlowPanel.setSize("100%", "100px");

		FlexTable flexTable = new FlexTable();
		flexTable.setStyleName("verticalComponent");
		headerFlowPanel.add(flexTable);
		flexTable.setSize("100%", "100%");

		Label lblPseaquantInterfaceFrom = new Label("PSEA-Quant interface from PINT");
		lblPseaquantInterfaceFrom.setStyleName("PSEAQuantPanel-MainTitle-Label ");
		flexTable.setWidget(0, 0, lblPseaquantInterfaceFrom);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

		Label lblNewLabel_4 = new Label(
				"PSEA-Quant is a protein set enrichment analysis algorithm for label-free and label-based mass spectrometry-based quantitative proteomics.\r\nIt identifies protein sets from the Gene Ontology and Molecular Signature databases that are statistically significantly enriched with abundant proteins, which are measured with high reproducibility across a set of replicates. ");
		flexTable.setWidget(1, 0, lblNewLabel_4);

		Label lblNewLabel_5 = new Label("PSEA-Quant web page: ");
		lblNewLabel_5.setStyleName("PSEAQuantPanel-Explanation-Label");
		flexTable.setWidget(2, 0, lblNewLabel_5);
		flexTable.getCellFormatter().setWidth(2, 0, "150px");
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 1);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 1);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);

		InlineHTML nlnhtmlNewInlinehtml = new InlineHTML(
				"<a href=\"http://sealion.scripps.edu:18080/PSEA-Quant/\" class=\"linkPINT\" target=\"_blank\">http://sealion.scripps.edu:18080/PSEA-Quant/</a>");
		flexTable.setWidget(2, 1, nlnhtmlNewInlinehtml);

		FlowPanel mainFlowPanel = new FlowPanel();
		mainFlowPanel.setStyleName("verticalComponent");
		mainPanel.add(mainFlowPanel);
		mainFlowPanel.setSize("100%", "100%");

		FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(flowPanel);

		FlexTable grid_6 = new FlexTable();
		flowPanel.add(grid_6);

		Label lblEnterYourEmail = new Label("Enter your email here:");
		lblEnterYourEmail.setStyleName("PSEAQuantPanel-Title-Label");
		grid_6.setWidget(0, 0, lblEnterYourEmail);
		grid_6.getCellFormatter().setWidth(0, 0, "150");
		grid_6.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);

		emailTextBox = new TextBox();
		grid_6.setWidget(0, 1, emailTextBox);

		Label lblYouWillReceive = new Label(
				"You will receive an email after sending the request with a link to your results");
		lblYouWillReceive.setStyleName("PSEAQuantPanel-Explanation-Label");
		grid_6.setWidget(1, 0, lblYouWillReceive);
		grid_6.getFlexCellFormatter().setColSpan(1, 0, 2);

		FlowPanel step1FlowPanel = new FlowPanel();
		step1FlowPanel.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(step1FlowPanel);

		FlexTable grid = new FlexTable();
		step1FlowPanel.add(grid);

		Label lblNewLabel = new Label("1. Select quantification type:");
		lblNewLabel.setStyleName("PSEAQuantPanel-Title-Label");
		grid.setWidget(0, 0, lblNewLabel);
		grid.getCellFormatter().setWidth(0, 0, "250px");

		Label lblNewLabel_2 = new Label(
				"Select the type of quantification value you want to use: either spectral counts or an specific quantification ratio present in the selector:");
		lblNewLabel_2.setStyleName("PSEAQuantPanel-Explanation-Label");
		grid.setWidget(1, 0, lblNewLabel_2);

		int row = 2;
		for (final PSEAQuantQuantType quantType : PSEAQuantQuantType.values()) {
			RadioButton radio = new RadioButton(PSEAQuantQuantType.getParameterName(), quantType.getText());
			radio.setFormValue(quantType.getQuanttype());
			if (row == 2) {
				radio.setValue(true);
			}
			grid.setWidget(row++, 0, radio);
			quantTypeRadioButtons.add(radio);
			radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					ratioTypesListbox.setEnabled(quantType == PSEAQuantQuantType.BASED
							|| quantType == PSEAQuantQuantType.BASED_PLUS_INVERSE);
				}
			});

		}

		ratioTypesListbox = new ListBox();
		ratioTypesListbox.setEnabled(false);
		ratioTypesListbox.setVisibleItemCount(1);
		grid.setWidget(3, 1, ratioTypesListbox);
		grid.getFlexCellFormatter().setColSpan(1, 0, 2);

		FlowPanel step2FlowPanel = new FlowPanel();
		step2FlowPanel.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(step2FlowPanel);

		Grid organismGrid = new Grid(3, 1);
		step2FlowPanel.add(organismGrid);

		Label lblOrganism = new Label("2. Organism:");
		lblOrganism.setStyleName("PSEAQuantPanel-Title-Label");
		organismGrid.setWidget(0, 0, lblOrganism);

		Label lblProteinsOnYour = new Label(
				"Select the one you want to use. PSEA-Quant will use the annotations (GO or Molecular Signature databases) for that organism.");
		lblProteinsOnYour.setStyleName("PSEAQuantPanel-Explanation-Label");
		organismGrid.setWidget(1, 0, lblProteinsOnYour);

		organismFlowPanel = new FlowPanel();
		organismFlowPanel.setStyleName("PSEAQuantPanel-Step");
		organismFlowPanel.add(new Label("loading organisms..."));
		organismGrid.setWidget(2, 0, organismFlowPanel);
		organismGrid.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);

		FlowPanel step3FlowPanel = new FlowPanel();
		step3FlowPanel.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(step3FlowPanel);

		FlexTable replicatesGrid = new FlexTable();
		step3FlowPanel.add(replicatesGrid);

		Label lblReplicateSelection = new Label("3. Replicate selection:");
		lblReplicateSelection.setStyleName("PSEAQuantPanel-Title-Label");
		replicatesGrid.setWidget(0, 0, lblReplicateSelection);

		Label lblNewLabel_1 = new Label(
				"PSEA-Quant needs data from at least two replicates. Here you can select the number of replicates you are going to use. Then, for each one, select the MSRun(s) or Experimental condition you want to use.");
		lblNewLabel_1.setStyleName("PSEAQuantPanel-Explanation-Label");
		replicatesGrid.setWidget(1, 0, lblNewLabel_1);

		Label lblNumerOfReplicates = new Label("Numer of replicates:");
		replicatesGrid.setWidget(2, 0, lblNumerOfReplicates);
		replicatesGrid.getCellFormatter().setWidth(2, 0, "130px");

		numReplicatesIncrementableTextBox = new IncrementableTextBox(PSEAQuantFormPanel.numReplicatesByDefault);
		replicatesGrid.setWidget(2, 1, numReplicatesIncrementableTextBox);
		replicatesGrid.getFlexCellFormatter().setColSpan(1, 0, 1);
		replicatesGrid.getFlexCellFormatter().setColSpan(0, 0, 2);
		replicatesGrid.getFlexCellFormatter().setColSpan(1, 0, 2);

		replicatesFlowPanel = new FlowPanel();
		replicatesGrid.setWidget(3, 0, replicatesFlowPanel);
		replicatesGrid.getFlexCellFormatter().setColSpan(3, 0, 2);
		numReplicatesIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// create a new ReplicatePicker
				createReplicatePickerPanel();

			}
		});
		numReplicatesIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeLastReplicatePickerPanel();

			}
		});

		// create one per replicate
		for (int i = 0; i < numReplicatesByDefault; i++) {
			createReplicatePickerPanel();
		}

		FlowPanel step4FlowPanel = new FlowPanel();
		step4FlowPanel.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(step4FlowPanel);

		Grid grid_1 = new Grid(3, 1);
		step4FlowPanel.add(grid_1);

		Label lblNumberOf = new Label("4. Number of samplings for statistical significance assessment:");
		lblNumberOf.setStyleName("PSEAQuantPanel-Title-Label");
		grid_1.setWidget(0, 0, lblNumberOf);

		Label lblrecommendedValue = new Label("(Recommended value: 10,000)");
		lblrecommendedValue.setStyleName("PSEAQuantPanel-Explanation-Label");
		grid_1.setWidget(1, 0, lblrecommendedValue);

		numberOfSamplingsBox = new LongBox();
		numberOfSamplingsBox.setAlignment(TextAlignment.CENTER);
		numberOfSamplingsBox.setText("10000");
		grid_1.setWidget(2, 0, numberOfSamplingsBox);
		numberOfSamplingsBox.setWidth("60px");

		FlowPanel step5FlowPanel = new FlowPanel();
		step5FlowPanel.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(step5FlowPanel);

		FlexTable grid_2 = new FlexTable();
		step5FlowPanel.add(grid_2);

		Label lblAnnotationDatabase = new Label("5. Annotation database:");
		lblAnnotationDatabase.setStyleName("PSEAQuantPanel-Title-Label");
		grid_2.setWidget(0, 0, lblAnnotationDatabase);

		final PSEAQuantAnnotationDatabase[] annDBs = PSEAQuantAnnotationDatabase.values();
		row = 1;
		for (PSEAQuantAnnotationDatabase pseaQuantAnnotationDatabase : annDBs) {
			RadioButton radio = new RadioButton(PSEAQuantAnnotationDatabase.getParameterName(),
					pseaQuantAnnotationDatabase.getText());
			radio.setFormValue(pseaQuantAnnotationDatabase.getAnnotationDBName());
			if (row == 1) {
				radio.setValue(true);
			}
			grid_2.setWidget(row++, 0, radio);
			annotationDBRadioButtons.add(radio);
		}

		FlowPanel step6FlowPanel = new FlowPanel();
		step6FlowPanel.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(step6FlowPanel);

		FlexTable grid_3 = new FlexTable();
		step6FlowPanel.add(grid_3);

		Label lblSampingProcedure = new Label("6. Samping procedure:");
		lblSampingProcedure.setStyleName("PSEAQuantPanel-Title-Label");
		grid_3.setWidget(0, 0, lblSampingProcedure);

		rdbtnInd = new RadioButton("sampling", "Assumes protein abundance independence in the dataset.");
		rdbtnInd.setValue(true);
		grid_3.setWidget(1, 0, rdbtnInd);

		rdbtnCv = new RadioButton("sampling", "Assumes protein abundance dependence in the dataset. ");
		grid_3.setWidget(2, 0, rdbtnCv);

		Label lblNewLabel_3 = new Label(
				"If assuming protein abundance dependence, please enter a coefficient of variation tolerance factor (minimum = 0.1): ");
		lblNewLabel_3.setStyleName("PSEAQuantPanel-Explanation-Label");
		grid_3.setWidget(3, 0, lblNewLabel_3);

		Grid grid_4 = new Grid(1, 2);
		grid_3.setWidget(4, 0, grid_4);

		final Label lblCvToleranceFactor = new Label("CV tolerance factor:");
		grid_4.setWidget(0, 0, lblCvToleranceFactor);

		cvTolFactorBox = new DoubleBox();
		cvTolFactorBox.setText("0.5");
		cvTolFactorBox.setEnabled(false);
		grid_4.setWidget(0, 1, cvTolFactorBox);
		cvTolFactorBox.setWidth("49px");
		grid_4.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid_4.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		rdbtnInd.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				cvTolFactorBox.setEnabled(false);
			}
		});
		rdbtnCv.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				cvTolFactorBox.setEnabled(true);
			}
		});

		FlowPanel step7flowPanel = new FlowPanel();
		step7flowPanel.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(step7flowPanel);

		Grid grid_5 = new Grid(2, 1);
		step7flowPanel.add(grid_5);

		Label lblAnnotationDatabase_1 = new Label("7. Litterature bias:");
		lblAnnotationDatabase_1.setStyleName("PSEAQuantPanel-Title-Label");
		grid_5.setWidget(0, 0, lblAnnotationDatabase_1);

		chckbxLiteratureBias = new CheckBox("Assumes protein annotation bias (recommended)");
		chckbxLiteratureBias.setValue(true);
		grid_5.setWidget(1, 0, chckbxLiteratureBias);

		FlowPanel flowPanel_1 = new FlowPanel();
		flowPanel_1.setStyleName("PSEAQuantPanel-Step");
		mainFlowPanel.add(flowPanel_1);

		sendButton = new Button("Send");
		sendButton.setTitle("Click to send data to PSEA-Quant");
		flowPanel_1.add(sendButton);

		flowPanelResults = new FlowPanel();
		flowPanelResults.setStyleName("PSEAQuantPanel-Step");
		flowPanelResults.add(super.getResultscontainer());
		mainFlowPanel.add(flowPanelResults);
		sendButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sendQuery();

			}
		});

		loadDataFromCurrentDataset();

	}

	protected void sendQuery() {
		try {
			checkInputParameters();
			PSEAQuantFormPanel.super.sendPSEAQuantQuery();
		} catch (IllegalArgumentException e) {
			super.showLoadingDialog("ERROR IN INPUT PARAMETERS: " + e.getMessage(), true, true, false);
		}

	}

	private void checkInputParameters() {

		final List<PSEAQuantReplicate> replicates = getReplicates();
		if (replicates == null || replicates.isEmpty()) {
			throw new IllegalArgumentException("No replicates selected");
		}
		if (replicates != null && replicates.size() < 2) {
			throw new IllegalArgumentException("Select at least two replicates");
		}
		int i = 1;
		for (PSEAQuantReplicate pseaQuantReplicate : replicates) {
			final List<Map<String, String>> listOfPairs = pseaQuantReplicate.getListOfPairs();
			if (listOfPairs == null || listOfPairs.isEmpty()) {
				throw new IllegalArgumentException("Select data (MSRun or Condition) for replicate number " + i);
			}
			i++;
		}
		final PSEAQuantSupportedOrganism organism = getOrganism();
		if (invalidOrganismError || organism == null) {
			final PSEAQuantSupportedOrganism[] values = PSEAQuantSupportedOrganism.values();
			StringBuilder sb = new StringBuilder();
			for (PSEAQuantSupportedOrganism pseaQuantSupportedOrganism : values) {
				if (!"".equals(sb.toString()))
					sb.append(",");
				sb.append(pseaQuantSupportedOrganism.getOrganismName());
			}
			throw new IllegalArgumentException(
					"Organism not supported by PSEA-Quant. Valid organisms are: " + sb.toString());
		}
		if (getNumberOfSamplings() < 1) {
			throw new IllegalArgumentException("Enter a valid number of number of samplings. Recommended value: 10000");
		}
		if (getCVTol() == PSEAQuantCVTol.CV && getCVTolFactor() < 0.1) {
			throw new IllegalArgumentException("Coefficient of variation tolerance factor cannot be less than 0.1");
		}
		if ("".equals(getEmail())) {
			throw new IllegalArgumentException("Enter an email for receiving the results");
		}
		if (!isValidEmailAddress(getEmail())) {
			throw new IllegalArgumentException("Email address is not valid");
		}

	}

	private boolean isValidEmailAddress(String email) {
		// boolean result = true;
		// try {
		// InternetAddress emailAddr = new InternetAddress(email);
		// emailAddr.validate();
		// } catch (AddressException ex) {
		// result = false;
		// }
		// return result;
		return true;
	}

	protected void removeLastReplicatePickerPanel() {
		if (!replicatePickerList.isEmpty()) {
			replicatePickerList.get(replicatePickerList.size() - 1).removeFromParent();
			replicatePickerList.remove(replicatePickerList.size() - 1);
		}
	}

	protected void createReplicatePickerPanel() {
		ReplicatePickerPanel panel = new ReplicatePickerPanel(projectTags);
		replicatesFlowPanel.add(panel);
		replicatePickerList.add(panel);
	}

	private void loadDataFromCurrentDataset() {
		loadOrganisms();
		loadRatios();
	}

	private void loadRatios() {
		service.getRatioDescriptorsFromProjects(projectTags, new AsyncCallback<List<RatioDescriptorBean>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<RatioDescriptorBean> result) {

				ratioTypesListbox.clear();
				ratioDescriptorsByKey.clear();
				for (RatioDescriptorBean ratioDescriptorBean : result) {
					String projectName = ratioDescriptorBean.getProjectTag() + "-";
					if (projectTags.size() == 1) {
						projectName = "";
					}
					final String value = projectName + SEPARATOR + ratioDescriptorBean.getRatioName();
					ratioTypesListbox.addItem(projectName + ratioDescriptorBean.getRatioName() + "\t("
							+ ratioDescriptorBean.getCondition1Name() + " / " + ratioDescriptorBean.getCondition2Name()
							+ ")", value);
					ratioDescriptorsByKey.put(value, ratioDescriptorBean);
				}
			}
		});

	}

	@Override
	public RatioDescriptorBean getRatioDescriptor() {
		for (RadioButton radio : quantTypeRadioButtons) {
			if (radio.getFormValue().equals(PSEAQuantQuantType.BASED.getQuanttype())
					|| radio.getFormValue().equals(PSEAQuantQuantType.BASED_PLUS_INVERSE.getQuanttype())) {
				if (ratioTypesListbox.getSelectedIndex() >= 0) {
					final String value = ratioTypesListbox.getValue(ratioTypesListbox.getSelectedIndex());
					if (ratioDescriptorsByKey.containsKey(value)) {
						return ratioDescriptorsByKey.get(value);
					}
				}
			}
		}
		return null;
	}

	private void loadOrganisms() {
		organismFlowPanel.clear();
		organismsRatioButtons.clear();
		for (String projectTag : projectTags) {
			service.getOrganismsByProject(projectTag, new AsyncCallback<Set<OrganismBean>>() {

				@Override
				public void onSuccess(Set<OrganismBean> result) {
					int numValidOrganisms = 0;
					StringBuilder invalidOrganismsString = new StringBuilder();
					for (OrganismBean organismBean : result) {
						PSEAQuantSupportedOrganism organismName = null;
						if (organismBean.getNcbiTaxID() != null) {
							try {
								organismName = PSEAQuantSupportedOrganism
										.getValueFromOrganismName(Integer.valueOf(organismBean.getNcbiTaxID()));
							} catch (Exception e) {

							}
						}
						if (organismName == null) {
							organismName = PSEAQuantSupportedOrganism.getValueFromOrganismName(organismBean.getId());
						}
						if (organismName != null) {
							numValidOrganisms++;
							String radioLabel = organismBean.getId();
							if (organismBean.getNcbiTaxID() != null && !"".equals(organismBean.getNcbiTaxID())) {
								radioLabel += " (" + organismBean.getNcbiTaxID() + ")";
							}
							RadioButton radioButton = new RadioButton("Organism", radioLabel);
							organismFlowPanel.add(radioButton);
							organismsRatioButtons.add(radioButton);
						} else {
							if (!"".equals(invalidOrganismsString.toString())) {
								invalidOrganismsString.append(",");
							}
							invalidOrganismsString.append(organismBean.getId());
						}
					}
					if (numValidOrganisms == 0) {
						HtmlTemplates template = GWT.create(HtmlTemplates.class);

						organismFlowPanel.add(new InlineHTML(template.coloredDiv("red",
								"No valid organism for this dataset: '" + invalidOrganismsString + "'")));
						StringBuilder supportedOrganisms = new StringBuilder();
						for (PSEAQuantSupportedOrganism organism : PSEAQuantSupportedOrganism.values()) {
							if (!"".equals(supportedOrganisms.toString())) {
								supportedOrganisms.append(", ");
							}
							supportedOrganisms.append(organism.getOrganismName());
						}

						organismFlowPanel.add(new InlineHTML(template.coloredDiv("black",
								"Supported organisms are: " + supportedOrganisms.toString())));
						// disable submit button
						sendButton.setEnabled(false);
						invalidOrganismError = true;
					} else if (numValidOrganisms == 1) {
						// select the only one
						organismsRatioButtons.get(0).setValue(true);
					}

				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			});
		}

	}

	@Override
	public PSEAQuantAnnotationDatabase getAnnotationDatabase() {
		for (RadioButton radio : annotationDBRadioButtons) {
			if (radio.getValue() != null && radio.getValue()) {
				return PSEAQuantAnnotationDatabase.getByAnnotationDBName(radio.getFormValue());
			}
		}
		throw new IllegalArgumentException("AnnotationDatabase error");
	}

	@Override
	public PSEAQuantCVTol getCVTol() {
		if (rdbtnCv.getValue() != null && rdbtnCv.getValue()) {
			return PSEAQuantCVTol.CV;
		}
		if (rdbtnInd.getValue() != null && rdbtnInd.getValue()) {
			return PSEAQuantCVTol.IND;
		}
		throw new IllegalArgumentException("CVTol error");
	}

	@Override
	public PSEAQuantLiteratureBias getLiteratureBias() {
		if (chckbxLiteratureBias.getValue() != null && chckbxLiteratureBias.getValue()) {
			return PSEAQuantLiteratureBias.BIAS;
		} else {
			return PSEAQuantLiteratureBias.NO_BIAS;
		}

	}

	@Override
	public PSEAQuantQuantType getQuantType() {
		for (RadioButton radio : quantTypeRadioButtons) {
			if (radio.getValue() != null && radio.getValue()) {
				return PSEAQuantQuantType.getByQuantType(radio.getFormValue());
			}
		}

		throw new IllegalArgumentException("Quant type error");
	}

	@Override
	public PSEAQuantSupportedOrganism getOrganism() {
		for (RadioButton radioButton : organismsRatioButtons) {
			if (radioButton.getValue() != null && radioButton.getValue()) {
				String text = radioButton.getText().trim();
				int ncbiID = -1;

				if (text.contains("(") && text.contains("(") && text.indexOf("(") < text.indexOf(")")) {
					try {
						ncbiID = Integer.valueOf(text.substring(text.indexOf("(") + 1, text.indexOf(")")));
					} catch (NumberFormatException e) {

					}
				}

				final PSEAQuantSupportedOrganism valueFromOrganismName = PSEAQuantSupportedOrganism
						.getValueFromOrganismName(text);
				if (valueFromOrganismName == null && ncbiID > 0) {
					final PSEAQuantSupportedOrganism valueFromOrganismName2 = PSEAQuantSupportedOrganism
							.getValueFromOrganismName(ncbiID);
					if (valueFromOrganismName2 != null)
						return valueFromOrganismName2;
				}
			}
		}
		throw new IllegalArgumentException("Quant type error");
	}

	@Override
	public String getEmail() {
		return emailTextBox.getValue();
	}

	@Override
	public long getNumberOfSamplings() {
		return numberOfSamplingsBox.getValue();
	}

	@Override
	public Double getCVTolFactor() {
		if (rdbtnCv.getValue() != null && rdbtnCv.getValue()) {
			return cvTolFactorBox.getValue();
		}
		return null;
	}

	@Override
	public List<PSEAQuantReplicate> getReplicates() {
		List<PSEAQuantReplicate> ret = new ArrayList<PSEAQuantReplicate>();

		for (ReplicatePickerPanel replicatePicker : replicatePickerList) {
			PSEAQuantReplicate replicate = new PSEAQuantReplicate();
			if (replicatePicker.isConditionsSelected()) {
				final Map<String, Set<String>> selectedConditionsByProject = replicatePicker
						.getSelectedConditionsByProject();
				for (String projectTag : selectedConditionsByProject.keySet()) {
					final Set<String> conditionNames = selectedConditionsByProject.get(projectTag);
					for (String conditionName : conditionNames) {
						replicate.addCondition(projectTag, conditionName);
					}
				}
			} else if (replicatePicker.isMSRunsSelected()) {
				final Map<String, Set<String>> selectedMSRunsByProject = replicatePicker.getSelectedMSRunsByProject();
				for (String projectTag : selectedMSRunsByProject.keySet()) {
					final Set<String> msRunsIDs = selectedMSRunsByProject.get(projectTag);
					for (String msRunID : msRunsIDs) {
						replicate.addMSRun(projectTag, msRunID);
					}
				}
			}
			ret.add(replicate);
		}
		return ret;
	}
}
