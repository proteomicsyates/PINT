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
		FlowPanel flowPanel = new FlowPanel();
		initWidget(flowPanel);

		flowPanel.setStyleName("sharingPeptidesPanel");

		FlowPanel explanationFlowPanel = new FlowPanel();
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String protenProviderType = "protein";
		if (proteinPeptideCluster.getPeptideProvider() instanceof ProteinGroupBean) {
			protenProviderType = "protein group";
		}
		sb.appendEscapedLines("Here you can see all shared peptides of the selected " + protenProviderType + ".\n"
				+ "Select any of the three options to change the view of the panel.\n"
				+ "Place the cursor over the protein accessions and over the peptide sequences\n"
				+ "to see more information about them.\n"
				+ "In 'by similarity' type view, a Neddleman-Wunsch alignment algorithm is performed.\n"
				+ "Change the default thresholds in order to consider or discard different alignments.");
		explanationFlowPanel.add(new InlineHTML(sb.toSafeHtml()));
		flowPanel.add(explanationFlowPanel);

		// button for showing the extended version or the short version of the
		// relationships
		final String groupName = "simpleOrExtended";

		FlexTable flexTable = new FlexTable();
		flexTable.setStyleName("sharingPeptidesPanel-Elements");
		flowPanel.add(flexTable);

		errorLabel = new Label("error label");
		errorLabel.setStyleName("font-red");
		errorLabel.setVisible(false);
		flexTable.setWidget(0, 0, errorLabel);
		Label lblViewType = new Label("View type:");
		flexTable.setWidget(1, 0, lblViewType);
		RadioButton simple = new RadioButton(groupName, "simple");
		simple.setTitle(
				"Show the peptides of the proteins selected, but not all other proteins belonging to these peptides.");
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
		RadioButton extended = new RadioButton(groupName, "extended");
		extended.setTitle("Show the peptides of the proteins selected, as well as the proteins of all these peptides.");
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
		RadioButton bysimilarity = new RadioButton(groupName, "by similarity");
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
				for (AlignmentResult alignmentResult : alignmentResults) {
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

						List<PeptideBean> sortedPeptides = getSortedSequences(result.keySet());
						Map<String, ProteinBean> proteinMap = new HashMap<String, ProteinBean>();
						List<String> totalProteinAccs = new ArrayList<String>();
						for (PeptideBean peptide : sortedPeptides) {

							final Set<ProteinBean> proteins = result.get(peptide);
							for (ProteinBean protein : proteins) {
								proteinMap.put(protein.getPrimaryAccession().getAccession(), protein);
								if (!totalProteinAccs.contains(protein.getPrimaryAccession().getAccession()))
									totalProteinAccs.add(protein.getPrimaryAccession().getAccession());
							}
						}
						Collections.sort(totalProteinAccs);
						// column headers: proteinAccs
						int i = 1;
						for (String proteinAcc : totalProteinAccs) {
							final Label proteinAccLabel = new Label(proteinAcc);
							final ProteinBean proteinBean = proteinMap.get(proteinAcc);
							proteinAccLabel.setTitle(getProteinAccessionTitle(proteinBean));
							proteinAccLabel.setStyleName(getProteinCSSStyleName(proteinBean));
							table.setWidget(0, i, proteinAccLabel);
							table.getCellFormatter().setAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER,
									HasVerticalAlignment.ALIGN_MIDDLE);
							i++;
						}
						table.setWidget(0, i, new Label("- # -"));
						table.getCellFormatter().setAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER,
								HasVerticalAlignment.ALIGN_MIDDLE);

						Set<PeptideBean> peptidesProcessed = new HashSet<PeptideBean>();
						// numbering the peptides
						int j = 1;
						int numDifferentPeptides = 0;
						for (PeptideBean peptide : sortedPeptides) {
							if (peptidesProcessed.contains(peptide))
								continue;
							peptidesProcessed.add(peptide);
							final AlignmentResult alignmentResult = passAlignmentThresholds(peptide, alignmentResults);
							final Label numPeptideLabel = new Label(String.valueOf(++numDifferentPeptides));
							if (alignmentResult != null) {
								peptidesProcessed.add(alignmentResult.getSeq1());
								peptidesProcessed.add(alignmentResult.getSeq2());
								table.setWidget(j, i, numPeptideLabel);
								table.getFlexCellFormatter().setRowSpan(j, i, 3);
								table.getCellFormatter().setAlignment(j, i, HasHorizontalAlignment.ALIGN_CENTER,
										HasVerticalAlignment.ALIGN_MIDDLE);
								j = j + 3;
							} else {
								table.setWidget(j, i, numPeptideLabel);
								table.getFlexCellFormatter().setRowSpan(j, i, 1);
								table.getCellFormatter().setAlignment(j, i, HasHorizontalAlignment.ALIGN_CENTER,
										HasVerticalAlignment.ALIGN_MIDDLE);
								j++;
							}
						}

						// row header
						peptidesProcessed.clear();
						j = 1;
						for (PeptideBean peptide : sortedPeptides) {
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
								table.setWidget(j, 0, peptide1SequenceLabel);
								table.getCellFormatter().setAlignment(j, 0, HasHorizontalAlignment.ALIGN_LEFT,
										HasVerticalAlignment.ALIGN_MIDDLE);
								table.getFlexCellFormatter().setRowSpan(j, 0, 1);
								j++;
								// print the alignment connections
								final Label label2 = new Label(
										alignmentResult.getAlignmentConnections().replace(" ", "_"));
								label2.setTitle(getAlignmentTitle(alignmentResult));
								table.setWidget(j, 0, label2);
								label2.setStyleName("labelFixedWidth");
								table.getCellFormatter().setAlignment(j, 0, HasHorizontalAlignment.ALIGN_LEFT,
										HasVerticalAlignment.ALIGN_MIDDLE);
								table.getFlexCellFormatter().setRowSpan(j, 0, 1);
								j++;
								// print the second peptide
								final Label peptide2SequenceLabel = new Label(alignmentResult.getAlignedSequence2());
								peptide2SequenceLabel.setTitle(getPeptideBeanTitle(alignmentResult.getSeq2()));
								peptide2SequenceLabel.setStyleName(getPeptideCSSStyleName(alignmentResult.getSeq2()));
								table.setWidget(j, 0, peptide2SequenceLabel);
								table.getCellFormatter().setAlignment(j, 0, HasHorizontalAlignment.ALIGN_LEFT,
										HasVerticalAlignment.ALIGN_MIDDLE);
								table.getFlexCellFormatter().setRowSpan(j, 0, 1);
							} else {

								final Label peptideSequenceLabel = new Label(peptide.getSequence());
								peptideSequenceLabel.setTitle(getPeptideBeanTitle(peptide));
								peptideSequenceLabel.setStyleName(getPeptideCSSStyleName(peptide));
								table.setWidget(j, 0, peptideSequenceLabel);
								table.getCellFormatter().setAlignment(j, 0, HasHorizontalAlignment.ALIGN_LEFT,
										HasVerticalAlignment.ALIGN_MIDDLE);
								table.getFlexCellFormatter().setRowSpan(j, 0, 1);

							}
							j++;
						}

						peptidesProcessed.clear();
						int row = 1;
						for (PeptideBean peptide : sortedPeptides) {
							if (peptidesProcessed.contains(peptide))
								continue;
							peptidesProcessed.add(peptide);
							int col = 1;
							final AlignmentResult passAlignmentThresholds = passAlignmentThresholds(peptide,
									alignmentResults);
							if (passAlignmentThresholds != null) {
								peptidesProcessed.add(passAlignmentThresholds.getSeq1());
								peptidesProcessed.add(passAlignmentThresholds.getSeq2());
								for (String proteinAcc : totalProteinAccs) {
									ProteinBean protein = proteinMap.get(proteinAcc);
									if (result.get(passAlignmentThresholds.getSeq1()).contains(protein)
											|| result.get(passAlignmentThresholds.getSeq2()).contains(protein)) {
										table.setWidget(row, col, new Label("x"));
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
								for (String proteinAcc : totalProteinAccs) {
									ProteinBean protein = proteinMap.get(proteinAcc);
									if (result.get(peptide).contains(protein)) {
										table.setWidget(row, col, new Label("x"));
										table.getCellFormatter().setAlignment(row, col,
												HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
									} else {
										table.setWidget(row, col, new Label("."));
										table.getCellFormatter().setAlignment(row, col,
												HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
									}
									table.getFlexCellFormatter().setRowSpan(row, col, 1);
									col++;
								}
								row++;
							}

						}
					} catch (PintException e) {
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
		if (cluster.getPeptideProvider().equals(proteinBean)) {
			bold = true;
		} else if (cluster.getPeptideProvider() instanceof ProteinGroupBean) {
			if (((ProteinGroupBean) cluster.getPeptideProvider()).contains(proteinBean)) {
				bold = true;
			}
		}
		return ClientSafeHtmlUtils.getProteinEvidenceCSSStyleName(proteinBean.getEvidence(), bold);

	}

	private String getPeptideCSSStyleName(PeptideBean peptideBean) {
		boolean bold = false;
		if (cluster.getPeptideProvider().getPeptides().contains(peptideBean)) {
			bold = true;
		}
		return ClientSafeHtmlUtils.getRelationCSSStyleName(peptideBean.getRelation(), bold);
	}

	private String getAlignmentTitle(AlignmentResult passAlignmentThresholds) {
		if (passAlignmentThresholds == null)
			return null;
		StringBuilder sb = new StringBuilder();
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
		StringBuilder sb = new StringBuilder();
		sb.append("Evidence: " + peptideBean.getRelation() + SharedConstants.SEPARATOR);
		sb.append("# PSMs: " + peptideBean.getNumPSMs() + SharedConstants.SEPARATOR);
		final Set<OrganismBean> organisms = peptideBean.getOrganisms();
		if (organisms != null && !organisms.isEmpty()) {
			List<OrganismBean> list = new ArrayList<OrganismBean>();
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
			for (OrganismBean organismBean : list) {
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
			for (String seq : peptideBean.getRawSequences()) {
				sb.append("\t" + seq + SharedConstants.SEPARATOR);
			}
		}
		return sb.toString();
	}

	private String getProteinAccessionTitle(ProteinBean proteinBean) {
		if (proteinBean == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append(proteinBean.getPrimaryAccession().getAccession() + ":\t" + proteinBean.getDescriptionString()
				+ SharedConstants.SEPARATOR);
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
			sb.append("Taxonomy: " + proteinBean.getOrganism() + SharedConstants.SEPARATOR);
		}
		return sb.toString();
	}

	private List<PeptideBean> getSortedSequences(Collection<PeptideBean> peptides) {
		List<PeptideBean> list = new ArrayList<PeptideBean>();
		Set<String> peptideSeqs = new HashSet<String>();
		for (PeptideBean peptideBean : peptides) {
			if (peptideSeqs.contains(peptideBean.getSequence())) {
				continue;
			}
			peptideSeqs.add(peptideBean.getSequence());
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
