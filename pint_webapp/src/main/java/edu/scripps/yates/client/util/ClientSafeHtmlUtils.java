package edu.scripps.yates.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AccessionType;
import edu.scripps.yates.shared.model.GeneBean;
import edu.scripps.yates.shared.model.OmimEntryBean;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.PeptideRelation;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinEvidence;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.ReactomePathwayRef;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.UniprotFeatureBean;
import edu.scripps.yates.shared.model.UniprotProteinExistence;
import edu.scripps.yates.shared.model.interfaces.ContainsGenes;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class ClientSafeHtmlUtils {
	private static final HtmlTemplates template = GWT.create(HtmlTemplates.class);
	private static final int MAX_LENGTH_FUNCTION = 150;
	private static final int MAX_LENGTH_OMIM = 150;

	public enum SEQUENCE_OVERLAPPING {
		TOTALLY_COVERED("pep. fully covered", "fullyCoveredSequenceRegion"), PARTIALLY_COVERED("pep. partially covered",
				"partiallyCoveredSequenceRegion"), NOT_COVERED("not covered", "");
		private final String description;
		private final String cssClassName;

		private SEQUENCE_OVERLAPPING(String description, String cssClassName) {
			this.description = description;
			this.cssClassName = cssClassName;
		}

		public String getDescription() {
			return description;
		}

		public String getCSSClassName() {
			return cssClassName;
		}
	}

	// private static final MyClientBundle myClientBundle =
	// MyClientBundle.INSTANCE;
	public static Anchor getPubmedLink(ProjectBean projectBean) {
		if (projectBean != null) {
			String pubmedId = projectBean.getPubmedLink();

			if (pubmedId != null) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendEscaped("[ pubmed ]");
				final Anchor link = new Anchor(sb.toSafeHtml(),
						UriUtils.fromString("http://www.ncbi.nlm.nih.gov/pubmed/" + pubmedId));
				link.setStyleName("linkPINT");
				link.setTarget("_blank");
				link.setTitle("Click here to go to the publication in PUBMED (id:" + pubmedId + ")");
				return link;
			}
		}
		return null;
	}

	public static SafeHtml getSafeHtmlFromGene(GeneBean gene, boolean includeTitle) {
		final int hgncId = gene.getHgncNumber();
		String geneId = gene.getGeneID();
		if (hgncId > 0) {
			String title = "";
			final String urlString = SharedConstants.GENENAMES_LINK + hgncId;
			if (includeTitle)
				title = gene.getName() + " (" + gene.getGeneID() + ")" + SharedConstants.SEPARATOR
						+ "(Click to go to HGNC repository)";
			final SafeHtml link = template.link(UriUtils.fromString(urlString), "geneLinkLink", title, title, geneId);

			return link;
		} else {
			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			String geneType = gene.getGeneType();
			if (geneType == null)
				geneType = "";
			sb.append(template.startToolTip(geneType));
			sb.appendEscaped(gene.getGeneID());
			sb.append(template.endToolTip());
			return sb.toSafeHtml();
		}
	}

	public static SafeHtml getSafeHtmlFromGenes(GeneBean primaryGene, List<GeneBean> otherGenes, boolean includeTitle) {
		if (primaryGene == null && otherGenes.isEmpty())
			return new SafeHtmlBuilder().appendEscaped("-").toSafeHtml();
		Set<String> geneIds = new HashSet<String>();
		GeneBean gene = primaryGene;
		if (primaryGene == null && otherGenes != null && !otherGenes.isEmpty()) {
			gene = otherGenes.get(0);
		}
		geneIds.add(gene.getGeneID());

		final int hgncId = gene.getHgncNumber();
		if (hgncId > 0) {
			String title = "";
			final String urlString = SharedConstants.GENENAMES_LINK + hgncId;
			if (includeTitle) {
				// iterate over the others
				String othersString = "";
				for (GeneBean otherGene : otherGenes) {
					if (geneIds.contains(otherGene.getGeneID()))
						continue;
					geneIds.add(otherGene.getGeneID());
					if (!"".equals(othersString))
						othersString += SharedConstants.SEPARATOR;
					othersString += otherGene.getGeneID();
					if (otherGene.getGeneType() != null)
						othersString += " (" + otherGene.getGeneType() + ")";
				}
				title = gene.getGeneID() + " (" + gene.getName() + ")" + SharedConstants.SEPARATOR
						+ "(Click to go to HGNC repository)" + SharedConstants.SEPARATOR + othersString;
			}
			final SafeHtml link = template.link(UriUtils.fromString(urlString), "geneLink", title, title,
					gene.getGeneID());

			return link;
		} else {
			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			String tooltip = gene.getGeneID();
			if (gene.getGeneType() != null)
				tooltip += " (" + gene.getGeneType() + ")" + SharedConstants.SEPARATOR;
			String othersString = "";
			for (GeneBean otherGene : otherGenes) {
				if (geneIds.contains(otherGene.getGeneID()))
					continue;
				geneIds.add(otherGene.getGeneID());
				if (!"".equals(othersString))
					othersString += SharedConstants.SEPARATOR;
				othersString += otherGene.getGeneID();
				if (otherGene.getGeneType() != null)
					othersString += " (" + otherGene.getGeneType() + ")";
			}
			tooltip += othersString;
			sb.append(template.startToolTip(tooltip));
			sb.appendEscaped(gene.getGeneID());
			sb.append(template.endToolTip());
			return sb.toSafeHtml();
		}
	}

	private static SafeHtml getGeneLink(ContainsGenes p, boolean includeTitle) {
		List<GeneBean> genes = p.getGenes(false);
		GeneBean primaryGene = null;
		for (GeneBean geneBean : genes) {
			if (geneBean != null && GeneBean.PRIMARY.equals(geneBean.getGeneType()))
				primaryGene = geneBean;
		}
		final SafeHtml safeHtmlFromGenes = getSafeHtmlFromGenes(primaryGene, genes, includeTitle);
		return safeHtmlFromGenes;
	}

	public static SafeHtml getGeneLinks(ContainsGenes p, boolean includeTitle) {
		if (p instanceof ProteinBean) {
			return getGeneLink(p, includeTitle);
		} else if (p instanceof ProteinGroupBean) {
			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			ProteinGroupBean proteinGroup = (ProteinGroupBean) p;
			Set<String> accs = new HashSet<String>();
			boolean addNewLine = false;

			Iterator<ProteinBean> iterator = proteinGroup.getIterator(SharedDataUtils.getComparatorByPrymaryAcc());
			while (iterator.hasNext()) {
				ProteinBean proteinBean = iterator.next();
				if (accs.contains(proteinBean.getPrimaryAccession().getAccession())) {
					continue;
				}
				accs.add(proteinBean.getPrimaryAccession().getAccession());
				// it is not necessary when the previous one was a DIV
				if (!"".equals(sb.toSafeHtml().asString()) && addNewLine) {
					sb.appendEscapedLines(SharedConstants.SEPARATOR);
				}
				final SafeHtml geneLink = getGeneLink(proteinBean, true);

				addNewLine = true;
				if (geneLink.asString().contains("<div") && geneLink.asString().contains("</div>")) {
					addNewLine = false;
				}
				sb.append(geneLink);

			}
			return sb.toSafeHtml();
		}
		return null;
	}

	public static SafeHtml getAccLinks(ContainsPrimaryAccessions p, boolean includeTitle) {
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		// sort the list by accession
		Collections.sort(primaryAccessions, SharedDataUtils.getComparatorByAccession());
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		Set<String> proteinAccs = new HashSet<String>();
		for (AccessionBean primaryAccession : primaryAccessions) {

			if (primaryAccession != null) {
				final String accessionString = primaryAccession.getAccession();
				if (proteinAccs.contains(accessionString))
					continue;
				if (!"".equals(sb.toSafeHtml().asString()))
					sb.appendEscapedLines(SharedConstants.SEPARATOR);

				proteinAccs.add(accessionString);
				String description = primaryAccession.getDescription();
				if (description == null)
					description = "";
				final AccessionType accessionType = primaryAccession.getAccessionType();
				if (accessionType != null && accessionType.equals(AccessionType.UNIPROT)) {
					String title = "";
					if (includeTitle)
						title = description + " (Click to go to UniprotKB)";

					String uniprotLinkURL = SharedConstants.UNIPROT_ACC_LINK + accessionString;
					// if it is a isoform/proteoform
					if (accessionString.contains("-")) {
						uniprotLinkURL += "#" + accessionString;
					}
					final SafeHtml link = template.link(UriUtils.fromString(uniprotLinkURL), "uniprotAccLink", title,
							description, accessionString);
					sb.append(link);

				} else if (accessionType == AccessionType.NCBI) {
					String title = "";
					if (includeTitle)
						title = description + " (Click to go to NCBI repository)";
					String accessionForLink = accessionString;
					if (accessionForLink.startsWith("gi|"))
						accessionForLink = accessionString.substring(3);
					final SafeHtml link = template.link(
							UriUtils.fromString(SharedConstants.NCBI_PROTEIN_LINK + accessionForLink), "uniprotAccLink",
							title, description, accessionString);
					return link;

				} else {
					sb.appendEscapedLines(accessionString);
				}

			} else {
				if (!"".equals(sb.toSafeHtml().asString()))
					sb.appendEscapedLines(SharedConstants.SEPARATOR);

				sb.appendEscapedLines("-");

			}
		}

		return sb.toSafeHtml();
	}

	public static String getGenesTooltip(ContainsGenes p) {
		StringBuilder sb = new StringBuilder();
		final List<GeneBean> genes = p.getGenes(false);
		Set<String> names = new HashSet<String>();
		for (GeneBean geneBean : genes) {
			if (geneBean != null && !names.contains(geneBean.getGeneID())) {
				if (!"".equals(sb.toString()))
					sb.append("\n");
				sb.append(geneBean.getGeneID());
				if (geneBean.getGeneType() != null)
					sb.append(" (" + geneBean.getGeneType() + ")");
				names.add(geneBean.getGeneID());
			}
		}
		return sb.toString();
	}

	public static SafeHtml getProteinFunctionSafeHtml(ProteinBean p) {
		String function = p.getFunctionString();
		String shortFunction = function;
		SafeHtmlBuilder sb = new SafeHtmlBuilder();

		if (shortFunction.length() > MAX_LENGTH_FUNCTION) {
			shortFunction = shortFunction.substring(0, MAX_LENGTH_FUNCTION) + "...";
		}
		sb.append(template.startToolTip(function));
		String[] split = new String[1];
		if (shortFunction.contains(". ")) {
			split = shortFunction.split("\\. ");
		} else {
			split[0] = shortFunction;
		}
		boolean first = true;
		for (String string : split) {
			if (!first) {
				sb.appendEscapedLines(SharedConstants.SEPARATOR);
			}
			sb.appendEscapedLines(string).appendEscaped(".");
			first = false;
		}

		sb.append(template.endToolTip());
		return sb.toSafeHtml();
	}

	public static SafeHtml getTaxonomyLink(OrganismBean organism) {
		final String urlString = SharedConstants.TAXONOMY_LINK_BY_ID + organism.getNcbiTaxID();

		final SafeHtml link = template.link(UriUtils.fromString(urlString), "uniprotAccLink",
				"(NCBI tax id:" + organism.getNcbiTaxID() + ")", "(NCBI tax id:" + organism.getNcbiTaxID() + ")",
				organism.getId());
		return link;

	}

	public static SafeHtml getOmimLinks(ProteinBean p) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		final List<Integer> omimIDs = p.getOmimSortedIDs();
		if (omimIDs != null && !omimIDs.isEmpty()) {

			for (Integer omimID : p.getOmimSortedIDs()) {
				final OmimEntryBean omimEntry = p.getOmimEntries().get(omimID);
				StringBuilder omimTooltip = new StringBuilder();
				omimTooltip.append("OMIM entry ID: " + omimEntry.getId() + SharedConstants.SEPARATOR);
				omimTooltip.append(omimEntry.getPreferredTitle() + SharedConstants.SEPARATOR);
				final List<String> alternativeTitles = omimEntry.getAlternativeTitles();
				omimTooltip.append("Alternative titles; symbols: " + SharedConstants.SEPARATOR);
				for (String alternativeTitle : alternativeTitles) {
					omimTooltip.append("\t" + alternativeTitle + SharedConstants.SEPARATOR);
				}
				omimTooltip.append("(click for open OMIM entry web page)");

				if (!"".equals(sb.toSafeHtml().asString()))
					sb.appendEscapedLines(SharedConstants.SEPARATOR);

				String shortTitle = omimEntry.getPreferredTitle();
				if (shortTitle.length() > MAX_LENGTH_OMIM) {
					shortTitle = shortTitle.substring(0, MAX_LENGTH_OMIM) + "...";
				}
				sb.appendEscaped(shortTitle + " (");
				final SafeHtml link = template.link(UriUtils.fromString(SharedConstants.OMIM_ENTRY_LINK + omimID),
						"uniprotAccLink", omimTooltip.toString(), omimTooltip.toString(), String.valueOf(omimID));
				sb.append(link);
				sb.append(new SafeHtmlBuilder().appendEscaped(")").toSafeHtml());
			}
		}
		return sb.toSafeHtml();
	}

	public static SafeHtml getGroupMemberEvidences(ProteinGroupBean proteinGroup) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		Set<ProteinEvidence> evidences = new HashSet<ProteinEvidence>();
		Set<String> accs = new HashSet<String>();
		Iterator<ProteinBean> iterator = proteinGroup.getIterator(SharedDataUtils.getComparatorByPrymaryAcc());
		while (iterator.hasNext()) {
			ProteinBean proteinBean = iterator.next();
			if (accs.contains(proteinBean.getPrimaryAccession().getAccession()))
				continue;
			accs.add(proteinBean.getPrimaryAccession().getAccession());

			if (proteinBean.getEvidence() != null) {
				sb.append(HtmlTemplates.instance.startToolTipWithClass(proteinBean.getEvidence().getDefinition(),
						getProteinEvidenceCSSStyleName(proteinBean.getEvidence(), false)));
				sb.appendEscaped(proteinBean.getEvidence().name());
				sb.append(HtmlTemplates.instance.endToolTip());
				evidences.add(proteinBean.getEvidence());
			}
		}
		if (evidences.size() == 1) {
			sb = new SafeHtmlBuilder();
			final ProteinEvidence next = evidences.iterator().next();
			sb.append(HtmlTemplates.instance.startToolTipWithClass(next.getDefinition(),
					getProteinEvidenceCSSStyleName(next, false)));
			sb.appendEscaped(next.name());
			sb.append(HtmlTemplates.instance.endToolTip());
		}
		return sb.toSafeHtml();
	}

	public static SafeHtml getGroupMemberExistences(ProteinGroupBean proteinGroup) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		Set<UniprotProteinExistence> existences = new HashSet<UniprotProteinExistence>();
		Set<String> accs = new HashSet<String>();
		Iterator<ProteinBean> iterator = proteinGroup.getIterator(SharedDataUtils.getComparatorByPrymaryAcc());
		while (iterator.hasNext()) {
			ProteinBean proteinBean = iterator.next();
			if (accs.contains(proteinBean.getPrimaryAccession().getAccession()))
				continue;
			accs.add(proteinBean.getPrimaryAccession().getAccession());

			if (proteinBean.getUniprotProteinExistence() != null) {
				sb.append(HtmlTemplates.instance.startToolTipWithClass(
						"Uniprot Protein existence: " + proteinBean.getUniprotProteinExistence().getLevel() + "-"
								+ proteinBean.getUniprotProteinExistence().getDescription(),
						getUniprotProteinExistenceCSSStyleName(proteinBean.getUniprotProteinExistence())));
				sb.appendEscaped(proteinBean.getUniprotProteinExistence().getName());
				sb.append(HtmlTemplates.instance.endToolTip());
				existences.add(proteinBean.getUniprotProteinExistence());
			}
		}
		if (existences.size() == 1) {
			sb = new SafeHtmlBuilder();
			final UniprotProteinExistence next = existences.iterator().next();
			sb.append(HtmlTemplates.instance.startToolTipWithClass(
					"Uniprot Protein existence: " + next.getLevel() + "-" + next.getDescription(),
					getUniprotProteinExistenceCSSStyleName(next)));
			sb.appendEscaped(next.getName());
			sb.append(HtmlTemplates.instance.endToolTip());
		}
		return sb.toSafeHtml();
	}

	public static String getProteinEvidenceCSSStyleName(ProteinEvidence evidence, boolean bold) {
		String boldString = "";
		if (bold) {
			boldString = "-bold";
		}
		if (evidence == null) {
			return "";
		}
		switch (evidence) {
		case AMBIGUOUSGROUP:
			return "ProteinEvidence-Ambiguous" + boldString;

		case CONCLUSIVE:
			return "ProteinEvidence-Conclusive" + boldString;

		case INDISTINGUISHABLE:
			return "ProteinEvidence-Indistinguisable" + boldString;

		case NONCONCLUSIVE:
			return "ProteinEvidence-NonConclusive" + boldString;

		case FILTERED:
			return "ProteinEvidence-Filtered" + boldString;

		default:
			break;
		}
		return "";
	}

	public static SafeHtml getProteinCoverageGraphic(ProteinGroupBean proteinGroup) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		Set<String> accs = new HashSet<String>();
		Iterator<ProteinBean> iterator = proteinGroup.getIterator(SharedDataUtils.getComparatorByPrymaryAcc());
		while (iterator.hasNext()) {
			ProteinBean proteinBean = iterator.next();
			if (accs.contains(proteinBean.getPrimaryAccession().getAccession()))
				continue;
			accs.add(proteinBean.getPrimaryAccession().getAccession());

			if (!"".equals(sb.toSafeHtml().asString())) {
				sb.appendEscapedLines(SharedConstants.SEPARATOR);
			}
			sb.append(getProteinCoverageGraphic(proteinBean));
		}
		return sb.toSafeHtml();
	}

	public static SafeHtml getProteinCoverageGraphic(ProteinBean p) {
		final NumberFormat formatter = NumberFormat.getFormat("#.#");
		final char[] coverageArrayString = p.getCoverageArrayString();
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		List<Double> percentages = new ArrayList<Double>();
		List<Boolean> coveredOrNot = new ArrayList<Boolean>();
		List<String> titles = new ArrayList<String>();
		String prefixTitle = "Protein coverage representation for protein: " + p.getPrimaryAccession().getAccession()
				+ "\n";
		if (p.getLength() > 0) {
			prefixTitle += "Protein total length=" + p.getLength() + " AAs\n";
		}

		int fragmentInitAA = 0;
		if (coverageArrayString != null) {
			int seqLenght = coverageArrayString.length;
			boolean currentAACovered = false;
			int lengthFragment = 0;
			for (int i = 0; i < coverageArrayString.length; i++) {
				if (coverageArrayString[i] == '1') {
					if (currentAACovered) {
						lengthFragment++;
					} else {
						double percentage = lengthFragment * 100.0 / seqLenght;
						percentages.add(percentage);
						coveredOrNot.add(false);

						currentAACovered = true;

						titles.add(prefixTitle + lengthFragment + " not covered AAs (" + formatter.format(percentage)
								+ "%)");
						fragmentInitAA = i + 1;
						lengthFragment = 1;
					}
				} else if (coverageArrayString[i] == '0') {
					if (!currentAACovered) {
						lengthFragment++;
					} else {
						double percentage = lengthFragment * 100.0 / seqLenght;
						percentages.add(percentage);
						coveredOrNot.add(true);

						currentAACovered = false;

						titles.add(prefixTitle + lengthFragment + " AAs length" + SharedConstants.SEPARATOR
								+ fragmentInitAA + " to " + (i + 1) + " (" + formatter.format(percentage) + "%)");
						fragmentInitAA = i + 1;
						lengthFragment = 1;
					}
				}
			}
			double percentage = lengthFragment * 100.0 / seqLenght;
			percentages.add(percentage);
			coveredOrNot.add(currentAACovered);
			if (currentAACovered) {
				titles.add(prefixTitle + lengthFragment + " AAs length" + SharedConstants.SEPARATOR + fragmentInitAA
						+ " to " + seqLenght + " (" + formatter.format(percentage) + "%)");
			} else {
				titles.add(prefixTitle + lengthFragment + " not covered AAs (" + formatter.format(percentage) + "%)");
			}

			if (coveredOrNot.size() == 1 && !coveredOrNot.get(0)) {
				sb.appendEscaped("-");
				return sb.toSafeHtml();
			}
			// iterate over resulting arraylists
			for (int i = 0; i < coveredOrNot.size(); i++) {
				final Boolean covered = coveredOrNot.get(i);
				percentage = percentages.get(i);
				SafeHtml safeHtml = null;
				if (covered) {
					// safeHtml = HtmlTemplates.instance.domainImage(percentage,
					// MyClientBundle.INSTANCE.sequenceMarker().getSafeUri(),
					// titles.get(i));
					// safeHtml = HtmlTemplates.instance.simpleDiv(percentage,
					// titles.get(i), "Domain graphicalview");
					final SimplePanel simplePanel = new SimplePanel();
					simplePanel.setStyleName("Domain graphicalview");
					simplePanel.setWidth(percentage + "%");
					simplePanel.setTitle(titles.get(i));

					safeHtml = new SafeHtmlBuilder().appendHtmlConstant(simplePanel.toString()).toSafeHtml();
				} else {
					// safeHtml = HtmlTemplates.instance.bufferImage(percentage,
					// MyClientBundle.INSTANCE.sequenceBuffer().getSafeUri(),
					// titles.get(i));
					// safeHtml = HtmlTemplates.instance.simpleDiv(percentage,
					// titles.get(i), "buffer graphicalview");
					final SimplePanel simplePanel = new SimplePanel();
					simplePanel.setStyleName("buffer graphicalview");
					simplePanel.setWidth(percentage + "%");
					simplePanel.setTitle(titles.get(i));

					safeHtml = new SafeHtmlBuilder().appendHtmlConstant(simplePanel.toString()).toSafeHtml();
				}
				sb.append(safeHtml);
				sb.appendEscaped(" ");
			}
		} else {
			sb.appendEscaped("-");
			return sb.toSafeHtml();
		}

		return sb.toSafeHtml();
	}

	public static SafeHtml getRatioGraphic(ContainsRatios p, RatioBean r) {
		final RatioDistribution ratioDistribution = p.getRatioDistribution(r);
		final char[] ratioNegative = getRatioDistributionNegativeCharArray(ratioDistribution, r);
		final char[] ratioPositive = getRatioDistributionPositiveCharArray(ratioDistribution, r);
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		char[] ratioDistributionArray = null;
		if (ratioNegative != null || ratioPositive != null) {
			ratioDistributionArray = new char[ratioNegative.length + ratioPositive.length];
			int index = 0;
			if (ratioNegative != null) {
				for (char c : ratioNegative) {
					ratioDistributionArray[index++] = c;
				}
			}
			if (ratioPositive != null) {
				for (char c : ratioPositive) {
					ratioDistributionArray[index++] = c;
				}
			}
		} else {
			sb.appendEscaped("-");
			return sb.toSafeHtml();
		}
		boolean isInfinity = false;
		if (Double.compare(r.getValue(), Double.MAX_VALUE) == 0
				|| Double.compare(r.getValue(), -Double.MAX_VALUE) == 0) {
			isInfinity = true;
		}
		String defaultTitle = new StringBuilder("Ratio value: ").append(r.getValue()).append(SharedConstants.SEPARATOR)
				.append(SharedDataUtils.getRatioHeaderTooltip(ColumnName.PSM_RATIO, r.getCondition1().getId(),
						r.getCondition2().getId(), r.getId()))
				.append(SharedConstants.SEPARATOR).append(ratioDistribution.toString()).toString();
		String minimumTitle = new StringBuilder("Minimum value of this ratio in the dataset:")
				.append(SharedConstants.SEPARATOR).append(ratioDistribution.getMinRatio()).toString();
		String maximumTitle = new StringBuilder("Maximum value of this ratio in the dataset:")
				.append(SharedConstants.SEPARATOR).append(ratioDistribution.getMaxRatio()).toString();

		List<Double> percentages = new ArrayList<Double>();
		List<Boolean> coveredOrNot = new ArrayList<Boolean>();
		List<String> titles = new ArrayList<String>();
		List<Boolean> positives = new ArrayList<Boolean>();
		int seqLenght = ratioNegative.length;
		boolean currentPositionCovered = false;
		int lengthFragment = 0;
		boolean positiveSign = false;
		for (int i = 0; i < ratioDistributionArray.length; i++) {
			positiveSign = i > ratioDistributionArray.length / 2;
			if (ratioDistributionArray[i] == '1') {
				if (currentPositionCovered) {
					lengthFragment++;
				} else {
					double percentage = lengthFragment * 100.0 / seqLenght;
					percentages.add(percentage);
					coveredOrNot.add(false);
					currentPositionCovered = true;
					titles.add(positiveSign ? maximumTitle : minimumTitle);
					positives.add(positiveSign);
					lengthFragment = 1;
				}
			} else {
				if (!currentPositionCovered) {
					lengthFragment++;
				} else {
					double percentage = lengthFragment * 100.0 / seqLenght;
					percentages.add(percentage);
					coveredOrNot.add(true);
					positives.add(positiveSign);
					currentPositionCovered = false;
					titles.add(defaultTitle);
					lengthFragment = 1;
				}
			}
		}
		double percentage = lengthFragment * 100.0 / seqLenght;
		percentages.add(percentage);
		positives.add(positiveSign);
		coveredOrNot.add(currentPositionCovered);
		if (!currentPositionCovered) {
			titles.add(positiveSign ? maximumTitle : minimumTitle);
		} else {
			titles.add(defaultTitle);
		}
		if (coveredOrNot.size() == 1 && !coveredOrNot.get(0)) {
			sb.appendEscaped("-");
			return sb.toSafeHtml();
		}
		final FlowPanel ratioBoxPanel = new FlowPanel();
		ratioBoxPanel.setStyleName("ratioBox");
		// iterate over resulting arraylists
		for (int i = 0; i < coveredOrNot.size(); i++) {
			String styleName = "NegativeRatio";
			if (positives.get(i)) {
				styleName = "PositiveRatio";
			}
			if (isInfinity) {
				styleName = "NegativeRatio:hover";
			}
			final Boolean covered = coveredOrNot.get(i);
			percentage = percentages.get(i) / 2.0;
			if (covered) {
				final SimplePanel simplePanel = new SimplePanel();
				simplePanel.setStyleName(styleName + " graphicalviewRatio");
				simplePanel.setWidth(percentage + "%");
				simplePanel.setTitle(titles.get(i));
				ratioBoxPanel.add(simplePanel);
			} else {
				final SimplePanel simplePanel = new SimplePanel();
				if (positives.get(i)) {
					simplePanel.setStyleName("bufferPositiveRatio graphicalviewRatio");
				} else {
					simplePanel.setStyleName("bufferNegativeRatio graphicalviewRatio");
				}
				simplePanel.setWidth(percentage + "%");
				simplePanel.setTitle(titles.get(i));
				ratioBoxPanel.add(simplePanel);
			}
		}

		sb.append(new SafeHtmlBuilder().appendHtmlConstant(ratioBoxPanel.toString()).toSafeHtml());
		return sb.toSafeHtml();
	}

	public static String getUniprotProteinExistenceCSSStyleName(UniprotProteinExistence pe) {

		switch (pe) {
		case HOMOLOGY_INFERRED:
			return "ProteinExistence-Inferred";

		case PREDICTED:
			return "ProteinExistence-Predicted";

		case PROTEIN_LEVEL:
			return "ProteinExistence-Protein";

		case TRANSCRIPT_LEVEL:
			return "ProteinExistence-Transcript";

		case UNCERTAIN:
			return "ProteinExistence-Uncertain";

		default:
			break;
		}
		return "";
	}

	public static String getRelationCSSStyleName(PeptideRelation relation, boolean bold) {
		String boldString = "";
		if (bold) {
			boldString = "-bold";
		}
		if (relation == null) {
			return "";
		}
		switch (relation) {
		case DISCRIMINATING:
			return "PeptideEvidence-Discriminating" + boldString;

		case NONDISCRIMINATING:
			return "PeptideEvidence-Nondiscriminating" + boldString;

		case UNIQUE:
			return "PeptideEvidence-Unique" + boldString;

		default:
			break;
		}
		return "";
	}

	private static char[] getRatioDistributionNegativeCharArray(RatioDistribution ratioDistribution, RatioBean ratio) {

		final int arraySize = 1000;
		if (Double.compare(-Double.MAX_VALUE, ratio.getValue()) == 0) {
			char[] ret = new char[arraySize];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = '1';
			}
			return ret;
		}
		if (ratio.getValue() <= 0.0) {
			if (ratioDistribution != null) {
				final double maxAbsRatio = ratioDistribution.getMaxAbsRatio();
				double proportion = arraySize / maxAbsRatio;
				int ratioIndex = Double.valueOf(Math.abs(ratio.getValue()) * proportion).intValue();
				ratioIndex = arraySize - ratioIndex;
				if (ratioIndex == arraySize) {
					// paint something
					ratioIndex = arraySize - 1;
				}
				char[] ret = new char[arraySize];
				for (int i = 0; i < ret.length; i++) {
					if (i < ratioIndex) {
					} else {
						ret[i] = '1';
					}
				}
				return ret;
			}
		}
		return new char[arraySize];
	}

	private static char[] getRatioDistributionPositiveCharArray(RatioDistribution ratioDistribution, RatioBean ratio) {
		final int arraySize = 1000;
		if (Double.compare(Double.MAX_VALUE, ratio.getValue()) == 0) {
			char[] ret = new char[arraySize];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = '1';
			}
			return ret;
		}
		if (ratio.getValue() >= 0.0) {
			if (ratioDistribution != null) {
				final double maxAbsRatio = ratioDistribution.getMaxAbsRatio();
				double proportion = arraySize / maxAbsRatio;
				int ratioIndex = Double.valueOf(Math.abs(ratio.getValue()) * proportion).intValue();
				char[] ret = new char[arraySize];
				for (int i = 0; i < ret.length; i++) {
					if (i <= ratioIndex) {
						ret[i] = '1';
					}
				}
				return ret;
			}
		}
		return new char[arraySize];
	}

	/**
	 * The same list getRatioScoreStringByConditions but including the score
	 * name
	 *
	 * @param condition1Name
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 * @param skipInfinities
	 * @return
	 */
	public static String getExtendedRatioScoreStringByConditions(ContainsRatios p, String condition1Name,
			String condition2Name, String projectTag, String ratioName, String ratioScoreName, boolean skipInfinities) {
		StringBuilder sb = new StringBuilder();

		final List<ScoreBean> ratioScores = p.getRatioScoresByConditions(condition1Name, condition2Name, projectTag,
				ratioName, ratioScoreName);
		for (ScoreBean ratioScore : ratioScores) {
			try {

				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append("Confident score associated to ratio: " + ratioName + SharedConstants.SEPARATOR
						+ "Conditions in ratio: " + condition1Name + " / " + condition2Name + SharedConstants.SEPARATOR
						+ "Score name: " + ratioScore.getScoreName() + SharedConstants.SEPARATOR);

				Double doubleValue = Double.valueOf(ratioScore.getValue());
				if (doubleValue.toString().endsWith(".0")) {
					sb.append("Value: " + String.valueOf(doubleValue.intValue()));
				} else {
					try {
						String format = SharedDataUtils.formatNumber(doubleValue, 3, true);
						sb.append("Score value: " + format);
					} catch (NumberFormatException e2) {

					}
				}
				if (ratioScore.getScoreType() != null) {
					sb.append(SharedConstants.SEPARATOR + "Score type: " + ratioScore.getScoreType());
				}
				if (ratioScore.getScoreDescription() != null) {
					sb.append(SharedConstants.SEPARATOR + "Score description: " + ratioScore.getScoreDescription());
				}
			} catch (NumberFormatException e) {
				// add the string as it is
			}

		}

		return sb.toString();
	}

	public static SafeHtml getUniprotFeatureSafeHtml(ProteinBean p, String... featureTypes) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		List<String> uniprotFeatureList = new ArrayList<String>();
		uniprotFeatureList.addAll(java.util.Arrays.asList(featureTypes));
		Collections.sort(uniprotFeatureList);
		for (String featureType : uniprotFeatureList) {

			final List<UniprotFeatureBean> uniprotFeatures = p.getUniprotFeaturesByFeatureType(featureType);

			if (uniprotFeatures.isEmpty()) {
				continue;
			}

			sb.append(template.startToolTipWithClass(featureType, "featureType"));
			sb.appendEscaped(featureType);
			sb.append(template.endToolTip());

			for (UniprotFeatureBean uniprotFeature : uniprotFeatures) {
				String toolTipText = getToolTipFromUniprotFeature(uniprotFeature);
				sb.append(template.startToolTip(toolTipText));
				if (uniprotFeature.getDescription() != null) {
					sb.appendEscaped(uniprotFeature.getDescription());
				} else {
					sb.appendEscaped(uniprotFeature.getFeatureType());
				}
				if (uniprotFeature.getPositionStart() > -1) {
					if (uniprotFeature.getPositionStart() == uniprotFeature.getPositionEnd()) {
						sb.appendEscaped(" (").append(uniprotFeature.getPositionStart()).appendEscaped(")");
					} else {
						sb.appendEscaped(" (").append(uniprotFeature.getPositionStart()).appendEscaped("-")
								.append(uniprotFeature.getPositionEnd()).appendEscaped(")");
					}
				}
				sb.append(template.endToolTip());
			}

		}
		return sb.toSafeHtml();
	}

	public static String getDownloadURL(String fileName, String fileType) {

		// return Window.Location.getProtocol() + "//" +
		// Window.Location.getHost() + "/pint/download?"
		// + SharedConstants.FILE_TO_DOWNLOAD + "=" + fileName + "&" +
		// SharedConstants.FILE_TYPE + "=" + fileType;

		return GWT.getModuleBaseURL() + "download?" + SharedConstants.FILE_TO_DOWNLOAD + "=" + fileName + "&"
				+ SharedConstants.FILE_TYPE + "=" + fileType;

		// return GWT.getModuleBaseURL() + "download?" +
		// SharedConstants.FILE_TO_DOWNLOAD + "=" + fileName + "&"
		// + SharedConstants.FILE_TYPE + "=" + fileType;
	}

	public static SafeHtml getUniprotFeatureSafeHtml(PSMBean p, String... featureTypes) {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = p.getStartingPositions();
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		final Set<ProteinBean> proteins = p.getProteins();
		final Map<String, ProteinBean> proteinBeanByAccession = SharedDataUtils
				.getProteinBeansByPrimaryAccession(proteins);
		// get a list of proteins according to the order of the primary
		// accessions
		List<ProteinBean> proteinBeanList = new ArrayList<ProteinBean>();
		for (AccessionBean acc : primaryAccessions) {
			proteinBeanList.add(proteinBeanByAccession.get(acc.getAccession()));
		}
		return getUniprotFeatureSafeHtml(startingPositions, proteinBeanList, featureTypes);
	}

	public static SafeHtml getUniprotFeatureSafeHtml(PeptideBean p, String... featureTypes) {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = p.getStartingPositions();
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		final Set<ProteinBean> proteins = p.getProteins();
		final Map<String, ProteinBean> proteinBeanByAccession = SharedDataUtils
				.getProteinBeansByPrimaryAccession(proteins);
		// get a list of proteins according to the order of the primary
		// accessions
		List<ProteinBean> proteinBeanList = new ArrayList<ProteinBean>();
		for (AccessionBean acc : primaryAccessions) {
			proteinBeanList.add(proteinBeanByAccession.get(acc.getAccession()));
		}
		return getUniprotFeatureSafeHtml(startingPositions, proteinBeanList, featureTypes);
	}

	private static SafeHtml getUniprotFeatureSafeHtml(
			Map<String, List<Pair<Integer, Integer>>> startingPositionsByProtein, List<ProteinBean> proteinBeans,
			String... featureTypes) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		for (String featureType : featureTypes) {
			Map<UniprotFeatureBean, Set<ProteinBean>> proteinBeansByUniprotFeatureBean = new HashMap<UniprotFeatureBean, Set<ProteinBean>>();
			for (ProteinBean p : proteinBeans) {

				if (startingPositionsByProtein.containsKey(p.getPrimaryAccession().getAccession())) {
					final List<UniprotFeatureBean> uniprotFeatureList = p.getUniprotFeaturesByFeatureType(featureType);
					if (uniprotFeatureList.isEmpty()) {
						continue;
					}
					for (UniprotFeatureBean uniprotFeature : uniprotFeatureList) {
						// only consider the ones with annotated start and end
						// positions
						if (uniprotFeature.getPositionStart() > -1 && uniprotFeature.getPositionEnd() > -1) {

							final List<Pair<Integer, Integer>> startingPositions = startingPositionsByProtein
									.get(p.getPrimaryAccession().getAccession());
							SEQUENCE_OVERLAPPING sequenceOverlapping = SharedDataUtils.isPeptideIncludedInThatRange(
									startingPositions, uniprotFeature.getPositionStart(),
									uniprotFeature.getPositionEnd());
							if (sequenceOverlapping != SEQUENCE_OVERLAPPING.NOT_COVERED) {
								if (proteinBeansByUniprotFeatureBean.containsKey(uniprotFeature)) {
									proteinBeansByUniprotFeatureBean.get(uniprotFeature).add(p);
								} else {
									Set<ProteinBean> proteinSet = new HashSet<ProteinBean>();
									proteinSet.add(p);
									proteinBeansByUniprotFeatureBean.put(uniprotFeature, proteinSet);
								}
							}
						}
					}
				}
			}
			boolean featureTypeHeaderPrinted = false;
			List<UniprotFeatureBean> uniprotFeatureList = new ArrayList<UniprotFeatureBean>();
			uniprotFeatureList.addAll(proteinBeansByUniprotFeatureBean.keySet());
			Collections.sort(uniprotFeatureList);
			for (UniprotFeatureBean uniprotFeature : uniprotFeatureList) {
				if (!featureTypeHeaderPrinted) {
					sb.append(template.startToolTipWithClass(featureType, "featureType"));
					sb.appendEscaped(featureType);
					sb.append(template.endToolTip());
					featureTypeHeaderPrinted = true;
				}
				String toolTipText = getToolTipFromUniprotFeature(uniprotFeature);
				sb.append(template.startToolTip(toolTipText));
				if (uniprotFeature.getDescription() != null) {
					sb.appendEscaped(uniprotFeature.getDescription());
				} else {
					sb.appendEscaped(uniprotFeature.getFeatureType());
				}

				if (uniprotFeature.getPositionStart() == uniprotFeature.getPositionEnd()) {
					sb.appendEscaped(" (").append(uniprotFeature.getPositionStart()).appendEscaped(")");
				} else {
					sb.appendEscaped(" (").append(uniprotFeature.getPositionStart()).appendEscaped("-")
							.append(uniprotFeature.getPositionEnd()).appendEscaped(")");
				}

				final Set<ProteinBean> proteinSet = proteinBeansByUniprotFeatureBean.get(uniprotFeature);
				Map<SEQUENCE_OVERLAPPING, List<ProteinBean>> proteinsBySequenceOverlapping = new HashMap<SEQUENCE_OVERLAPPING, List<ProteinBean>>();
				for (ProteinBean proteinBean : proteinSet) {

					final List<Pair<Integer, Integer>> startingPositions = startingPositionsByProtein
							.get(proteinBean.getPrimaryAccession().getAccession());
					SEQUENCE_OVERLAPPING sequenceOverlapping = SharedDataUtils.isPeptideIncludedInThatRange(
							startingPositions, uniprotFeature.getPositionStart(), uniprotFeature.getPositionEnd());
					if (proteinsBySequenceOverlapping.containsKey(sequenceOverlapping)) {
						proteinsBySequenceOverlapping.get(sequenceOverlapping).add(proteinBean);
					} else {
						List<ProteinBean> list = new ArrayList<ProteinBean>();
						list.add(proteinBean);
						proteinsBySequenceOverlapping.put(sequenceOverlapping, list);
					}
				}

				for (SEQUENCE_OVERLAPPING overlapping : SEQUENCE_OVERLAPPING.values()) {
					if (proteinsBySequenceOverlapping.containsKey(overlapping)) {
						final List<ProteinBean> proteinList = proteinsBySequenceOverlapping.get(overlapping);
						if (!proteinList.isEmpty()) {
							Collections.sort(proteinList);// because is
															// comparable
							sb.appendHtmlConstant("<div class='" + overlapping.getCSSClassName() + "'>");
							String plural = "";
							if (proteinList.size() > 0) {
								plural = "s";
							}
							sb.appendEscaped(" (" + overlapping.getDescription() + " in protein" + plural + " ");
							boolean first = true;
							for (ProteinBean proteinBean : proteinList) {
								if (!first) {
									sb.appendEscaped(",");
								}
								sb.appendEscaped(proteinBean.getPrimaryAccession().getAccession());
								first = false;
							}
							sb.appendEscaped(")");
							sb.appendHtmlConstant("</div>");
						}
					}
				}
				sb.append(template.endToolTip());

				sb.appendEscaped(SharedConstants.NEW_LINE_JAVA);
			}
		}

		return sb.toSafeHtml();

	}

	private static String getToolTipFromUniprotFeature(UniprotFeatureBean uniprotFeature) {
		StringBuilder sb = new StringBuilder();
		sb.append(uniprotFeature.getFeatureType()).append(":\n");
		sb.append(uniprotFeature.getDescription()).append("\n");
		if (uniprotFeature.getPositionStart() > -1) {
			if (uniprotFeature.getPositionStart() != uniprotFeature.getPositionEnd()) {
				sb.append("Positions: ").append(uniprotFeature.getPositionStart()).append("-")
						.append(uniprotFeature.getPositionEnd()).append("\n");
			} else {
				sb.append("Position: ").append(uniprotFeature.getPositionStart()).append("\n");
			}
			if (uniprotFeature.getOriginal() != null && !"".equals(uniprotFeature.getOriginal())) {
				sb.append("Original: ").append(uniprotFeature.getOriginal()).append("\n");
			}
			if (uniprotFeature.getVariation() != null && !"".equals(uniprotFeature.getVariation())) {
				sb.append("Variation: ").append(uniprotFeature.getVariation()).append("\n");
			}
			sb.append("Length: ").append(uniprotFeature.getLength());
		}
		return sb.toString();
	}

	public static SafeHtml getReactomeSafeHtml(ProteinBean p) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();

		Set<String> ids = new HashSet<String>();
		Collections.sort(p.getReactomePathways());
		for (ReactomePathwayRef reactome : p.getReactomePathways()) {
			if (ids.contains(reactome.getId())) {
				continue;
			}
			ids.add(reactome.getId());
			if (!"".equals(sb.toSafeHtml().asString())) {
				sb.appendEscapedLines(SharedConstants.SEPARATOR);
			}
			final SafeHtml geneLink = getReactomeEntryLink(reactome);
			sb.append(geneLink);
		}

		return sb.toSafeHtml();
	}

	private static SafeHtml getReactomeEntryLink(ReactomePathwayRef reactome) {
		final String urlString = SharedConstants.REACTOME_ENTRY_LINK + reactome.getId();

		String title = "Go to reactome.org to see " + reactome.getId() + " entry";

		final SafeHtml link = template.link(UriUtils.fromString(urlString), "reactomeLink", title, title,
				reactome.getDescription());
		return link;
	}

}
