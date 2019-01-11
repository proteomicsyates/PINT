package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.PSMBean;

public class PSMColumns implements ColumnProvider<PSMBean> {
	private static PSMColumns instance;
	private ArrayList<ColumnWithVisibility> columns;
	private static final String EMPTY_VALUE = "-";

	private PSMColumns() {

	}

	public static PSMColumns getInstance() {
		if (instance == null) {
			instance = new PSMColumns();
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

			ColumnWithVisibility col = new ColumnWithVisibility(ColumnName.LINK_TO_PRIDE_CLUSTER, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_ID, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_RUN_ID, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.ACC, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_EVIDENCE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_SEQUENCE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_LENGTH, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.CHARGE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.POSITION_IN_PROTEIN, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.TAXONOMY, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_SCORE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.NUM_PTMS, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.NUM_PTM_SITES, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PTMS, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PTM_SCORE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_PI, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_AMOUNT, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_RATIO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_RATIO_GRAPH, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PSM_RATIO_SCORE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.CONDITION, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_DOMAIN_FAMILIES, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_SECONDARY_STRUCTURE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_ACTIVE_SITE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_EXPERIMENTAL_INFO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_MOLECULAR_PROCESSING, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_PTM, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_NATURAL_VARIATIONS, false);
			columns.add(col);
		}
		return columns;
	}

}
