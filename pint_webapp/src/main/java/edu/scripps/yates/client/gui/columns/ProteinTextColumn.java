package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ProteinColumns;
import edu.scripps.yates.shared.columns.comparator.ProteinComparator;
import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.OmimEntryBean;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.UniprotFeatures;

public class ProteinTextColumn extends CustomTextColumn<ProteinBean> implements MyIdColumn<ProteinBean> {
	private final HtmlTemplates template = GWT.create(HtmlTemplates.class);

	private final ColumnName columnName;
	private final Comparator<ProteinBean> comparator;
	private double width;
	private final double defaultWidth;
	private final String conditionName;
	private final AmountType amountType;
	// in case of ratios
	private final String condition2Name;
	private final String projectTag;
	private final Header<String> footer;
	private final Header<?> header;
	private boolean visibleState;
	private Set<String> currentExperimentalConditions = new HashSet<String>();

	private String ratioName;

	public ProteinTextColumn(ColumnName columnName, boolean visibleState, Header<?> header, Header<String> footer) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new ProteinComparator(columnName);
		defaultWidth = getDefaultWidth(columnName);
		conditionName = null;
		condition2Name = null;
		amountType = null;
		projectTag = null;
		this.footer = footer;
		this.header = header;
		this.visibleState = visibleState;
		if (visibleState)
			width = defaultWidth;
		else
			width = 0;
	}

	public ProteinTextColumn(ColumnName columnName, boolean visibleState, Header<?> header, Header<String> footer,
			String conditionName, AmountType amountType, String projectTag) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new ProteinComparator(columnName, conditionName, amountType, projectTag);
		defaultWidth = getDefaultWidth(columnName);
		this.conditionName = conditionName;
		condition2Name = null;
		this.amountType = amountType;
		this.projectTag = projectTag;
		this.footer = footer;
		this.header = header;
		this.visibleState = visibleState;
		if (visibleState)
			width = defaultWidth;
		else
			width = 0;
	}

	public ProteinTextColumn(ColumnName columnName, boolean visibleState, Header<?> header, Header<String> footer,
			String condition1Name, String condition2Name, String projectTag, String ratioName) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new ProteinComparator(columnName, condition1Name, condition2Name, projectTag, ratioName);
		defaultWidth = getDefaultWidth(columnName);
		conditionName = condition1Name;
		this.condition2Name = condition2Name;
		amountType = null;
		this.ratioName = ratioName;
		this.projectTag = projectTag;
		this.footer = footer;
		this.header = header;
		this.visibleState = visibleState;
		if (visibleState)
			width = defaultWidth;
		else
			width = 0;
	}

	public static double getDefaultWidth(ColumnName columnName) {
		switch (columnName) {
		case ACC:
			return 100;
		case DESCRIPTION:
			return 300;
		case SPECTRUM_COUNT:
			return 60;
		case COVERAGE:
			return 60;
		case GENE:
			return 100;
		case MOL_W:
			return 80;
		case PROTEIN_PI:
			return 60;
		case SEQUENCE_COUNT:
			return 60;
		case PROTEIN_AMOUNT:
			return 60;
		case PROTEIN_RATIO:
			return 100;
		case PROTEIN_RATIO_SCORE:
			return 100;
		case PROTEIN_LENGTH:
			return 60;
		case ALTERNATIVE_NAMES:
			return 300;
		case NUM_PROTEIN_GROUP_MEMBERS:
			return 60;
		case TAXONOMY:
			return 150;
		case PROTEIN_FUNCTION:
			return 300;
		case SECONDARY_ACCS:
			return 150;
		case CONDITION:
			return 150;
		case OMIM:
			return 300;
		case PROTEIN_GROUP_TYPE:
			return 150;
		case UNIPROT_PROTEIN_EXISTENCE:
			return 150;
		case PROTEIN_SEQUENCE_COVERAGE_IMG:
			return 200;
		case PROTEIN_RATIO_GRAPH:
			return 100;
		case PROTEIN_DOMAIN_FAMILIES:
			return 150;
		case PROTEIN_ACTIVE_SITE:
			return 150;
		case PROTEIN_EXPERIMENTAL_INFO:
			return 150;
		case PROTEIN_MOLECULAR_PROCESSING:
			return 150;
		case PROTEIN_PTM:
			return 150;
		case PROTEIN_NATURAL_VARIATIONS:
			return 150;
		case PROTEIN_SECONDARY_STRUCTURE:
			return 150;

		default:
			return 100;
		}
	}

	@Override
	public String getValue(ProteinBean p) {

		final String value = ProteinColumns.getInstance().getValue(columnName, p, conditionName, condition2Name,
				projectTag, amountType, null, ratioName, false);
		// if (width == 0) {
		// // if it is text, not a number, return ""
		// try {
		// Double.valueOf(value);
		// } catch (NumberFormatException e) {
		// return "";
		// }
		// }
		return value;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.cellview.client.Column#render(com.google.gwt.cell
	 * .client.Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, ProteinBean p, SafeHtmlBuilder sb) {

		// if (width == 0 || p == null) {
		if (p == null) {
			return;
		}

		DataGridRenderValue data;
		switch (columnName) {
		case ACC:
			if (p.getSecondaryAccessions() != null && !p.getSecondaryAccessions().isEmpty()) {
				String secAccs = "";
				for (AccessionBean acc : p.getSecondaryAccessions()) {
					if (!"".equals(secAccs))
						secAccs += ",";
					secAccs += acc.getAccession();
				}
				sb.append(template.startToolTip(secAccs));
				sb.append(ClientSafeHtmlUtils.getAccLinks(p, false));

				sb.append(template.endToolTip());
			} else {
				sb.append(ClientSafeHtmlUtils.getAccLinks(p, true));
			}
			break;
		// this makes this cell to be a new line per secondary accession.
		// I disabled it because sometimes there is too many
		// case SECONDARY_ACCS:
		// sb.append(template.startToolTip(p.getSecondaryAccessionsString()));
		// sb.appendEscaped(p.getSecondaryAccessionsString());
		//
		// sb.append(template.endToolTip());
		//
		// break;
		case ALTERNATIVE_NAMES:
			sb.append(template.startToolTip(p.getAlternativeNamesString()));
			sb.appendEscapedLines(p.getAlternativeNamesString());
			sb.append(template.endToolTip());

			break;
		case PROTEIN_AMOUNT:
			data = DataGridRenderValue.getAmountDataGridRenderValue(p, conditionName, amountType, projectTag);
			sb.append(template.startToolTip(data.getTooltip()));
			// super.render(context, p, sb);
			sb.append(data.getValueAsSafeHtml());
			sb.append(template.endToolTip());
			break;
		case PROTEIN_RATIO:

			sb.append(template.startToolTip("Ratio type: " + ratioName + "\nConditions: " + conditionName + " / "
					+ condition2Name + "\nValue: "
					+ p.getRatioStringByConditions(conditionName, condition2Name, projectTag, ratioName, false)));
			super.render(context, p, sb);
			sb.append(template.endToolTip());
			break;
		case PROTEIN_RATIO_SCORE:

			final String extendedRatioScoreStringByConditions = ClientSafeHtmlUtils
					.getExtendedRatioScoreStringByConditions(p, conditionName, condition2Name, projectTag, ratioName,
							false);
			sb.append(template.startToolTip(extendedRatioScoreStringByConditions));

			super.render(context, p, sb);
			sb.append(template.endToolTip());
			break;

		case GENE:
			// if (p.getGenes(true).isEmpty()) {
			// template = GWT.create(HtmlTemplates.class);
			// sb.append(template.startToolTip(getGenesTooltip(p)));
			// super.render(context, p, sb);
			// sb.append(template.endToolTip());
			// } else {
			sb.append(ClientSafeHtmlUtils.getGeneLinks(p, true));
			// }
			break;
		case PROTEIN_FUNCTION:
			sb.append(ClientSafeHtmlUtils.getProteinFunctionSafeHtml(p));
			break;
		case COVERAGE:
			final Double coverage = p.getCoverage();
			if (coverage != null && coverage != 0.0) {
				final int length = p.getLength();
				final int numAACovered = Double.valueOf(length * coverage).intValue();
				sb.append(template.startToolTip("Covered " + numAACovered + " out of " + length + " AAs."
						+ SharedConstants.SEPARATOR + "Sequence coverage: "
						+ com.google.gwt.i18n.client.NumberFormat.getFormat("#.#").format(coverage * 100) + "%"));
			} else {
				sb.append(template.startToolTip("Coverage cannot be calculated for any reason"));
			}
			super.render(context, p, sb);
			sb.append(template.endToolTip());
			break;
		case TAXONOMY:
			final OrganismBean organism = p.getOrganism();
			if (organism != null) {
				if (organism.getNcbiTaxID() != null && !"".equals(organism.getNcbiTaxID())) {
					// create a link to the ncbi taxonomy
					sb.append(ClientSafeHtmlUtils.getTaxonomyLink(organism));
				} else {
					sb.append(new SafeHtmlBuilder().appendEscaped(organism.getId()).toSafeHtml());
				}
			} else {
				sb.append(new SafeHtmlBuilder().appendEscaped("-").toSafeHtml());
			}
			break;
		case OMIM:

			final Map<Integer, OmimEntryBean> omimEntries = p.getOmimEntries();
			if (omimEntries != null && !omimEntries.isEmpty()) {

				sb.append(ClientSafeHtmlUtils.getOmimLinks(p));

			} else {
				sb.append(new SafeHtmlBuilder().appendEscaped("-").toSafeHtml());
			}
			break;
		case CONDITION:
			sb.append(new SafeHtmlBuilder().appendEscapedLines(p.getConditionsString()).toSafeHtml());
			break;
		case PROTEIN_GROUP_TYPE:
			if (p.getEvidence() != null) {
				sb.append(HtmlTemplates.instance.startToolTip(p.getEvidence().getDefinition()));
				// super.render(context, p, sb);
				String text = ProteinColumns.getInstance().getValue(columnName, p, conditionName, condition2Name,
						projectTag, amountType, null, ratioName, false);
				sb.append(HtmlTemplates.instance
						.spanClass(ClientSafeHtmlUtils.getProteinEvidenceCSSStyleName(p.getEvidence(), false), text));
				sb.append(HtmlTemplates.instance.endToolTip());
			}
			break;
		case UNIPROT_PROTEIN_EXISTENCE:
			if (p.getUniprotProteinExistence() != null) {
				sb.append(HtmlTemplates.instance
						.startToolTip("Uniprot Protein existence: " + p.getUniprotProteinExistence().getLevel() + "-"
								+ p.getUniprotProteinExistence().getDescription()));
				// super.render(context, p, sb);
				String text2 = ProteinColumns.getInstance().getValue(columnName, p, conditionName, condition2Name,
						projectTag, amountType, null, ratioName, false);
				sb.append(HtmlTemplates.instance.spanClass(
						ClientSafeHtmlUtils.getUniprotProteinExistenceCSSStyleName(p.getUniprotProteinExistence()),
						text2));
				sb.append(HtmlTemplates.instance.endToolTip());
			}
			break;
		case PROTEIN_SEQUENCE_COVERAGE_IMG:
			sb.append(ClientSafeHtmlUtils.getProteinCoverageGraphic(p));
			break;
		case PROTEIN_RATIO_GRAPH:
			final List<RatioBean> ratios = p.getRatiosByConditions(conditionName, condition2Name, projectTag, ratioName,
					true);
			if (!ratios.isEmpty()) {
				sb.append(ClientSafeHtmlUtils.getRatioGraphic(p, ratios.get(0)));
			}
			break;

		case PROTEIN_ACTIVE_SITE:
		case PROTEIN_DOMAIN_FAMILIES:
		case PROTEIN_NATURAL_VARIATIONS:
		case PROTEIN_SECONDARY_STRUCTURE:
		case PROTEIN_EXPERIMENTAL_INFO:
		case PROTEIN_MOLECULAR_PROCESSING:
		case PROTEIN_PTM:
			sb.append(ClientSafeHtmlUtils.getUniprotFeatureSafeHtml(p,
					UniprotFeatures.getUniprotFeaturesByColumnName(columnName)));
			break;
		case REACTOME_ID_LINK:
			sb.append(ClientSafeHtmlUtils.getReactomeSafeHtml(p));
			break;
		default:
			sb.append(template.startToolTip(ProteinColumns.getInstance().getValue(columnName, p, conditionName,
					condition2Name, projectTag, amountType, null, ratioName, false)));
			super.render(context, p, sb);
			sb.append(template.endToolTip());
			break;
		}
	}

	@Override
	public Comparator<ProteinBean> getComparator() {
		return comparator;
	}

	@Override
	public double getDefaultWidth() {
		return defaultWidth;

	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * @return the columnName
	 */
	@Override
	public ColumnName getColumnName() {
		return columnName;
	}

	/**
	 * @return the currentExperimentalConditions
	 */
	public Set<String> getCurrentExperimentalConditions() {
		return currentExperimentalConditions;
	}

	/**
	 * @param currentExperimentalConditions
	 *            the currentExperimentalConditions to set
	 */
	public void setCurrentExperimentalConditions(Set<String> currentExperimentalConditions) {
		this.currentExperimentalConditions = currentExperimentalConditions;
	}

	/**
	 * @return the experimentalConditionName
	 */
	@Override
	public String getExperimentalConditionName() {
		return conditionName;
	}

	/**
	 * @return the experimentalConditionName
	 */
	@Override
	public String getExperimentalCondition2Name() {
		return condition2Name;
	}

	@Override
	public Unit getDefaultWidthUnit() {
		// TODO
		if (true)
			return Unit.PX;
		switch (columnName) {
		case DESCRIPTION:
			return Unit.PCT;
		case ALTERNATIVE_NAMES:
			return Unit.PCT;
		case TAXONOMY:
			return Unit.PCT;
		case PROTEIN_FUNCTION:
			return Unit.PCT;
		case GENE:
			return Unit.PCT;
		case ACC:
			return Unit.PCT;
		default:
			return Unit.PX;
		}
	}

	@Override
	public Header<String> getFooter() {
		if (SharedConstants.FOOTERS_ENABLED)
			return footer;
		return null;
	}

	@Override
	public boolean isVisible() {
		return visibleState;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public void setVisible(boolean visible) {
		visibleState = visible;

	}

	@Override
	public String getProjectTag() {
		return projectTag;
	}

	@Override
	public String getScoreName() {
		return null;
	}

	@Override
	public String getRatioName() {
		return ratioName;
	}

	@Override
	public AmountType getAmountType() {
		return amountType;
	}

	@Override
	public Header<?> getHeader() {
		return header;
	}

}
