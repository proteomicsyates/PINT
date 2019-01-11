package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import org.reactome.web.analysis.client.model.PathwaySummary;

public class PathwaysColumns implements ColumnProvider<PathwaySummary> {
	private static PathwaysColumns instance;
	private ArrayList<ColumnWithVisibility> columns;
	private static final String EMPTY_VALUE = "-";

	private PathwaysColumns() {

	}

	public static PathwaysColumns getInstance() {
		if (instance == null) {
			instance = new PathwaysColumns();
		}
		return instance;
	}

	@Override
	public ColumnWithVisibility getColumn(ColumnName columnName) {
		final List<ColumnWithVisibility> columns2 = getColumns();
		for (ColumnWithVisibility columnWithVisibility : columns2) {
			if (columnWithVisibility.getColumn() == columnName)
				return columnWithVisibility;
		}
		return null;
	}

	@Override
	public List<ColumnWithVisibility> getColumns() {
		if (columns == null) {
			columns = new ArrayList<ColumnWithVisibility>();

			ColumnWithVisibility col = new ColumnWithVisibility(ColumnName.PATHWAY_ID, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_NAME, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_ENTITIES_RESOURCE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_ENTITIES_FOUND, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_ENTITIES_TOTAL, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_ENTITIES_RATIO, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_ENTITIES_CURATED_FOUND, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_ENTITIES_CURATED_TOTAL, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_PVALUE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_FDR, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_REACTIONS_RESOURCE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_REACTIONS_FOUND, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_REACTIONS_TOTAL, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PATHWAY_REACTIONS_RATIO, true);
			columns.add(col);

		}
		return columns;
	}

	private String parseEmptyString(String string) {
		// if (string == null || "0".equals(string) || "".equals(string))
		if (string == null || "".equals(string))
			return EMPTY_VALUE;
		return string;
	}

	private String parseZeroAndEmptyString(String string) {
		if (string == null || "0".equals(string) || "".equals(string))
			return EMPTY_VALUE;
		return string;
	}
}
