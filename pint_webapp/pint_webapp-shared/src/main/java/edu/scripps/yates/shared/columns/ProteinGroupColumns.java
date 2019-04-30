package edu.scripps.yates.shared.columns;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.ProteinGroupBean;

public class ProteinGroupColumns implements ColumnProvider<ProteinGroupBean> {
	private static ProteinGroupColumns instance;
	private ArrayList<ColumnWithVisibility> columns;

	private ProteinGroupColumns() {

	}

	public static ProteinGroupColumns getInstance() {
		if (instance == null) {
			instance = new ProteinGroupColumns();
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

			ColumnWithVisibility col = new ColumnWithVisibility(ColumnName.PEPTIDES_TABLE_BUTTON, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.ACC, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SECONDARY_ACCS, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.DESCRIPTION, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.ALTERNATIVE_NAMES, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.UNIPROT_PROTEIN_EXISTENCE, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_GROUP_TYPE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.NUM_PROTEIN_GROUP_MEMBERS, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.GENE, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.TAXONOMY, false);
			columns.add(col);
			// addColumn(ColumnName.COVERAGE, false);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_SEQUENCE_COVERAGE_IMG, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SEQUENCE_COUNT, true);
			columns.add(col);
//			if (
//			// Pint.psmCentric
//			false) {
			col = new ColumnWithVisibility(ColumnName.SPECTRUM_COUNT, true);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.SPC_PER_CONDITION, false);
			columns.add(col);
//			}
			col = new ColumnWithVisibility(ColumnName.PROTEIN_AMOUNT, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.PROTEIN_RATIO, false);
			columns.add(col);
			col = new ColumnWithVisibility(ColumnName.CONDITION, false);
			columns.add(col);
		}
		return columns;
	}

	private String parseZeroAndEmptyString(String string) {
		if (string == null || "0".equals(string) || "".equals(string))
			return "-";
		return string;
	}

	private String parseEmptyString(String string) {
		if (string == null || "".equals(string))
			return "-";
		return string;
	}
}
