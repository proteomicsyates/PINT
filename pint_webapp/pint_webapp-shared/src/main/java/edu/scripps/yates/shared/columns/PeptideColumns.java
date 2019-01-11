package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.PeptideBean;

public class PeptideColumns implements ColumnProvider<PeptideBean> {
	private static PeptideColumns instance;
	private ArrayList<ColumnWithVisibility> columns;

	private PeptideColumns() {

	}

	public static PeptideColumns getInstance() {
		if (instance == null) {
			instance = new PeptideColumns();
		}
		return instance;
	}

	@Override
	public ColumnWithVisibility getColumn(ColumnName columnName) {
		final List<ColumnWithVisibility> columns2 = getColumns();
		for (final ColumnWithVisibility columnWithVisibility : columns2) {
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
			col = new ColumnWithVisibility(ColumnName.ACC, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_EVIDENCE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_SEQUENCE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_LENGTH, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.POSITION_IN_PROTEIN, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SPECTRUM_COUNT, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SPC_PER_CONDITION, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.TAXONOMY, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.NUM_PTMS, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.NUM_PTM_SITES, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PTMS, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_AMOUNT, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_RATIO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_RATIO_GRAPH, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PEPTIDE_RATIO_SCORE, false);
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
