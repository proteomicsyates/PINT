package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.reactome.web.analysis.client.model.PathwaySummary;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.util.SharedConstants;

public class PathWayTextColumn extends CustomTextColumn<PathwaySummary> implements MyColumn<PathwaySummary> {

	private final ColumnName columnName;
	private final Comparator<PathwaySummary> comparator;
	private double width;
	private final double defaultWidth;
	private final Header<String> footer;
	private final Header<?> header;
	private boolean visibleState;
	private Set<String> currentExperimentalConditions = new HashSet<String>();
	private static final NumberFormat scientificFormat = com.google.gwt.i18n.client.NumberFormat.getScientificFormat();
	private static final NumberFormat decimalFormat = com.google.gwt.i18n.client.NumberFormat.getFormat("#.###");

	public PathWayTextColumn(ColumnName columnName, boolean visibleState, Header<?> header, Header<String> footer) {
		super(columnName);
		setSortable(true);
		this.columnName = columnName;
		// comparator = new PSMComparator(columnName);
		comparator = null;
		defaultWidth = getDefaultWidth(columnName);
		this.header = header;
		this.footer = footer;
		this.visibleState = visibleState;
		if (visibleState)
			width = defaultWidth;
		else
			width = 0;
	}

	@Override
	public String getValue(PathwaySummary p) {
		final String value = "-";
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
	public void render(Context context, PathwaySummary pathWay, SafeHtmlBuilder sb) {
		// if (width == 0 || pathWay == null) {
		if (pathWay == null) {
			return;
		}

		switch (columnName) {

		case PATHWAY_NAME:
			final SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
			safeHtmlBuilder.appendEscaped(pathWay.getName());
			sb.append(safeHtmlBuilder.toSafeHtml());
			break;
		case PATHWAY_FDR:
			sb.appendEscaped(formatNumber(pathWay.getEntities().getFdr()));
			break;
		case PATHWAY_PVALUE:
			sb.appendEscaped(formatNumber(pathWay.getEntities().getpValue()));
			break;
		case PATHWAY_ENTITIES_RESOURCE:
			final SafeHtmlBuilder safeHtmlBuilder2 = new SafeHtmlBuilder();
			safeHtmlBuilder2.appendEscaped(pathWay.getEntities().getResource());
			sb.append(safeHtmlBuilder2.toSafeHtml());
			break;
		case PATHWAY_ENTITIES_FOUND:
			sb.append(pathWay.getEntities().getFound());
			break;
		case PATHWAY_ENTITIES_TOTAL:
			sb.append(pathWay.getEntities().getTotal());
			break;
		case PATHWAY_ENTITIES_RATIO:
			sb.appendEscaped(formatNumber(pathWay.getEntities().getRatio()));
			break;

		case PATHWAY_REACTIONS_FOUND:
			sb.append(pathWay.getReactions().getFound());
			break;
		case PATHWAY_REACTIONS_TOTAL:
			sb.append(pathWay.getReactions().getTotal());
			break;
		case PATHWAY_REACTIONS_RATIO:
			sb.appendEscaped(formatNumber(pathWay.getReactions().getRatio()));
			break;
		case PATHWAY_ENTITIES_CURATED_FOUND:
			if (pathWay.getEntities().getCuratedFound() != null) {
				sb.append(pathWay.getEntities().getCuratedFound());
			}
			break;
		case PATHWAY_ENTITIES_CURATED_TOTAL:
			if (pathWay.getEntities().getCuratedTotal() != null) {
				sb.append(pathWay.getEntities().getCuratedTotal());
			}
			break;
		case PATHWAY_REACTIONS_RESOURCE:
			final SafeHtmlBuilder safeHtmlBuilder3 = new SafeHtmlBuilder();
			safeHtmlBuilder3.appendEscaped(pathWay.getReactions().getResource());
			sb.append(safeHtmlBuilder3.toSafeHtml());
			break;
		// case PATHWAY_ID:
		//
		// sb.append(ClientSafeHtmlUtils.getPathWayLink(pathWay));
		// break;
		default:

			break;
		}
	}

	private String formatNumber(Double number) {
		if (number == null) {
			return null;
		}
		if (number < 0.001) {
			return scientificFormat.format(number);
		} else {
			return decimalFormat.format(number);
		}
	}

	@Override
	public Comparator<PathwaySummary> getComparator() {
		return comparator;
	}

	public static double getDefaultWidth(ColumnName columnName) {

		switch (columnName) {
		case PATHWAY_NAME:
			return 300;
		case PATHWAY_FDR:
			return 50;
		case PATHWAY_ENTITIES_FOUND:
			return 50;
		case PATHWAY_ENTITIES_RATIO:
			return 50;
		case PATHWAY_ENTITIES_TOTAL:
			return 50;
		case PATHWAY_ID:
			return 90;
		case PATHWAY_PVALUE:
			return 90;
		case PATHWAY_REACTIONS_FOUND:
			return 50;
		case PATHWAY_REACTIONS_RATIO:
			return 50;
		case PATHWAY_REACTIONS_TOTAL:
			return 50;
		case PATHWAY_ENTITIES_RESOURCE:
			return 120;
		default:
			return 100;

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
	public Header<?> getHeader() {
		return header;
	}

}
