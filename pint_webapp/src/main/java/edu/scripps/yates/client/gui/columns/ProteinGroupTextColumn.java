package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ProteinGroupColumns;
import edu.scripps.yates.shared.columns.comparator.ProteinGroupComparator;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedConstants;

public class ProteinGroupTextColumn extends CustomTextColumn<ProteinGroupBean> implements MyIdColumn<ProteinGroupBean> {

	private final ColumnName columnName;
	private final Comparator<ProteinGroupBean> comparator;
	private double width;
	private final double defaultWidth;
	private final String conditionName;
	// in case of ratios
	private final String condition2Name;
	private final AmountType amountType;
	private final String projectTag;
	private final Header<String> footer;
	private boolean visibleState;
	private Set<String> currentExperimentalConditions = new HashSet<String>();
	private String ratioName;
	private final Header<?> header;
	private static HtmlTemplates template = GWT.create(HtmlTemplates.class);

	public ProteinGroupTextColumn(ColumnName columnName, boolean visibleState, Header<?> header,
			Header<String> footer) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new ProteinGroupComparator(columnName);
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

	public ProteinGroupTextColumn(ColumnName columnName, boolean visibleState, Header<?> header, Header<String> footer,
			String conditionName, AmountType amountType, String projectTag) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new ProteinGroupComparator(columnName, conditionName, amountType, projectTag);
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

	public ProteinGroupTextColumn(ColumnName columnName, boolean visibleState, Header<?> header, Header<String> footer,
			String condition1Name, String condition2Name, String projectTag, String ratioName) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		comparator = new ProteinGroupComparator(columnName, condition1Name, condition2Name, projectTag, ratioName);
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

		case PROTEIN_GROUP_TYPE:
			return 300;
		case NUM_PROTEIN_GROUP_MEMBERS:
			return 150;

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
	public Unit getDefaultWidthUnit() {
		if (true)
			return Unit.PX;
		switch (columnName) {
		case DESCRIPTION:
			return Unit.PCT;
		case ALTERNATIVE_NAMES:
			return Unit.PCT;
		case TAXONOMY:
			return Unit.PCT;
		default:
			return Unit.PX;
		}
	}

	@Override
	public String getValue(ProteinGroupBean p) {
		final String value = ProteinGroupColumns.getInstance().getValue(columnName, p, conditionName, condition2Name,
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
	 * @see
	 * com.google.gwt.user.cellview.client.Column#render(com.google.gwt.cell
	 * .client.Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, ProteinGroupBean p, SafeHtmlBuilder sb) {
		if (width == 0) {
			return;
		}
		DataGridRenderValue data;
		switch (columnName) {
		case ACC:

			sb.append(ClientSafeHtmlUtils.getAccLinks(p, true));

			break;
		case DESCRIPTION:
			sb.append(new SafeHtmlBuilder().appendEscapedLines(p.getDescriptionsString()).toSafeHtml());
			break;
		case TAXONOMY:
			sb.append(new SafeHtmlBuilder().appendEscapedLines(p.getOrganismsString()).toSafeHtml());
			break;
		case PROTEIN_AMOUNT:
			data = DataGridRenderValue.getAmountDataGridRenderValue(p, conditionName, amountType, projectTag);
			sb.append(template.startToolTip(data.getTooltip()));
			// super.render(context, p, sb);
			sb.append(data.getValueAsSafeHtml());
			sb.append(template.endToolTip());
			break;
		case PROTEIN_RATIO:

			template = GWT.create(HtmlTemplates.class);
			sb.append(template.startToolTip("Ratio type: " + ratioName + "\nConditions: " + conditionName + " / "
					+ condition2Name + "\nValue: "
					+ p.getRatioStringByConditions(conditionName, condition2Name, projectTag, ratioName, false)));
			super.render(context, p, sb);
			sb.append(template.endToolTip());
			break;
		case GENE:
			sb.append(ClientSafeHtmlUtils.getGeneLinks(p, true));
			// template = GWT.create(HtmlTemplates.class);
			// sb.append(template.startToolTip(getGenesTooltip(p)));
			// // super.render(context, p, sb);
			// sb.appendEscapedLines(p.getGenesString(true));
			// sb.append(template.endToolTip());
			break;
		case PROTEIN_GROUP_TYPE:
			sb.append(ClientSafeHtmlUtils.getGroupMemberEvidences(p));
			break;
		case UNIPROT_PROTEIN_EXISTENCE:
			sb.append(ClientSafeHtmlUtils.getGroupMemberExistences(p));
			break;
		case PROTEIN_SEQUENCE_COVERAGE_IMG:

			sb.append(ClientSafeHtmlUtils.getProteinCoverageGraphic(p));

			break;
		default:
			super.render(context, p, sb);
			break;
		}
	}

	@Override
	public Comparator<ProteinGroupBean> getComparator() {
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
