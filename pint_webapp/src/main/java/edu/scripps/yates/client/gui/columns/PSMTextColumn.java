package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.columns.comparator.PSMComparator;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedConstants;

public class PSMTextColumn extends CustomTextColumn<PSMBean> implements MyColumn<PSMBean> {

	private final ColumnName columnName;
	private final Comparator<PSMBean> comparator;
	private double width;
	private final double defaultWidth;
	private final String conditionName;
	private final String condition2Name;
	private final AmountType amountType;
	private final String projectTag;
	private final Header<String> footer;
	private boolean visibleState;
	private Set<String> currentExperimentalConditions = new HashSet<String>();
	private final String scoreName;
	private final HtmlTemplates template = GWT.create(HtmlTemplates.class);
	private String ratioName;

	public PSMTextColumn(ColumnName columnName, boolean visibleState, Header<String> footer) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new PSMComparator(columnName);
		defaultWidth = getDefaultWidth(columnName);
		conditionName = null;
		condition2Name = null;
		amountType = null;
		projectTag = null;
		scoreName = null;
		this.footer = footer;
		this.visibleState = visibleState;
		if (visibleState)
			width = defaultWidth;
		else
			width = 0;
	}

	public PSMTextColumn(ColumnName columnName, boolean visibleState, Header<String> footer, String conditionName,
			AmountType amountType, String projectTag) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new PSMComparator(columnName, conditionName, amountType, projectTag);
		defaultWidth = getDefaultWidth(columnName);
		this.conditionName = conditionName;
		condition2Name = null;
		this.amountType = amountType;
		this.projectTag = projectTag;
		scoreName = null;
		this.footer = footer;
		this.visibleState = visibleState;
		if (visibleState)
			width = defaultWidth;
		else
			width = 0;
	}

	public PSMTextColumn(ColumnName columnName, boolean visibleState, Header<String> footer, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new PSMComparator(columnName, condition1Name, condition2Name, projectTag, ratioName);
		defaultWidth = getDefaultWidth(columnName);
		conditionName = condition1Name;
		this.condition2Name = condition2Name;
		amountType = null;
		this.ratioName = ratioName;
		this.projectTag = projectTag;
		scoreName = null;
		this.footer = footer;
		this.visibleState = visibleState;
		if (visibleState)
			width = defaultWidth;
		else
			width = 0;
	}

	public PSMTextColumn(ColumnName columnName, boolean visibleState, Header<String> footer, String scoreName) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new PSMComparator(columnName, scoreName);
		defaultWidth = getDefaultWidth(columnName);
		conditionName = null;
		condition2Name = null;
		amountType = null;
		projectTag = null;
		this.scoreName = scoreName;
		this.footer = footer;
		this.visibleState = visibleState;
		if (visibleState)
			width = defaultWidth;
		else
			width = 0;
	}

	@Override
	public String getValue(PSMBean p) {
		final String value = PSMColumns.getInstance().getValue(columnName, p, conditionName, condition2Name, projectTag,
				amountType, scoreName, ratioName, false);
		if (width == 0) {
			// if it is text, not a number, return ""
			try {
				Double.valueOf(value);
			} catch (NumberFormatException e) {
				return "";
			}
		}
		return value;

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.Column#render(com.google.gwt.cell
	 * .client.Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, PSMBean psm, SafeHtmlBuilder sb) {
		if (width == 0 || psm == null) {
			return;
		}
		DataGridRenderValue data;

		switch (columnName) {

		case PEPTIDE_SEQUENCE:
			sb.append(template.startToolTip(psm.getFullSequence()));
			sb.append(getRichPeptideSequence(psm));
			// super.render(context, psm, sb);
			sb.append(template.endToolTip());
			break;
		case PSM_AMOUNT:
			data = DataGridRenderValue.getAmountDataGridRenderValue(psm, conditionName, amountType, projectTag);
			sb.append(template.startToolTip(data.getTooltip()));
			// super.render(context, p, sb);
			sb.append(data.getValueAsSafeHtml());
			sb.append(template.endToolTip());

			// template = GWT.create(HtmlTemplates.class);
			// String amountType = psm.getAmountTypeString(conditionName,
			// projectName);
			// sb.append(template.startToolTip(amountType));
			// super.render(context, psm, sb);
			// sb.append(template.endToolTip());
			break;
		case TAXONOMY:
			sb.append(new SafeHtmlBuilder().appendEscapedLines(psm.getOrganismsString()).toSafeHtml());
			break;
		case PSM_RATIO:
			sb.append(template.startToolTip("Ratio type: " + ratioName + "\nConditions: " + conditionName + " / "
					+ condition2Name + "\nValue: "
					+ psm.getRatioStringByConditions(conditionName, condition2Name, projectTag, ratioName, false)));
			super.render(context, psm, sb);
			sb.append(template.endToolTip());
			break;
		case ACC:
			sb.append(ClientSafeHtmlUtils.getAccLinks(psm, true));

			break;
		case POSITION_IN_PROTEIN:

			final String startingPositionsString = psm.getExtendedStartingPositionsString();

			sb.append(template.startToolTip(startingPositionsString));

			super.render(context, psm, sb);
			sb.append(template.endToolTip());
			break;
		case CONDITION:
			sb.append(new SafeHtmlBuilder().appendEscapedLines(psm.getConditionsString()).toSafeHtml());
			break;
		case PEPTIDE_EVIDENCE:
			if (psm.getRelation() != null) {
				sb.append(HtmlTemplates.instance.startToolTip(psm.getRelation().getDefinition()));
				// super.render(context, psm, sb);
				sb.append(HtmlTemplates.instance.spanClass(
						ClientSafeHtmlUtils.getRelationCSSStyleName(psm.getRelation(), false),
						psm.getRelation().name()));
				sb.append(HtmlTemplates.instance.endToolTip());
			} else {
				super.render(context, psm, sb);
			}
			break;
		case PSM_RATIO_GRAPH:
			final List<RatioBean> ratios = psm.getRatiosByConditions(conditionName, condition2Name, projectTag,
					ratioName, true);
			if (!ratios.isEmpty()) {
				sb.append(ClientSafeHtmlUtils.getPSMRatioGraphic(psm, ratios.get(0)));
			}
			break;
		default:
			sb.append(template.startToolTip(PSMColumns.getInstance().getValue(columnName, psm, conditionName,
					condition2Name, projectTag, amountType, scoreName, ratioName, false)));
			super.render(context, psm, sb);
			sb.append(template.endToolTip());
			break;
		}
	}

	private SafeHtml getRichPeptideSequence(PSMBean p) {
		final String sequence = p.getSequence();
		if (p.getPtms() == null || p.getPtms().isEmpty())
			return new SafeHtmlBuilder().appendEscaped(sequence).toSafeHtml();

		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		for (int i = 0; i < sequence.length(); i++) {
			boolean modified = false;
			for (PTMBean ptm : p.getPtms()) {
				for (PTMSiteBean ptmSite : ptm.getPtmSites()) {
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

	@Override
	public Comparator<PSMBean> getComparator() {
		return comparator;
	}

	public static double getDefaultWidth(ColumnName columnName) {

		switch (columnName) {
		case PEPTIDE_PI:
			return 50;
		case PSM_SCORE:
			return 100;
		case PSM_AMOUNT:
			return 100;
		case CHARGE:
			return 50;
		case PSM_RATIO:
			return 100;
		case PEPTIDE_SEQUENCE:
			return 300;
		case PTM_SCORE:
			return 100;
		case PTMS:
			return 300;
		case NUM_PTMS:
			return 70;
		case NUM_PTM_SITES:
			return 120;
		case PSM_ID:
			return 300;
		case PSM_RUN_ID:
			return 150;
		case PEPTIDE_LENGTH:
			return 50;
		case POSITION_IN_PROTEIN:
			return 150;
		case TAXONOMY:
			return 300;
		case CONDITION:
			return 150;
		case PEPTIDE_EVIDENCE:
			return 150;
		case PSM_RATIO_GRAPH:
			return 200;
		default:
			try {
				// look if it is a column defined in the protein column, like
				// accession
				return ProteinTextColumn.getDefaultWidth(columnName);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Default width not defined for PSM column: " + columnName.getName());
			}
		}

	}

	@Override
	public double getDefaultWidth() {
		return defaultWidth;
	}

	@Override
	public Unit getDefaultWidthUnit() {
		// TODO
		if (true)
			return Unit.PX;
		switch (columnName) {

		case PEPTIDE_SEQUENCE:
			return Unit.PCT;
		case PTMS:
			return Unit.PCT;
		case PSM_ID:
			return Unit.PCT;
		case PSM_RUN_ID:
			return Unit.PCT;
		case PSM_SCORE:
			return Unit.PCT;
		case PTM_SCORE:
			return Unit.PCT;
		case ACC:
			return Unit.PCT;
		case TAXONOMY:
			return Unit.PCT;
		default:
			return Unit.PX;
		}
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
	public String getExperimentalCondition2Name() {
		return condition2Name;
	}

	@Override
	public String getProjectTag() {
		return projectTag;
	}

	@Override
	public String getScoreName() {
		return scoreName;
	}

	@Override
	public String getRatioName() {
		return ratioName;
	}

	@Override
	public AmountType getAmountType() {
		return amountType;
	}
}
