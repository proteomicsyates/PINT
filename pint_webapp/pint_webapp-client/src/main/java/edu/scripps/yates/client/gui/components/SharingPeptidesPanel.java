package edu.scripps.yates.client.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;

import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.util.AlignmentResult;
import edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults;
import edu.scripps.yates.shared.util.SharedConstants;

public class SharingPeptidesPanel extends Composite {
	private final static NumberFormat format = NumberFormat.getFormat("#.#");
	private final FlexTable table;
	private final ProteinPeptideCluster cluster;
	private final IntegerBox scoreText;
	private final DoubleBox similarityText;
	private final Label errorLabel;
	private final Label lblAlignmentSimilarityThreshold;
	private final Label scoreThresholdLabel;
	private MyDialogBox loadingDialog;

	public SharingPeptidesPanel(final ProteinPeptideCluster proteinPeptideCluster) {

		cluster = proteinPeptideCluster;
		if (cluster == null) {
			StatusReportersRegister.getInstance().notifyStatusReporters("Protein-peptide cluster is null");
		}
		final ScrollPanel scroll = new ScrollPanel();
		final FlowPanel flowPanel = new FlowPanel();
		scroll.add(flowPanel);
		initWidget(scroll);

		flowPanel.setStyleName("sharingPeptidesPanel");

		final FlowPanel explanationFlowPanel = new FlowPanel();
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String protenProviderType = "protein";
		if (proteinPeptideCluster.getLightPeptideProvider() instanceof ProteinGroupBean) {
			protenProviderType = "protein group";
		}
		sb.appendEscapedLines("Here you can see all shared peptides of the selected " + protenProviderType + ".\n"
				+ "Select any of the three options to change the view of the panel.\n"
				+ "Place the cursor over the protein accessions and over the peptide sequences to see more information about them.\n"
				+ "In 'by similarity' type view, a Neddleman-Wunsch sequence alignment algorithm is performed. "
				+ "Change the default thresholds in order to consider or discard different alignments.");
		explanationFlowPanel.add(new InlineHTML(sb.toSafeHtml()));
		flowPanel.add(explanationFlowPanel);

		// button for showing the extended version or the short version of the
		// relationships
		final String groupName = "simpleOrExtended";

		final FlexTable flexTable = new FlexTable();
		flexTable.setStyleName("sharingPeptidesPanel-Elements");
		flowPanel.add(flexTable);

		errorLabel = new Label("error label");
		errorLabel.setStyleName("font-red");
		errorLabel.setVisible(false);
		flexTable.setWidget(0, 0, errorLabel);
		final Label lblViewType = new Label("View type:");
		flexTable.setWidget(1, 0, lblViewType);
		final RadioButton simple = new RadioButton(groupName, "simple (show peptides of the selected protein(s))");
		simple.setTitle(
				"Show the peptides of the proteins selected, but not all other proteins mapping to these peptides.");
		flexTable.setWidget(1, 1, simple);
		simple.setValue(true);
		simple.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorLabel.setVisible(false);
				similarityText.setEnabled(false);
				scoreText.setEnabled(false);

				updateTable(cluster.getRelationships(), null, "Updating table...");

			}
		});
		final RadioButton extended = new RadioButton(groupName,
				"extended (show extended relationships proteins-peptides)");
		extended.setTitle(
				"Show the peptides of the proteins selected, as well as the proteins (and their peptides) of all these peptides.");
		flexTable.setWidget(2, 1, extended);
		extended.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorLabel.setVisible(false);
				similarityText.setEnabled(false);
				scoreText.setEnabled(false);

				updateTable(cluster.getExtendedRelationships(), null, "Updating table...");

			}
		});
		final RadioButton bysimilarity = new RadioButton(groupName, "by similarity");
		bysimilarity
				.setTitle("Show the peptides of the proteins selected, as well as the proteins of all these peptides.\n"
						+ "Additionally, allows to align similar peptides applying the Needleman-Wunsch alignment algorithm");
		flexTable.setWidget(3, 1, bysimilarity);
		bysimilarity.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorLabel.setVisible(false);
				similarityText.setEnabled(true);
				scoreText.setEnabled(true);

				updateTable(cluster.getExtendedRelationships(), cluster.getAligmentResults(), "Updating table...");

			}
		});

		scoreThresholdLabel = new Label("NW alignment score threshold: ");
		flexTable.setWidget(4, 0, scoreThresholdLabel);
		// Score threshold
		scoreText = new IntegerBox();
		flexTable.setWidget(4, 1, scoreText);
		scoreText.setWidth("50px");
		scoreText.setTitle("Minimum aligment score needed to be considered as aligned,\n"
				+ "using the Needleman-Wunsch alignment algorithm");
		flexTable.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		lblAlignmentSimilarityThreshold = new Label("Alignment identity threshold (%): ");
		flexTable.setWidget(5, 0, lblAlignmentSimilarityThreshold);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		// Score threshold
		similarityText = new DoubleBox();
		similarityText.setTitle("Minimum % of identity between the two peptides to be considered as aligned (1-100)");
		similarityText.setEnabled(false);
		flexTable.setWidget(5, 1, similarityText);
		similarityText.setWidth("50px");
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setVerticalAlignment(5, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(4, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(4, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(5, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getFlexCellFormatter().setRowSpan(1, 0, 3);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		FlexTableHelper.fixRowSpan(flexTable);
		similarityText.setValue(SharedConstants.DEFAULT_SIMILARITY_SCORE);
		similarityText.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				errorLabel.setVisible(false);
				updateTable(cluster.getExtendedRelationships(), cluster.getAligmentResults(), "Updating table...");

			}
		});
		scoreText.setValue(SharedConstants.DEFAULT_ALIGNMENT_SCORE);
		scoreText.setEnabled(false);
		scoreText.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				errorLabel.setVisible(false);

				updateTable(cluster.getExtendedRelationships(), cluster.getAligmentResults(), "Updating table...");

			}
		});

		table = new com.google.gwt.user.client.ui.FlexTable();
		flowPanel.add(table);
		// by default show the relations of the short version
		final Map<PeptideBean, Set<ProteinBean>> result = cluster.getRelationships();

		updateTable(result, null, "Loading table with shared peptides...");

	}

	protected void showError(PintException e) {
		errorLabel.setText(e.getMessage());
		errorLabel.setVisible(true);
	}

	private int getAlignmentScoreThreshold() throws PintException {
		final Integer value = scoreText.getValue();
		if (value == null) {
			throw new PintException("Alignment score threshold error. Only integers are allowed",
					PINT_ERROR_TYPE.TEXT_FORMAT_ERROR);
		}
		return value;
	}

	private double getAlignmentIdentityThreshold() throws PintException {
		final Double value = similarityText.getValue();
		if (value == null || value < 0.0 || value > 100.0) {
			throw new PintException("Identity score threshold error. Only numbers between 0 and 100 are allowed.",
					PINT_ERROR_TYPE.TEXT_FORMAT_ERROR);
		}
		return value;
	}

	private AlignmentResult passAlignmentThresholds(PeptideBean peptide,
			ProteinPeptideClusterAlignmentResults aligments) throws PintException {
		if (aligments != null) {
			final Set<AlignmentResult> alignmentResults = aligments.getResultsByPeptideSequence()
					.get(peptide.getSequence());
			if (alignmentResults != null) {
				for (final AlignmentResult alignmentResult : alignmentResults) {
					if (alignmentResult.getFinalAlignmentScore() >= getAlignmentScoreThreshold()) {
						if (Double.compare(alignmentResult.getSequenceIdentity(),
								getAlignmentIdentityThreshold()) >= 0) {

							System.out.println(peptide + " is well aligned as:");
							System.out.println(alignmentResult.getAlignmentString());
							return alignmentResult;
						}
					}
				}
			}
		}
		return null;
	}

	protected void updateTable(final Map<PeptideBean, Set<ProteinBean>> result,
			final ProteinPeptideClusterAlignmentResults alignmentResults, String textLoading) {
		try {

			showLoadingDialog(textLoading, false, true);
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					try {
						// clear table first
						table.clear(true);

						// get first the thresholds in order to get the
						// exception
						// now if there is some error in teh text box. in that
						// way,
						// the table will be empty with the error.
						getAlignmentScoreThreshold();
						getAlignmentIdentityThreshold();

						final List<PeptideBean> sortedPeptides = getSortedSequences(result.keySet());
						final Map<String, ProteinBean> proteinMap = new HashMap<String, ProteinBean>();
						final List<String> totalProteinAccs = new ArrayList<String>();
						for (final PeptideBean peptide : sortedPeptides) {

							final Set<ProteinBean> proteins = result.get(peptide);
							for (final ProteinBean protein : proteins) {
								proteinMap.put(protein.getPrimaryAccession().getAccession(), protein);
								if (!totalProteinAccs.contains(protein.getPrimaryAccession().getAccession()))
									totalProteinAccs.add(protein.getPrimaryAccession().getAccession());
							}
						}
						Collections.sort(totalProteinAccs);
						// column headers: Num - Peptide - proteinAccs
						int row = 0;
						int col = 0;
						table.setWidget(row, col, new Label("Num."));
						table.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER,
								HasVerticalAlignment.ALIGN_MIDDLE);
						col++;
						table.setWidget(row, col, new Label("Peptide"));
						table.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER,
								HasVerticalAlignment.ALIGN_MIDDLE);
						col++;
						for (final String proteinAcc : totalProteinAccs) {
							final Label proteinAccLabel = new Label(proteinAcc);
							final ProteinBean proteinBean = proteinMap.get(proteinAcc);
							proteinAccLabel.setTitle(getProteinAccessionTitle(proteinBean));
							proteinAccLabel.setStyleName(getProteinCSSStyleName(proteinBean));
							proteinAccLabel.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
							table.setWidget(row, col, proteinAccLabel);
							table.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER,
									HasVerticalAlignment.ALIGN_MIDDLE);
							col++;
						}

						final Set<PeptideBean> peptidesProcessed = new HashSet<PeptideBean>();
						// numbering the peptides
						row = 1;
						col = 0;
						int numDifferentPeptides = 0;
						for (final PeptideBean peptide : sortedPeptides) {
							if (peptidesProcessed.contains(peptide))
								continue;
							peptidesProcessed.add(peptide);
							final AlignmentResult alignmentResult = passAlignmentThresholds(peptide, alignmentResults);
							final Label numPeptideLabel = new Label(String.valueOf(++numDifferentPeptides));
							if (alignmentResult != null) {
								peptidesProcessed.add(alignmentResult.getSeq1());
								peptidesProcessed.add(alignmentResult.getSeq2());
								table.setWidget(row, col, numPeptideLabel);
								table.getFlexCellFormatter().setRowSpan(row, col, 3);
								table.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER,
										HasVerticalAlignment.ALIGN_MIDDLE);
								row = row + 3;
							} else {
								table.setWidget(row, col, numPeptideLabel);
								table.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER,
										HasVerticalAlignment.ALIGN_MIDDLE);
								row++;
							}
						}

						// row header
						peptidesProcessed.clear();
						row = 1;
						col = 1;
						for (final PeptideBean peptide : sortedPeptides) {
							if (peptidesProcessed.contains(peptide))
								continue;
							peptidesProcessed.add(peptide);
							final AlignmentResult alignmentResult = passAlignmentThresholds(peptide, alignmentResults);
							if (alignmentResult != null) {
								peptidesProcessed.add(alignmentResult.getSeq1());
								peptidesProcessed.add(alignmentResult.getSeq2());

								// print the first peptide
								final Label peptide1SequenceLabel = new Label(alignmentResult.getAlignedSequence1());
								peptide1SequenceLabel.setTitle(getPeptideBeanTitle(alignmentResult.getSeq1()));
								peptide1SequenceLabel.setStyleName(getPeptideCSSStyleName(alignmentResult.getSeq1()));
								table.setWidget(row, col, peptide1SequenceLabel);
								table.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_LEFT,
										HasVerticalAlignment.ALIGN_MIDDLE);
								row++;
								// print the alignment connections
								final Label label2 = new Label(
										alignmentResult.getAlignmentConnections().replace(" ", "_"));
								label2.setTitle(getAlignmentTitle(alignmentResult));
								table.setWidget(row, col - 1, label2);
								label2.setStyleName("labelFixedWidth");
								table.getCellFormatter().setAlignment(row, col - 1, HasHorizontalAlignment.ALIGN_LEFT,
										HasVerticalAlignment.ALIGN_MIDDLE);
								row++;
								// print the second peptide
								final Label peptide2SequenceLabel = new Label(alignmentResult.getAlignedSequence2());
								peptide2SequenceLabel.setTitle(getPeptideBeanTitle(alignmentResult.getSeq2()));
								peptide2SequenceLabel.setStyleName(getPeptideCSSStyleName(alignmentResult.getSeq2()));
								table.setWidget(row, col - 1, peptide2SequenceLabel);
								table.getCellFormatter().setAlignment(row, col - 1, HasHorizontalAlignment.ALIGN_LEFT,
										HasVerticalAlignment.ALIGN_MIDDLE);
							} else {

								final Label peptideSequenceLabel = new Label(peptide.getSequence());
								peptideSequenceLabel.setTitle(getPeptideBeanTitle(peptide));
								peptideSequenceLabel.setStyleName(getPeptideCSSStyleName(peptide));
								table.setWidget(row, col, peptideSequenceLabel);
								table.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_LEFT,
										HasVerticalAlignment.ALIGN_MIDDLE);

							}
							row++;
						}

						// X or . mapping between peptides and proteins
						peptidesProcessed.clear();
						row = 1;
						for (final PeptideBean peptide : sortedPeptides) {
							if (peptidesProcessed.contains(peptide))
								continue;
							peptidesProcessed.add(peptide);
							col = 2;
							final AlignmentResult passAlignmentThresholds = passAlignmentThresholds(peptide,
									alignmentResults);

							if (passAlignmentThresholds != null) {
								peptidesProcessed.add(passAlignmentThresholds.getSeq1());
								peptidesProcessed.add(passAlignmentThresholds.getSeq2());
								for (final String proteinAcc : totalProteinAccs) {
									final ProteinBean protein = proteinMap.get(proteinAcc);
									if (result.get(passAlignmentThresholds.getSeq1()).contains(protein)
											|| result.get(passAlignmentThresholds.getSeq2()).contains(protein)) {
										final Label xLabel = new Label("x");
										xLabel.setTitle("Peptide '" + peptide.getSequence() + " present in protein "
												+ proteinAcc);
										table.setWidget(row, col, xLabel);
										table.getCellFormatter().setAlignment(row, col,
												HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
									} else {
										table.setWidget(row, col, new Label("."));
										table.getCellFormatter().setAlignment(row, col,
												HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
									}
									table.getFlexCellFormatter().setRowSpan(row, col, 3);
									col++;
								}
								row = row + 3;
							} else {
								for (final String proteinAcc : totalProteinAccs) {
									final ProteinBean protein = proteinMap.get(proteinAcc);
									if (result.get(peptide).contains(protein)) {
										final Label xLabel = new Label("x");
										xLabel.setTitle("Peptide '" + peptide.getSequence() + " present in protein "
												+ proteinAcc);
										table.setWidget(row, col, xLabel);
										table.getCellFormatter().setAlignment(row, col,
												HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
									} else {
										table.setWidget(row, col, new Label("."));
										table.getCellFormatter().setAlignment(row, col,
												HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
									}
									col++;
								}
								row++;
							}

						}
					} catch (final PintException e) {
						showError(e);
					}
				}
			});
		} finally {
			hideLoadingDialog();
		}
	}

	private String getProteinCSSStyleName(ProteinBean proteinBean) {
		boolean bold = false;
		if (cluster.getLightPeptideProvider().equals(proteinBean)) {
			bold = true;
		} else if (cluster.getLightPeptideProvider() instanceof ProteinGroupBean) {
			if (((ProteinGroupBean) cluster.getLightPeptideProvider()).contains(proteinBean)) {
				bold = true;
			}
		}
		return ClientSafeHtmlUtils.getProteinEvidenceCSSStyleName(proteinBean.getEvidence(), bold);

	}

	private String getPeptideCSSStyleName(PeptideBean peptideBean) {
		boolean bold = false;
		if (cluster.getLightPeptideProvider().getPeptides().contains(peptideBean)) {
			bold = true;
		}
		return ClientSafeHtmlUtils.getRelationCSSStyleName(peptideBean.getRelation(), bold);
	}

	private String getAlignmentTitle(AlignmentResult passAlignmentThresholds) {
		if (passAlignmentThresholds == null)
			return null;
		final StringBuilder sb = new StringBuilder();
		sb.append("Score: " + passAlignmentThresholds.getFinalAlignmentScore() + SharedConstants.SEPARATOR);
		sb.append("Alignment lenth: " + passAlignmentThresholds.getAlignmentLength() + SharedConstants.SEPARATOR);
		sb.append("Identical lenth: " + passAlignmentThresholds.getIdenticalLength() + SharedConstants.SEPARATOR);
		sb.append("Sequence identity: " + format.format(passAlignmentThresholds.getSequenceIdentity()) + "%"
				+ SharedConstants.SEPARATOR);

		return sb.toString();
	}

	private String getPeptideBeanTitle(PeptideBean peptideBean) {
		if (peptideBean == null)
			return null;
		final StringBuilder sb = new StringBuilder();
		sb.append("Peptide sequence: " + peptideBean.getFullSequence() + SharedConstants.SEPARATOR);
		sb.append("Evidence: " + peptideBean.getRelation() + "\t(" + peptideBean.getRelation().getDefinition() + ")"
				+ SharedConstants.SEPARATOR);
		sb.append("# PSMs: " + peptideBean.getNumPSMs() + SharedConstants.SEPARATOR);
		final Set<OrganismBean> organisms = peptideBean.getOrganisms();
		if (organisms != null && !organisms.isEmpty()) {
			final List<OrganismBean> list = new ArrayList<OrganismBean>();
			list.addAll(organisms);
			if (list.size() > 1) {
				Collections.sort(list, new Comparator<OrganismBean>() {

					@Override
					public int compare(OrganismBean o1, OrganismBean o2) {
						return o1.getId().compareTo(o2.getId());
					}
				});
			}
			int i = 0;
			for (final OrganismBean organismBean : list) {
				if (i > 0)
					sb.append(", ");
				i++;
				sb.append(organismBean.getId());
				if (organismBean.getNcbiTaxID() != null) {
					sb.append(" (" + organismBean.getNcbiTaxID() + ")");
				}
			}
			sb.append(SharedConstants.SEPARATOR);
		}
		if (peptideBean.getRawSequences() != null && peptideBean.getRawSequences().size() > 1) {
			sb.append("Different versions of this peptide where detected: " + SharedConstants.SEPARATOR);
			for (final String seq : peptideBean.getRawSequences()) {
				sb.append("\t" + seq + SharedConstants.SEPARATOR);
			}
		}
		return sb.toString();
	}

	private String getProteinAccessionTitle(ProteinBean proteinBean) {
		if (proteinBean == null)
			return null;
		final StringBuilder sb = new StringBuilder();
		final String descriptionString = proteinBean.getDescriptionString();
		sb.append(proteinBean.getPrimaryAccession().getAccession());
		if (descriptionString != null) {
			sb.append(":\t" + descriptionString + SharedConstants.SEPARATOR);
		} else {
			sb.append(":\t No description available");
		}
		sb.append("Evidence: " + proteinBean.getEvidence() + SharedConstants.SEPARATOR);
		sb.append("# PSMs: " + proteinBean.getNumPSMs() + SharedConstants.SEPARATOR);
		sb.append("# Peptides: " + proteinBean.getNumPeptides() + SharedConstants.SEPARATOR);
		if (proteinBean.getOrganism() != null) {
			String organismText = "";
			if (proteinBean.getOrganism().getId() != null) {
				organismText = proteinBean.getOrganism().getId();
			}
			if (proteinBean.getOrganism().getNcbiTaxID() != null) {
				if (!"".equals(organismText)) {
					organismText += "\t";
				}
				organismText += "(" + proteinBean.getOrganism().getNcbiTaxID() + ")";
			}
			sb.append("Taxonomy: " + organismText + SharedConstants.SEPARATOR);
		}
		return sb.toString();
	}

	private List<PeptideBean> getSortedSequences(Collection<PeptideBean> peptides) {
		final List<PeptideBean> list = new ArrayList<PeptideBean>();
		final Set<String> peptideSeqs = new HashSet<String>();
		for (final PeptideBean peptideBean : peptides) {
			final String sequence = peptideBean.getSequence();

			if (peptideSeqs.contains(sequence)) {
				continue;
			}
			peptideSeqs.add(sequence);
			list.add(peptideBean);
		}
		Collections.sort(list, new Comparator<PeptideBean>() {

			@Override
			public int compare(PeptideBean o1, PeptideBean o2) {
				return o1.getSequence().compareTo(o2.getSequence());
			}
		});

		return list;
	}

	private void showLoadingDialog(String text, boolean autohide, boolean modal) {
		if (loadingDialog == null) {
			loadingDialog = new MyDialogBox(text, autohide, modal);
		} else {
			loadingDialog.setText(text);
			loadingDialog.setAutoHideEnabled(autohide);
			loadingDialog.setModal(modal);
		}
		loadingDialog.center();
	}

	private void hideLoadingDialog() {
		if (loadingDialog != null) {
			loadingDialog.hide();
		}
	}
}
