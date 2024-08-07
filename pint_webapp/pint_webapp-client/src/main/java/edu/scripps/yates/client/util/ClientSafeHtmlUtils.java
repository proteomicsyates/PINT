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
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.PeptideRelation;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.ProteinEvidence;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.ReactomePathwayRef;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.UniprotFeatureBean;
import edu.scripps.yates.shared.model.UniprotProteinExistence;
import edu.scripps.yates.shared.model.interfaces.ContainsGenes;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.model.light.ProteinBeanLight;
import edu.scripps.yates.shared.model.light.ProteinGroupBeanLight;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SequenceOverlapping;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class ClientSafeHtmlUtils {
	private static final HtmlTemplates template = GWT.create(HtmlTemplates.class);
	private static final int MAX_LENGTH_FUNCTION = 150;
	private static final int MAX_LENGTH_OMIM = 150;

	// private static final MyClientBundle myClientBundle =
	// MyClientBundle.INSTANCE;
	public static Anchor getPubmedLink(ProjectBean projectBean) {
		if (projectBean != null) {
			final String pubmedId = projectBean.getPubmedLink();

			if (pubmedId != null) {
				final SafeHtmlBuilder sb = new SafeHtmlBuilder();
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
		final String geneId = gene.getGeneID();
		if (hgncId > 0) {
			String title = "";
			final String urlString = SharedConstants.GENENAMES_LINK + hgncId;
			if (includeTitle)
				title = gene.getName() + " (" + gene.getGeneID() + ")" + SharedConstants.SEPARATOR
						+ "(Click to go to HGNC repository)";
			final SafeHtml link = template.link(UriUtils.fromString(urlString), "geneLinkLink", title, title, geneId);

			return link;
		} else {
			final SafeHtmlBuilder sb = new SafeHtmlBuilder();
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
		final Set<String> geneIds = new HashSet<String>();
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
				for (final GeneBean otherGene : otherGenes) {
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
			final SafeHtmlBuilder sb = new SafeHtmlBuilder();
			String tooltip = gene.getGeneID();
			if (gene.getGeneType() != null)
				tooltip += " (" + gene.getGeneType() + ")" + SharedConstants.SEPARATOR;
			String othersString = "";
			for (final GeneBean otherGene : otherGenes) {
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
		final List<GeneBean> genes = p.getGenes(false);
		GeneBean primaryGene = null;
		for (final GeneBean geneBean : genes) {
			if (geneBean != null && GeneBean.PRIMARY.equals(geneBean.getGeneType()))
				primaryGene = geneBean;
		}
		final SafeHtml safeHtmlFromGenes = getSafeHtmlFromGenes(primaryGene, genes, includeTitle);
		return safeHtmlFromGenes;
	}

	public static SafeHtml getGeneLinks(ContainsGenes p, boolean includeTitle) {
		if (p instanceof ProteinBeanLight) {
			return getGeneLink(p, includeTitle);
		} else if (p instanceof ProteinGroupBeanLight) {
			final SafeHtmlBuilder sb = new SafeHtmlBuilder();
			final ProteinGroupBeanLight proteinGroup = (ProteinGroupBeanLight) p;
			final Set<String> accs = new HashSet<String>();
			boolean addNewLine = false;

			final Iterator<ProteinBeanLight> iterator = proteinGroup
					.getIterator(SharedDataUtil.getComparatorByPrymaryAccForLightProteins());
			while (iterator.hasNext()) {
				final ProteinBeanLight proteinBean = iterator.next();
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
		Collections.sort(primaryAccessions, SharedDataUtil.getComparatorByAccession());
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();
		final Set<String> proteinAccs = new HashSet<String>();
		for (final AccessionBean primaryAccession : primaryAccessions) {

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
		final StringBuilder sb = new StringBuilder();
		final List<GeneBean> genes = p.getGenes(false);
		final Set<String> names = new HashSet<String>();
		for (final GeneBean geneBean : genes) {
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

	public static SafeHtml getProteinFunctionSafeHtml(ProteinBeanLight p) {
		final String function = p.getFunctionString();
		String shortFunction = function;
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

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
		for (final String string : split) {
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

	public static SafeHtml getOmimLinks(ProteinBeanLight p) {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();
		final List<Integer> omimIDs = p.getOmimSortedIDs();
		if (omimIDs != null && !omimIDs.isEmpty()) {

			for (final Integer omimID : p.getOmimSortedIDs()) {
				final OmimEntryBean omimEntry = p.getOmimEntries().get(omimID);
				final StringBuilder omimTooltip = new StringBuilder();
				omimTooltip.append("OMIM entry ID: " + omimEntry.getId() + SharedConstants.SEPARATOR);
				omimTooltip.append(omimEntry.getPreferredTitle() + SharedConstants.SEPARATOR);
				final List<String> alternativeTitles = omimEntry.getAlternativeTitles();
				omimTooltip.append("Alternative titles; symbols: " + SharedConstants.SEPARATOR);
				for (final String alternativeTitle : alternativeTitles) {
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

	public static SafeHtml getGroupMemberEvidences(ProteinGroupBeanLight p) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		final Set<ProteinEvidence> evidences = new HashSet<ProteinEvidence>();
		final Set<String> accs = new HashSet<String>();
		final Iterator<ProteinBeanLight> iterator = p
				.getIterator(SharedDataUtil.getComparatorByPrymaryAccForLightProteins());
		while (iterator.hasNext()) {
			final ProteinBeanLight proteinBean = iterator.next();
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

	public static SafeHtml getGroupMemberExistences(ProteinGroupBeanLight proteinGroup) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		final Set<UniprotProteinExistence> existences = new HashSet<UniprotProteinExistence>();
		final Set<String> accs = new HashSet<String>();
		final Iterator<ProteinBeanLight> iterator = proteinGroup
				.getIterator(SharedDataUtil.getComparatorByPrymaryAccForLightProteins());
		while (iterator.hasNext()) {
			final ProteinBeanLight proteinBean = iterator.next();
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

	public static SafeHtml getProteinCoverageGraphic(ProteinGroupBeanLight proteinGroup) {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();
		final Set<String> accs = new HashSet<String>();
		final Iterator<ProteinBeanLight> iterator = proteinGroup
				.getIterator(SharedDataUtil.getComparatorByPrymaryAccForLightProteins());
		while (iterator.hasNext()) {
			final ProteinBeanLight proteinBean = iterator.next();
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

	public static SafeHtml getProteinCoverageGraphic(ProteinBeanLight p) {
		return getProteinCoverageGraphic(p, false);
	}

	public static SafeHtml getProteinCoverageGraphic(ProteinBeanLight p, boolean bigRepresentation) {
		final NumberFormat formatter = NumberFormat.getFormat("#.#");
		final char[] coverageArrayString = p.getCoverageArrayString();
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();
		final List<Double> percentages = new ArrayList<Double>();
		final List<Boolean> coveredOrNot = new ArrayList<Boolean>();
		final List<String> titles = new ArrayList<String>();
		String prefixTitle = "Protein coverage representation for protein: " + p.getPrimaryAccession().getAccession()
				+ "\n";
		if (p.getLength() > 0) {
			prefixTitle += "Protein total length=" + p.getLength() + " AAs\n";
		}

		int fragmentInitAA = 0;
		if (coverageArrayString != null) {
			final int seqLenght = coverageArrayString.length;
			boolean currentAACovered = false;
			int lengthFragment = 0;
			for (int i = 0; i < coverageArrayString.length; i++) {
				if (coverageArrayString[i] == '1') {
					if (currentAACovered) {
						lengthFragment++;
					} else {
						final double percentage = lengthFragment * 100.0 / seqLenght;
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
						final double percentage = lengthFragment * 100.0 / seqLenght;
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

					final SimplePanel simplePanel = new SimplePanel();
					if (bigRepresentation) {
						simplePanel.setStyleName("Domain graphicalview_big");
					} else {
						simplePanel.setStyleName("Domain graphicalview");
					}
					simplePanel.setWidth(percentage + "%");
					simplePanel.setTitle(titles.get(i));
					safeHtml = new SafeHtmlBuilder().appendHtmlConstant(simplePanel.toString()).toSafeHtml();
				} else {

					final SimplePanel simplePanel = new SimplePanel();
					if (bigRepresentation) {
						simplePanel.setStyleName("buffer graphicalview_big");
					} else {
						simplePanel.setStyleName("buffer graphicalview");
					}
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
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();
		char[] ratioDistributionArray = null;
		if (ratioDistribution != null && (ratioNegative != null || ratioPositive != null)) {
			ratioDistributionArray = new char[ratioNegative.length + ratioPositive.length];
			int index = 0;
			if (ratioNegative != null) {
				for (final char c : ratioNegative) {
					ratioDistributionArray[index++] = c;
				}
			}
			if (ratioPositive != null) {
				for (final char c : ratioPositive) {
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
		final String defaultTitle = new StringBuilder("Ratio value: ").append(r.getValue())
				.append(SharedConstants.SEPARATOR)
				.append(SharedDataUtil.getRatioHeaderTooltip(ColumnName.PSM_RATIO, r.getCondition1().getId(),
						r.getCondition2().getId(), r.getId()))
				.append(SharedConstants.SEPARATOR).append(ClientDataUtil.getRatioDistributionString(ratioDistribution))
				.toString();
		final String minimumTitle = new StringBuilder("Minimum value of this ratio in the dataset:")
				.append(SharedConstants.SEPARATOR).append(ratioDistribution.getMinRatio()).toString();
		final String maximumTitle = new StringBuilder("Maximum value of this ratio in the dataset:")
				.append(SharedConstants.SEPARATOR).append(ratioDistribution.getMaxRatio()).toString();

		final List<Double> percentages = new ArrayList<Double>();
		final List<Boolean> coveredOrNot = new ArrayList<Boolean>();
		final List<String> titles = new ArrayList<String>();
		final List<Boolean> positives = new ArrayList<Boolean>();
		final int seqLenght = ratioNegative.length;
		boolean currentPositionCovered = false;
		int lengthFragment = 0;
		boolean positiveSign = false;
		for (int i = 0; i < ratioDistributionArray.length; i++) {
			positiveSign = i > ratioDistributionArray.length / 2;
			if (ratioDistributionArray[i] == '1') {
				if (currentPositionCovered) {
					lengthFragment++;
				} else {
					final double percentage = lengthFragment * 100.0 / seqLenght;
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
					final double percentage = lengthFragment * 100.0 / seqLenght;
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
			final char[] ret = new char[arraySize];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = '1';
			}
			return ret;
		}
		if (ratio.getValue() <= 0.0) {
			if (ratioDistribution != null) {
				final double maxAbsRatio = ratioDistribution.getMaxAbsRatio();
				final double proportion = arraySize / maxAbsRatio;
				int ratioIndex = Double.valueOf(Math.abs(ratio.getValue()) * proportion).intValue();
				ratioIndex = arraySize - ratioIndex;
				if (ratioIndex == arraySize) {
					// paint something
					ratioIndex = arraySize - 1;
				}
				final char[] ret = new char[arraySize];
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
			final char[] ret = new char[arraySize];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = '1';
			}
			return ret;
		}
		if (ratio.getValue() >= 0.0) {
			if (ratioDistribution != null) {
				final double maxAbsRatio = ratioDistribution.getMaxAbsRatio();
				final double proportion = arraySize / maxAbsRatio;
				final int ratioIndex = Double.valueOf(Math.abs(ratio.getValue()) * proportion).intValue();
				final char[] ret = new char[arraySize];
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
	 * The same list getRatioScoreStringByConditions but including the score name
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
		final StringBuilder sb = new StringBuilder();

		final List<ScoreBean> ratioScores = p.getRatioScoresByConditions(condition1Name, condition2Name, projectTag,
				ratioName, ratioScoreName);
		for (final ScoreBean ratioScore : ratioScores) {
			try {

				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append("Confident score associated to ratio: " + ratioName + SharedConstants.SEPARATOR
						+ "Conditions in ratio: " + condition1Name + " / " + condition2Name + SharedConstants.SEPARATOR
						+ "Score name: " + ratioScore.getScoreName() + SharedConstants.SEPARATOR);

				final Double doubleValue = Double.valueOf(ratioScore.getValue());
				if (doubleValue.toString().endsWith(".0")) {
					sb.append("Value: " + String.valueOf(doubleValue.intValue()));
				} else {
					try {
						// no formatting because this is the tooltip, we want to
						// show everything
						final String format = String.valueOf(doubleValue);
						sb.append("Score value: " + format);
					} catch (final NumberFormatException e2) {

					}
				}
				if (ratioScore.getScoreType() != null) {
					sb.append(SharedConstants.SEPARATOR + "Score type: " + ratioScore.getScoreType());
				}
				if (ratioScore.getScoreDescription() != null) {
					sb.append(SharedConstants.SEPARATOR + "Score description: " + ratioScore.getScoreDescription());
				}
			} catch (final NumberFormatException e) {
				// add the string as it is
			}

		}

		return sb.toString();
	}

	public static SafeHtml getUniprotFeatureSafeHtml(ProteinBeanLight p, String... featureTypes) {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();
		final List<String> uniprotFeatureList = new ArrayList<String>();
		uniprotFeatureList.addAll(java.util.Arrays.asList(featureTypes));
		Collections.sort(uniprotFeatureList);
		for (final String featureType : uniprotFeatureList) {

			final List<UniprotFeatureBean> uniprotFeatures = p.getUniprotFeaturesByFeatureType(featureType);

			if (uniprotFeatures.isEmpty()) {
				continue;
			}

			sb.append(template.startToolTipWithClass(featureType, "featureType"));
			sb.appendEscaped(featureType);
			sb.append(template.endToolTip());

			for (final UniprotFeatureBean uniprotFeature : uniprotFeatures) {
				final String toolTipText = getToolTipFromUniprotFeature(uniprotFeature);
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

	public static String getProjectZipDownloadURL(String projectTag) {

		return GWT.getModuleBaseURL() + "download?" + SharedConstants.FILE_TO_DOWNLOAD + "=" + projectTag + "&"
				+ SharedConstants.FILE_TYPE + "=" + SharedConstants.DATASET_INPUT_FILES_ZIP;

	}

	public static SafeHtml getUniprotFeatureSafeHtml(PSMBeanLight p, String... featureTypes) {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = p.getStartingPositions();
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		final Set<ProteinBeanLight> proteins = p.getProteins();
		final Map<String, ProteinBeanLight> proteinBeanByAccession = SharedDataUtil
				.getLightProteinBeansByPrimaryAccession(proteins);
		// get a list of proteins according to the order of the primary
		// accessions
		final List<ProteinBeanLight> proteinBeanList = new ArrayList<ProteinBeanLight>();
		for (final AccessionBean acc : primaryAccessions) {
			proteinBeanList.add(proteinBeanByAccession.get(acc.getAccession()));
		}
		return getUniprotFeatureSafeHtml(startingPositions, proteinBeanList, featureTypes);
	}

	public static SafeHtml getUniprotFeatureSafeHtml(PeptideBeanLight p, String... featureTypes) {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = p.getStartingPositions();
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		final Set<ProteinBeanLight> proteins = p.getProteins();

		final Map<String, ProteinBeanLight> proteinBeanByAccession = SharedDataUtil
				.getLightProteinBeansByPrimaryAccession(proteins);
		// get a list of proteins according to the order of the primary
		// accessions
		final List<ProteinBeanLight> proteinBeanList = new ArrayList<ProteinBeanLight>();
		for (final AccessionBean acc : primaryAccessions) {
			final ProteinBeanLight protein = proteinBeanByAccession.get(acc.getAccession());
			proteinBeanList.add(protein);
		}
		return getUniprotFeatureSafeHtml(startingPositions, proteinBeanList, featureTypes);
	}

	private static SafeHtml getUniprotFeatureSafeHtml(
			Map<String, List<Pair<Integer, Integer>>> startingPositionsByProtein, List<ProteinBeanLight> proteinBeans,
			String... featureTypes) {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();
		for (final String featureType : featureTypes) {
			final Map<UniprotFeatureBean, Set<ProteinBeanLight>> proteinBeansByUniprotFeatureBean = new HashMap<UniprotFeatureBean, Set<ProteinBeanLight>>();
			for (final ProteinBeanLight p : proteinBeans) {

				final AccessionBean primaryAccession = p.getPrimaryAccession();
				if (startingPositionsByProtein.containsKey(primaryAccession.getAccession())) {
					final List<UniprotFeatureBean> uniprotFeatureList = p.getUniprotFeaturesByFeatureType(featureType);
					if (uniprotFeatureList.isEmpty()) {
						continue;
					}
					for (final UniprotFeatureBean uniprotFeature : uniprotFeatureList) {
						// only consider the ones with annotated start and end
						// positions
						if (uniprotFeature.getPositionStart() > -1 && uniprotFeature.getPositionEnd() > -1) {

							final List<Pair<Integer, Integer>> startingPositions = startingPositionsByProtein
									.get(primaryAccession.getAccession());
							final SequenceOverlapping sequenceOverlapping = SharedDataUtil.isPeptideIncludedInThatRange(
									startingPositions, uniprotFeature.getPositionStart(),
									uniprotFeature.getPositionEnd());
							if (sequenceOverlapping != SequenceOverlapping.NOT_COVERED) {
								if (proteinBeansByUniprotFeatureBean.containsKey(uniprotFeature)) {
									proteinBeansByUniprotFeatureBean.get(uniprotFeature).add(p);
								} else {
									final Set<ProteinBeanLight> proteinSet = new HashSet<ProteinBeanLight>();
									proteinSet.add(p);
									proteinBeansByUniprotFeatureBean.put(uniprotFeature, proteinSet);
								}
							}
						}
					}
				}
			}
			boolean featureTypeHeaderPrinted = false;
			final List<UniprotFeatureBean> uniprotFeatureList = new ArrayList<UniprotFeatureBean>();
			uniprotFeatureList.addAll(proteinBeansByUniprotFeatureBean.keySet());
			Collections.sort(uniprotFeatureList);
			for (final UniprotFeatureBean uniprotFeature : uniprotFeatureList) {
				if (!featureTypeHeaderPrinted) {
					sb.append(template.startToolTipWithClass(featureType, "featureType"));
					sb.appendEscaped(featureType);
					sb.append(template.endToolTip());
					featureTypeHeaderPrinted = true;
				}
				final String toolTipText = getToolTipFromUniprotFeature(uniprotFeature);
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

				final Set<ProteinBeanLight> proteinSet = proteinBeansByUniprotFeatureBean.get(uniprotFeature);
				final Map<SequenceOverlapping, List<ProteinBeanLight>> proteinsBySequenceOverlapping = new HashMap<SequenceOverlapping, List<ProteinBeanLight>>();
				for (final ProteinBeanLight proteinBean : proteinSet) {

					final List<Pair<Integer, Integer>> startingPositions = startingPositionsByProtein
							.get(proteinBean.getPrimaryAccession().getAccession());
					final SequenceOverlapping sequenceOverlapping = SharedDataUtil.isPeptideIncludedInThatRange(
							startingPositions, uniprotFeature.getPositionStart(), uniprotFeature.getPositionEnd());
					if (proteinsBySequenceOverlapping.containsKey(sequenceOverlapping)) {
						proteinsBySequenceOverlapping.get(sequenceOverlapping).add(proteinBean);
					} else {
						final List<ProteinBeanLight> list = new ArrayList<ProteinBeanLight>();
						list.add(proteinBean);
						proteinsBySequenceOverlapping.put(sequenceOverlapping, list);
					}
				}

				for (final SequenceOverlapping overlapping : SequenceOverlapping.values()) {
					if (proteinsBySequenceOverlapping.containsKey(overlapping)) {
						final List<ProteinBeanLight> proteinList = proteinsBySequenceOverlapping.get(overlapping);
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
							for (final ProteinBeanLight proteinBean : proteinList) {
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
		final StringBuilder sb = new StringBuilder();
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

	public static SafeHtml getReactomeSafeHtml(ProteinBeanLight p) {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		final Set<String> ids = new HashSet<String>();
		Collections.sort(p.getReactomePathways());
		for (final ReactomePathwayRef reactome : p.getReactomePathways()) {
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

		final String title = "Go to reactome.org to see " + reactome.getId() + " entry";

		final SafeHtml link = template.link(UriUtils.fromString(urlString), "reactomeLink", title, title,
				reactome.getDescription());
		return link;
	}

	public static SafeHtml getRichPeptideSequence(String sequence, List<PTMBean> ptms) {
		if (ptms == null || ptms.isEmpty())
			return new SafeHtmlBuilder().appendEscaped(sequence).toSafeHtml();
		final HtmlTemplates template = GWT.create(HtmlTemplates.class);
		final SafeHtmlBuilder shb = new SafeHtmlBuilder();
		for (int i = 0; i < sequence.length(); i++) {
			boolean modified = false;
			for (final PTMBean ptm : ptms) {
				for (final PTMSiteBean ptmSite : ptm.getPtmSites()) {
					final int position = ptmSite.getPosition();
					if (position == i + 1) {
						final SafeHtml html = template.spanClass("modifiedAA", String.valueOf(sequence.charAt(i)));
						shb.append(html);
						modified = true;
						break;
					}
				}
				if (modified)
					break;
			}
			if (!modified)
				shb.append(new SafeHtmlBuilder().appendEscaped(String.valueOf(sequence.charAt(i))).toSafeHtml());
		}
		return shb.toSafeHtml();
	}
}
